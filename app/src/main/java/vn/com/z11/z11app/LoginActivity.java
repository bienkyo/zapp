package vn.com.z11.z11app;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;
import vn.com.z11.z11app.ApiResponseModel.LanguageResponse;
import vn.com.z11.z11app.ApiResponseModel.LoginResponse;
import vn.com.z11.z11app.Database.Query.LocalDb;
import vn.com.z11.z11app.RestAPI.ApiUser;
import vn.com.z11.z11app.RestAPI.ErrorUtils;
import vn.com.z11.z11app.Utilities.AppConstants;
import vn.com.z11.z11app.Utilities.CommonMethod;

public class LoginActivity extends AccountAuthenticatorActivity {
    public static final String ARG_ACCOUNT_TYPE = "account_type";
    public static final String ARG_IS_ADDING_NEW = "new_account";
    public static final String ARG_AUTH_TYPE = "auth_type";
    private AccountManager mAccountManager;
    RelativeLayout loading;
    EditText edt_email, edt_password;
    String mAuthTokenType;
    Button btn_login, btn_facebook, btn_google;
    TextView txtv_forgotpass, txtv_register;
    LocalDb localDb = new LocalDb(this);
    String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        from = getIntent().getStringExtra("other");

        mAccountManager = AccountManager.get(this);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        initView();
        loginPassword();
        register();
    }

    public void initView() {
        loading = (RelativeLayout) findViewById(R.id.rl_loading);
        loading.setVisibility(View.INVISIBLE);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_pass);
        btn_login = (Button) findViewById(R.id.btn_loginAccount);
        btn_facebook = (Button) findViewById(R.id.btn_loginFB);
        btn_google = (Button) findViewById(R.id.btn_loginGoogle);
        txtv_forgotpass = (TextView) findViewById(R.id.txtv_forgotpass);
        txtv_register = (TextView) findViewById(R.id.txtv_register);
    }

    public void loginPassword() {
        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
        final CommonMethod commonMethod = new CommonMethod();
        final ApiUser apiUser = RestAdapter.getClient().create(ApiUser.class);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString();
                final String pass = edt_password.getText().toString();
                Boolean checkEmail = commonMethod.validateEmail(email, LoginActivity.this);
                if (!email.isEmpty() && checkEmail) {
                    if (!pass.isEmpty() && pass.length() >= 6) {
                        loading.setVisibility(View.VISIBLE);
                        Call<LoginResponse> loginResponseCall = apiUser.userLogin("password", email, pass, null);
                        loginResponseCall.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                loading.setVisibility(View.INVISIBLE);
                                final int code = response.code();
                                final Bundle data = new Bundle();

                                if (code == 200) {
                                    LoginResponse result = response.body();
                                    String token = "Bearer {" + result.getMetadata().getToken() + "}";
                                    final LoginResponse.Metadata.User user = result.getMetadata().getUser();

                                    localDb.insertUser(result.getMetadata().getUser(),token);
                                    loadLanguages();

//                                    data.putString(AccountManager.KEY_ACCOUNT_NAME, user.getEmail());
//                                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
//                                    data.putString(AccountManager.KEY_INTENT, token);
//                                    data.putString(AccountManager.KEY_PASSWORD, pass);
//                                    final Intent intent = new Intent();
//                                    intent.putExtras(data);
//                                    finishLogin(intent);
                                } else if (code == 400 || code == 404) {

                                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                                    data.putString(AccountManager.KEY_ERROR_MESSAGE, erroResponse.getStatus());
                                    Toast.makeText(LoginActivity.this, erroResponse.getStatus(), Toast.LENGTH_SHORT).show();

                                }

                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.INVISIBLE);
                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "password short", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "email invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void loadLanguages() {
        Call<LanguageResponse> caller = RestAdapter.getClient().create(ApiUser.class).getLanguages();
        caller.enqueue(languageCallback);

    }

    public void register() {
        txtv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(register, AppConstants.REGISTER_KEY);
                overridePendingTransition(R.anim.fade_in_right, R.anim.fade_out_right);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppConstants.REGISTER_KEY:
                if (resultCode == 200) {
                    Bundle response = data.getBundleExtra("registerResponse");
                    String email = (String) response.get("email");
                    String password = (String) response.get("password");

                    edt_email.setText(email);
                    edt_password.setText(password);
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (from == "other") {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//        }
//        overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishLogin(Intent intent) {
        final String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        final String accountPassword = intent.getStringExtra(AccountManager.KEY_PASSWORD);
        final String accountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

        final Account account = new Account(accountName, accountType);


        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW, false)) {
            final String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            final String authTokenType = "password";

            mAccountManager.addAccountExplicitly(account, accountPassword, null /* user data */);
            mAccountManager.setAuthToken(account,authTokenType,authToken);
        } else {
            mAccountManager.setPassword(account,accountPassword);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK,intent);
        finish();

    }


    private Callback<LanguageResponse> languageCallback = new Callback<LanguageResponse>() {
        @Override
        public void onResponse(Call<LanguageResponse> call, Response<LanguageResponse> response) {

            if (response.isSuccessful()) {
                LanguageResponse result = response.body();
                localDb.setLanguages(result.languages);
                finish();

            } else {
                ErroResponse erroResponse = ErrorUtils.parseError(response);
                Toast.makeText(LoginActivity.this, erroResponse.getStatus(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<LanguageResponse> call, Throwable t) {
            t.printStackTrace();
            Toast.makeText(LoginActivity.this, "email invalid", Toast.LENGTH_SHORT).show();
        }
    };
}

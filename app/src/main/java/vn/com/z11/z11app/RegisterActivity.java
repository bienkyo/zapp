package vn.com.z11.z11app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;
import vn.com.z11.z11app.ApiResponseModel.LoginResponse;
import vn.com.z11.z11app.ApiResponseModel.RegisterResponse;
import vn.com.z11.z11app.RestAPI.ApiRegister;
import vn.com.z11.z11app.RestAPI.ErrorUtils;
import vn.com.z11.z11app.Utilities.AppConstants;
import vn.com.z11.z11app.Utilities.CommonMethod;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_user_name;
    EditText edt_email;
    EditText edt_pass;
    EditText edt_confirm_pass;
    AppCompatSpinner spinner;
    Button btn_register;
    RelativeLayout loading;

    String gender;
    String username;
    String email;
    String password;
    String confirm_pass;

    final CommonMethod commonMethod = new CommonMethod();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.WhiteColor), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        initView();
    }

    public void initView() {
        edt_user_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_pass = (EditText) findViewById(R.id.edt_pass);
        edt_confirm_pass = (EditText) findViewById(R.id.edt_confirm_pass);
        spinner = (AppCompatSpinner) findViewById(R.id.gender);
        btn_register = (Button) findViewById(R.id.btnRegister);
        loading = (RelativeLayout) findViewById(R.id.rl_loading);
        loading.setVisibility(View.INVISIBLE);
        final String arr_gender[] = {"Male", "FeMale"};
        gender = "Male";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr_gender);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = arr_gender[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edt_user_name.getText().toString();
                email = edt_email.getText().toString();
                password = edt_pass.getText().toString();
                confirm_pass = edt_confirm_pass.getText().toString();


                Boolean checkEmail = commonMethod.validateEmail(email, RegisterActivity.this);
                if (!email.isEmpty() && checkEmail) {
                    if (!password.isEmpty() && password.length() >= 6 && password.equals(confirm_pass)) {
                        //true

                        ApiRegister apiRegister = RestAdapter.getClient().create(ApiRegister.class);
                        loading.setVisibility(View.VISIBLE);
                        Call<RegisterResponse> registerResponseCall = apiRegister.userRegister(username, email, gender, password, confirm_pass);
                        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
                            @Override
                            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                                loading.setVisibility(View.INVISIBLE);
                                int code = response.code();
                                if (code == 200) {
                                    RegisterResponse result = response.body();
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", email);
                                    bundle.putString("password", password);
                                    intent.putExtra("registerResponse", bundle);
                                    setResult(200, intent);
                                    finish();
                                    overridePendingTransition(R.anim.fade_in_right, R.anim.fade_out_right);
                                } else if (code == 400 || code == 404) {
                                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                                    Toast.makeText(RegisterActivity.this, erroResponse.getStatus(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                                loading.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        Toast.makeText(RegisterActivity.this, "password or confirm_pass invalid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "email invalid", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

package vn.com.z11.z11app.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Bien-kun on 22/02/2017.
 */

public class ZAppAuthenticatior extends AbstractAccountAuthenticator {
    public static final String TAG = "ZAppAuthenticator";
    private final Context mContext;

    public ZAppAuthenticatior(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
//        Log.d(TAG, "addAccount");
//
//        final Intent intent = new Intent(mContext, LoginActivity.class);
//        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
//        intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
//        intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW, true);
//        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
//
//        final Bundle bundle = new Bundle();
//        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
//        return bundle;
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
//        final AccountManager am = AccountManager.get(mContext);
//        String authToken = am.peekAuthToken(account, authTokenType);
//
//        if (TextUtils.isEmpty(authToken)) {
//            //If token is expired try to login again
//            final String password = am.getPassword(account);
//
//            if (password != null) {
//                final ApiUser apiUser = RestAdapter.getClient().create(ApiUser.class);
//                Call<LoginResponse> caller = apiUser.userLogin("password", account.name, password, null);
//                try {
//                    Response<LoginResponse> loginResponse = caller.execute();
//                    final int code = loginResponse.code();
//                    if (code == 200) {
//                        LoginResponse result = loginResponse.body();
//                        authToken = "Bearer {" + result.getMetadata().getToken() + "}";
//
//                    } else if (code == 400 || code == 404) {
//                        ErroResponse erroResponse = ErrorUtils.parseError(loginResponse);
//                        Log.e(TAG,erroResponse.getStatus());
//
//                    }
//                } catch (IOException e) {
//                    Log.e(TAG, "Can't connect to server, check your connectivity", e);
//                }
//            }
//
//        }
//
//        if (!TextUtils.isEmpty(authToken)) {
//            final Bundle result = new Bundle();
//            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
//            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
//            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
//            return result;
//        }
//
//        final Intent intent = new Intent(mContext, LoginActivity.class);
//        final Bundle bundle = new Bundle();
//        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
//        return bundle;

        throw new UnsupportedOperationException("Stub");
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }
}

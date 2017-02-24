package vn.com.z11.z11app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;
import vn.com.z11.z11app.ApiResponseModel.LoginResponse;
import vn.com.z11.z11app.Database.Query.LocalDb;
import vn.com.z11.z11app.R;
import vn.com.z11.z11app.RestAPI.ApiUser;
import vn.com.z11.z11app.RestAPI.ErrorUtils;

/**
 * Created by Bien-kun on 23/02/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int SYNC_INTERVAL = 60 * 60;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private final Context mContext;
    private final AccountManager mAccountManager;
    private LocalDb userDb;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String token = userDb.getToken();
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Call<LoginResponse> userProfileCall = RestAdapter.getClient().create(ApiUser.class).getUserProfile();
        try {
            Response<LoginResponse> loginResponse = userProfileCall.execute();
            final int code = loginResponse.code();
            if (code == 200) {
                LoginResponse result = loginResponse.body();

                userDb.updateProfile(result.getMetadata().getUser().getProfile());

            } else if (code == 400 || code == 404) {
                ErroResponse erroResponse = ErrorUtils.parseError(loginResponse);
                Log.e(TAG, erroResponse.getStatus());
            }

        } catch (IOException e) {
            Log.e(TAG, "Can't connect to server, check your connectivity", e);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        context.getContentResolver().requestSync(getSyncAccount(context), context.getString(R.string.content_authority)
                , bundle);
    }
    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(context.getString(R.string.app_name), AccountGeneral.ACCOUNT_TYPE);

        if (null == accountManager.getPassword(newAccount)) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void onAccountCreated(Account newAccount, Context context) {
        SyncAdapter.confirguredPeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }
    /* Helper method to schedule the sync adapter periodic execution */
    public static void confirguredPeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(Bundle.EMPTY)
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }
}

package vn.com.z11.z11app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Bien-kun on 22/02/2017.
 */

public class AccountAuthenticatorService extends Service {
    ZAppAuthenticatior mAthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mAthenticator = new ZAppAuthenticatior(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAthenticator.getIBinder();
    }
}

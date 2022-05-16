package omt.aduc8386.loginmodule;

import android.app.Application;

import io.realm.Realm;
import omt.aduc8386.loginmodule.helper.SharedPreferencesHelper;

public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesHelper.init(this);
        Realm.init(this);
    }
}

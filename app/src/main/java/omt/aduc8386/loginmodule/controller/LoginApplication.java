package omt.aduc8386.loginmodule.controller;

import android.app.Application;

import butterknife.ButterKnife;
import io.realm.Realm;
import omt.aduc8386.loginmodule.controller.helper.SharedPreferencesHelper;

public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesHelper.init(this);
        Realm.init(this);
    }
}

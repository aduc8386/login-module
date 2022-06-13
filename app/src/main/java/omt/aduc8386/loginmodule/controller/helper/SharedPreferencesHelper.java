package omt.aduc8386.loginmodule.controller.helper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    public static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    public static final String REMEMBER_ME = "REMEMBER_ME";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASSWORD = "USER_PASSWORD";

    private static SharedPreferences instance;

    @SuppressLint("CommitPrefEdits")
    public static void init(Context context) {
        if (instance == null)
            instance = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getInstance() {
        return instance;
    }

    public static boolean getRememberMeCheck() {
        return instance.getBoolean(REMEMBER_ME, false);
    }

    public static String getUserEmail() {
        return instance.getString(USER_EMAIL, "");
    }

    public static String getUserPassword() {
        return instance.getString(USER_PASSWORD, "");
    }

    public static String getUserToken() {
        return instance.getString(USER_TOKEN, "");
    }

    public static void setUserEmail(String userEmail) {
        instance.edit().putString(USER_EMAIL, userEmail).apply();
    }

    public static void setUserPassword(String name) {
        instance.edit().putString(USER_PASSWORD, name).apply();
    }

    public static void setRememberMe(boolean rememberMe) {
        instance.edit().putBoolean(REMEMBER_ME, rememberMe).apply();
    }

    public static void setUserToken(String userToken) {
        instance.edit().putString(USER_TOKEN, userToken).apply();
    }

}

package omt.aduc8386.loginmodule;


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

    public static boolean getRememberMeCheck(String key) {
        return instance.getBoolean(key, false);
    }

    public static String getUserEmail(String key) {
        return instance.getString(key, "");
    }

    public static String getUserPassword(String key) {
        return instance.getString(key, "");
    }

    public static String getUserToken(String key) {
        return instance.getString(key, "");
    }
}

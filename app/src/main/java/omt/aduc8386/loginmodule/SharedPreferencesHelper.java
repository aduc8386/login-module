package omt.aduc8386.loginmodule;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    public static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    public static final String REMEMBER_ME = "REMEMBER_ME";
    public static final String USER_TOKEN = "USER_TOKEN";

    private static SharedPreferences instance;

    @SuppressLint("CommitPrefEdits")
    public static SharedPreferences getInstance(Context context) {
        if (instance == null)
            instance = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return instance;
    }

}

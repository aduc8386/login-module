package omt.aduc8386.loginmodule;

import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.api.Service;
import omt.aduc8386.loginmodule.controller.helper.RealmHelper;
import omt.aduc8386.loginmodule.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceHelper {

    private static Service instance;

    private static Service getInstance() {
        if(instance == null) instance = AppService.init();
        return instance;
    }

    public static void updateUser(User user) {
        getInstance().updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}

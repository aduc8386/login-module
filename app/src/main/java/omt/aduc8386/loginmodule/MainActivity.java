package omt.aduc8386.loginmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import io.realm.RealmList;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.model.User;
import omt.aduc8386.loginmodule.model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private RecyclerView rcvUserList;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        callApi();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = SharedPreferencesHelper.getInstance().edit();

                editor.putBoolean(SharedPreferencesHelper.REMEMBER_ME, false);
                editor.putString(SharedPreferencesHelper.USER_EMAIL, "");
                editor.putString(SharedPreferencesHelper.USER_PASSWORD, "");
                editor.apply();
            }
        });
    }

    private void callApi() {
        AppService.init().getUsers().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful() && response.body() != null) {

                    users = response.body().getUsers();

                    showUserList(users);

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void showUserList(List<User> users) {
        UserAdapter userAdapter = new UserAdapter(users);
        rcvUserList.setAdapter(userAdapter);
    }

    private void bindView() {
        rcvUserList = findViewById(R.id.rcv_user_list);
        btnLogout = findViewById(R.id.btn_logout);
        rcvUserList.setLayoutManager(new LinearLayoutManager(this));
    }

}
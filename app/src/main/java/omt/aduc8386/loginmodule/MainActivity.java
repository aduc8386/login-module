package omt.aduc8386.loginmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.model.User;
import omt.aduc8386.loginmodule.model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private Button btnAddUser;
    private RecyclerView rcvUserList;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        callApi();

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = SharedPreferencesHelper.getInstance().edit();

                editor.putBoolean(SharedPreferencesHelper.REMEMBER_ME, false);
                editor.putString(SharedPreferencesHelper.USER_EMAIL, "");
                editor.putString(SharedPreferencesHelper.USER_PASSWORD, "");
                editor.putString(SharedPreferencesHelper.USER_TOKEN, "");
                editor.apply();

                Intent intent = new Intent(getBaseContext(), LoginActivity.class);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void callApi() {
        AppService.init().getUsers(2).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Api called successful", Toast.LENGTH_SHORT).show();
                    users = response.body().getUsers();

                    RealmHelper realmHelper = new RealmHelper();

                    for (User user : users) {
                        realmHelper.insertToRealm(user);
                    }

                    List<User> userRealmResults = realmHelper.getInstance().where(User.class).findAll();

                    showUserList(userRealmResults);

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Api call failed", Toast.LENGTH_SHORT).show();
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
        btnAddUser = findViewById(R.id.btn_add_user);
        rcvUserList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isLoggedIn()) {
            finishAffinity();
            System.exit(0);
        }
    }

    private boolean isLoggedIn() {
        return !SharedPreferencesHelper.getUserToken(SharedPreferencesHelper.USER_TOKEN).isEmpty();
    }
}
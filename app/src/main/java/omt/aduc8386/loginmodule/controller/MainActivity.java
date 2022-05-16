package omt.aduc8386.loginmodule.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.helper.RealmHelper;
import omt.aduc8386.loginmodule.helper.SharedPreferencesHelper;
import omt.aduc8386.loginmodule.model.User;
import omt.aduc8386.loginmodule.model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserListener {

    private RecyclerView rcvUserList;

    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        getUsers();
    }

    private void openAddUserDialog() {
        AddUserDialogFragment addUserDialogFragment = new AddUserDialogFragment();
        addUserDialogFragment.show(getSupportFragmentManager(), AddUserDialogFragment.TAG);
    }

    private void getUsers() {
        AppService.init().getUsers(2).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Api called successful", Toast.LENGTH_SHORT).show();
                    List<User> users = response.body().getUsers();

                    for (User user : users) {
                        RealmHelper.insertToRealm(user);
                    }
                    showUserList(users);
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Api call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUserList(List<User> users) {
        UserAdapter userAdapter = new UserAdapter(users, this);
        rcvUserList.setAdapter(userAdapter);
    }

    private void bindView() {
        rcvUserList = findViewById(R.id.rcv_user_list);
        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnAddUser = findViewById(R.id.btn_add_user);
        rcvUserList.setLayoutManager(new LinearLayoutManager(this));

        btnAddUser.setOnClickListener(v -> openAddUserDialog());

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = SharedPreferencesHelper.getInstance().edit();

            editor.putBoolean(SharedPreferencesHelper.REMEMBER_ME, false);
            editor.putString(SharedPreferencesHelper.USER_EMAIL, "");
            editor.putString(SharedPreferencesHelper.USER_PASSWORD, "");
            editor.putString(SharedPreferencesHelper.USER_TOKEN, "");
            editor.apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onUserClick(int userId) {
        UpdateUserDialogFragment updateUserDialogFragment = UpdateUserDialogFragment.newInstance(userId);
        updateUserDialogFragment.show(getSupportFragmentManager(), UpdateUserDialogFragment.TAG);
    }
}
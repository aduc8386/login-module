package omt.aduc8386.loginmodule.controller;

import android.content.Intent;
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
    private Button btnLogout;
    private Button btnAddUser;

    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        getUsers();
    }

    private void openAddUserDialog() {
        AddUserDialogFragment addUserDialogFragment = new AddUserDialogFragment(new AddUserDialogFragment.OnAddUserListener() {
            @Override
            public void onSuccess() {
                getUsers();
            }

            @Override
            public void onFailure() {

            }
        });
        addUserDialogFragment.show(getSupportFragmentManager(), AddUserDialogFragment.TAG);
    }

    private void getUsers() {
        AppService.init().getUsers(2).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Api called successful", Toast.LENGTH_SHORT).show();
                    List<User> users = response.body().getUsers();

                    RealmHelper.insertOrUpdateUsersToRealm(users);
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
        btnLogout = findViewById(R.id.btn_logout);
        btnAddUser = findViewById(R.id.btn_add_user);
        rcvUserList.setLayoutManager(new LinearLayoutManager(this));

        btnAddUser.setOnClickListener(v -> openAddUserDialog());

        btnLogout.setOnClickListener(v -> {
            SharedPreferencesHelper.setUserEmail("");
            SharedPreferencesHelper.setUserPassword("");
            SharedPreferencesHelper.setRememberMe(false);
            SharedPreferencesHelper.setUserToken("");

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onUserClick(int userId) {
        UpdateUserDialogFragment updateUserDialogFragment = UpdateUserDialogFragment.newInstance(userId, new UpdateUserDialogFragment.OnUpdateUserListener() {
            @Override
            public void onSuccess() {
                getUsers();
            }

            @Override
            public void onFailure() {

            }
        });
        updateUserDialogFragment.show(getSupportFragmentManager(), UpdateUserDialogFragment.TAG);
    }
}
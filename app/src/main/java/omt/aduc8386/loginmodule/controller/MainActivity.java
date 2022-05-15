package omt.aduc8386.loginmodule.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private Button btnLogout;
    private Button btnAddUser;
    private RecyclerView rcvUserList;
    private RealmHelper realmHelper;

    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        getUsers();

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddUserDialog();
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

    private void openAddUserDialog() {
        AddUserFragment addUserFragment = new AddUserFragment();
        addUserFragment.show(getSupportFragmentManager(), "ADD_USER_FRAGMENT");

    }

    private void getUsers() {
        AppService.init().getUsers(2).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Api called successful", Toast.LENGTH_SHORT).show();
                    List<User> users = response.body().getUsers();

                    realmHelper = new RealmHelper();

                    for (User user : users) {
                        realmHelper.insertToRealm(user);
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
        btnLogout = findViewById(R.id.btn_logout);
        btnAddUser = findViewById(R.id.btn_add_user);
        rcvUserList.setLayoutManager(new LinearLayoutManager(this));
    }

    private boolean isLoggedIn() {
        return !SharedPreferencesHelper.getUserToken(SharedPreferencesHelper.USER_TOKEN).isEmpty();
    }

    @Override
    public void onUserClick(int userId) {

        Bundle bundle = new Bundle();
        bundle.putInt(USER_ID, userId);
        UpdateUserFragment updateUserFragment = new UpdateUserFragment();
        updateUserFragment.setArguments(bundle);
        updateUserFragment.show(getSupportFragmentManager(), "USER_INFO_FRAGMENT");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isLoggedIn()) {
            finishAffinity();
            System.exit(0);
        }
    }
}
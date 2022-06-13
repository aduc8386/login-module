package omt.aduc8386.loginmodule.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.controller.helper.RealmHelper;
import omt.aduc8386.loginmodule.controller.helper.SharedPreferencesHelper;
import omt.aduc8386.loginmodule.model.User;
import omt.aduc8386.loginmodule.model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserListener {

    @BindView(R.id.rcv_user_list) RecyclerView rcvUserList;
    private Button btnLogout;
    private Button btnAddUser;

    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkIsNotLoggedIn()) return;

        setContentView(R.layout.activity_main);
        bindView();

        getUsers();
    }

    private boolean checkIsNotLoggedIn() {
        String userToken = SharedPreferencesHelper.getUserToken();

        if (userToken.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    private void openAddUserDialog() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        AddUserDialogFragment addUserDialogFragment = new AddUserDialogFragment(new AddUserDialogFragment.OnAddUserListener() {

            @Override
            public void onSuccess() {
                getUsers();
            }

            @Override
            public void onFailure() {

            }
        });
        addUserDialogFragment.show(fragmentTransaction, AddUserDialogFragment.TAG);
    }

    private void getUsers() {
        AppService.init().getUsers(2).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("TAG", "onResponse: api called successful");
                    List<User> users = response.body().getUsers();

                    RealmHelper.insertOrUpdateUsersToRealm(users);
                    showUserList(users);
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: api call failed");
            }
        });
    }

    private void showUserList(List<User> users) {
        UserAdapter userAdapter = new UserAdapter(users, this);
        rcvUserList.setAdapter(userAdapter);
    }

    private void bindView() {
        ButterKnife.bind(this);
//        rcvUserList = findViewById(R.id.rcv_user_list);
        btnLogout = findViewById(R.id.btn_logout);
        btnAddUser = findViewById(R.id.btn_add_user);
        rcvUserList.setLayoutManager(new LinearLayoutManager(this));

        btnAddUser.setOnClickListener(v -> openAddUserDialog());

        btnLogout.setOnClickListener(v -> {
            SharedPreferencesHelper.setUserPassword("");
            SharedPreferencesHelper.setRememberMe(false);
            SharedPreferencesHelper.setUserToken("");

            Intent intent = new Intent(this, LoginActivity.class);

            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onUserClick(int userId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DialogFragment updateUserDialogFragment = UpdateUserDialogFragment.newInstance(userId, new UpdateUserDialogFragment.OnUpdateUserListener() {
            @Override
            public void onSuccess() {
                getUsers();
            }

            @Override
            public void onFailure() {

            }
        });

        updateUserDialogFragment.show(fragmentTransaction, UpdateUserDialogFragment.TAG);
    }
}
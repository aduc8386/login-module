package omt.aduc8386.loginmodule.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.helper.RealmHelper;
import omt.aduc8386.loginmodule.helper.SharedPreferencesHelper;
import omt.aduc8386.loginmodule.model.Account;
import omt.aduc8386.loginmodule.model.MyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private CheckBox cbRememberMe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferencesHelper.init(this);
        Realm.init(this);
        RealmHelper.init();

        bindView();

        if(checkRememberAccount()) return;
    }

    private boolean checkRememberAccount() {
        boolean rememberMe = SharedPreferencesHelper.getRememberMeCheck(SharedPreferencesHelper.REMEMBER_ME);
        String userEmail = SharedPreferencesHelper.getUserEmail(SharedPreferencesHelper.USER_EMAIL);
        String userPassword = SharedPreferencesHelper.getUserPassword(SharedPreferencesHelper.USER_PASSWORD);

        if(rememberMe) {
            Account userAccount = new Account(userEmail, userPassword);
            edtEmail.setText(userEmail);
            edtPassword.setText(userPassword);
            cbRememberMe.setChecked(true);
            login(userAccount);
            return true;
        }

        return false;
    }

    private void login(Account account) {
        AppService.init().login(account).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                MyResponse myResponse = response.body();

                if (response.code() == 200 && myResponse != null) {
                    String authToken = myResponse.getAuthToken();
                    SharedPreferences.Editor editor = SharedPreferencesHelper.getInstance().edit();
                    editor.putString(SharedPreferencesHelper.USER_TOKEN, authToken);
                    editor.putString(SharedPreferencesHelper.USER_EMAIL, account.getEmail());
                    editor.putString(SharedPreferencesHelper.USER_PASSWORD, account.getPassword());
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, String.format("Token: %s", authToken), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else if(account.getEmail().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else if(account.getPassword().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Missing password", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(LoginActivity.this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindView() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        cbRememberMe = findViewById(R.id.cb_remember_me);
        Button btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(view -> {

            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            Account account = new Account(email, password);

            login(account);
        });

        cbRememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = SharedPreferencesHelper.getInstance().edit();

            if (buttonView.isChecked()) {
                editor.putBoolean(SharedPreferencesHelper.REMEMBER_ME, true);
                Toast.makeText(LoginActivity.this, "Remember password", Toast.LENGTH_SHORT).show();
            } else {
                editor.putBoolean(SharedPreferencesHelper.REMEMBER_ME, false);
                editor.putString(SharedPreferencesHelper.USER_EMAIL, "");
                editor.putString(SharedPreferencesHelper.USER_PASSWORD, "");
                Toast.makeText(LoginActivity.this, "Forgot password", Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        });
    }
}

package omt.aduc8386.loginmodule.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.controller.helper.SharedPreferencesHelper;
import omt.aduc8386.loginmodule.model.Account;
import omt.aduc8386.loginmodule.model.MyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        checkRememberAccount();
    }

    private void checkRememberAccount() {
        boolean rememberMe = SharedPreferencesHelper.getRememberMeCheck();

        if (rememberMe) {
            String userEmail = SharedPreferencesHelper.getUserEmail();
            String userPassword = SharedPreferencesHelper.getUserPassword();
            Account userAccount = new Account(userEmail, userPassword);
            edtEmail.setText(userEmail);
            edtPassword.setText(userPassword);
            cbRememberMe.setChecked(true);
            login(userAccount);
        }
    }

    private void login(Account account) {
        AppService.init().login(account).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                MyResponse myResponse = response.body();

                if (response.code() == 200 && myResponse != null) {
                    String authToken = myResponse.getAuthToken();
                    SharedPreferencesHelper.setUserToken(authToken);
                    SharedPreferencesHelper.setUserEmail(account.getEmail());
                    SharedPreferencesHelper.setUserPassword(account.getPassword());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else if (account.getEmail().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else if (account.getPassword().isEmpty()) {
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
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            Account account = new Account(email, password);

            login(account);
        });

        cbRememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                SharedPreferencesHelper.setRememberMe(true);
            } else {
                SharedPreferencesHelper.setUserEmail("");
                SharedPreferencesHelper.setUserPassword("");
                SharedPreferencesHelper.setRememberMe(false);
            }
        });
    }
}

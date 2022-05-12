package omt.aduc8386.loginmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import omt.aduc8386.loginmodule.api.AppService;
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
    private Button btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferencesHelper.init(this);

        bindView();

        boolean rememberMe = SharedPreferencesHelper.getRememberMeCheck(SharedPreferencesHelper.REMEMBER_ME);
        String userEmail = SharedPreferencesHelper.getUserEmail(SharedPreferencesHelper.USER_EMAIL);
        String userPassword = SharedPreferencesHelper.getUserPassword(SharedPreferencesHelper.USER_PASSWORD);

        Account userAccount = new Account(userEmail, userPassword);

        if(rememberMe) login(userAccount);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                Account account = new Account(email, password);

                login(account);
            }
        });

        cbRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = SharedPreferencesHelper.getInstance().edit();

                if (buttonView.isChecked()) {
                    editor.putBoolean(SharedPreferencesHelper.REMEMBER_ME, true);
                    Toast.makeText(LoginActivity.this, "Remember password", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean(SharedPreferencesHelper.REMEMBER_ME, false);
                    Toast.makeText(LoginActivity.this, "Forgot password", Toast.LENGTH_SHORT).show();
                }

                editor.apply();
            }
        });
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
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else if(account.getPassword().trim().isEmpty()) {
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
        btnSignUp = findViewById(R.id.btn_sign_up);
    }
}

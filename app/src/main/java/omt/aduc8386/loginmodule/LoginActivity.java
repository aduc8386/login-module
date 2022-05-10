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

import org.json.JSONException;
import org.json.JSONObject;

import omt.aduc8386.loginmodule.api.AppService;
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

        bindView();

        boolean remember_me = SharedPreferencesHelper.getInstance(this).getBoolean(SharedPreferencesHelper.REMEMBER_ME, false);

        if (remember_me) {
            cbRememberMe.setChecked(true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

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
                SharedPreferences.Editor editor = SharedPreferencesHelper.getInstance(getApplicationContext()).edit();

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
        AppService.init().login(account).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 200 && response.body() != null) {
                    Token token = response.body();
                    String authToken = token.getAuthToken();
                    SharedPreferences.Editor editor = SharedPreferencesHelper.getInstance(getApplicationContext()).edit();
                    editor.putString(SharedPreferencesHelper.USER_TOKEN, authToken);
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, authToken, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
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

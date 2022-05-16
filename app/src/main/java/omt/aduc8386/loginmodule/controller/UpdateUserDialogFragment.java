package omt.aduc8386.loginmodule.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserDialogFragment extends DialogFragment {

    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private ImageView ivAvatar;

    private Context context;

    private int userId;

    public static final String TAG = "UPDATE_USER_DIALOG_FRAGMENT";

    public static UpdateUserDialogFragment newInstance(int userId) {
        UpdateUserDialogFragment updateUserDialogFragment = new UpdateUserDialogFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(MainActivity.USER_ID, userId);
        updateUserDialogFragment.setArguments(bundle);

        return updateUserDialogFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AlertDialog_AppCompat_RoundedBackground);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_user_dialog, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {
        edtFirstName = view.findViewById(R.id.edt_update_first_name);
        edtLastName = view.findViewById(R.id.edt_update_last_name);
        edtEmail = view.findViewById(R.id.edt_update_email);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnUpdate = view.findViewById(R.id.btn_update);

        btnCancel.setOnClickListener(v -> UpdateUserDialogFragment.this.getDialog().cancel());

        if(getArguments() != null) {
            userId = getArguments().getInt(MainActivity.USER_ID);

            getUser(userId);

            btnUpdate.setOnClickListener(v -> {
                String firstName = edtFirstName.getText().toString();
                String lastName = edtLastName.getText().toString();
                String email = edtEmail.getText().toString();

                User user = new User(firstName, lastName, email);

                updateUser(userId, user);

                UpdateUserDialogFragment.this.getDialog().cancel();
            });
        }
    }

    private void getUser(int userId) {
        AppService.init().getUserById(userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body() != null && response.isSuccessful()) {
                    Toast.makeText(context, "Get user information successful", Toast.LENGTH_SHORT).show();

                    String userJson = response.body().getAsJsonObject("data").toString();
                    Gson gson = new Gson();
                    User user = gson.fromJson(userJson, User.class);

                    edtFirstName.setText(user.getFirstName());
                    edtLastName.setText(user.getLastName());
                    edtEmail.setText(user.getEmail());
                    Glide.with(ivAvatar.getContext())
                            .load(user.getAvatar())
                            .centerCrop()
                            .error(R.drawable.avatar)
                            .into(ivAvatar);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "Get user information failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(int userId, User user) {
        AppService.init().updateUser(userId, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "User update fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

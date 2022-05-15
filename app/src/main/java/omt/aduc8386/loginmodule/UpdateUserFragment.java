package omt.aduc8386.loginmodule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserFragment extends DialogFragment {

    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private ImageView ivAvatar;
    private Button btnCancel;
    private Button btnUpdate;

    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog_AppCompat_RoundedBackground);

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_update_user_dialog, null);
        bindView(view);

        int userId = -1;

        if(getArguments() != null) {
            userId = getArguments().getInt(MainActivity.USER_ID);
        }

        getUser(userId);
        int finalUserId = userId;

        btnCancel.setOnClickListener(v -> UpdateUserFragment.this.getDialog().cancel());


        btnUpdate.setOnClickListener(v -> {
            String firstName = edtFirstName.getText().toString();
            String lastName = edtLastName.getText().toString();
            String email = edtEmail.getText().toString();

            User user = new User(firstName, lastName, email);

            updateUser(finalUserId, user);

            UpdateUserFragment.this.getDialog().cancel();
        });

        builder.setView(view);
        return builder.create();
    }



    private void bindView(View view) {
        edtFirstName = view.findViewById(R.id.edt_update_first_name);
        edtLastName = view.findViewById(R.id.edt_update_last_name);
        edtEmail = view.findViewById(R.id.edt_update_email);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnUpdate = view.findViewById(R.id.btn_update);
    }

    private void getUser(int userId) {
        AppService.init().getUserById(userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body() != null && response.isSuccessful()) {

                    String userJson = response.body().getAsJsonObject("data").toString();
                    Gson gson = new Gson();
                    User user = gson.fromJson(userJson, User.class);

                    edtFirstName.setText(user.getFirstName());
                    edtLastName.setText(user.getLastName());
                    edtEmail.setText(user.getEmail());
                    Glide.with(getContext())
                            .load(user.getAvatar())
                            .centerCrop()
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}

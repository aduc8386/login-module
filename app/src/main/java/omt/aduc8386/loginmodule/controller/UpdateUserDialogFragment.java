package omt.aduc8386.loginmodule.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.realm.Realm;
import omt.aduc8386.loginmodule.LoadingDialogFragment;
import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.helper.RealmHelper;
import omt.aduc8386.loginmodule.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserDialogFragment extends DialogFragment {

    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private ImageView ivAvatar;
    private Button btnCancel;
    private Button btnUpdate;

    private OnUpdateUserListener onUpdateUserListener;

    private int userId = -1;

    public static final String TAG = "UPDATE_USER_DIALOG_FRAGMENT";

    public UpdateUserDialogFragment() {
        super(R.layout.fragment_update_user_dialog);
    }

    public static UpdateUserDialogFragment newInstance(int userId, OnUpdateUserListener onUpdateUserListener) {
        UpdateUserDialogFragment updateUserDialogFragment = new UpdateUserDialogFragment();
        updateUserDialogFragment.onUpdateUserListener = onUpdateUserListener;
        Bundle bundle = new Bundle();

        bundle.putInt(MainActivity.USER_ID, userId);
        updateUserDialogFragment.setArguments(bundle);

        return updateUserDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_AppCompat_RoundedBackground);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
    }

    private void bindView(View view) {
        edtFirstName = view.findViewById(R.id.edt_update_first_name);
        edtLastName = view.findViewById(R.id.edt_update_last_name);
        edtEmail = view.findViewById(R.id.edt_update_email);
        ivAvatar = view.findViewById(R.id.iv_avatar);
         btnCancel = view.findViewById(R.id.btn_cancel);
         btnUpdate = view.findViewById(R.id.btn_update);

        btnCancel.setOnClickListener(v -> dismiss());

        if(getArguments() != null) {
            userId = getArguments().getInt(MainActivity.USER_ID);
            getUser(userId);

            btnUpdate.setOnClickListener(v -> {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                LoadingDialogFragment loadingDialogFragment = new LoadingDialogFragment();
                loadingDialogFragment.show(fragmentTransaction, LoadingDialogFragment.TAG);
                String firstName = edtFirstName.getText().toString();
                String lastName = edtLastName.getText().toString();
                String email = edtEmail.getText().toString();
                User user = new User(userId, firstName, lastName, email);

                updateUser(user);
                loadingDialogFragment.dismiss();

            });
        }
    }

    private void getUser(int userId) {
        AppService.init().getUserById(userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body() != null && response.isSuccessful()) {
                    Log.d("TAG", "onResponse: get user information successful");

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
                Log.d("TAG", "onFailure: get user information failed");
            }
        });
    }

    private void updateUser(User user) {
        AppService.init().updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful() && response.body() != null){
                    Log.d("TAG", "onResponse: user information updated");
                    RealmHelper.insertOrUpdateUserToRealm(user);
                    if(onUpdateUserListener != null) onUpdateUserListener.onSuccess();
                }
                else Log.d("TAG", "onResponse: something went wrong");

                dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", "onFailure: user information update failed");
                if(onUpdateUserListener != null) onUpdateUserListener.onFailure();
                dismiss();
            }
        });
    }

    public interface OnUpdateUserListener {
        void onSuccess();

        void onFailure();
    }
}

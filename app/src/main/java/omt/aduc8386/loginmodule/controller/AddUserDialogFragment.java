package omt.aduc8386.loginmodule.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.controller.helper.RealmHelper;
import omt.aduc8386.loginmodule.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserDialogFragment extends DialogFragment {

    private EditText edtName;
    private EditText edtJob;
    private Button btnAdd;
    private Button btnCancel;
    private OnAddUserListener addUserListener;

    public final static String TAG = "ADD_USER_DIALOG_FRAGMENT";
    private LoadingDialogFragment loadingDialogFragment;

    public AddUserDialogFragment(OnAddUserListener addUserListener) {
        super(R.layout.fragment_add_user_dialog);
        this.addUserListener = addUserListener;
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
        edtName = view.findViewById(R.id.edt_name);
        edtJob = view.findViewById(R.id.edt_job);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnAdd = view.findViewById(R.id.btn_add);

        loadingDialogFragment = new LoadingDialogFragment();


        btnCancel.setOnClickListener(v -> dismiss());

        btnAdd.setOnClickListener(v -> {
            loadingDialogFragment.show(getParentFragmentManager(), LoadingDialogFragment.TAG);

            String name = edtName.getText().toString().trim();
            String job = edtJob.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Name can not be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(name, job);
            addUser(newUser);
        });
    }

    private void addUser(User newUser) {
        AppService.init().addUser(newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User newUserResponse = response.body();

                if (response.isSuccessful() && newUserResponse != null) {

                    Toast.makeText(getContext(), "User added", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onResponse: user added");

                    RealmHelper.insertOrUpdateUserToRealm(newUser);
                } else
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                if (addUserListener != null) {
                    addUserListener.onSuccess();
                }
                loadingDialogFragment.dismiss();
                dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (addUserListener != null) {
                    addUserListener.onFailure();
                }
                Toast.makeText(getContext(), "User add failed", Toast.LENGTH_SHORT).show();
                loadingDialogFragment.dismiss();
                dismiss();
            }
        });
    }

    public interface OnAddUserListener {
        void onSuccess();

        void onFailure();
    }

}

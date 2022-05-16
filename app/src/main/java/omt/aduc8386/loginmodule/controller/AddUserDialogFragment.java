package omt.aduc8386.loginmodule.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.api.AppService;
import omt.aduc8386.loginmodule.helper.RealmHelper;
import omt.aduc8386.loginmodule.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserDialogFragment extends DialogFragment {

    private EditText edtName;
    private EditText edtJob;
    private Context context;

    public final static String TAG = "ADD_USER_DIALOG_FRAGMENT";

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
        View view = inflater.inflate(R.layout.fragment_add_user_dialog, container, false);
        bindView(view);
        return view;
    }

    private void addUser(User newUser) {
        AppService.init().addUser(newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User newUserResponse = response.body();

                if(response.isSuccessful() && newUserResponse != null) {
                    Toast.makeText(context, "User added", Toast.LENGTH_SHORT).show();

                    RealmHelper.insertToRealm(newUser);
                }
                else Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "User add failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindView(View view) {
        edtName = view.findViewById(R.id.edt_name);
        edtJob = view.findViewById(R.id.edt_job);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnAdd = view.findViewById(R.id.btn_add);

        btnCancel.setOnClickListener(v -> AddUserDialogFragment.this.getDialog().cancel());

        btnAdd.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String job = edtJob.getText().toString().trim();

            if(name.isEmpty()) {
                Toast.makeText(getContext(), "Name can not be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(name, job);

            addUser(newUser);

            AddUserDialogFragment.this.getDialog().cancel();
        });
    }

}

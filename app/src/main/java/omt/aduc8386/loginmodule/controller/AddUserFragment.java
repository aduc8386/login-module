package omt.aduc8386.loginmodule.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class AddUserFragment extends DialogFragment {

    private EditText edtName;
    private EditText edtJob;
    private Button btnCancel;
    private Button btnAdd;
    private Context context;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog_AppCompat_RoundedBackground);

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_user_dialog, null);

        bindView(view);

        btnCancel.setOnClickListener(v -> AddUserFragment.this.getDialog().cancel());

        btnAdd.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String job = edtJob.getText().toString().trim();

            if(name.isEmpty()) {
                Toast.makeText(getContext(), "Name can not be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(name, job);

            addUser(newUser);

            AddUserFragment.this.getDialog().cancel();
        });

        builder.setView(view);
        return builder.create();
    }

    private void addUser(User newUser) {
        AppService.init().addUser(newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User newUserResponse = response.body();

                if(response.isSuccessful() && newUserResponse != null) {
                    Toast.makeText(context, "User added", Toast.LENGTH_SHORT).show();

                    RealmHelper realmHelper = new RealmHelper();
                    realmHelper.insertToRealm(newUser);
                }
                else Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "User add failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void bindView(View view) {
        edtName = view.findViewById(R.id.edt_name);
        edtJob = view.findViewById(R.id.edt_job);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnAdd = view.findViewById(R.id.btn_add);
    }

}

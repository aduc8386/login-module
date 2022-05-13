package omt.aduc8386.loginmodule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import omt.aduc8386.loginmodule.model.User;

public class AddUserFragment extends DialogFragment {

    private EditText edtName;
    private EditText edtJob;
    private Button btnCancel;
    private Button btnAdd;
    private AddUserListener addUserListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog_AppCompat_RoundedBackground);

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_user_dialog, null);

        edtName = view.findViewById(R.id.edt_name);
        edtJob = view.findViewById(R.id.edt_job);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnAdd = view.findViewById(R.id.btn_add);

        btnCancel.setOnClickListener(v -> AddUserFragment.this.getDialog().cancel());

        btnAdd.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String job = edtJob.getText().toString().trim();

            if(name.isEmpty()) {
                Toast.makeText(getContext(), "Name can not be empty", Toast.LENGTH_SHORT).show();
                AddUserFragment.this.getDialog().cancel();
            }

            User newUser = new User(name, job);

            addUserListener.addUser(newUser);
            AddUserFragment.this.getDialog().cancel();
        });




        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            addUserListener = (AddUserListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AddUserListener {
        void addUser(User user);
    }
}

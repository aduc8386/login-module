package omt.aduc8386.loginmodule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import omt.aduc8386.loginmodule.model.User;

public class AddUserFragment extends AppCompatDialogFragment {

    private EditText edtName;
    private EditText edtJob;
    private AddUserListener addUserListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_user_dialog, null);

        edtName = view.findViewById(R.id.edt_name);
        edtJob = view.findViewById(R.id.edt_job);

        builder.setView(view)
                .setTitle("Add user")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String job = edtJob.getText().toString().trim();

                    if(name.isEmpty()) return;

                    addUserListener.addUser(new User(name, job));
                });


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

package omt.aduc8386.loginmodule;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LoadingDialogFragment extends DialogFragment {

    public final static String TAG = "LOADING_DIALOG_FRAGMENT";

    public LoadingDialogFragment() {
        super(R.layout.fragment_loading_dialog);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_AppCompat_RoundedBackground);
    }


}

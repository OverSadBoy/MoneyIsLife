package Kazuki.moneyislife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;


public class DialogFragmentConfirm extends DialogFragment {

    private DialogListener listener = null;
    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialogi = new AlertDialog.Builder(getContext())
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog, null))
                .setPositiveButton("Да", (dialog, which) -> listener.positiveClick())
                .setNegativeButton("Нет", (dialog, which) -> listener.negativeClick())
                .create();
        return dialogi;

    }
}

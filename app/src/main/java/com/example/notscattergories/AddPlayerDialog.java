package com.example.notscattergories;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class AddPlayerDialog extends DialogFragment{
    public  interface AddPlayerDialogListener {
        public void onDialogPositiveClick(DialogFragment dialogFragment, String newPlayerName);
        public void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    AddPlayerDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Use Layout
        builder.setView(inflater.inflate(R.layout.dialog_add_player, null))
                .setPositiveButton("Add Player", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirm(dialog);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancel(dialog);
                    }
                });

        return builder.create();
    }



    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            //Make listener so name can be sent back
            listener = (AddPlayerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }
    }

    public void confirm(DialogInterface dialogInterface){
        String newPlayerName;
        EditText editText = (EditText) getDialog().findViewById(R.id.playerName);
        newPlayerName = editText.getText().toString();
        listener.onDialogPositiveClick(AddPlayerDialog.this, newPlayerName.trim());
    }
    public void cancel(DialogInterface dialogInterface){
        listener.onDialogNegativeClick(AddPlayerDialog.this);
    }


}

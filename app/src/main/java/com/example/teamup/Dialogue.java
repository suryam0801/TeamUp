package com.example.teamup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialogue extends AppCompatDialogFragment {
    private EditText shtdesc;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener=(DialogueInterfaceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement Doalogue Listener");
        }
    }

    private DialogueInterfaceListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialogue, null);


        builder.setView(view)
                .setTitle("Short Pitch")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String desc=shtdesc.getText().toString().trim();
                        listener.applydesc(desc);

                    }
                });
        shtdesc=view.findViewById(R.id.shrtdesc);
        return builder.create();
    }
    public interface DialogueInterfaceListener
    {
        void applydesc(String shortdesc);
    }
}

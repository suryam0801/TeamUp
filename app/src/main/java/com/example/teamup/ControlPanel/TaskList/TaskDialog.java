package com.example.teamup.ControlPanel.TaskList;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.teamup.R;


public class TaskDialog extends AppCompatDialogFragment {
    private EditText taskNameGet;
    private EditText taskDescriptionGet;
    private TaskDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.task_dialogue_layout, null);

        builder.setView(view)
                .setTitle("New Task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String taskName = taskNameGet.getText().toString();
                        String taskDescription = taskDescriptionGet.getText().toString();
                        listener.applyTexts(taskName, taskDescription);
                    }
                });

        taskNameGet = view.findViewById(R.id.edit_name);
        taskDescriptionGet = view.findViewById(R.id.edit_description);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (TaskDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement TaskDialogListener");
        }
    }

    public interface TaskDialogListener {
        void applyTexts(String username, String password);
    }
}
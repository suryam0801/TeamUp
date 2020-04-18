package com.example.teamup.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.teamup.chat.OnItemClick;
import com.example.teamup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class CustomChatDialogFragment extends DialogFragment implements OnItemClick {


    private ListView mListview;
    private Context context;
    public  final String TAG=this.getClass().getSimpleName();

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick)
    {
        this.onItemClick=onItemClick;
    }

    @Override
     public void onUserSelected(UsersModel usersModel){
        Log.d(TAG, "onUserSelected: "+usersModel.toString());
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.custom_chat_users_layout, null);
        final Dialog dialog=new Dialog(requireContext());
        dialog.setContentView(view);
        mListview=view.findViewById(R.id.listview_custom_users_layout);
        Bundle bundle=getArguments();
        assert bundle != null;
        final ArrayList<UsersModel> arrayList=bundle.getParcelableArrayList("users");
        assert arrayList != null;
        Log.d(TAG, "onCreateDialog: "+ Objects.requireNonNull(arrayList).toString());
        final String[] array=new String[arrayList.size()];
        for (int i=0;i<arrayList.size();i++)
        {
            array[i]=arrayList.get(i).getName();
        }
        ArrayAdapter adapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,array);
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClick.onUserSelected(arrayList.get(position));
                dismiss();
            }
        });
        return dialog;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

}

package com.example.teamup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamup.ControlPanel.DisplayApplicants.Applicant;
import com.example.teamup.Explore.Project;
import com.example.teamup.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddUsersChatActivity extends AppCompatActivity {

    private ListView mListView;
    private Project project;
    private String  currentUserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    private Button createGroupBtn;
    private Button selectAllButton;
    private EditText groupNameEditText;
    private ArrayList<UserModel> updateList;
    private ListAdapter adapter;

    public final String TAG=this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users_chat);
        groupNameEditText=findViewById(R.id.edit_text_group_name);
        createGroupBtn=findViewById(R.id.create_group_btn);
        selectAllButton=findViewById(R.id.select_all_btn);
        updateList=new ArrayList<>();
        mListView=findViewById(R.id.users_listview);
        project=getIntent().getParcelableExtra("project");
        populateData();
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=groupNameEditText.getText().toString().trim();
                if (!name.equals(""))
                createGroup(name);

            }
        });
        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < updateList.size(); i++) {
                    updateList.get(i).setChecked(true);
                    Log.d(TAG, "onClick: "+updateList.get(i).toString());
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void createGroup(final String groupName){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("groups").child(project.getProjectId());
        final List<String> list=new ArrayList<>();
        final List<String> membersList=new ArrayList<>();
        for (UserModel userModel:updateList)
        {
            if (userModel.isChecked())
            {
                membersList.add(userModel.getUserId());
            }
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    list.add(dataSnapshot1.getKey());
                }
                if (list.contains(groupName))
                {
                    Toast.makeText(getApplicationContext(), "Change group Name", Toast.LENGTH_LONG).show();
                }else{
                    if (membersList.size()>1) {
                        for (String member : membersList) {
                            databaseReference.child(groupName).child(member).setValue(true);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("message", "Success");
                        setResult(1, intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Select More than 1 user",Toast.LENGTH_LONG ).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateData()
    {
            List<UserModel> user=new ArrayList<>();
            List<Applicant> applicantList=project.getWorkersList();
            for (Applicant applicant:applicantList)
            {
                UserModel userModel=new UserModel();
                userModel.setUserId(applicant.getUserId());
                userModel.setChecked(userModel.isCurrentUser());
                userModel.setUserName(applicant.getApplicantName());
                if (userModel.isCurrentUser()) {
                    Log.d(TAG, "populateData: "+userModel.toString());
                    user.add(0,userModel);
                }else{
                    user.add(userModel);
                    Log.d(TAG, "populateData 1: "+userModel.toString());
                }
            }
            UserModel userModel=new UserModel();
            userModel.setUserName(project.getCreatorName());
            userModel.setUserId(project.getCreatorId());
            userModel.setChecked(userModel.isCurrentUser());
            user.add(0,userModel);
            updateList.addAll(user);
            adapter=new ListAdapter(getApplicationContext());
            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

    }


    class ListAdapter extends BaseAdapter{

        private Context mContext;
        public ListAdapter(Context context) {
            this.mContext=context;
        }

        @Override
        public int getCount() {
            return updateList.size();
        }

        @Override
        public Object getItem(int position) {
            return updateList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            UsersViewHolder viewHolder;
            if (convertView!=null)
            {
                viewHolder=(UsersViewHolder) convertView.getTag();
            }else {
                convertView=LayoutInflater.from(mContext).inflate(R.layout.custom_layout_users_with_checkbox, null);
                viewHolder=new UsersViewHolder(convertView);
            }
            final UserModel userModel=updateList.get(position);
            Log.d(TAG, "getView: "+userModel.toString());
            viewHolder.nameTextView.setText(userModel.getUserName());
            Log.d(TAG, "getView: "+userModel.isChecked);
            if (userModel.isCurrentUser()) {
                viewHolder.checkBox.setEnabled(false);
            }
            viewHolder.checkBox.setOnCheckedChangeListener(null);
            viewHolder.checkBox.setChecked(userModel.isChecked());
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!userModel.isCurrentUser()){
                        updateList.get(position).setChecked(isChecked);
                        Log.d(TAG, "onCheckedChanged: "+updateList.toString());
                    }
                }
            });
            notifyDataSetChanged();
            convertView.setTag(viewHolder);
            return convertView;
        }
    }


    public class UsersViewHolder extends RecyclerView.ViewHolder{

         TextView nameTextView;
         CheckBox checkBox;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.list_view_user_name_text);
            checkBox=itemView.findViewById(R.id.list_view_item_checkbox);

        }

    }


    private class UserModel{
        private String userName;
         private String userId;
         private boolean isChecked;
        public UserModel(String userName, String userId,boolean isChecked) {
            this.userName = userName;
            this.userId = userId;
            this.isChecked=isChecked;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public UserModel() {
        }

        private boolean isCurrentUser()
        {
            return currentUserId.equals(userId);
        }

        @Override
        public String toString() {
            return "UserModel{" +
                    "userName='" + userName + '\n' +
                    ", userId='" + userId + '\n' +
                    ", isChecked=" + isChecked +
                    '}'+"\n\n";
        }
    }
}

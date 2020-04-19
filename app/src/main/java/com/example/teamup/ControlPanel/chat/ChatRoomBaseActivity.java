package com.example.teamup.ControlPanel.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatRoomBaseActivity extends AppCompatActivity implements OnItemClick {


    private Drawer drawer;
    private Project project;
    private OnItemClick onItemClick;
    public final String TAG=this.getClass().getSimpleName();
    private final String currentUserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final String name=Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
    Toolbar toolbar;

    private Map<String,String> userModelMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_base);
        toolbar=findViewById(R.id.toolbar_chat_room);
        userModelMap=new HashMap<>();
        project= SessionStorage.getProject(this);
        setUpFragment(true, "general",null,"#General");
        setUpDrawer();
    }

    private void setUpDrawer()
    {
       // toolbar.setTitle(project.getProjectName());
        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withName("Groups")
                .withIcon(R.drawable.ic_add_circle_outline_black_24dp);
        item2.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent intent=new Intent(getApplicationContext(),AddUsersChatActivity.class);
                startActivityForResult(intent, 1);
                return false;
            }
        });
        final AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.ic_launcher_background)
                .addProfiles(
                        new ProfileDrawerItem().withName("Project").withIcon(getResources().getDrawable(R.drawable.round))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();

         drawer=new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.material_drawer_item_profile)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(item2).build();
         addAvailableGroups();
        PrimaryDrawerItem primaryDrawerItem = new PrimaryDrawerItem()
                .withName("Personal messages")
                .withIcon(R.drawable.ic_add_circle_outline_black_24dp);
        primaryDrawerItem.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                showUsersDialog();
                return false;
            }
        });
        drawer.addItem(primaryDrawerItem);
        addPersonalMessages();
    }


    public void addAvailableGroups(){
        SecondaryDrawerItem secondaryDrawerItem=new SecondaryDrawerItem().withName("#general");
        secondaryDrawerItem.withSetSelected(true);
        secondaryDrawerItem.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                setUpFragment(true, "general",null,"#General");
                return false;
            }
        });
        drawer.addItem(secondaryDrawerItem);

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("groups")
                .child(project.getProjectId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (final DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Iterable<DataSnapshot> dataSnapshots=dataSnapshot1.getChildren();
                    for (DataSnapshot ds:dataSnapshots)
                    {
                        if (ds.getKey().equals(currentUserId))
                        {
                            SecondaryDrawerItem secondaryDrawerItem=new SecondaryDrawerItem().withName("#"+dataSnapshot1.getKey());
                            drawer.addItemAtPosition(secondaryDrawerItem,3);
                            secondaryDrawerItem.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    Log.d(TAG, "onItemClick: hello"+dataSnapshot1.getKey());
                                    setUpFragment(true, dataSnapshot1.getKey(), null,"#"+dataSnapshot1.getKey());
                                    return false;
                                }
                            });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addPersonalMessages()
    {
        SecondaryDrawerItem secondaryDrawerItem=new SecondaryDrawerItem().withName(name+"(Me)");
        secondaryDrawerItem.withSetSelected(true);
        secondaryDrawerItem.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                UsersModel model=new UsersModel(name, currentUserId);
                setUpFragment(false, currentUserId,model,"#Me");
                return false;
            }
        });
        drawer.addItem(secondaryDrawerItem);
        loadPreviousMessages();
    }


    private void loadPreviousMessages(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("personal").child(project.getProjectId()).child(currentUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    final UsersModel model = dataSnapshot1.getValue(UsersModel.class);
                    assert model != null;
                    if (!userModelMap.containsKey(model.getUserId()))
                    {
                        if (!model.getUserId().equals(currentUserId)) {
                            SecondaryDrawerItem secondaryDrawerItem = new SecondaryDrawerItem().withName(model.getName());
                            drawer.addItem(secondaryDrawerItem);
                            secondaryDrawerItem.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    setUpFragment(false, model.getUserId(), model,"#"+model.getName());
                                    return false;
                                }
                            });
                        }
                        userModelMap.put(model.getUserId(),model.getName());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setUpFragment(boolean isGroup, String id, UsersModel usersModel,String title)
    {

        Log.d(TAG, "setUpFragment: "+project.toString());
        String tbTitle=project.getProjectName()+"\n"+title;
        Toolbar toolbar=findViewById(R.id.toolbar_chat_room);
        toolbar.setTitle(tbTitle);
        Log.d(TAG, "setUpFragment: "+id);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fragment=new ChatRoom();
        Bundle bundle=new Bundle();
        bundle.putString("projectId",project.getProjectId());
        bundle.putBoolean("isgroup", isGroup);
        bundle.putString("id", id);
        bundle.putString("userid",currentUserId);
        bundle.putParcelable("model", usersModel);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack(id);
        ft.replace(R.id.chat_activity_frame_layout, fragment);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            String message;
            if (Objects.requireNonNull(data).getStringExtra("message").equals("Success")){
                message="Group Created";
            }else{
                message="Group Not Created";
            }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            drawer.removeAllItems();
            setUpDrawer();
        }
    }


    private void showUsersDialog() {
        ArrayList<UsersModel> usersModelArrayList=new ArrayList<>();
        List<Applicant> list=project.getWorkersList();
        for (Applicant applicant:list)
        {
            if (!applicant.getUserId().equals(currentUserId)){
                usersModelArrayList.add(new UsersModel(applicant.getApplicantName(),applicant.getUserId()));
            }
        }
        if (!project.getCreatorId().equals(currentUserId))
        usersModelArrayList.add(new UsersModel(project.getCreatorName(),project.getCreatorId()));
        if (usersModelArrayList.size()>0) {
            CustomChatDialogFragment chatDialogFragment = new CustomChatDialogFragment();
            Bundle bundle = new Bundle();
            Log.d(TAG, "showUsersDialog: " + usersModelArrayList.toString());
            bundle.putParcelableArrayList("users", usersModelArrayList);
            chatDialogFragment.setArguments(bundle);
            onItemClick = this;
            chatDialogFragment.setOnItemClick(onItemClick);
            FragmentManager fm = getSupportFragmentManager();
            chatDialogFragment.show(fm, CustomChatDialogFragment.class.getName());
        }else{
            Toast.makeText(getApplicationContext(), "No Members to Chat", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUserSelected(UsersModel usersModel) {
        if (!userModelMap.containsKey(usersModel.getUserId()))
        {
            userModelMap.put(usersModel.getUserId(), usersModel.getName());
            SecondaryDrawerItem secondaryDrawerItem = new SecondaryDrawerItem().withName(usersModel.getName());
            setUpFragment(false, usersModel.getUserId(),usersModel,"#"+usersModel.getName());
            drawer.removeAllItems();
            setUpDrawer();
            drawer.addItem(secondaryDrawerItem);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

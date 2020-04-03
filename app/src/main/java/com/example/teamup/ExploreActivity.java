package com.example.teamup;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExploreActivity extends AppCompatActivity implements Dialogue.DialogueInterfaceListener {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth currentUser;
    SwipeMenuListView lvproject;
    private List<projects> ProjectList;
    ProgressBar progressBar;
    projects projects;

    private ProjectAdapter adapter;
    TextView apply;
    String proid="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        progressBar=findViewById(R.id.progress_bar);
        currentUser=FirebaseAuth.getInstance();
        projects=new projects();
        loadprojectlist();
    }
    public void loadprojectlist()
    {
        lvproject=(SwipeMenuListView) findViewById(R.id.listview);

        ProjectList=new ArrayList<>();
        if (ProjectList.size()>0)
            ProjectList.clear();

        db.collection("Projects")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot querySnapshot: task.getResult())
                        {
                            String proname=querySnapshot.getString("projectName");
                            String prodesc=querySnapshot.getString("projectDescription");
                            proid=querySnapshot.getString("projectId");
                            projects proj=new projects(proname, prodesc, proid);
                            ProjectList.add(proj);
                        }

                            adapter= new ProjectAdapter(getApplicationContext(),ProjectList);
                            lvproject.setAdapter(adapter);

                            SwipeMenuCreator creator = new SwipeMenuCreator() {

                                @Override
                                public void create(SwipeMenu menu) {
                                    // create "open" item
                                    SwipeMenuItem openItem = new SwipeMenuItem(
                                            getApplicationContext());
                                    // set item background
                                    openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                                            0xff)));
                                    // set item width
                                    openItem.setWidth(250);
                                    // set item title
                                    openItem.setTitle("Apply");
                                    // set item title fontsize
                                    openItem.setTitleSize(18);
                                    // set item title font color
                                    openItem.setTitleColor(Color.WHITE);
                                    // add to menu
                                    menu.addMenuItem(openItem);

                                    // create "delete" item
//                                    SwipeMenuItem deleteItem = new SwipeMenuItem(
//                                            getApplicationContext());
//                                    // set item background
//                                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                                            0x3F, 0x25)));
//                                    // set item width
//                                    deleteItem.setWidth(170);
//                                    // set a icon
//                                    deleteItem.setTitle("Apply");
////                                    deleteItem.setIcon(R.drawable.ic_phone);
//                                    // add to menu
//                                    menu.addMenuItem(deleteItem);
                                }
                            };

                            lvproject.setMenuCreator(creator);

                            lvproject.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                    switch (index) {
                                        case 0:
//                                            Toast.makeText(ExploreActivity.this,"Applied",Toast.LENGTH_SHORT).show();
                                            apply();
                                            break;
//                                        case 1:
//                                            Toast.makeText(ExploreActivity.this,"Apply",Toast.LENGTH_SHORT).show();
//                                            break;
                                    }
                                    // false : close the menu; true : not close the menu
                                    return false;
                                }
                            });


                            progressBar.setVisibility(View.INVISIBLE);

                        }else {
                            Log.d("Logger","No such Valid Item");
                            Toast.makeText(ExploreActivity.this,"No such Document",Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ExploreActivity.this,e.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void apply() {
//        Toast.makeText(ExploreActivity.this,"Clicked",Toast.LENGTH_LONG).show();
        Dialogue dialog=new Dialogue();
        dialog.show(getSupportFragmentManager(),"Dialogue");



    }

    @Override
    public void applydesc(String shortdesc) {
//        Toast.makeText(this,shortdesc,Toast.LENGTH_LONG).show();

        DocumentReference addedDocRef = db.collection("Projects").document();
        String refId = addedDocRef.getId();
        String applicanid=currentUser.getUid();
        String pitch=shortdesc;
        String pid=projects.getPid().toString();

        Map<String,Object> docData=new HashMap<>();
        docData.put("applicantID",applicanid);
        docData.put("pitch",pitch);
        docData.put("projectID",proid);

        db.collection("Applicants").add(docData);
        proid="";

        Toast.makeText(this,"Your Application Has been Successfully sended to Project Creator!!!",Toast.LENGTH_LONG).show();

    }
}
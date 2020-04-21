package com.example.teamup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.example.teamup.Explore.ExploreTab;
import com.example.teamup.Explore.ProjectAdapter;
import com.example.teamup.model.Project;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Objects;

public class TabbedActivityMain extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem exploreTab, workbenchTab;
    public PageAdapterMainPage pagerAdapter;
    FirebaseFirestore db;
    FirebaseAuth currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_tabbed_main);

        tabLayout = findViewById(R.id.main_tab_layout);
        exploreTab = findViewById(R.id.main_explore_tab);
        workbenchTab = findViewById(R.id.main_workbench_tab);
        viewPager = findViewById(R.id.main_viewpager);

        db=FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance();

        pagerAdapter = new PageAdapterMainPage(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        loadUser();

        Bundle bundle = new Bundle();
        bundle.putString("edttext", "From Activity");
// set Fragmentclass Arguments
        ExploreTab fragobj = new ExploreTab();
        fragobj.setArguments(bundle);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void loadUser(){
        DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                SessionStorage.saveUser(TabbedActivityMain.this, user);
            }
        });
    }

}

package com.example.teamup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.teamup.EditOrView.EditOrViewProfile;
import com.example.teamup.Explore.ExploreTab;
import com.example.teamup.Notification.NotificationActivity;
import com.example.teamup.Notification.SendNotification;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabbedActivityMain extends AppCompatActivity {

    private static final String TAG ="Tabbed Activity" ;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem exploreTab, workbenchTab;
    public PageAdapterMainPage pagerAdapter;
    private CircleImageView profPic;
    private ImageButton notificationBell;
    FirebaseFirestore db;
    FirebaseAuth currentUser;
    User user;
    ExploreTab fragobj;


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
        profPic = findViewById(R.id.mainActivity_profilePicture);
        notificationBell = findViewById(R.id.main_activity_notifications_bell);

        db=FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance();
        loadUser();

        DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                Log.d("TABBED ACTIVITY MAIN", user.toString());
                SessionStorage.saveUser(TabbedActivityMain.this, user);
                Glide.with(TabbedActivityMain.this)
                        .load(user.getProfileImageLink())
                        .placeholder(ContextCompat.getDrawable(TabbedActivityMain.this, R.drawable.ic_account_circle_black_24dp))
                        .into(profPic);
            }
        });

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TabbedActivityMain.this, EditOrViewProfile.class);
                intent.putExtra("userID", user.getUserId());
                intent.putExtra("flag", "owner");
                startActivity(intent);
            }
        });

        notificationBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TabbedActivityMain.this, NotificationActivity.class));
            }
        });

        pagerAdapter = new PageAdapterMainPage(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        Bundle bundle = new Bundle();
        bundle.putString("edttext", "From Activity");
// set Fragmentclass Arguments
        fragobj = new ExploreTab();
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

        SendNotification.sendnotification("application accepted", "86d0e543-39f7-4b24-93ee-cac279bd4701", "Testing", "VCZlNIGu44SzUMMD0ODvA5Yx5Py2");
        SendNotification.sendnotification("application rejected", "86d0e543-39f7-4b24-93ee-cac279bd4701", "My Running Group", "VCZlNIGu44SzUMMD0ODvA5Yx5Py2");
        SendNotification.sendnotification("", "86d0e543-39f7-4b24-93ee-cac279bd4701", "Testing", "VCZlNIGu44SzUMMD0ODvA5Yx5Py2");


    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    ((EditText) v).clearFocus();
                    ((EditText)v).setCursorVisible(false);
                    Log.d(TAG, "Tabbed: "+"cleared focus");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    ((EditText) v).setCursorVisible(false);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}

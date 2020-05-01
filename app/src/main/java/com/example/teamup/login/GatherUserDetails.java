package com.example.teamup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.teamup.ControlPanel.ProjectWall.ProjectWall;
import com.example.teamup.R;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GatherUserDetails extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Uri downloadUri;
    private CircleImageView profilePic;
    SharedPreferences pref;
    String primSkill, secSkill, loc, fName, lName, email, password, userId,profileImageLink,contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gather_user_details);
        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();


        final AutoCompleteTextView location=findViewById(R.id.location);
        final EditText firstname = findViewById(R.id.fname);
        final EditText lastname = findViewById(R.id.lname);
        final EditText primarySkill = findViewById(R.id.primarySkill);
        final EditText secondarySkill = findViewById(R.id.secondarySkill);
//        final EditText location = findViewById(R.id.location);
        Button register = findViewById(R.id.registerButton);
        Button profilepicButton = findViewById(R.id.profilePicSetterImage);
        profilePic=findViewById(R.id.profile_image);

        location.setAdapter(new PlaceAutoSuggestAdapter(GatherUserDetails.this,android.R.layout.simple_list_item_1));

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        profilepicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(GatherUserDetails.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(GatherUserDetails.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE);
                }
                else {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                }

            }
        });

        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("Address : ",location.getText().toString());
//                LatLng latLng=getLatLngFromAddress(location.getText().toString());
//                if(latLng!=null) {
//                    Log.d("Lat Lng : ", " " + latLng.latitude + " " + latLng.longitude);
//                    Address address=getAddressFromLatLng(latLng);
//                    if(address!=null) {
//                        Log.d("Address : ", "" + address.toString());
//                        Log.d("Address Line : ",""+address.getAddressLine(0));
//                        Log.d("Phone : ",""+address.getPhone());
//                        Log.d("Pin Code : ",""+address.getPostalCode());
//                        Log.d("Feature : ",""+address.getFeatureName());
//                        Log.d("More : ",""+address.getLocality());
//                    }
//                    else {
//                        Log.d("Adddress","Address Not Found");
//                    }
//                }
//                else {
//                    Log.d("Lat Lng","Lat Lng Not Found");
//                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fName = firstname.getText().toString();
                lName = lastname.getText().toString();
                primSkill = primarySkill.getText().toString();
                secSkill = secondarySkill.getText().toString();
                loc = location.getText().toString();
//                contact = "+91"+phn_num.getText().toString();
                contact=pref.getString("key_name5", null);
                registerFunction();
            }
        });
    }

//    private LatLng getLatLngFromAddress(String toString) {
//        Geocoder geocoder=new Geocoder(GatherUserDetails.this);
//        List<Address> addressList;
//
//        try {
//            addressList = geocoder.getFromLocationName(address, 1);
//            if(addressList!=null){
//                Address singleaddress=addressList.get(0);
//                LatLng latLng=new LatLng(singleaddress.getLatitude(),singleaddress.getLongitude());
//                return latLng;
//            }
//            else{
//                return null;
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(GatherUserDetails.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(GatherUserDetails.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            if(filePath != null)
            {

                final ProgressDialog progressDialog = new ProgressDialog(GatherUserDetails.this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                String id= UUID.randomUUID().toString();
                final StorageReference profileRef = storageReference.child("ProfilePics/"+id);

                profileRef.putFile(filePath).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                })
                        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return  profileRef.getDownloadUrl();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressDialog.dismiss();
                        //and displaying a success toast
                        Toast.makeText(getApplicationContext(), "Profile Pic Uploaded ", Toast.LENGTH_LONG).show();
                        downloadUri=uri;
                        Glide.with(GatherUserDetails.this).load(downloadUri.toString()).into(profilePic);

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });


            }

        }
    }

    private void addUserToCollection() {

        String token_id= FirebaseInstanceId.getInstance().getToken();
        User user = new User(fName,lName,contact,primSkill,secSkill,loc,userId,downloadUri.toString(), 0, 0, 0,token_id);
        db.collection("Users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void registerFunction(){
        Log.d("GATHERUSERDETAILS: ", "Function Called25");
        if(!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName))
        {
            Log.d("GATHERUSERDETAILS: ", "Function Called");
             userId = firebaseAuth.getInstance().getCurrentUser().getUid();
             Log.d("GATHERUSERDETAILS: ", userId);
             addUserToCollection();
             UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                     .setDisplayName(fName+" "+lName)
                     .build();
                firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(GatherUserDetails.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(GatherUserDetails.this, TabbedActivityMain.class));
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(GatherUserDetails.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        firebaseAuth.signOut();
                                        firebaseAuth.getCurrentUser().delete();
                                    } }
                     });
        }
        else {
            Toast.makeText(GatherUserDetails.this,"Enter Valid details",Toast.LENGTH_LONG).show();
        }
    }
//    private Address getAddressFromLatLng(LatLng latLng){
//        Geocoder geocoder=new Geocoder(GatherUserDetails.this);
//        List<Address> addresses;
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
//            if(addresses!=null){
//                Address address=addresses.get(0);
//                return address;
//            }
//            else{
//                return null;
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//
//    }
}
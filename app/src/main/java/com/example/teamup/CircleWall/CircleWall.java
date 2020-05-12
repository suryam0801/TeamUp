package com.example.teamup.CircleWall;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.teamup.model.Broadcast;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Poll;
import com.example.teamup.model.ProjectWallDataClass;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CircleWall extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 100;
    private FloatingActionButton fab;

    private ListView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private Broadcast broadcast;
    private Uri filePath = null;
    private LinearLayout fileSelect, pollCreateDisplay, pollCreateAnswerOptionsDisplay, newFilePollTextview;
    private Button addOption, sendBroadcast;
    private ArrayList<ProjectWallDataClass> arrayList;
    private List<Poll> pollList = new ArrayList<>();
    private List<String> pollAnswerOptionsList = null, retrievalPollOptions = new ArrayList<>();
    private LinearLayout emptyPlaceHolder;
    private String uniqueID, downloadUri;
    private ImageButton back;
    private ImageView uploadFileCloudButton;
    private TextView uploadFileTextView, createPoll, orFillerTextView, uploadFile;
    private EditText broadcastDescription, pollCreateQuestionEntry, pollCreateAnswerEntry;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View dialogue;
    private StorageReference storageReference;
    private int counter = 0;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_project_wall);

        fab = findViewById(R.id.newResourceFAB);

        arrayList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        emptyPlaceHolder = findViewById(R.id.projectWall_empty_display);

        recyclerView = findViewById(R.id.projectViewRecyclerView);
        back = findViewById(R.id.bck_projectwall);
        broadcast = SessionStorage.getProject(this);
        uniqueID = UUID.randomUUID().toString();

        final CircleWallAdapter circleWallAdapter = new CircleWallAdapter(CircleWall.this, arrayList, pollList);


        final Query query = firebaseFirestore.collection("ProjectWall").document(broadcast.getBroadcastId())
                .collection("Broadcasts").orderBy("Time", Query.Direction.DESCENDING);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(ProjectWall.this, ControlPanel.class));
                finish();*/
            }
        });

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    arrayList.clear();
                    for (DocumentSnapshot doc : task.getResult()) {
                        final ProjectWallDataClass projectWallDataClass = doc.toObject(ProjectWallDataClass.class);
                        Log.d("CIRCLE WALL CLASS", projectWallDataClass.toString());
                        if(projectWallDataClass.isHasPoll() == true){
                            arrayList.add(projectWallDataClass);
                        } else {

                        }
                    }
                    recyclerView.setAdapter(circleWallAdapter);
                    circleWallAdapter.notifyDataSetChanged();

                    if (arrayList.isEmpty())
                        emptyPlaceHolder.setVisibility(View.VISIBLE);
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder = new AlertDialog.Builder(CircleWall.this);

                inflater = getLayoutInflater();

                dialogue = inflater.inflate(R.layout.file_upload_alert_layout_1, null);

                builder.setView(dialogue);

                fileSelect = dialogue.findViewById(R.id.fileSelectAlertButton);
                newFilePollTextview = dialogue.findViewById(R.id.new_file_or_poll_layout);
                uploadFileCloudButton = dialogue.findViewById(R.id.create_broadcast_file_upload_cloud_image_button);
                uploadFileTextView = dialogue.findViewById(R.id.create_broadcast_file_upload_text);
                broadcastDescription = dialogue.findViewById(R.id.fileDescriptionEditText);
                pollCreateDisplay = dialogue.findViewById(R.id.poll_create_layout);
                pollCreateAnswerOptionsDisplay = dialogue.findViewById(R.id.poll_create_answer_option_display);
                createPoll = dialogue.findViewById(R.id.poll_add_button);
                uploadFile = dialogue.findViewById(R.id.upload_file_button);
                addOption = dialogue.findViewById(R.id.poll_create_answer_option_add_buttom);
                pollCreateQuestionEntry = dialogue.findViewById(R.id.poll_create_question_editText);
                pollCreateAnswerEntry = dialogue.findViewById(R.id.poll_create_answer_option_editText);
                sendBroadcast = dialogue.findViewById(R.id.send_broadcast_message);
                orFillerTextView = dialogue.findViewById(R.id.upload_or_poll_or_textview);

                fileSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(CircleWall.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CircleWall.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE);
                        } else {
                            pickFile();
                        }
                    }
                });

                createPoll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        counter = counter + 1;
                        pollCreateDisplay.setVisibility(View.VISIBLE);
                        createPoll.setVisibility(View.GONE);
                        if(counter == 2)
                            newFilePollTextview.setVisibility(View.GONE);
                        else
                            orFillerTextView.setVisibility(View.GONE);
                    }
                });

                uploadFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        counter = counter + 1;
                        fileSelect.setVisibility(View.VISIBLE);
                        uploadFile.setVisibility(View.GONE);
                        if(counter == 2)
                            newFilePollTextview.setVisibility(View.GONE);
                        else
                            orFillerTextView.setVisibility(View.GONE);
                    }
                });

                pollAnswerOptionsList = new ArrayList<>();
                addOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!pollCreateAnswerEntry.getText().toString().isEmpty() && !pollCreateQuestionEntry.getText().toString().isEmpty()) {
                            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lparams.setMargins(0, 10, 20, 0);
                            final TextView tv = new TextView(CircleWall.this);
                            tv.setLayoutParams(lparams);
                            tv.setText(pollCreateAnswerEntry.getText().toString());
                            tv.setTextColor(Color.BLACK);
                            tv.setBackground(getResources().getDrawable(R.drawable.light_blue_nav_button));
                            tv.setGravity(Gravity.CENTER);
                            tv.setCompoundDrawablesWithIntrinsicBounds(0,0 , R.drawable.ic_clear_black_24dp, 0);
                            tv.setPaddingRelative(10,10,10,10);
                            tv.setTextColor(Color.parseColor("#6CACFF"));
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pollCreateAnswerOptionsDisplay.removeView(tv);
                                    pollAnswerOptionsList.remove(tv.getText());
                                    Log.d("CIRCLE WALL, ", pollAnswerOptionsList.toString());
                                }
                            });
                            pollAnswerOptionsList.add(pollCreateAnswerEntry.getText().toString());
                            Log.d("CIRCLE WALL, ", pollAnswerOptionsList.toString());
                            pollCreateAnswerOptionsDisplay.addView(tv);
                        }
                    }
                });

                sendBroadcast.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        publishBroadcast();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void pickFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(CircleWall.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                pickFile();
            } else {
                Toast.makeText(CircleWall.this,
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

            final StorageReference riversRef = storageReference.child("ProjectWall/" + broadcast.getBroadcastId() + "/" + uniqueID);

            final ProgressDialog progressDialog = new ProgressDialog(CircleWall.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(filePath));

            riversRef.putFile(filePath).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
                            return riversRef.getDownloadUrl();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    downloadUri = uri.toString();
                    progressDialog.dismiss();
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

            if (extension != null) {
                switch (extension) {
                    case "pdf":
                        uploadFileCloudButton.setBackground(getResources().getDrawable(R.drawable.pdf_image));
                        break;
                    case "ppt":
                        uploadFileCloudButton.setBackground(getResources().getDrawable(R.drawable.ppt_image));
                        break;
                    case "doc":
                        uploadFileCloudButton.setBackground(getResources().getDrawable(R.drawable.doc_image));
                        break;
                    case "jpg":
                    case "jpeg":
                    case "png":
                    case "webp":
                        uploadFileCloudButton.setImageURI(filePath);
                }
            }

            uploadFileTextView.setVisibility(View.GONE);
        }
    }

    public void publishBroadcast() {
        final String file_description = broadcastDescription.getText().toString();

        if (filePath == null && pollAnswerOptionsList == null) {

            Log.d("CIRCLE WALL TAB", "ONLY 1 CONDITION SATISFIED");

            Map<String, Object> map = new HashMap<>();
            map.put("Link", null);
            map.put("Time", System.currentTimeMillis());
            map.put("ownerId", SessionStorage.getUser(CircleWall.this).getUserId());
            map.put("ownerName", SessionStorage.getUser(CircleWall.this).getFirstName().trim() + " " +
                    SessionStorage.getUser(CircleWall.this).getLastName().trim());
            map.put("hasPoll", false);
            map.put("pollID", null);
            map.put("description", file_description);
            map.put("ownerPicURL", SessionStorage.getUser(CircleWall.this).getProfileImageLink());

            firebaseFirestore.collection("ProjectWall").document(broadcast.getBroadcastId())
                    .collection("Broadcasts").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        alertDialog.dismiss();
                        /*Intent intent = new Intent(CircleWall.this, CircleWall.class);
                        intent.putExtra("project", broadcast);
                        startActivity(intent);*/
                    }
                }
            });
        } else if (filePath != null && pollAnswerOptionsList == null) {
            //displaying a progress dialog while upload is going on

            Log.d("CIRCLE WALL TAB", "ONLY 2 CONDITIONS SATISFIED");

            Map<String, Object> map = new HashMap<>();
            map.put("Link", downloadUri.toString());
            map.put("Time", System.currentTimeMillis());
            map.put("ownerId", SessionStorage.getUser(CircleWall.this).getUserId());
            map.put("ownerName", SessionStorage.getUser(CircleWall.this).getFirstName().trim() + " " +
                    SessionStorage.getUser(CircleWall.this).getLastName().trim());
            map.put("hasPoll", false);
            map.put("pollID", null);
            map.put("description", file_description);
            map.put("ownerPicURL", SessionStorage.getUser(CircleWall.this).getProfileImageLink());

            firebaseFirestore.collection("ProjectWall").document(broadcast.getBroadcastId())
                    .collection("Broadcasts").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        alertDialog.dismiss();
                                /*Intent intent = new Intent(CircleWall.this, CircleWall.class);
                                intent.putExtra("project", broadcast);
                                startActivity(intent);*/
                    }
                }
            });

        } else if (filePath == null && pollAnswerOptionsList != null) {
            Log.d("CIRCLE WALL TAB", "ONLY POLLING ");

            Map<String, Object> map = new HashMap<>();
            map.put("Link", null);
            map.put("Time", System.currentTimeMillis());
            map.put("ownerId", SessionStorage.getUser(CircleWall.this).getUserId());
            map.put("ownerName", SessionStorage.getUser(CircleWall.this).getFirstName().trim() + " " +
                    SessionStorage.getUser(CircleWall.this).getLastName().trim());
            map.put("description", file_description);
            map.put("hasPoll", true);
            map.put("pollID", uniqueID);
            map.put("ownerPicURL", SessionStorage.getUser(CircleWall.this).getProfileImageLink());

            Map<String, Object> pollmap = new HashMap<>();
            pollmap.put("Question", pollCreateQuestionEntry.getText().toString());
            int i = 1;
            for (String opt : pollAnswerOptionsList) {
                pollmap.put("Option " + i, opt);
                i++;
            }
            pollmap.put("NumberOfOptions", i-1);

            map.put("Poll", pollmap);

            firebaseFirestore.collection("ProjectWall")
                    .document(broadcast.getBroadcastId())
                    .collection("Broadcasts")
                    .document()
                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        alertDialog.dismiss();
                                /*Intent intent = new Intent(CircleWall.this, CircleWall.class);
                                intent.putExtra("project", broadcast);
                                startActivity(intent);*/
                    }
                }
            });

        } else if (filePath != null && pollAnswerOptionsList != null) {

            Log.d("CIRCLE WALL TAB", "ALL CONDITIONS SATISFIED");
            Map<String, Object> map = new HashMap<>();
            map.put("Link", downloadUri.toString());
            map.put("Time", System.currentTimeMillis());
            map.put("ownerId", SessionStorage.getUser(CircleWall.this).getUserId());
            map.put("ownerName", SessionStorage.getUser(CircleWall.this).getFirstName().trim() + " " +
                    SessionStorage.getUser(CircleWall.this).getLastName().trim());
            map.put("description", file_description);
            map.put("hasPoll", true);
            map.put("pollID", uniqueID);
            map.put("ownerPicURL", SessionStorage.getUser(CircleWall.this).getProfileImageLink());

            Map<String, Object> pollmap = new HashMap<>();
            pollmap.put("Question", pollCreateQuestionEntry.getText().toString());
            int i = 1;
            for (String opt : pollAnswerOptionsList) {
                pollmap.put("Option " + i, opt);
                i++;
            }
            pollmap.put("NumberOfOptions", i-1);

            map.put("Poll", pollmap);

            firebaseFirestore.collection("ProjectWall")
                    .document(broadcast.getBroadcastId())
                    .collection("Broadcasts")
                    .document()
                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        alertDialog.dismiss();
                                /*Intent intent = new Intent(CircleWall.this, CircleWall.class);
                                intent.putExtra("project", broadcast);
                                startActivity(intent);*/
                    }
                }
            });
        }
    }
}
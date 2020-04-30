package com.example.teamup.ControlPanel.ProjectWall;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.ProjectWallDataClass;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Map;

public class ProjectWall extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 100;
    private FloatingActionButton fab;

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private Project project;
    private Uri filePath;
    private ArrayList<ProjectWallDataClass> arrayList;
    private LinearLayout emptyPlaceHolder;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    LayoutInflater inflater;
    View dialogue;
    private StorageReference storageReference;
    private static final int STORAGE_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_wall);


        fab = findViewById(R.id.newResourceFAB);

        arrayList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        emptyPlaceHolder = findViewById(R.id.projectWall_empty_display);

        recyclerView = findViewById(R.id.projectViewRecyclerView);

        project = SessionStorage.getProject(this);

        final Query query = firebaseFirestore.collection("ProjectWall").document(project.getProjectId())
                .collection("Files").orderBy("Time", Query.Direction.DESCENDING);

        final ProjectWallAdapter projectWallAdapter = new ProjectWallAdapter(this, arrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(projectWallAdapter);
        recyclerView.setHasFixedSize(true);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    arrayList.clear();
                    for (DocumentSnapshot doc : task.getResult()) {
                        ProjectWallDataClass projectWallDataClass = doc.toObject(ProjectWallDataClass.class);

                        arrayList.add(projectWallDataClass);

                        projectWallAdapter.notifyDataSetChanged();
                    }
                    if (arrayList.isEmpty())
                        emptyPlaceHolder.setVisibility(View.VISIBLE);
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder = new AlertDialog.Builder(ProjectWall.this);

                inflater = getLayoutInflater();

                dialogue = inflater.inflate(R.layout.file_upload_alert_layout_1, null);

                builder.setView(dialogue);


                final LinearLayout fileSelect = dialogue.findViewById(R.id.fileSelectAlertButton);

                fileSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(ProjectWall.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProjectWall.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE);
                        } else {
                            pickFile();
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void pickFile () {
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
                Toast.makeText(ProjectWall.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                pickFile();
            } else {
                Toast.makeText(ProjectWall.this,
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
            alertDialog.dismiss();


            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(filePath));

            AlertDialog.Builder builder = new AlertDialog.Builder(ProjectWall.this);

            LayoutInflater inflater = getLayoutInflater();

            final View dialogue = inflater.inflate(R.layout.file_upload_alert_layout_2, null);

            builder.setView(dialogue);


            final ImageView imageView = dialogue.findViewById(R.id.filePreviewImage);
            final EditText nameEditText = dialogue.findViewById(R.id.fileNameEditText);
            final EditText descriptionEditText = dialogue.findViewById(R.id.fileDescriptionEditText);
            final Button button = dialogue.findViewById(R.id.fileUploadAlertButton);


            if (extension != null) {
                switch (extension) {
                    case "pdf":
                        imageView.setImageResource(R.drawable.pdf_image);
                        break;
                    case "ppt":
                        imageView.setImageResource(R.drawable.ppt_image);
                        break;
                    case "doc":
                        imageView.setImageResource(R.drawable.word_image);
                        break;
                    case "jpg":
                    case "jpeg":
                    case "png":
                    case "webp":
                        imageView.setImageURI(filePath);
                }
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String file_name = nameEditText.getText().toString();
                    final String file_description = descriptionEditText.getText().toString();
                    if (!TextUtils.isEmpty(file_name) && filePath != null) {
                        //displaying a progress dialog while upload is going on
                        final ProgressDialog progressDialog = new ProgressDialog(ProjectWall.this);
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();

                        final StorageReference riversRef = storageReference.child("ProjectWall/" + project.getProjectId() + "/" + file_name);

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
                                progressDialog.dismiss();
                                alertDialog.dismiss();

                                //and displaying a success toast
                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                                Uri downloadUri = uri;

                                Map<String, Object> map = new HashMap<>();
                                map.put("Link", downloadUri.toString());
                                map.put("Time", System.currentTimeMillis());
                                map.put("FileName", file_name);
                                map.put("ownerId", SessionStorage.getUser(ProjectWall.this).getUserId());
                                map.put("ownerName", SessionStorage.getUser(ProjectWall.this).getFirstName().trim() + " " +
                                        SessionStorage.getUser(ProjectWall.this).getLastName().trim());
                                map.put("description", file_description);
                                map.put("ownerPicURL", SessionStorage.getUser(ProjectWall.this).getProfileImageLink());

                                firebaseFirestore.collection("ProjectWall").document(project.getProjectId())
                                        .collection("Files").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(ProjectWall.this, ProjectWall.class);
                                            intent.putExtra("project", project);
                                            startActivity(intent);
                                        }
                                    }
                                });


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
            });

            alertDialog = builder.create();
            alertDialog.show();


        }
    }
}
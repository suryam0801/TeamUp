package com.example.teamup.CircleWall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Comment;
import com.example.teamup.model.Broadcast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileComments extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private ListView commentsListView;
    private List<Comment> commentsList;
    private CommentAdapter commentAdapter;
    private EditText commentEditText;
    private Button commentSend;
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_comments);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        commentsListView = findViewById(R.id.comments_listView);
        commentEditText = findViewById(R.id.comment_type_editText);
        commentSend = findViewById(R.id.comment_send_button);
        commentsList = new ArrayList<>();

        commentEditText.clearFocus();

        broadcast = SessionStorage.getProject(FileComments.this);

        loadComments();

        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commentEditText.getText().toString().trim().equals(""))
                    makeCommentEntry();
            }
        });
    }

    public void loadComments(){
        final Query query = firebaseFirestore.collection("ProjectWall").document(broadcast.getBroadcastId())
                .collection("Comments").orderBy("timestamp", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {

                        Comment comment = doc.toObject(Comment.class);

                        commentsList.add(comment);
                    }
                    commentAdapter= new CommentAdapter(FileComments.this, commentsList);
                    commentsListView.setAdapter(commentAdapter);
                }
            }
        });

    }

    public void makeCommentEntry () {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", System.currentTimeMillis());
        map.put("comment", commentEditText.getText().toString().trim());
        map.put("commentorId", SessionStorage.getUser(FileComments.this).getUserId());
        map.put("commentorPicURL", SessionStorage.getUser(FileComments.this).getProfileImageLink());
        map.put("commentorName", SessionStorage.getUser(FileComments.this).getFirstName().trim() + " " +
                SessionStorage.getUser(FileComments.this).getLastName().trim());

        firebaseFirestore.collection("ProjectWall").document(broadcast.getBroadcastId())
                .collection("Comments").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), FileComments.class));
                }
            }
        });
    }
}
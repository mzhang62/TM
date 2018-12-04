package com.cs121.tmtm.nav_bar_testing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectInfoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView projectName;
    TextView projectMembers;
    TextView projectDescription;
    TextView add_comment;

    RecyclerView commentView;
    Button approveBtn;
    Button denyBtn;
    private ProjectObject this_project;
    private static final int APPROVED = 1;
    private static final int DENIED = -1;
    private static final int PENDING = 0;
    private  String projectID;
    private DatabaseReference projectReference;
    private DatabaseReference studentReference;
    private ArrayList<String> projectComments;
    private FirebaseAuth mAuth;
    private FirebaseUser this_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        approveBtn = (Button) findViewById(R.id.approve);
        denyBtn = (Button) findViewById(R.id.decline);
        projectName = (TextView) findViewById(R.id.projectName);
        projectMembers = (TextView) findViewById(R.id.projectMembers);
        projectDescription = (TextView) findViewById(R.id.long_des);
        projectDescription.setMovementMethod(new ScrollingMovementMethod());
        commentView = (RecyclerView) findViewById(R.id.comment_recycler);
        commentView.setLayoutManager(new LinearLayoutManager(this));
        add_comment = (TextView) findViewById(R.id.add_comment);
        add_comment.setOnClickListener(this);
        this_user = mAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        ProjectObject this_project = intent.getParcelableExtra("projectObj");
        projectID = this_project.getProjectID();
        Log.i("project", "project ID is:" + this_project.getProjectID());
        projectName.setText(this_project.getProjectName());
        String memberString = "";
        //this project's specific branch
        projectReference = FirebaseDatabase.getInstance().getReference("Projects").child(this_project.getProjectID());
        projectReference.child("projectMembers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if this project contains any members
                if(dataSnapshot.exists()){
                    String allMembers = "";
                    for(DataSnapshot members : dataSnapshot.getChildren()){
                        String memberNickName = members.getValue(String.class);
                        allMembers += memberNickName + " ";
                    }
                    projectMembers.setText(allMembers);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //if the user is a student, make the approve and denied button not available
        studentReference = FirebaseDatabase.getInstance().getReference("Student").child(this_user.getUid());
        studentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if the user is a Student, make the buttons disappear
                if(dataSnapshot.exists()){
                    approveBtn.setVisibility(View.GONE);
                    denyBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        projectDescription.setText(this_project.getProjectDescription());
        //set adaptor for comment
        projectComments = new ArrayList<>();
        projectReference.child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectComments.clear();
                //if the current project contains any comments
                if(dataSnapshot.exists()){
                    for(DataSnapshot everyComment : dataSnapshot.getChildren()){
                        String this_comment = everyComment.getValue(String.class);
                        projectComments.add(this_comment);
                    }
                Project_Comment_RecyclerList adaptor = new Project_Comment_RecyclerList(projectComments);
                commentView.setAdapter(adaptor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setApproved(View view) {
        updateProjectStatusInDB(APPROVED);
    }
    public void setDenied(View view) {
        updateProjectStatusInDB(DENIED);
    }

    private void updateProjectStatusInDB(final int status){
        DatabaseReference projectReference = FirebaseDatabase.getInstance().getReference("Projects");
//        String projectID = this_project.getProjectID();
        HashMap<String, Object> params = new HashMap<>();
        params.put("projectAcceptedStatus", status);
        projectReference.child(projectID).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(status == APPROVED){
                    Toast.makeText(getApplicationContext(), "Project Approved by an Instructor.", Toast.LENGTH_SHORT).show();

                }
                else if (status == DENIED){
                    Toast.makeText(getApplicationContext(), "Project Declined by an Instructor.", Toast.LENGTH_SHORT).show();

                }
                finish();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_comment:
                Intent intent = new Intent(getApplicationContext(), PostComment_Activity.class);
                startActivityForResult(intent, 21);
                break;
        }
    }
    //get the user comment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21 && resultCode == RESULT_OK) {
            String userComment = data.getStringExtra("userComment");
            saveCommentToDB(userComment);
            Toast.makeText(this, "Successfully added a comment!", Toast.LENGTH_SHORT).show();
        }
    }
    //add the comment to the database
    private void saveCommentToDB(final String userComment) {
        final String uid = projectReference.child("comment").push().getKey();
        projectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = this_user.getEmail();
                int index = email.indexOf('@');
                email = email.substring(0,index);
                projectReference.child("comment").child(uid).setValue(email +  ": " + userComment.trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

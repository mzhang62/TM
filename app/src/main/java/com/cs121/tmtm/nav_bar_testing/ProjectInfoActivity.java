package com.cs121.tmtm.nav_bar_testing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProjectInfoActivity extends AppCompatActivity {
    TextView projectName;
    TextView projectMembers;
    TextView projectDescription;
    private ProjectObject this_project;
    private static final int APPROVED = 1;
    private static final int DENIED = -1;
    private static final int PENDING = 0;
    private  String projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        projectName = (TextView) findViewById(R.id.projectName);
        projectMembers = (TextView) findViewById(R.id.projectMembers);
        projectDescription = (TextView) findViewById(R.id.long_des);
        projectDescription.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        ProjectObject this_project = intent.getParcelableExtra("projectObj");
        projectID = this_project.getProjectID();
        Log.i("project", "project ID is:" + this_project.getProjectID());

        projectName.setText(this_project.getProjectName());
        String memberString = "";
        for(String member : this_project.getProjectMembers()){
            memberString += member + " ";
        }
        projectMembers.setText(memberString);
        projectDescription.setText(this_project.getProjectDescription());

    }

    public void setApproved(View view) {
        updateProjectStatusInDB(APPROVED);
    }
    public void setDenied(View view) {
        updateProjectStatusInDB(DENIED);
    }

    private void updateProjectStatusInDB(int status){
        DatabaseReference projectReference = FirebaseDatabase.getInstance().getReference("Projects");
//        String projectID = this_project.getProjectID();
        HashMap<String, Object> params = new HashMap<>();
        params.put("projectAcceptedStatus", status);
        projectReference.child(projectID).updateChildren(params);
        if(status == APPROVED){
            Toast.makeText(this, "Project Approved by an Instructor.", Toast.LENGTH_SHORT).show();

        }
        else if (status == DENIED){
            Toast.makeText(this, "Project Declined by an Instructor.", Toast.LENGTH_SHORT).show();

        }
        finish();
    }
}

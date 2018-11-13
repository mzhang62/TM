package com.cs121.tmtm.nav_bar_testing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProjectInfoActivity extends AppCompatActivity {
    TextView projectName;
    TextView projectMembers;
    TextView projectDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        projectName = (TextView) findViewById(R.id.projectName);
        projectMembers = (TextView) findViewById(R.id.projectMembers);
        projectDescription = (TextView) findViewById(R.id.long_des);

        Intent intent = getIntent();
        ProjectObject this_project = intent.getParcelableExtra("projectObj");
        projectName.setText(this_project.getProjectName());
        String memberString = "";
        for(String member : this_project.getProjectMembers()){
            memberString += member + ", ";
        }
        projectMembers.setText(memberString);
        projectDescription.setText(this_project.getProjectDescription());

    }
}

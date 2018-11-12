package com.cs121.tmtm.nav_bar_testing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProjectObject {
    private String projectID;
    private String projectName;
    private ArrayList<String> projectMembers;
    private String projectAcceptedStatus;
    private ArrayList<String> projectTags;

    public ProjectObject(String projectID, String projectName, ArrayList<String> projectMembers,
                         String projectAcceptedStatus, ArrayList<String> projectTags) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectMembers = projectMembers;
        this.projectAcceptedStatus = projectAcceptedStatus;
        this.projectTags = projectTags;
    }

    public String getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public ArrayList<String> getProjectMembers() {
        return projectMembers;
    }


    public ArrayList<String> getProjectTags() {
        return projectTags;
    }

    public void setProjectMembers(ArrayList<String> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public void setProjectAcceptedStatus(String projectAcceptedStatus) {
        this.projectAcceptedStatus = projectAcceptedStatus;
    }

    public String getProjectAcceptedStatus() {

        return projectAcceptedStatus;
    }
}


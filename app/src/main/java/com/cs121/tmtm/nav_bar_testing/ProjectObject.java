package com.cs121.tmtm.nav_bar_testing;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProjectObject implements Parcelable {
    private String projectID;
    private String projectName;
    private ArrayList<String> projectMembers;
    //approved: 1; denied: -1; pending:0
    private int projectAcceptedStatus;
    private String projectDescription;
    private int groupCapacity;

    public ProjectObject(){}

    public ProjectObject(String projectID, String projectName, ArrayList<String> projectMembers,
                         int projectAcceptedStatus, String projectDescription, int groupCapacity) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectMembers = projectMembers;
        this.projectAcceptedStatus = projectAcceptedStatus;
        this.projectDescription = projectDescription;
        this.groupCapacity = groupCapacity;
    }

    protected ProjectObject(Parcel in) {
        projectID = in.readString();
        projectName = in.readString();
        projectMembers = in.createStringArrayList();
        projectAcceptedStatus = in.readInt();
        projectDescription = in.readString();
        groupCapacity = in.readInt();
    }

    public static final Creator<ProjectObject> CREATOR = new Creator<ProjectObject>() {
        @Override
        public ProjectObject createFromParcel(Parcel in) {
            return new ProjectObject(in);
        }

        @Override
        public ProjectObject[] newArray(int size) {
            return new ProjectObject[size];
        }
    };

    public String getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public ArrayList<String> getProjectMembers() {
        return projectMembers;
    }


    public void setProjectMembers(ArrayList<String> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public String getProjectDescription() {
        return projectDescription;
    }


    public void setProjectAcceptedStatus(int projectAcceptedStatus) {
        this.projectAcceptedStatus = projectAcceptedStatus;
    }

    public int getProjectAcceptedStatus() {

        return projectAcceptedStatus;
    }

    public int getGroupCapacity() {
        return groupCapacity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(projectID);
        parcel.writeString(projectName);
        parcel.writeStringList(projectMembers);
        parcel.writeInt(projectAcceptedStatus);
        parcel.writeString(projectDescription);
        parcel.writeInt(groupCapacity);
    }
}


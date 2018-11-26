package com.cs121.tmtm.nav_bar_testing;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClassObject {
    private String classID;
    private String classTitle;
    private String classDescription;
    private ArrayList<String> classInstructor;
    private ArrayList<String> classStudent;
    private ArrayList<String> classProject;

    public ClassObject(String classID, String classTitle, String classDescription,
                       ArrayList<String> classInstructor, ArrayList<String> classStudent, ArrayList<String> classProject) {
        this.classID = classID;
        this.classTitle = classTitle;
        this.classDescription = classDescription;
        this.classInstructor = classInstructor;
        this.classStudent = classStudent;
        this.classProject = classProject;
    }

    public String getClassID() {
        return classID;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public ArrayList<String> getClassInstructor() {
        return classInstructor;
    }

    public ArrayList<String> getClassStudent() {
        return classStudent;
    }

    public ArrayList<String> getClassProject() {
        return classProject;
    }
}

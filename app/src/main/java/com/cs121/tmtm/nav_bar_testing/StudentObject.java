package com.cs121.tmtm.nav_bar_testing;

import java.util.ArrayList;

/**
 * Student Object to user student-user information into the FireBase.
 */
public class StudentObject {
    private String uid;
    private String name;
    private String email;
    private ArrayList<String> myClass;
    private ArrayList<String> myProject;

    public StudentObject() {
    }

    public StudentObject(String uid, String name, String email, ArrayList<String> myClass,
                         ArrayList<String> myProject) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.myClass = myClass;
        this.myProject = myProject;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getMyClass() {
        return myClass;
    }

    public ArrayList<String> getMyProject() {
        return myProject;
    }
}

package com.cs121.tmtm.nav_bar_testing;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Instructor Object to store instructor-user information into the FireBase.
 */
public class InstructorObject {
    private String uid;
    private String name;
    private String email;
    private ArrayList<String> myClass;

    public InstructorObject() {
    }

    public InstructorObject(String uid, String name, String email, ArrayList<String> myClass) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.myClass = myClass;
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
}


package com.cs121.tmtm.nav_bar_testing;

/**
 * Instructor Object to store instructor-user information into the FireBase.
 */
public class InstructorObject {
    private String name;
    private String email;
    private String institution;

    public InstructorObject() {
    }

    public InstructorObject(String name, String email, String institution) {
        this.name = name;
        this.email = email;
        this.institution = institution;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getInstitution() {
        return institution;
    }
}


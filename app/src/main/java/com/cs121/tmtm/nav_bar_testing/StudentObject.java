package com.cs121.tmtm.nav_bar_testing;

/**
 * Student Object to user student-user information into the FireBase.
 */
public class StudentObject {
    private String name;
    private String email;
    private String institution;
    private String knownSkill;
    private String unknownSkill;

    public StudentObject() {
    }

    public StudentObject(String name, String email, String institution, String knownSkill, String unknownSkill) {
        this.name = name;
        this.email = email;
        this.institution = institution;
        this.knownSkill = knownSkill;
        this.unknownSkill = unknownSkill;
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

    public String getKnownSkill() {
        return knownSkill;
    }

    public String getUnknownSkill() {
        return unknownSkill;
    }
}

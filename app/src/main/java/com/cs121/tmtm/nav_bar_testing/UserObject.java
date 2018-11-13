package com.cs121.tmtm.nav_bar_testing;

public class UserObject {
    private String name;
    private String email;
    private String institution;
    private String knownSkill;
    private String unknownSkill;

    public UserObject(String name, String email, String institution, String knownSkill, String unknownStill) {
        this.name = name;
        this.email = email;
        this.institution = institution;
        this.knownSkill = knownSkill;
        this.unknownSkill = unknownStill;
    }

    public String getName() {return name;}

    public String getEmail() {return email;}

    public String getInstitution() {return institution;}

    public String getKnownSkill() {return knownSkill;}

    public String getUnknownSkill() {return unknownSkill;}
}

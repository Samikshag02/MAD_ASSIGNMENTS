package com.example.projectcompanionfinder;

public class User {

    public String name, email, branch, skills, uid, imageUrl, year, division;
    public String projectTitle, projectDesc, projectRoles;
    public String status;

    public User() {}

    public User(String name, String email, String branch,
                String skills, String uid, String imageUrl,
                String year, String division) {

        this.name = name;
        this.email = email;
        this.branch = branch;
        this.skills = skills;
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.year = year;
        this.division = division;
        this.status = "offline";
    }
}
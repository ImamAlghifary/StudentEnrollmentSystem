package com.example.studentenrollmentsystem;

import java.util.List;

public class UsersModel {
    private String userId;
    private String email;
    private int currentCredits;
    private List<String> enrolledSubjects; // List of subject IDs or titles

    public UsersModel(String userId, String email, int currentCredits, List<String> enrolledSubjects) {
        this.userId = userId;
        this.email = email;
        this.currentCredits = currentCredits;
        this.enrolledSubjects = enrolledSubjects;
    }

    public UsersModel() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCurrentCredits() {
        return currentCredits;
    }

    public void setCurrentCredits(int currentCredits) {
        this.currentCredits = currentCredits;
    }

    public List<String> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    public void setEnrolledSubjects(List<String> enrolledSubjects) {
        this.enrolledSubjects = enrolledSubjects;
    }

}

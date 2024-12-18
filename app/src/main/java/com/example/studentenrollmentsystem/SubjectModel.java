package com.example.studentenrollmentsystem;

public class SubjectModel {
    private String subjectId;
    private String title;
    private int credit;

    public SubjectModel(String subjectId, String title, int credit) {
        this.subjectId = subjectId;
        this.title = title;
        this.credit = credit;
    }

    public SubjectModel(String title){
        this.title = title;
    }

    public SubjectModel(){}

    public String getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getCredit(){
        return this.credit;
    }

    public void setCredit(int credit){
        this.credit = credit;
    }
}

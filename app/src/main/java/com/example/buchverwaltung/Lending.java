package com.example.buchverwaltung;

public class Lending {

    private int id;
    private int book_id;
    private String lender;
    private String start;
    private String planned_end;
    private Boolean isBack;
    private String comment;

    public Lending(int id, int book_id, String lender, String start, String planned_end, Boolean isBack, String comment) {
        this.id = id;
        this.book_id = book_id;
        this.lender = lender;
        this.start = start;
        this.planned_end = planned_end;
        this.isBack = isBack;
        this.comment = comment;
    }

    public Lending(int book_id, String lender, String start, String planned_end, Boolean isBack, String comment) {
        this.book_id = book_id;
        this.lender = lender;
        this.start = start;
        this.planned_end = planned_end;
        this.isBack = isBack;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getPlanned_end() {
        return planned_end;
    }

    public void setPlanned_end(String planned_end) {
        this.planned_end = planned_end;
    }

    public Boolean getIsBack() {
        return isBack;
    }

    public void setIsBack(Boolean isBack) {
        this.isBack = isBack;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

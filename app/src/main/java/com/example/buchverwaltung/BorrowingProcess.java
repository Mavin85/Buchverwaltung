package com.example.buchverwaltung;

public class BorrowingProcess {

    private int id;
    private int book_id;
    private String borrower;
    private String beginning;
    private String ending;
    private boolean completed;
    private String comment;

    public BorrowingProcess(int id, int book_id, String borrower, String beginning, String ending, boolean completed, String comment) {
        this.id = id;
        this.book_id = book_id;
        this.borrower = borrower;
        this.beginning = beginning;
        this.ending = ending;
        this.completed = completed;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "BorrowingProcess{" +
                "id=" + id +
                ", book_id=" + book_id +
                ", borrower='" + borrower + '\'' +
                ", beginning='" + beginning + '\'' +
                ", ending='" + ending + '\'' +
                ", completed=" + completed +
                ", comment='" + comment + '\'' +
                '}';
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

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getBeginning() {
        return beginning;
    }

    public void setBeginning(String beginning) {
        this.beginning = beginning;
    }

    public String getEnding() {
        return ending;
    }

    public void setEnding(String ending) {
        this.ending = ending;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

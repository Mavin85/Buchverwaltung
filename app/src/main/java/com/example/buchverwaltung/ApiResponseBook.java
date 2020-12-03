package com.example.buchverwaltung;

public class ApiResponseBook {

    public ApiResponseBook(String isbn, ApiDetails volumeInfo) {
        this.isbn = isbn;
        this.volumeInfo = volumeInfo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public ApiDetails getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(ApiDetails volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    private String isbn;
    private ApiDetails volumeInfo;



}

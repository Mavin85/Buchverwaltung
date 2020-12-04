package com.example.buchverwaltung;

public class ApiResponseBook {

    private String isbn;
    private ApiDetails volumeInfo;

    public ApiResponseBook(String isbn, ApiDetails volumeInfo) {
        this.isbn = isbn;
        this.volumeInfo = volumeInfo;
    }

    @Override
    public String toString() {
        return "ApiResponseBook{" +
                "isbn='" + isbn + '\'' +
                ", volumeInfo=" + volumeInfo +
                '}';
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

}

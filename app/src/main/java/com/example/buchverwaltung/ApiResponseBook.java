package com.example.buchverwaltung;

public class ApiResponseBook {
    private String bib_key;
    private String thumbnail_url;
    private Details details;

    public String getBib_key() {
        return bib_key;
    }

    public void setBib_key(String bib_key) {
        this.bib_key = bib_key;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public ApiResponseBook(String bib_key, String thumbnail_url, Details details) {
        this.bib_key = bib_key;
        this.thumbnail_url = thumbnail_url;
        this.details = details;
    }
}

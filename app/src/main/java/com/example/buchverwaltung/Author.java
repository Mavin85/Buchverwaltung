package com.example.buchverwaltung;

public class Author {
    private String name;
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Author(String name, String key) {
        this.name = name;
        this.key = key;
    }
}

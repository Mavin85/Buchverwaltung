package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

public class ApiResponseBook {

    @SerializedName("volumeInfo")
    private ApiDetails apiDetails;

    public ApiResponseBook(ApiDetails apiDetails) {
        this.apiDetails = apiDetails;
    }

    public ApiDetails getApiDetails() {
        return apiDetails;
    }

    public void setApiDetails(ApiDetails apiDetails) {
        this.apiDetails = apiDetails;
    }
}

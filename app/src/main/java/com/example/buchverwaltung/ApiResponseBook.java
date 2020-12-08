package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponseBook {

    @SerializedName("industryIdentifiers")
    private List<ApiIndustryIdentifier> industryIdentifiers;
    @SerializedName("volumeInfo")
    private ApiDetails apiDetails;

    public ApiResponseBook(List<ApiIndustryIdentifier> industryIdentifiers, ApiDetails apiDetails) {
        this.industryIdentifiers = industryIdentifiers;
        this.apiDetails = apiDetails;
    }

    @Override
    public String toString() {
        return "ApiResponseBook{" +
                "industryIdentifiers=" + industryIdentifiers +
                ", apiDetails=" + apiDetails +
                '}';
    }

    public List<ApiIndustryIdentifier> getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(List<ApiIndustryIdentifier> industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
    }

    public ApiDetails getApiDetails() {
        return apiDetails;
    }

    public void setApiDetails(ApiDetails apiDetails) {
        this.apiDetails = apiDetails;
    }
}

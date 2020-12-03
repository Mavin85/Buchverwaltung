package com.example.buchverwaltung;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCallService {
    @GET("books/v1/volumes")
    Call<ResponseMapper> getbook(@Query("q") String isbn);


}

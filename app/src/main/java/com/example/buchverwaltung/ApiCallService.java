package com.example.buchverwaltung;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCallService {
    @GET("api/books.json")
    Call<ResponseMapper> getbookList(@Query("bibkeys") String isbn,
                                            @Query("details") String details);

}

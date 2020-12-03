package com.example.buchverwaltung;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRepo {
    ApiCallService apiCallService;
    public BookRepo(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openlibrary.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCallService = retrofit.create(ApiCallService.class);

    }

    public void getBook(Callback<ResponseMapper> callback){
        Call<ResponseMapper> apiResultCall = apiCallService.getbookList("ISBN:0201558025","true");
        apiResultCall.enqueue(callback);
    }

}

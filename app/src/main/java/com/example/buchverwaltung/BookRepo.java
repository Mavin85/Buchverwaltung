package com.example.buchverwaltung;

import android.util.Log;
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
                .baseUrl("https://www.googleapis.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCallService = retrofit.create(ApiCallService.class);

    }

    public void getBook(Callback<ResponseMapper> callback, String isbn){
        String isbn_string = "isbn:" + isbn;
        Call<ResponseMapper> apiResultCall = apiCallService.getbook(isbn_string);
        apiResultCall.enqueue(callback);
    }

}

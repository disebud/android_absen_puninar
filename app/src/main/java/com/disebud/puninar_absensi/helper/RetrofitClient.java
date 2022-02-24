package com.disebud.puninar_absensi.helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

        private static String BASE_URL = "http://192.168.29.101/absensi_lokasi/"; // MY IP


    private static RetrofitClient retrofitClient;
    private static  Retrofit retrofit;

    public RetrofitClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static synchronized RetrofitClient getInstance() {
        if (retrofitClient == null){
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }

}

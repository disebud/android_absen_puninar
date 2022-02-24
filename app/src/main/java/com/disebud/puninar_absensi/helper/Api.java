package com.disebud.puninar_absensi.helper;

import com.disebud.puninar_absensi.model.ResTambah;
import com.disebud.puninar_absensi.model.getData.ResGetData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("postData.php")
    Call<ResTambah> tambahData(
            @Field("dateAbsen") String dateAbsen,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("address") String address,
            @Field("img") String img,
            @Field("status") String status
    );


    @GET("getData.php")
    Call<ResGetData> dataAbsensi(
    );

}

package com.example.project.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("ambilData.php")
    Call<ResponseBody> getData(@Field("email") String email);

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerRequest(@Field("nama") String nama,
                                       @Field("username") String username,
                                       @Field("email") String email,
                                       @Field("password") String password);
    @FormUrlEncoded
    @POST("update1.php")
    Call<ResponseBody> updateRequest(@Field("nama") String nama,
                                     @Field("username") String username,
                                     @Field("email") String email);
}
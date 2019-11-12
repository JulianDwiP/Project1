package com.example.project.Api;

import com.example.project.entity.BukuResponse;
import com.example.project.entity.rakBukuInsert;
import com.example.project.entity.rakBukuResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

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

    @FormUrlEncoded
    @POST("insertRakBuku.php")
    Call<rakBukuInsert> insertRakBuku(@Field("Rb_nama") String Rb_nama,
                                      @Field("Rb_deskripsi") String Rb_deskripsi,
                                      @Field("Rb_author") String Rb_author,
                                      @Field("Rb_pdf_icon") String Rb_pdf_icon,
                                      @Field("Rb_pdf_url") String Rb_pdf_url,
                                      @Field("Rb_peringkat") String Rb_peringkat,
                                      @Field("Rb_kategori") String Rb_kategori,
                                      @Field("id_user") String id_user);

    @GET("ambilBuku.php")
    Call<BukuResponse> getSemuaBuku();

    @FormUrlEncoded
    @POST("ambilRakBuku.php")
    Call<rakBukuResponse> getSemuaRakbuku(@Field("id_user") String id_user);
}
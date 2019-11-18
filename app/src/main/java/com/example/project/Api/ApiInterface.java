package com.example.project.Api;

import com.example.project.Model.KategoriResponse;
import com.example.project.Model.ListSpinnerResponse;
import com.example.project.entity.BukuResponse;
import com.example.project.entity.SearchResponse;
import com.example.project.entity.rakBukuInsert;
import com.example.project.entity.rakBukuResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
    Call<rakBukuInsert> insertRakBuku(@Field("nama") String nama,
                                      @Field("deskripsi") String deskripsi,
                                      @Field("author") String author,
                                      @Field("pdf_icon") String pdf_icon,
                                      @Field("pdf_url") String pdf_url,
                                      @Field("peringkat") String peringkat,
                                      @Field("kategori") String kategori,
                                      @Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("ambilRakBuku.php")
    Call<rakBukuResponse> getSemuaRakbuku(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("kategori.php")
    Call<KategoriResponse> getByKategori(@Field("kategori") String kategori);

    @GET("ambilBuku.php")
    Call<BukuResponse> getSemuaBuku();

    @GET("listKategori.php")
    Call<ListSpinnerResponse> getListSpinner();

    @GET("searchBuku.php")
    Call<SearchResponse> search(
            @Query("item_type") String item_type,
            @Query("key") String keyword);
}
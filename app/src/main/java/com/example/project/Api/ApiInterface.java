package com.example.project.Api;

import com.example.project.Model.KategoriResponse;
import com.example.project.Model.ListSpinnerResponse;
import com.example.project.entity.BukuResponse;
import com.example.project.entity.cobaSearchResponse;
import com.example.project.entity.deleteListBuku;
import com.example.project.entity.downloadResponse;
import com.example.project.entity.masukanPeringkatModel;
import com.example.project.entity.View;
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
    Call<rakBukuInsert> insertRakBuku(@Field("nama") String nama,
                                      @Field("deskripsi") String deskripsi,
                                      @Field("author") String author,
                                      @Field("pdf_icon") String pdf_icon,
                                      @Field("pdf_url") String pdf_url,
                                      @Field("peringkat") String peringkat,
                                      @Field("kategori") String kategori,
                                      @Field("id_user") String id_user,
                                      @Field("id_buku") String id_buku,
                                      @Field("pengunjung") int pengunjung);


    @FormUrlEncoded
    @POST("downloaded.php")
    Call<downloadResponse> masukanDownload(@Field("nama") String  nama,
                                           @Field("pdf_url") String pdf_url,
                                           @Field("id_user") String id_user);
    @FormUrlEncoded
    @POST("insertPeringkat.php")
    Call<masukanPeringkatModel> masukanPeringkat(@Field("id_buku") String id_buku,
                                                 @Field("dPeringkat") Float dPeringkat);
    @FormUrlEncoded
    @POST("ambilRakBuku.php")
    Call<rakBukuResponse> getSemuaRakbuku(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("kategori.php")
    Call<KategoriResponse> getByKategori(@Field("kategori") String kategori);

    @FormUrlEncoded
    @POST("deleteListBuku.php")
    Call<deleteListBuku> deleteBuku(@Field("nama") String nama,
                                    @Field("id_user") String id_user);
    @FormUrlEncoded
    @POST("ambilDownloaded.php")
    Call<downloadResponse> getDownloaded(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("verifikasiAcc.php")
    Call<ResponseBody> getAkun(@Field("email") String email,
                             @Field("username") String username);

    @FormUrlEncoded
    @POST("search.php")
    Call<cobaSearchResponse> cobaSearch(@Field("nama") String nama);

    @FormUrlEncoded
    @POST("getView.php")
    Call<View> getViewBuku(@Field("id") String id);
    @FormUrlEncoded
    @POST("getViewRak.php")
    Call<View> getViewRak(@Field("id_buku") String id_buku);

    @FormUrlEncoded
    @POST("pengunjung.php")
    Call<ResponseBody> tambahView(@Field("id") String id,
                                  @Field("pengunjung") int pengunjung,
                                  @Field("id_buku") String id_buku);

    @FormUrlEncoded
    @POST("gantiPassword.php")
    Call<ResponseBody> setPassword(@Field("id") String id,
                                   @Field("password") String password);

    @GET("ambilBuku.php")
    Call<BukuResponse> getSemuaBuku();

    @GET("listKategori.php")
    Call<ListSpinnerResponse> getListSpinner();
}
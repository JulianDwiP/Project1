package com.example.project.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.Hasil;
import com.example.project.entity.cekDownload;
import com.example.project.entity.deleteDownload;
import com.example.project.entity.downloadResponse;
import com.example.project.entity.masukanPeringkatModel;
import com.example.project.entity.View;
import com.example.project.entity.rakBukuInsert;
import com.example.project.utils.DownloadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class deskripsiBuku extends AppCompatActivity {
    public static  final String TAG = "Masukan Ke db Download";
    TextView judulBuku, deskripsiBuku1, authorDesBuku, perinkatDesBuku, kategoriDesBuku, pembaca;
    ImageView imageDesBuku;
    Button baca, add, sebelumLogin, downlaod, folder;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Toolbar desToolbar;
    LinearLayout linearRating;
    String judul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_buku);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        init();

        String cek = getIntent().getStringExtra("judul");
        File exStore = Environment.getExternalStorageDirectory();
        String a = cek+".pdf";
        File myFile = new File(exStore.getAbsolutePath() + "/Ebook Download/"+a);
        if (myFile.exists()){
//            downlaod.setText("Terdownload");
        }else{
            mApiInterface.deleteDownload(sharedPrefManager.getId(), cek).enqueue(new Callback<deleteDownload>() {
                @Override
                public void onResponse(Call<deleteDownload> call, Response<deleteDownload> response) {
                    Log.e("Hapus Data", "Berhasil");
                    sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekDownload, false);
                    downlaod.setText("download");
                }

                @Override
                public void onFailure(Call<deleteDownload> call, Throwable t) {
                    Log.e("Hapus Data", "Gagal");
                }
            });
        }
        if(sharedPrefManager.getSPSudahLogin()){
            sebelumLogin.setVisibility(android.view.View.GONE);
        }else{
            baca.setVisibility(android.view.View.GONE);
            add.setVisibility(android.view.View.GONE);
            linearRating.setVisibility(android.view.View.GONE);
            downlaod.setVisibility(android.view.View.GONE);
        }
        cekUdahDownload();
        ambilDataBuku();
        if (sharedPrefManager.getCekDownload()){
            downlaod.setText("Terdownload");
        }else{
            downlaod.setText("Download");
        }

    }

    private void cekUdahDownload() {
        judul = getIntent().getStringExtra("judul");
        String hasil = judul;
        mApiInterface.cekDownload(hasil, sharedPrefManager.getId()).enqueue(new Callback<cekDownload>() {
            @Override
            public void onResponse(Call<cekDownload> call, Response<cekDownload> response) {
                if (response.isSuccessful()){
                    Hasil hasili = response.body().getHasil();
                    if (hasili == null){
                        sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekDownload, false);
                    }else{
                        downlaod.setText("Terdownload");
                        sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekDownload, true);
                    }
                }
            }

            @Override
            public void onFailure(Call<cekDownload> call, Throwable t) {
                Toast.makeText(deskripsiBuku.this, "Gagal " + judul, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void ambilDataBuku() {
        String id_buku = getIntent().getStringExtra("id_buku");
        String img_url = getIntent().getStringExtra("img");

        String deskripsi = getIntent().getStringExtra("deskripsi");
        String pdf_url = getIntent().getStringExtra("pdf_url");
        String peringkat = getIntent().getStringExtra("peringkat");
        String author = getIntent().getStringExtra("author");
        String kategori = getIntent().getStringExtra("kategori");
        String Stpembacaa = getIntent().getStringExtra("pengunjung");

        mApiInterface.getViewBuku(id_buku).enqueue(new Callback<View>() {
            @Override
            public void onResponse(Call<View> call, Response<View> response) {
                int c = response.body().getPengunjung();
                sharedPrefManager.simpanSPInt(SharedPrefManager.View, c);
                pembaca.setText(String.valueOf(c));
            }

            @Override
            public void onFailure(Call<View> call, Throwable t) {

            }
        });
        Bitmap bmp = null;
        try{
            URL url = new URL(ApiClient.BASE_URL+img_url);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos );
        }catch (IOException e){
            e.printStackTrace();
        }
        judulBuku.setText(judul);
        deskripsiBuku1.setText(deskripsi);
        imageDesBuku.setImageBitmap(bmp);
        perinkatDesBuku.setText(peringkat);
        authorDesBuku.setText(author);
        kategoriDesBuku.setText(kategori);
        getSupportActionBar().setTitle("");

        baca.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (sharedPrefManager.getSPSudahLogin()){
                    mApiInterface.tambahView(id_buku, 1, id_buku).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Intent i = new Intent(deskripsiBuku.this, PdfActivity.class);
                                i.putExtra("pdf_urll", pdf_url);
                                i.putExtra("judul", judul);
                                startActivity(i);
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("error", "Errornya : "+t.getMessage());
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(), "Harap Login Terlebih Dahulu", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(deskripsiBuku.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        add.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                mApiInterface.insertRakBuku(judul,deskripsi,author,img_url,pdf_url,peringkat,kategori, sharedPrefManager.getId(),
                        id_buku, sharedPrefManager.getView())
                        .enqueue(new Callback<rakBukuInsert>() {
                            @Override
                            public void onResponse(Call<rakBukuInsert> call, Response<rakBukuInsert> response) {
                                if (response.isSuccessful()){
                                    Boolean Status = response.body().getStatus();
                                    if (Status == true){
                                        Toast.makeText(getApplicationContext(), "Ditambahkan ke List Bacaan",Toast.LENGTH_LONG).show();
                                    }if (sharedPrefManager.getId().equals("")){
                                        Toast.makeText(getApplicationContext(), "Login Terlebih Dahulu", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(deskripsiBuku.this, MainActivity.class);
                                        startActivity(intent);
                                    }if (Status == false){
                                        Toast.makeText(deskripsiBuku.this, "Gagal, buku sudah ada", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<rakBukuInsert> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Login Terlebih Dahulu", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(deskripsiBuku.this, MainActivity.class);
                                startActivity(intent);
                     }
                });
            }
        });
        linearRating.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(deskripsiBuku.this);
                    android.view.View layout = null;
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    layout = inflater.inflate(R.layout.rating, null);
                    final RatingBar ratingBar = layout.findViewById(R.id.ratingBar);
                    builder.setTitle("Sentuh untuk memberi rating");
                    builder.setMessage("Terima kasih");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Float value = ratingBar.getRating();
                            mApiInterface.masukanPeringkat(id_buku, value).enqueue(new Callback<masukanPeringkatModel>() {
                                @Override
                                public void onResponse(Call<masukanPeringkatModel> call, Response<masukanPeringkatModel> response) {
                                    if (response.isSuccessful()){
                                        String peringkat = response.body().getPeringkat();
                                        perinkatDesBuku.setText(peringkat);
                                    }
                                }
                                @Override
                                public void onFailure(Call<masukanPeringkatModel> call, Throwable t) {
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Maaf, Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.setView(layout);
                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        desToolbar.setNavigationOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(deskripsiBuku.this, Beranda.class);
                intent.putExtra("backTo", "2");
                sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekIntent, true);
                startActivity(intent);
            }
        });


        sebelumLogin.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Toast.makeText(deskripsiBuku.this, "Harap login terlebih dahulu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(deskripsiBuku.this, MainActivity.class);
                startActivity(intent);
            }
        });

        downlaod.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (sharedPrefManager.getCekDownload()){
                    Intent intent = new Intent(deskripsiBuku.this, downloadedActivity.class);
                    startActivity(intent);
                }else{
                    sharedPrefManager.simpanSPSring(SharedPrefManager.namaFile, judul);
                    sharedPrefManager.simpanSPSring(SharedPrefManager.urlFile, ApiClient.BASE_URL+pdf_url);
                    String url = ApiClient.BASE_URL+pdf_url;
                    if (isConnectingToInternet()){
                        new DownloadTask(deskripsiBuku.this, downlaod, url);
                        mApiInterface.masukanDownload(judul, pdf_url, sharedPrefManager.getId()).enqueue(new Callback<downloadResponse>() {
                            @Override
                            public void onResponse(Call<downloadResponse> call, Response<downloadResponse> response) {
                                Log.i(TAG, "Berhasil Memasukan ke DB");
                            }
                            @Override
                            public void onFailure(Call<downloadResponse> call, Throwable t) {
                                Log.e(TAG, "Error saat memasukan ke db, errornya "+t.getMessage());

                            }
                        });
                    }else{
                        Toast.makeText(deskripsiBuku.this, "Tidak Ada Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }

    }

    private void init() {
        judulBuku = findViewById(R.id.judulDesBuku);
        deskripsiBuku1 = findViewById(R.id.sinopsisDesBuku);
        imageDesBuku = findViewById(R.id.imgDesBuku);
        baca = findViewById(R.id.btnBaca);
        authorDesBuku = findViewById(R.id.authorDesBuku);
        kategoriDesBuku = findViewById(R.id.tvDesKategori);
        add = findViewById(R.id.btnTambah);
        perinkatDesBuku = findViewById(R.id.peringkatDesBuku);
        pembaca = findViewById(R.id.tvPembaca);
        desToolbar = findViewById(R.id.desToolbar);
        linearRating  = findViewById(R.id.ratingLinear);
        downlaod = findViewById(R.id.btnDownload);
        sebelumLogin = findViewById(R.id.btnSebelumloginDesBuk);

        setSupportActionBar(desToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(deskripsiBuku.this, Beranda.class);
        intent.putExtra("backTo", "2");
        sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekIntent, true);
        startActivity(intent);
    }
}

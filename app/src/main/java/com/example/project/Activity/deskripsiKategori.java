package com.example.project.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.View;
import com.example.project.entity.masukanPeringkatModel;
import com.example.project.entity.rakBukuInsert;
import com.example.project.utils.DownloadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class deskripsiKategori extends AppCompatActivity {

    TextView judulBuku, deskripsiBuku1, authorDesBuku, perinkatDesBuku, kategoriDesBuku, pembaca;
    ImageView imageDesBuku;
    Button baca, add, sebelumLogin, downloadKat;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Toolbar desKatToolbar;
    LinearLayout linearRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_kategori);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        init();
        sharedPrefManager = new SharedPrefManager(this);
        if(sharedPrefManager.getSPSudahLogin()){
            sebelumLogin.setVisibility(android.view.View.GONE);
        }else{
            baca.setVisibility(android.view.View.GONE);
            add.setVisibility(android.view.View.GONE);
            downloadKat.setVisibility(android.view.View.GONE);
            linearRating.setVisibility(android.view.View.GONE);
        }
        ambilDataBuku();
    }


    private void ambilDataBuku() {
        String id_buku = getIntent().getStringExtra("id_buku");
        String img_url = getIntent().getStringExtra("img");
        String judul = getIntent().getStringExtra("judul");
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
        getSupportActionBar().setTitle(judul);

        File exStore = Environment.getExternalStorageDirectory();
        String a = judul+".pdf";
        File myFile = new File(exStore.getAbsolutePath() + "/Ebook Download/"+a);
        if (myFile.exists()){
            downloadKat.setText("Terdownload");
            sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekDownload, true);
        }else{
            downloadKat.setText("Download");
            sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekDownload, false);
        }

        baca.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (sharedPrefManager.getSPSudahLogin()){
                    mApiInterface.tambahView(id_buku, 1, id_buku).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Intent i = new Intent(deskripsiKategori.this, PdfActivity.class);
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
                    Intent intent = new Intent(deskripsiKategori.this, MainActivity.class);
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
                                        Intent intent = new Intent(deskripsiKategori.this, MainActivity.class);
                                        startActivity(intent);
                                    }if (Status == false){
                                        Toast.makeText(deskripsiKategori.this, "Gagal, buku sudah ada", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<rakBukuInsert> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Login Terlebih Dahulu", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(deskripsiKategori.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        });
        linearRating.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(deskripsiKategori.this);
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
        desKatToolbar.setNavigationOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                finish();
            }
        });


        sebelumLogin.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Toast.makeText(deskripsiKategori.this, "Harap login terlebih dahulu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(deskripsiKategori.this, MainActivity.class);
                startActivity(intent);
            }
        });
        downloadKat.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (sharedPrefManager.getCekDownload()){
                    Toast.makeText(deskripsiKategori.this, "Buku sudah di download", Toast.LENGTH_LONG).show();
                }else{
                    sharedPrefManager.simpanSPSring(SharedPrefManager.namaFile, judul);
                    sharedPrefManager.simpanSPSring(SharedPrefManager.urlFile, ApiClient.BASE_URL+pdf_url);
                    String url = ApiClient.BASE_URL+pdf_url;
                    if (isConnectingToInternet()){
                        new DownloadTask(deskripsiKategori.this, downloadKat, url);
                    }else{
                        Toast.makeText(deskripsiKategori.this, "Tidak Ada Internet", Toast.LENGTH_SHORT).show();
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
        imageDesBuku = findViewById(R.id.imgDesKatBuku);
        baca = findViewById(R.id.btnBaca);
        authorDesBuku = findViewById(R.id.authorDesBuku);
        kategoriDesBuku = findViewById(R.id.tvDesKategori);
        add = findViewById(R.id.btnTambah);
        perinkatDesBuku = findViewById(R.id.peringkatDesBuku);
        pembaca = findViewById(R.id.tvPembaca);
        desKatToolbar = findViewById(R.id.desKatToolbar);
        linearRating  = findViewById(R.id.ratingLinear);
        sebelumLogin = findViewById(R.id.btnSebelumloginDesBuk);
        downloadKat = findViewById(R.id.btnKatDownload);

        setSupportActionBar(desKatToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed(){
        finish();
    }
}
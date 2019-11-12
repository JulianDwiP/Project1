package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.rakBukuInsert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class deskripsiBuku extends AppCompatActivity {

    TextView judulBuku, deskripsiBuku1, authorDesBuku, perinkatDesBuku;
    ImageView imageDesBuku;
    Button baca, add;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_buku);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        init();
        ambilDataBuku();
        sharedPrefManager = new SharedPrefManager(this);
    }

    private void ambilDataBuku() {
        String img_url = getIntent().getStringExtra("img");
        String judul = getIntent().getStringExtra("judul");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String pdf_url = getIntent().getStringExtra("pdf_url");
        String peringkat = getIntent().getStringExtra("peringkat");
        String author = getIntent().getStringExtra("author");
        String kategori = getIntent().getStringExtra("kategori");
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
        perinkatDesBuku.setText(": "+peringkat);
        authorDesBuku.setText(": "+author);

        baca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefManager.getSPSudahLogin()){
                    Intent i = new Intent(deskripsiBuku.this, PdfActivity.class);
                    i.putExtra("pdf_urll", pdf_url);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Harap Login Terlebih Dahulu", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(deskripsiBuku.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApiInterface.insertRakBuku(judul,deskripsi,author,img_url,pdf_url,peringkat,kategori, sharedPrefManager.getId())
                        .enqueue(new Callback<rakBukuInsert>() {
                            @Override
                            public void onResponse(Call<rakBukuInsert> call, Response<rakBukuInsert> response) {
                                if (response.isSuccessful()){
                                    Boolean Status = response.body().getStatus();
                                    if (Status == true){
                                        Toast.makeText(getApplicationContext(), "Ditambahkan ke RakBuku",Toast.LENGTH_LONG).show();
                                    }if (sharedPrefManager.getId().equals("")){
                                        Toast.makeText(getApplicationContext(), "Login Terlebih Dahulu", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(deskripsiBuku.this, MainActivity.class);
                                        startActivity(intent);
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
    }


    private void init() {
        judulBuku = findViewById(R.id.judulDesBuku);
        deskripsiBuku1 = findViewById(R.id.sinopsisDesBuku);
        imageDesBuku = findViewById(R.id.imgDesBuku);
        baca = findViewById(R.id.btnBaca);
        authorDesBuku = findViewById(R.id.authorDesBuku);
        add = findViewById(R.id.btnTambah);
        perinkatDesBuku = findViewById(R.id.peringkatDesBuku);
    }
}

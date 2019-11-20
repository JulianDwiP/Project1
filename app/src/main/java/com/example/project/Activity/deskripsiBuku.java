package com.example.project.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.masukanPeringkatModel;
import com.example.project.entity.rakBukuInsert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class deskripsiBuku extends AppCompatActivity {

    TextView judulBuku, deskripsiBuku1, authorDesBuku, perinkatDesBuku, kategoriDesBuku;
    ImageView imageDesBuku;
    Button baca, add, sebelumLogin;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Toolbar desToolbar;
    LinearLayout linearRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_buku);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        init();
        sharedPrefManager = new SharedPrefManager(this);
        if(sharedPrefManager.getSPSudahLogin()){
            sebelumLogin.setVisibility(View.GONE);
        }else{
            baca.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            linearRating.setVisibility(View.GONE);
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
        linearRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(deskripsiBuku.this);
                    View layout = null;
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
        desToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(deskripsiBuku.this, Beranda.class);
                startActivity(intent);
            }
        });

        sebelumLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(deskripsiBuku.this, "Harap login terlebih dahulu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(deskripsiBuku.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
//        rating = findViewById(R.id.btnRating);
        desToolbar = findViewById(R.id.desToolbar);
        linearRating  = findViewById(R.id.ratingLinear);
        sebelumLogin = findViewById(R.id.btnSebelumloginDesBuk);
        setSupportActionBar(desToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(deskripsiBuku.this, Beranda.class);
        startActivity(intent);
    }
}

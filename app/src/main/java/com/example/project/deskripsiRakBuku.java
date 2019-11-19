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
import androidx.appcompat.widget.Toolbar;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.deleteListBuku;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class deskripsiRakBuku extends AppCompatActivity {

    TextView judulBuku, deskripsiBuku1, authorDesBuku, perinkatDesBuku;
    ImageView imageDesBuku;
    Button baca, hapus;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Toolbar desListToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_rak_buku);
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
        String id_user = getIntent().getStringExtra("id_user");
        Bitmap bmp = null;
        try {
            URL url = new URL(ApiClient.BASE_URL + img_url);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        judulBuku.setText(judul);
        deskripsiBuku1.setText(deskripsi);
        imageDesBuku.setImageBitmap(bmp);
        perinkatDesBuku.setText(": " + peringkat);
        authorDesBuku.setText(": " + author);

        baca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefManager.getSPSudahLogin()) {
                    Intent i = new Intent(deskripsiRakBuku.this, PdfActivity.class);
                    i.putExtra("pdf_urll", pdf_url);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Harap Login Terlebih Dahulu", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(deskripsiRakBuku.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiInterface.deleteBuku(judul, id_user).enqueue(new Callback<deleteListBuku>() {
                    @Override
                    public void onResponse(Call<deleteListBuku> call, Response<deleteListBuku> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(deskripsiRakBuku.this, "Berhasil dihapus dari list", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(deskripsiRakBuku.this, "Gagal Menghapus", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<deleteListBuku> call, Throwable t) {
                        Toast.makeText(deskripsiRakBuku.this, "Cek koneksi anda", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        desListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(deskripsiRakBuku.this, Beranda.class );
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
        perinkatDesBuku = findViewById(R.id.peringkatDesBuku);
        hapus = findViewById(R.id.btnHapus);
        desListToolbar = findViewById(R.id.desListToolbar);

        setSupportActionBar(desListToolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(deskripsiRakBuku.this, Beranda.class);
        startActivity(intent);
    }
}

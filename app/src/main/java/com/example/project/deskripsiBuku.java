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

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Api.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class deskripsiBuku extends AppCompatActivity {

    TextView judulBuku, deskripsiBuku;
    ImageView imageDesBuku;
    Button download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_buku);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        init();
        ambilDataBuku();

    }

    private void ambilDataBuku() {
        String img_url = getIntent().getStringExtra("img");
        String judul = getIntent().getStringExtra("judul");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String pdf_url = getIntent().getStringExtra("pdf_url");
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
        deskripsiBuku.setText(deskripsi);
        imageDesBuku.setImageBitmap(bmp);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(deskripsiBuku.this, PdfActivity.class);
                i.putExtra("pdf_urll", pdf_url);
                startActivity(i);
            }
        });
    }

    private void init() {
        judulBuku = findViewById(R.id.judulDesBuku);
        deskripsiBuku = findViewById(R.id.deskripsiBuku);
        imageDesBuku = findViewById(R.id.imgDesBuku);
        download = findViewById(R.id.btnDownload);

    }
}

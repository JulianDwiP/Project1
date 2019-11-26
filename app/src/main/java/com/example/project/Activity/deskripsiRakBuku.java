package com.example.project.Activity;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.project.entity.deleteListBuku;
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

public class deskripsiRakBuku extends AppCompatActivity {

    TextView judulBuku, deskripsiBuku1, authorDesBuku, perinkatDesBuku, kategoriDesBuku, listPembaca;
    ImageView imageDesBuku;
    Button baca, hapus, downloadRak;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Toolbar desToolbar;
    LinearLayout linearRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_rak_buku);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        init();
        sharedPrefManager = new SharedPrefManager(this);
        ambilDataBuku();

    }

    private void ambilDataBuku() {
        String img_url = getIntent().getStringExtra("img");
        String judul = getIntent().getStringExtra("judul");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String pdf_url = getIntent().getStringExtra("pdf_url");
        String peringkat = getIntent().getStringExtra("peringkat");
        String author = getIntent().getStringExtra("author");
        String id = getIntent().getStringExtra("id");
        String kategori = getIntent().getStringExtra("kategori");
        String id_user = getIntent().getStringExtra("id_user");
        String id_buku = getIntent().getStringExtra("id_buku");
        String pembaca = getIntent().getStringExtra("pengunjung");
        mApiInterface.getViewRak(id_buku).enqueue(new Callback<com.example.project.entity.View>() {
            @Override
            public void onResponse(Call<com.example.project.entity.View> call, Response<com.example.project.entity.View> response) {
                int x = response.body().getPengunjung();
                listPembaca.setText(String.valueOf(x));
            }

            @Override
            public void onFailure(Call<com.example.project.entity.View> call, Throwable t) {

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
//        listPembaca.setText(pembaca);
        getSupportActionBar().setTitle(judul);

        File exStore = Environment.getExternalStorageDirectory();
        String a = judul+".pdf";
        File myFile = new File(exStore.getAbsolutePath() + "/Ebook Download/"+a);
        if (myFile.exists()){
            downloadRak.setText("Terdownload");
            sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekDownload, true);
        }else{
            downloadRak.setText("Download");
            sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekDownload, false);
        }
        baca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefManager.getSPSudahLogin()){
                    mApiInterface.tambahView(id_buku, 1, id_buku).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Intent i = new Intent(deskripsiRakBuku.this, PdfListBacaan.class);
                                i.putExtra("nama", judul);
                                i.putExtra("pdf_urll", pdf_url);
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
        linearRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(deskripsiRakBuku.this);
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
                Intent intent = new Intent(deskripsiRakBuku.this, Beranda.class);
                intent.putExtra("backTo", "1");
                sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekIntent, true);
                startActivity(intent);
            }
        });
        downloadRak.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (sharedPrefManager.getCekDownload()){
                    Toast.makeText(deskripsiRakBuku.this, "Buku sudah di download", Toast.LENGTH_LONG).show();
                }else{
                    sharedPrefManager.simpanSPSring(SharedPrefManager.namaFile, judul);
                    sharedPrefManager.simpanSPSring(SharedPrefManager.urlFile, ApiClient.BASE_URL+pdf_url);
                    String url = ApiClient.BASE_URL+pdf_url;
                    if (isConnectingToInternet()){
                        new DownloadTask(deskripsiRakBuku.this, downloadRak, url);
                    }else{
                        Toast.makeText(deskripsiRakBuku.this, "Tidak Ada Internet", Toast.LENGTH_SHORT).show();
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
        judulBuku = findViewById(R.id.judulDesRakBuku);
        deskripsiBuku1 = findViewById(R.id.sinopsisDesRakBuku);
        imageDesBuku = findViewById(R.id.imgDesRakBuku);
        baca = findViewById(R.id.btnDesRakBaca);
        hapus = findViewById(R.id.btnDesRakHapus);
        authorDesBuku = findViewById(R.id.authorDesRakBuku);
        kategoriDesBuku = findViewById(R.id.tvDesRakKategori);
        perinkatDesBuku = findViewById(R.id.peringkatDesRakBuku);
        desToolbar = findViewById(R.id.desRakToolbar);
        linearRating  = findViewById(R.id.ratingRakLinear);
        listPembaca = findViewById(R.id.tvListPembaca);
        downloadRak = findViewById(R.id.btnRakDownload);

        setSupportActionBar(desToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(deskripsiRakBuku.this, Beranda.class);
        intent.putExtra("backTo", "1");
        sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekIntent, true);
        startActivity(intent);
    }
}

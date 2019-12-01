package com.example.project.Activity;

import android.app.ProgressDialog;
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
import android.os.Handler;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.Hasil;
import com.example.project.entity.ambilStatusResponse;
import com.example.project.entity.cekDownload;
import com.example.project.entity.deleteDownload;
import com.example.project.entity.downloadResponse;
import com.example.project.entity.masukanPeringkatModel;
import com.example.project.entity.View;
import com.example.project.entity.masukanStatusBuku;
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
    Button baca, add, sebelumLogin, downlaod,  beli;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Toolbar desToolbar;
    LinearLayout linearRating;
    String judul;
    SwipeRefreshLayout swipeRefreshLayout;
    String id_buku, img_url, deskripsi, pdf_url, kategori,peringkat, author,Stpembacaa, harga;
    Float value;
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
        }else{
            mApiInterface.deleteDownload(sharedPrefManager.getId(), cek).enqueue(new Callback<deleteDownload>() {
                @Override
                public void onResponse(Call<deleteDownload> call, Response<deleteDownload> response) {
                    Log.e("Hapus Data", "Berhasil");
                    sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekDownload, false);
                    downlaod.setText("Unduh");
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
        cekUdahBeli();
        ambilPembaca();
        if (sharedPrefManager.getCekDownload()){
            downlaod.setText("Terunduh");
        }else{
            downlaod.setText("Unduh");
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                ambilDataBuku();
                ambilPembaca();
                cekUdahBeli();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });
    }

    private void cekUdahBeli() {
        if (harga.equals("Gratis")){
            beli.setVisibility(android.view.View.GONE);
        }else{
            mApiInterface.ambilStatusBuku(sharedPrefManager.getId(), id_buku).enqueue(new Callback<ambilStatusResponse>() {
                @Override
                public void onResponse(Call<ambilStatusResponse> call, Response<ambilStatusResponse> response) {
                    beli.setVisibility(android.view.View.GONE);
                }

                @Override
                public void onFailure(Call<ambilStatusResponse> call, Throwable t) {
                    baca.setVisibility(android.view.View.GONE);
                    add.setVisibility(android.view.View.GONE);
                    linearRating.setVisibility(android.view.View.GONE);
                    downlaod.setVisibility(android.view.View.GONE);
                    sebelumLogin.setVisibility(android.view.View.GONE);
                    beli.setText("Beli Rp." + harga);
                }
            });
        }
    }

    private void ambilPembaca() {
        mApiInterface.getViewBuku(id_buku).enqueue(new Callback<View>() {
            @Override
            public void onResponse(Call<View> call, Response<View> response) {
                int c = response.body().getPengunjung();
                sharedPrefManager.simpanSPInt(SharedPrefManager.View, c);
                float m = response.body().getPeringkat();
                pembaca.setText(String.valueOf(c));
                perinkatDesBuku.setText(String.valueOf(m));
            }

            @Override
            public void onFailure(Call<View> call, Throwable t) {

            }
        });
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

    private void masukanPeringkat(){
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


    private void ambilDataBuku() {
         id_buku = getIntent().getStringExtra("id_buku");
         img_url = getIntent().getStringExtra("img");

         deskripsi = getIntent().getStringExtra("deskripsi");
         pdf_url = getIntent().getStringExtra("pdf_url");
         peringkat = getIntent().getStringExtra("peringkat");
         author = getIntent().getStringExtra("author");
         kategori = getIntent().getStringExtra("kategori");
         Stpembacaa = getIntent().getStringExtra("pengunjung");
         harga = getIntent().getStringExtra("harga");

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
                            value = ratingBar.getRating();
                            masukanPeringkat();
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
                finish();
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
        beli.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(deskripsiBuku.this);
                builder.setTitle("Beli Buku " + judul);
                builder.setMessage("Harga Rp. "+ harga);
                builder.setPositiveButton("Beli", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(sharedPrefManager.getSPSudahLogin()){
                            masukanStatus();
                        }else{
                            Toast.makeText(deskripsiBuku.this, "Login Terlebih dahulu", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(deskripsiBuku.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });

    }

    private void masukanStatus() {
        mApiInterface.masukanStatus(id_buku, sharedPrefManager.getId(), "dibeli").enqueue(new Callback<masukanStatusBuku>() {
            @Override
            public void onResponse(Call<masukanStatusBuku> call, Response<masukanStatusBuku> response) {
                if (response.isSuccessful()){
                    baca.setVisibility(android.view.View.VISIBLE);
                    add.setVisibility(android.view.View.VISIBLE);
                    linearRating.setVisibility(android.view.View.VISIBLE);
                    downlaod.setVisibility(android.view.View.VISIBLE);
                    beli.setVisibility(android.view.View.GONE);
                    Toast.makeText(deskripsiBuku.this, "Pembelian Berhasil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<masukanStatusBuku> call, Throwable t) {

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
        swipeRefreshLayout = findViewById(R.id.swipeDeskripsiBuku);
        beli = findViewById(R.id.btnBeli);

        setSupportActionBar(desToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed(){
        finish();
    }
}

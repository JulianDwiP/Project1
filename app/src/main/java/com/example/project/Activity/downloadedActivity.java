package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.BukuAdapter;
import com.example.project.Adapter.DownloadAdapter;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.downloadModel;
import com.example.project.entity.downloadResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class downloadedActivity extends AppCompatActivity {

    ApiInterface mApiInterface;
    DownloadAdapter downloadAdapter;
    RecyclerView recyclerView;
    Context context;
    SharedPrefManager sharedPrefManager;
    TextView lokasiDownload, gadaBuku;
    Toolbar toolbarDownload;
    private static final String TAG = "Download List";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        recyclerView = findViewById(R.id.rvDownload);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        sharedPrefManager = new SharedPrefManager(this);
        init();
        getDownloadList();
    }

    private void init() {
        lokasiDownload = findViewById(R.id.downloadLok);
        gadaBuku = findViewById(R.id.tvGagaDownload);
        toolbarDownload = findViewById(R.id.toolbarDownload);

        setSupportActionBar(toolbarDownload);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Buku Terdownload");

        String s = Environment.getExternalStorageDirectory().getPath() +"/Ebook Download/";
        lokasiDownload.setText(s);

    }

    private void getDownloadList() {
        mApiInterface.getDownloaded(sharedPrefManager.getId()).enqueue(new Callback<downloadResponse>() {
            @Override
            public void onResponse(Call<downloadResponse> call, Response<downloadResponse> response) {
                if (response.isSuccessful()) {
                    gadaBuku.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    final List<downloadModel> downloadModels = response.body().getList();
                    context = downloadedActivity.this;
                    downloadAdapter = new DownloadAdapter(downloadModels, context);
                    recyclerView.setAdapter(downloadAdapter);
                }else{
                    Log.e(TAG, "Gagal");
                }
            }

            @Override
            public void onFailure(Call<downloadResponse> call, Throwable t) {
                gadaBuku.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}
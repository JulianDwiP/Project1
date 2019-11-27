package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
    SwipeRefreshLayout swipeRefreshLayout;
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getDownloadList();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });
    }

    private void init() {
        lokasiDownload = findViewById(R.id.downloadLok);
        gadaBuku = findViewById(R.id.tvGagaDownload);
        toolbarDownload = findViewById(R.id.toolbarDownload);

        setSupportActionBar(toolbarDownload);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Buku Terdownload");
        swipeRefreshLayout = findViewById(R.id.swipeDownload);

        String s = Environment.getExternalStorageDirectory().getPath() +"/Ebook Download/";
        lokasiDownload.setText(s);

        toolbarDownload.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(downloadedActivity.this, Beranda.class);
                startActivity(intent);
            }
        });

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

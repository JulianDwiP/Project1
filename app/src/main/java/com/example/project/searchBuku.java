package com.example.project;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.SearchAdapter;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.entity.SearchModel;
import com.example.project.entity.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchBuku extends AppCompatActivity {

    ApiInterface mApiInterface;
    RecyclerView rvSearchbuku;
    SearchAdapter searchAdapter;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_buku);
        initToolbar();
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        rvSearchbuku = findViewById(R.id.rvSearch);
        rvSearchbuku.setLayoutManager(new LinearLayoutManager(this));
//        rvSearchbuku.setHasFixedSize(true);
        getBuku("buku", "");
    }

    private void initToolbar() {
            Toolbar toolbar = findViewById(R.id.searchToolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
//            Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getBuku(String type, String key) {
        mApiInterface.search(type, key).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    final List<SearchModel> searchModels = response.body().getSearchModelList();
                    searchAdapter = new SearchAdapter(searchModels, searchBuku.this);
                    rvSearchbuku.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.i("debug", "Error\n"+t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_buku_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_buku_menu).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getBuku("buku", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getBuku("buku", newText);
                return false;
            }
        });
        return true;
    }
}

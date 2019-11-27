package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.cobaAdapter;
import com.example.project.Adapter.cobaSearchAdapter;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.entity.Buku;
import com.example.project.entity.BukuResponse;
import com.example.project.entity.cobaSearchModel;
import com.example.project.entity.cobaSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchCoba extends AppCompatActivity {
    ApiInterface mApiInterface;
    RecyclerView rvCobaSearch;
    Context context;
    EditText cobaSearch;
    ImageView img;
    cobaSearchAdapter cobaSearchAdapter;
    cobaAdapter cobaAdapterr;
    TextView tvNull;
    Spinner spinnerFilter, spinnerKategori;
    String filter, kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_coba);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        rvCobaSearch = findViewById(R.id.rvCobaSearch);
        rvCobaSearch.setLayoutManager(new LinearLayoutManager(this));
        init();
        tvNull.setVisibility(View.GONE);
        ambilData();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        cobaSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getData();
                    return true;
                }
                return false;
            }
        });
    }

    private void ambilData() {
        mApiInterface.getSemuaBuku().enqueue(new Callback<BukuResponse>() {
            @Override
            public void onResponse(Call<BukuResponse> call, Response<BukuResponse> response) {
                if (response.isSuccessful()){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    final List<Buku> semuaBukus = response.body().getList();
                    context = searchCoba.this;
                    cobaAdapterr = new cobaAdapter(semuaBukus, context);
                    rvCobaSearch.setAdapter(cobaAdapterr);
                    cobaAdapterr.notifyDataSetChanged();
                }else{
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BukuResponse> call, Throwable t) {

            }
        });
    }

    private void getData() {
        String search = cobaSearch.getText().toString();
        mApiInterface.cobaSearch(search, filter, kategori).enqueue(new Callback<cobaSearchResponse>() {
            @Override
            public void onResponse(Call<cobaSearchResponse> call, Response<cobaSearchResponse> response) {
                if (response.isSuccessful()){
                    tvNull.setVisibility(View.GONE);
                    rvCobaSearch.setVisibility(View.VISIBLE);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    final List<cobaSearchModel> cobaSearchModels = response.body().getList();
                    context = searchCoba.this;
                    cobaSearchAdapter = new cobaSearchAdapter(cobaSearchModels, context);
                    rvCobaSearch.setAdapter(cobaSearchAdapter);
                    }else{
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<cobaSearchResponse> call, Throwable t) {
                tvNull.setVisibility(View.VISIBLE);
                rvCobaSearch.setVisibility(View.GONE);
            }
        });
    }

    private void init() {
        cobaSearch = findViewById(R.id.etSearch);
        img = findViewById(R.id.imgSearch);
        tvNull = findViewById(R.id.tvSearchNull);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        spinnerKategori = findViewById(R.id.spinnerKategori);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filter = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kategori = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}

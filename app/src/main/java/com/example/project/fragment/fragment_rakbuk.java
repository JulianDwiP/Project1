package com.example.project.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project.Adapter.rakBukuAdapter;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.Buku;
import com.example.project.entity.rakBuku;
import com.example.project.entity.rakBukuResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_rakbuk extends Fragment {

    ApiInterface mApiInterface;
    rakBukuAdapter rakBukuAdapter;
    RecyclerView rvRakBuku;
    fragment_rakbuk context;
    SharedPrefManager sharedPrefManager;
    TextView tvNull;
    SwipeRefreshLayout swipeRefreshLayout;

    public fragment_rakbuk() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rakbuk, container, false);
        final FragmentActivity c = getActivity();
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        rvRakBuku = view.findViewById(R.id.rvRakBuku);
        tvNull = view.findViewById(R.id.tvRakBukNull);
        rvRakBuku.setLayoutManager(new LinearLayoutManager(c));
        sharedPrefManager = new SharedPrefManager(c);
        getRakBuku();
        swipeRefreshLayout = view.findViewById(R.id.swipeList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getRakBuku();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });


       return view;
    }
    private void getRakBuku(){
        mApiInterface.getSemuaRakbuku(sharedPrefManager.getId()).enqueue(new Callback<rakBukuResponse>() {
            @Override
            public void onResponse(Call<rakBukuResponse> call, Response<rakBukuResponse> response) {
                if (response.isSuccessful()){
                    tvNull.setVisibility(View.GONE);
                    rvRakBuku.setVisibility(View.VISIBLE);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    final List<rakBuku> rakBukus = response.body().getRakBukuList();
                    context = fragment_rakbuk.this;
                    rakBukuAdapter = new rakBukuAdapter(getContext(), rakBukus);
                    rakBukuAdapter.notifyDataSetChanged();
                    rvRakBuku.setAdapter(rakBukuAdapter);
                }else{
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<rakBukuResponse> call, Throwable t) {
                tvNull.setVisibility(View.VISIBLE);
                rvRakBuku.setVisibility(View.GONE);
            }
        });
    }

}

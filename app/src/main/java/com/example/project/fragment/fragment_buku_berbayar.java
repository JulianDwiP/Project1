package com.example.project.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.Adapter.BukuAdapter;
import com.example.project.Adapter.BukuBayarAdapter;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.entity.Buku;
import com.example.project.entity.BukuBayar;
import com.example.project.entity.BukuBayarResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_buku_berbayar extends Fragment {

    ApiInterface mApiInterface;
    BukuBayarAdapter bukuBayarAdapter;
    RecyclerView recyclerView;
    fragment_buku_berbayar context;
    SwipeRefreshLayout swipeRefreshLayout;
    public fragment_buku_berbayar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fragment_buku_berbayar, container, false);
        final FragmentActivity c = getActivity();
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        recyclerView = view.findViewById(R.id.rvBukuBayar);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        ambilBuku();

        swipeRefreshLayout = view.findViewById(R.id.swipeBukuBayar);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                ambilBuku();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });
        return view;
    }

    private void ambilBuku() {
        mApiInterface.getSemuaBukuBayar().enqueue(new Callback<BukuBayarResponse>() {
            @Override
            public void onResponse(Call<BukuBayarResponse> call, Response<BukuBayarResponse> response) {
                if (response.isSuccessful()) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    final List<BukuBayar> bukuBayars = response.body().getList();
                    context = fragment_buku_berbayar.this;
                    bukuBayarAdapter = new BukuBayarAdapter(bukuBayars, getContext());
                    recyclerView.setAdapter(bukuBayarAdapter);
                }else{
                    Toast.makeText(getContext(), "Cek Koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BukuBayarResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Cek Koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

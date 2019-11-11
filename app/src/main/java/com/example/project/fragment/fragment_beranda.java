package com.example.project.fragment;


import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.BukuAdapter;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.entity.Buku;
import com.example.project.entity.BukuResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_beranda extends Fragment {

    ApiInterface mApiInterface;
    BukuAdapter bukuAdapter;
    RecyclerView recyclerView;
    fragment_beranda context;

    public fragment_beranda() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        final FragmentActivity c = getActivity();

        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        getBuku();

        return view;
    }

    private void getBuku(){
        mApiInterface.getSemuaBuku().enqueue(new Callback<BukuResponse>() {
            @Override
            public void onResponse(Call<BukuResponse> call, Response<BukuResponse> response) {
                if (response.isSuccessful()){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    final List<Buku> semuaBukus = response.body().getList();
                    context = fragment_beranda.this;
                    bukuAdapter = new BukuAdapter(getContext(), semuaBukus);
                    recyclerView.setAdapter(bukuAdapter);
                }else{
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<BukuResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();

            }
        });
    }


}

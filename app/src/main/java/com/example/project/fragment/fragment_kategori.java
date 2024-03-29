package com.example.project.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project.Adapter.KategoriAdapter;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.Model.Kategori;
import com.example.project.Model.KategoriResponse;
import com.example.project.Model.ListSpinnerKategori;
import com.example.project.Model.ListSpinnerResponse;
import com.example.project.R;
import com.example.project.entity.Buku;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_kategori extends Fragment {
    ApiInterface mApiInterface;

    Spinner spinner;
    RecyclerView rvKategori;
    fragment_kategori context;
    TextView tvNull;
    KategoriAdapter kategoriAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String diClick;
    public fragment_kategori() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_kategori, container, false);
        final FragmentActivity c = getActivity();

        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        spinner = view.findViewById(R.id.spinner);
        tvNull = view.findViewById(R.id.tvKategoriNull);
        rvKategori = view.findViewById(R.id.rvKategori);
        rvKategori.setLayoutManager(new LinearLayoutManager(c));

        initSpinner();

        swipeRefreshLayout = view.findViewById(R.id.swipeKategori);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                diClick = parent.getItemAtPosition(position).toString();
                getResult();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getResult();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });
        return view;
    }
    public void initSpinner(){
        mApiInterface.getListSpinner().enqueue(new Callback<ListSpinnerResponse>() {
            @Override
            public void onResponse(Call<ListSpinnerResponse> call, Response<ListSpinnerResponse> response) {
                if (response.isSuccessful()){
                    List<ListSpinnerKategori> semua_kategori = response.body().getList();
                    List<String> listSpinner = new ArrayList<String>();
                    for (int i = 0; i < semua_kategori.size(); i++ ){
                        listSpinner.add(semua_kategori.get(i).getKategori());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Gagal mengambil data ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ListSpinnerResponse> call, Throwable t) {

            }
        });
    }

    private void getResult(){
        mApiInterface.getByKategori(diClick).enqueue(new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                if (response.isSuccessful()){
                    tvNull.setVisibility(View.GONE);
                    rvKategori.setVisibility(View.VISIBLE);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    final List<Kategori> semuaKategori = response.body().getKategoriList();
                    context = fragment_kategori.this;
                    kategoriAdapter = new KategoriAdapter(semuaKategori, getContext());
                    rvKategori.setAdapter(kategoriAdapter);
                }else{
                    Toast.makeText(getContext(), "Tidak Ada Buku", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {
                tvNull.setVisibility(View.VISIBLE);
                rvKategori.setVisibility(View.GONE);
            }
        });
    }

}

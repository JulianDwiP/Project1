package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.deskripsiBuku;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.Buku;
import com.example.project.entity.BukuBayar;
import com.example.project.entity.ambilStatus;
import com.example.project.entity.ambilStatusResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BukuBayarAdapter extends RecyclerView.Adapter<BukuBayarAdapter.BukuBayarHolder> {
    List<BukuBayar> bukuBayars;
    Context mContext;
    ApiInterface mApiInterface;
    SharedPrefManager sharedPrefManager;

    public BukuBayarAdapter(List<BukuBayar> bukuBayar, Context context) {
        bukuBayars = bukuBayar;
        mContext = context;
    }

    @Override
    public BukuBayarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recyclerview_buku, parent, false);
        return new BukuBayarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BukuBayarHolder holder, int position) {
        final BukuBayar bukuBayar = bukuBayars.get(position);
        Bitmap bmp = null;
        String poto;
        if (bukuBayar.getPdfIcon().equals("")){
            poto = "pdf-icons/pingu.jpg";
        }else{
            poto = bukuBayar.getPdfIcon();
        }
        try{
            URL url = new URL(ApiClient.BASE_URL+poto);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos );
        }catch (IOException e){
            e.printStackTrace();
        }
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(mContext);

        if (sharedPrefManager.getId().equals("")){
        }else{
            mApiInterface.ambilStatusBuku(sharedPrefManager.getId(),bukuBayar.getId()).enqueue(new Callback<ambilStatusResponse>() {
                @Override
                public void onResponse(Call<ambilStatusResponse> call, Response<ambilStatusResponse> response) {
                    ambilStatus ambilStatus = new ambilStatus();
                    holder.hargaRp.setVisibility(View.GONE);
                    holder.status.setVisibility(View.VISIBLE);
                    holder.hargaGratis.setVisibility(View.GONE);
                    holder.status.setText("dibeli");
                }

                @Override
                public void onFailure(Call<ambilStatusResponse> call, Throwable t) {
                    if (bukuBayar.getHarga().equals("Gratis")){
                        holder.hargaRp.setVisibility(View.GONE);
                        holder.status.setVisibility(View.GONE);
                        holder.hargaGratis.setVisibility(View.VISIBLE);
                        holder.hargaGratis.setText(bukuBayar.getHarga());
                    }else{
                        holder.hargaRp.setVisibility(View.VISIBLE);
                        holder.hargaGratis.setVisibility(View.GONE);
                        holder.status.setVisibility(View.GONE);
                        holder.hargaRp.setText("Rp " + bukuBayar.getHarga());
                    }
                }
            });
        }
        int pembaca = bukuBayar.getPengunjung();
        holder.fotoBuku.setImageBitmap(bmp);
        holder.judulBuku.setText(bukuBayar.getNama());
        holder.sinopsisBuku.setText(bukuBayar.getDeskripsi());
        holder.peringkatBuku.setText(bukuBayar.getPeringkat());

        holder.cardViewBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, deskripsiBuku.class);
                i.putExtra("id_buku", bukuBayar.getId());
                i.putExtra("judul", bukuBayar.getNama());
                i.putExtra("deskripsi", bukuBayar.getDeskripsi());
                i.putExtra("img", bukuBayar.getPdfIcon());
                i.putExtra("pdf_url", bukuBayar.getPdfUrl());
                i.putExtra("peringkat", bukuBayar.getPeringkat());
                i.putExtra("author", bukuBayar.getAuthor());
                i.putExtra("kategori", bukuBayar.getKategori());
                i.putExtra("pengunjung", String.valueOf(pembaca));
                i.putExtra("harga", bukuBayar.getHarga());
                i.putExtra("status", bukuBayar.getStatus());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bukuBayars.size();
    }

    public class BukuBayarHolder extends RecyclerView.ViewHolder {
        TextView judulBuku, penulisBuku, sinopsisBuku, peringkatBuku, hargaRp, hargaGratis, status;
        ImageView fotoBuku;
        CardView cardViewBuku;
        public BukuBayarHolder(@NonNull View itemView) {
            super(itemView);
            judulBuku = itemView.findViewById(R.id.judulBuku);
//            penulisBuku = itemView.findViewById(R.id.penulisBuku);
            sinopsisBuku = itemView.findViewById(R.id.sinopsisBuku);
            fotoBuku = itemView.findViewById(R.id.fotoBuku);
            cardViewBuku = itemView.findViewById(R.id.CardViewBuku);
            peringkatBuku = itemView.findViewById(R.id.peringkatRecyclerView);
            hargaRp = itemView.findViewById(R.id.hargaRp);
            hargaGratis = itemView.findViewById(R.id.hargaGratis);
            status = itemView.findViewById(R.id.status);
        }
    }
}

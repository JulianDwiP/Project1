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
import com.example.project.Activity.deskripsiKategori;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.Model.Kategori;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.ambilStatus;
import com.example.project.entity.ambilStatusResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder> {
    List<Kategori> semuaIsiKategori;
    Context mContext;
    ApiInterface mApiInterface;
    SharedPrefManager sharedPrefManager;

    public class KategoriViewHolder extends RecyclerView.ViewHolder {
        TextView Kat_judulBuku, Kat_penulisBuku, Kat_sinopsisBuku, peringkat, hargaRp, hargaGratis, status ;
        ImageView Kat_fotoBuku;
        CardView Kat_cardViewBuku;
        public KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            Kat_judulBuku = itemView.findViewById(R.id.judulBuku);
            Kat_sinopsisBuku = itemView.findViewById(R.id.sinopsisBuku);
            Kat_fotoBuku = itemView.findViewById(R.id.fotoBuku);
            Kat_cardViewBuku = itemView.findViewById(R.id.CardViewBuku);
            peringkat = itemView.findViewById(R.id.peringkatRecyclerView);
            hargaRp = itemView.findViewById(R.id.hargaRp);
            hargaGratis = itemView.findViewById(R.id.hargaGratis);
            status = itemView.findViewById(R.id.status);
        }
    }

    public KategoriAdapter(List<Kategori> semuaIsiKategori, Context mContext) {
        this.semuaIsiKategori = semuaIsiKategori;
        this.mContext = mContext;
    }

    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recyclerview_buku, parent, false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriViewHolder holder, int position) {
        final  Kategori kategori = semuaIsiKategori.get(position);
        Bitmap bmp = null;
        String poto;
        if (kategori.getPdfIcon().equals("")){
            poto = "pdf-icons/pingu.jpg";
        }else{
            poto = kategori.getPdfIcon();
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
            mApiInterface.ambilStatusBuku(sharedPrefManager.getId(),kategori.getId()).enqueue(new Callback<ambilStatusResponse>() {
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
                    if (kategori.getHarga().equals("Gratis")){
                        holder.hargaRp.setVisibility(View.GONE);
                        holder.status.setVisibility(View.GONE);
                        holder.hargaGratis.setVisibility(View.VISIBLE);
                        holder.hargaGratis.setText(kategori.getHarga());
                    }else{
                        holder.hargaRp.setVisibility(View.VISIBLE);
                        holder.hargaGratis.setVisibility(View.GONE);
                        holder.status.setVisibility(View.GONE);
                        holder.hargaRp.setText("Rp " + kategori.getHarga());
                    }
                }
            });
        }
        holder.Kat_fotoBuku.setImageBitmap(bmp);
        holder.Kat_judulBuku.setText(kategori.getNama());
        holder.Kat_sinopsisBuku.setText(kategori.getDeskripsi());
        holder.peringkat.setText(kategori.getPeringkat());

        holder.Kat_cardViewBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, deskripsiKategori.class);
                i.putExtra("judul", kategori.getNama());
                i.putExtra("deskripsi", kategori.getDeskripsi());
                i.putExtra("img", kategori.getPdfIcon());
                i.putExtra("pdf_url", kategori.getPdfUrl());
                i.putExtra("peringkat", kategori.getPeringkat());
                i.putExtra("author", kategori.getAuthor());
                i.putExtra("kategori", kategori.getKategori());
                i.putExtra("id_buku", kategori.getId());
                i.putExtra("harga", kategori.getHarga());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return semuaIsiKategori.size();
    }


}

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
import com.example.project.R;
import com.example.project.entity.cobaSearchModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class cobaSearchAdapter extends RecyclerView.Adapter<cobaSearchAdapter.ViewHolder> {
    List<cobaSearchModel> cobaSearchModels;
    Context mContext;

    public cobaSearchAdapter(List<cobaSearchModel> cobaSearchModels, Context mContext) {
        this.cobaSearchModels = cobaSearchModels;
        this.mContext = mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judulBuku, penulisBuku, sinopsisBuku, peringkat, gadaBuku;
        ImageView fotoBuku;
        CardView cardViewBuku;
        public ViewHolder(View itemView) {
            super(itemView);
            judulBuku = itemView.findViewById(R.id.judulBuku);
//            penulisBuku = itemView.findViewById(R.id.penulisBuku);
            sinopsisBuku = itemView.findViewById(R.id.sinopsisBuku);
            fotoBuku = itemView.findViewById(R.id.fotoBuku);
            cardViewBuku = itemView.findViewById(R.id.CardViewBuku);
            peringkat = itemView.findViewById(R.id.peringkatRecyclerView);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recyclerview_buku, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(cobaSearchAdapter.ViewHolder holder, int position) {
        final cobaSearchModel search = cobaSearchModels.get(position);

            Bitmap bmp = null;
            String poto;
            if (search.getPdfIcon().equals("")){
                poto = "pdf-icons/pingu.jpg";
            }else{
                poto = search.getPdfIcon();
            }
            try{
                URL url = new URL(ApiClient.BASE_URL+poto);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos );
            }catch (IOException e){
                e.printStackTrace();
            }
            holder.fotoBuku.setImageBitmap(bmp);
            holder.judulBuku.setText(search.getNama());
//            holder.penulisBuku.setText(search.getAuthor());
            holder.sinopsisBuku.setText(search.getDeskripsi());
            holder.peringkat.setText(search.getPeringkat());

            holder.cardViewBuku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent i = new Intent(mContext, deskripsiBuku.class);
                        i.putExtra("judul", search.getNama());
                        i.putExtra("deskripsi", search.getDeskripsi());
                        i.putExtra("img", search.getPdfIcon());
                        i.putExtra("pdf_url", search.getPdfUrl());
                        i.putExtra("peringkat", search.getPeringkat());
                        i.putExtra("author", search.getAuthor());
                        i.putExtra("kategori", search.getKategori());
                        i.putExtra("pengunjung", String.valueOf(search.getPengunjung()));
                        i.putExtra("id_buku", search.getId());
                        mContext.startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return cobaSearchModels.size();
    }


}

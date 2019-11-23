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
import com.example.project.entity.Buku;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class cobaAdapter extends RecyclerView.Adapter<cobaAdapter.ViewHolder> {
    List<Buku> semuaBukus;
    Context mContext;

    public cobaAdapter(List<Buku> semuaBuku, Context context) {
        semuaBukus = semuaBuku;
        mContext = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judulBuku, penulisBuku, sinopsisBuku, peringkatBuku;
        ImageView fotoBuku;
        CardView cardViewBuku;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judulBuku = itemView.findViewById(R.id.judulBuku);
//            penulisBuku = itemView.findViewById(R.id.penulisBuku);
            sinopsisBuku = itemView.findViewById(R.id.sinopsisBuku);
            fotoBuku = itemView.findViewById(R.id.fotoBuku);
            cardViewBuku = itemView.findViewById(R.id.CardViewBuku);
            peringkatBuku = itemView.findViewById(R.id.peringkatRecyclerView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recyclerview_buku, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cobaAdapter.ViewHolder holder, int position) {
        final Buku buku = semuaBukus.get(position);
        Bitmap bmp = null;
        String poto;
        if (buku.getPdfIcon().equals("")){
            poto = "pdf-icons/pingu.jpg";
        }else{
            poto = buku.getPdfIcon();
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
        holder.judulBuku.setText(buku.getNama());
//        holder.penulisBuku.setText(buku.getAuthor());
        holder.sinopsisBuku.setText(buku.getDeskripsi());
        holder.peringkatBuku.setText(buku.getPeringkat());

        holder.cardViewBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, deskripsiKategori.class);
                intent.putExtra("id_buku", buku.getId());
                intent.putExtra("judul", buku.getNama());
                intent.putExtra("deskripsi", buku.getDeskripsi());
                intent.putExtra("img", buku.getPdfIcon());
                intent.putExtra("pdf_url", buku.getPdfUrl());
                intent.putExtra("peringkat", buku.getPeringkat());
                intent.putExtra("author", buku.getAuthor());
                intent.putExtra("kategori", buku.getKategori());
                intent.putExtra("pengunjung", String.valueOf(buku.getPengunjung()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return semuaBukus.size();
    }


}

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
import com.example.project.R;
import com.example.project.entity.Buku;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.BukuViewHolder> {

    List<Buku> semuaBuku;
    Context mContext;
    class BukuViewHolder extends  RecyclerView.ViewHolder{
        TextView judulBuku, penulisBuku, sinopsisBuku, peringkatBuku;
        ImageView fotoBuku;
        CardView cardViewBuku;

        public BukuViewHolder(@NonNull View itemView) {
            super(itemView);
            judulBuku = itemView.findViewById(R.id.judulBuku);
            penulisBuku = itemView.findViewById(R.id.penulisBuku);
            sinopsisBuku = itemView.findViewById(R.id.sinopsisBuku);
            fotoBuku = itemView.findViewById(R.id.fotoBuku);
            cardViewBuku = itemView.findViewById(R.id.CardViewBuku);
            peringkatBuku = itemView.findViewById(R.id.peringkatRecyclerView);
        }
    }
    public BukuAdapter (Context context, List<Buku> bukuList ){
        mContext = context;
        semuaBuku = bukuList;
    }
    @Override
    public BukuViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recyclerview_buku, parent, false);
        return  new BukuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BukuViewHolder holder, int position) {
        final Buku buku = semuaBuku.get(position);
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
        int pembaca = buku.getPengunjung();
        holder.fotoBuku.setImageBitmap(bmp);
        holder.judulBuku.setText(buku.getNama());
        holder.penulisBuku.setText(buku.getAuthor());
        holder.sinopsisBuku.setText(buku.getDeskripsi());
        holder.peringkatBuku.setText(buku.getPeringkat());

        holder.cardViewBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, deskripsiBuku.class);
                i.putExtra("id_buku", buku.getId());
                i.putExtra("judul", buku.getNama());
                i.putExtra("deskripsi", buku.getDeskripsi());
                i.putExtra("img", buku.getPdfIcon());
                i.putExtra("pdf_url", buku.getPdfUrl());
                i.putExtra("peringkat", buku.getPeringkat());
                i.putExtra("author", buku.getAuthor());
                i.putExtra("kategori", buku.getKategori());
                i.putExtra("pengunjung", String.valueOf(pembaca));
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return semuaBuku.size();
    }
}

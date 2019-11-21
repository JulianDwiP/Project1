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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.deskripsiRakBuku;
import com.example.project.Api.ApiClient;
import com.example.project.R;
import com.example.project.entity.rakBuku;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class rakBukuAdapter extends RecyclerView.Adapter<rakBukuAdapter.rakBukuHolder> {
    List<rakBuku> semuaBuku;
    Context mContext;

    public class rakBukuHolder extends RecyclerView.ViewHolder {
        TextView Rb_judulBuku, Rb_penulisBuku, Rb_sinopsisBuku, peringkat;
        ImageView Rb_fotoBuku;
        CardView Rb_cardViewBuku;
        public rakBukuHolder(View view) {
            super(view);
            Rb_judulBuku = view.findViewById(R.id.judulBuku);
            Rb_penulisBuku = view.findViewById(R.id.penulisBuku);
            Rb_sinopsisBuku = view.findViewById(R.id.sinopsisBuku);
            Rb_fotoBuku = view.findViewById(R.id.fotoBuku);
            Rb_cardViewBuku = view.findViewById(R.id.CardViewBuku);
            peringkat = view.findViewById(R.id.peringkatRecyclerView);
        }
    }

    public rakBukuAdapter(Context context, List<rakBuku> rakBukus){
        mContext = context;
        semuaBuku = rakBukus;
    }


    @Override
    public rakBukuHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recyclerview_buku, parent, false);
        return new rakBukuHolder(view);
    }

    @Override
    public void onBindViewHolder(rakBukuHolder holder, int position) {
        final  rakBuku rakBuku = semuaBuku.get(position);
        Bitmap bmp = null;
        String poto;
        if (rakBuku.getPdfIcon().equals("")){
            poto = "pdf-icons/pingu.jpg";
        }else{
            poto = rakBuku.getPdfIcon();
        }
        try{
            URL url = new URL(ApiClient.BASE_URL+poto);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos );
        }catch (IOException e){
            e.printStackTrace();
        }
        holder.Rb_fotoBuku.setImageBitmap(bmp);
        holder.Rb_judulBuku.setText(rakBuku.getNama());
        holder.Rb_penulisBuku.setText(rakBuku.getAuthor());
        holder.Rb_sinopsisBuku.setText(rakBuku.getDeskripsi());
        holder.peringkat.setText(rakBuku.getPeringkat());
        holder.Rb_cardViewBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, deskripsiRakBuku.class);
                i.putExtra("judul", rakBuku.getNama());
                i.putExtra("deskripsi", rakBuku.getDeskripsi());
                i.putExtra("img", rakBuku.getPdfIcon());
                i.putExtra("id", rakBuku.getId());
                i.putExtra("pdf_url", rakBuku.getPdfUrl());
                i.putExtra("peringkat", rakBuku.getPeringkat());
                i.putExtra("author",rakBuku.getAuthor());
                i.putExtra("kategori", rakBuku.getKategori());
                i.putExtra("id_user", rakBuku.getId_user());
                i.putExtra("id_buku", rakBuku.getId_buku());
                i.putExtra("pengunjung", String.valueOf(rakBuku.getPengunjung()));
                mContext.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return semuaBuku.size();
    }

}

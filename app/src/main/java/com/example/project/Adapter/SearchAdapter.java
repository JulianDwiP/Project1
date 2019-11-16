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

import com.example.project.Api.ApiClient;
import com.example.project.R;
import com.example.project.deskripsiBuku;
import com.example.project.entity.SearchModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    List<SearchModel> semuaSearch;
    Context mContext;

    public SearchAdapter(List<SearchModel> semuaSearch, Context mContext) {
        this.semuaSearch = semuaSearch;
        this.mContext = mContext;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recyclerview_buku, parent, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchHolder holder, int position) {
        final SearchModel searchModel = semuaSearch.get(position);
        Bitmap bmp = null;
        String poto;
        if (searchModel.getPdfIcon().equals("")){
            poto = "pdf-icons/pingu.jpg";
        }else{
            poto = searchModel.getPdfIcon();
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
        holder.judulBuku.setText(searchModel.getNama());
        holder.penulisBuku.setText(searchModel.getAuthor());
        holder.sinopsisBuku.setText(searchModel.getDeskripsi());

        holder.cardViewBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, deskripsiBuku.class);
                i.putExtra("judul", searchModel.getNama());
                i.putExtra("deskripsi", searchModel.getDeskripsi());
                i.putExtra("img", searchModel.getPdfIcon());
                i.putExtra("pdf_url", searchModel.getPdfUrl());
                i.putExtra("peringkat", searchModel.getPeringkat());
                i.putExtra("author", searchModel.getAuthor());
                i.putExtra("kategori", searchModel.getKategori());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return semuaSearch.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {
        TextView judulBuku, penulisBuku, sinopsisBuku;
        ImageView fotoBuku;
        CardView cardViewBuku;
        public SearchHolder( View itemView) {
            super(itemView);
            judulBuku = itemView.findViewById(R.id.judulBuku);
            penulisBuku = itemView.findViewById(R.id.penulisBuku);
            sinopsisBuku = itemView.findViewById(R.id.sinopsisBuku);
            fotoBuku = itemView.findViewById(R.id.fotoBuku);
            cardViewBuku = itemView.findViewById(R.id.CardViewBuku);
        }
    }
}

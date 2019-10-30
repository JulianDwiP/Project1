package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.model_DBbuku;
import com.example.project.R;
import com.example.project.sqlHelper.WorldListOpenHelper;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.BukuViewHolder> {

    class BukuViewHolder extends  RecyclerView.ViewHolder{
        public final TextView judulBuku, penulisBuku, sinopsisBuku;
        ImageView fotoBuku;

        public BukuViewHolder(@NonNull View itemView) {
            super(itemView);
            judulBuku = itemView.findViewById(R.id.judulBuku);
            penulisBuku = itemView.findViewById(R.id.penulisBuku);
            sinopsisBuku = itemView.findViewById(R.id.sinopsisBuku);
            fotoBuku = itemView.findViewById(R.id.fotoBuku);
        }
    }
    private static  final String  TAG = BukuAdapter.class.getSimpleName();

    private final LayoutInflater mInflater;
    WorldListOpenHelper mDB;
    Context mContext;
    public BukuAdapter (Context context, WorldListOpenHelper db ){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB=db;
    }
    @Override
    public BukuViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.model_recyclerview_buku, parent, false);
        return  new BukuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BukuViewHolder holder, int position) {
        model_DBbuku current = mDB.query(position);
        holder.judulBuku.setText(current.getJudul()+"");
        holder.fotoBuku.setImageResource(current.getFoto());
        holder.sinopsisBuku.setText(current.getSinopsis()+"");
        holder.penulisBuku.setText(current.getPenulis()+"");
    }

    @Override
    public int getItemCount() {
        return (int)mDB.count();
    }
}

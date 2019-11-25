package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.PdfActivity;
import com.example.project.R;
import com.example.project.entity.downloadModel;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.HolderDownload> {

    List<downloadModel> models;
    Context mContext;

    public DownloadAdapter(List<downloadModel> models, Context mContext) {
        this.models = models;
        this.mContext = mContext;
    }

    @Override
    public HolderDownload onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_rv_download, parent, false);
        return new HolderDownload(view);
    }

    @Override
    public void onBindViewHolder(HolderDownload holder, int position) {
        final downloadModel downloadModel = models.get(position);
        String s = downloadModel.getNama()+".pdf";
        holder.tvDownload.setText(s);

        holder.cvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, PdfActivity.class);
                i.putExtra("pdf_urll", downloadModel.getPdfUrl());
                i.putExtra("judul", downloadModel.getNama());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class HolderDownload extends RecyclerView.ViewHolder {
        TextView tvDownload;
        CardView cvDownload;
        public HolderDownload(@NonNull View itemView) {
            super(itemView);
            tvDownload = itemView.findViewById(R.id.tvDownload);
            cvDownload = itemView.findViewById(R.id.cvDownload);
        }
    }
}

package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.PdfActivity;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.deleteDownload;
import com.example.project.entity.downloadModel;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.HolderDownload> {

    List<downloadModel> models;
    Context mContext;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;

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

        sharedPrefManager = new SharedPrefManager(mContext);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);

        String cek = downloadModel.getNama();
        File exStore = Environment.getExternalStorageDirectory();
        String a = cek+".pdf";
        File myFile = new File(exStore.getAbsolutePath() + "/Ebook Download/"+a);
        if (myFile.exists()){
        }else{
            mApiInterface.deleteDownload(sharedPrefManager.getId(), cek).enqueue(new Callback<deleteDownload>() {
                @Override
                public void onResponse(Call<deleteDownload> call, Response<deleteDownload> response) {
                    Log.e("Hapus Data", "Berhasil");
                }
                @Override
                public void onFailure(Call<deleteDownload> call, Throwable t) {
                    Log.e("Hapus Data", "Gagal");
                }
            });
        }

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

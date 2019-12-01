package com.example.project.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context mContext;
    private Button btnDownload;
    private String downloadUrl ="", downloadFileName ="";
    SharedPrefManager sharedPrefManager;
    private ProgressDialog mProgressDialog;
    PowerManager.WakeLock mWakeLock;

    public DownloadTask(Context mContext, Button btnDownload, String downloadUrl) {
        this.mContext = mContext;
        this.btnDownload = btnDownload;
        this.downloadUrl = downloadUrl;

//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setMessage("Mengunduh");
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        mProgressDialog.setCancelable(true);

        sharedPrefManager = new SharedPrefManager(mContext);

        downloadFileName = downloadUrl.replace(sharedPrefManager.getNamaFile(), "");
        Log.e(TAG, downloadFileName);

        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Integer, Void>{

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnDownload.setEnabled(false);
            mProgressDialog = ProgressDialog.show(mContext, null, "Mengunduh..", true, false);
//            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                    getClass().getName());
//            mWakeLock.acquire();
//            mProgressDialog.show();
        }

//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            // if we get here, length is known, now set indeterminate to false
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setMax(100);
//            mProgressDialog.setProgress(progress[0]);
//        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try{
                if (outputFile!= null){
                    btnDownload.setEnabled(true);
//                    mWakeLock.release();
                    mProgressDialog.dismiss();
                    btnDownload.setText("Terunduh");
                    Toast.makeText(mContext, "Berhasil Terunduh", Toast.LENGTH_SHORT).show();
                    sharedPrefManager.simpanSPSring(SharedPrefManager.namaFile, "");
                }else{
                    btnDownload.setText("Unduh Gagal");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnDownload.setEnabled(true);
                            btnDownload.setText("Unduh");
                        }
                    }, 3000);
                }
            } catch (Exception e) {
                e.printStackTrace();
//                mWakeLock.release();
                mProgressDialog.dismiss();
                btnDownload.setText("Unduh Gagal");new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnDownload.setEnabled(true);
                        btnDownload.setText("Unduh");
                    }
                }, 3000);
                Log.e(TAG, "Mengunduh Gagal - " + e.getLocalizedMessage());
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                URL url = new URL(downloadUrl);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();

                if (c.getResponseCode() != HttpURLConnection.HTTP_OK){
                    Log.e(TAG, "Server ngembaliin HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());
                }

                if (new CekForSDCard().isSDCardPresent()){
                    apkStorage = new File(Environment.getExternalStorageDirectory()+ "/Ebook Download");
                }else{
                    Toast.makeText(mContext, "Tidak Ada SD Card.", Toast.LENGTH_SHORT).show();
                }

                if (!apkStorage.exists()){
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Dibuat");
                }
                String a = sharedPrefManager.getNamaFile()+".pdf";
                outputFile = new File(apkStorage, a);
                Log.e(TAG, a);
                if (!outputFile.exists()){
                    outputFile.createNewFile();
                    Log.e(TAG, "File Dibuat");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while((len1 = is.read(buffer)) != -1){
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Unduh Error Exception " + e.getMessage());
            }
            return null;
        }


    }




}

package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.project.Api.ApiClient;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class PdfListBacaan extends AppCompatActivity implements OnLoadCompleteListener, OnPageErrorListener {

    SharedPrefManager sharedPrefManager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list_bacaan);
        toolbar = findViewById(R.id.ToolbarPdfList);
        setSupportActionBar(toolbar);
        String nama_buku = getIntent().getStringExtra("nama");
        getSupportActionBar().setTitle(nama_buku);
        final PDFView pdfView = findViewById(R.id.pdfViewLis);
        sharedPrefManager = new SharedPrefManager(this);

        Intent i = this.getIntent();
        final String pdf_url = i.getExtras().getString("pdf_urll");
        FileLoader.with(this)
                .load(ApiClient.BASE_URL + pdf_url, false)
                .fromDirectory("pdf", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        File pdfFile = response.getBody();
                        try {
                            pdfView.fromFile(pdfFile)
                                    .defaultPage(1)
                                    .enableAnnotationRendering(true)
                                    .onLoad(PdfListBacaan.this)
                                    .scrollHandle(new DefaultScrollHandle(PdfListBacaan.this))
                                    .spacing(10)
                                    .onPageError(PdfListBacaan.this)
                                    .load();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Toast.makeText(PdfListBacaan.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
    }


    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(PdfListBacaan.this, String.valueOf(nbPages), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Toast.makeText(PdfListBacaan.this, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
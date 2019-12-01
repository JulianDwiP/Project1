package com.example.project.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class PdfActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageErrorListener {
    SharedPrefManager sharedPrefManager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        final PDFView pdfView = findViewById(R.id.pdfView);
        sharedPrefManager = new SharedPrefManager(this);

        toolbar = findViewById(R.id.ToolbarPdf);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String judul = getIntent().getStringExtra("judul");
        getSupportActionBar().setTitle(judul);

        Intent i = this.getIntent();
        final String pdf_url = i.getExtras().getString("pdf_urll");
        FileLoader.with(this)
                .load(ApiClient.BASE_URL+pdf_url, false)
                .fromDirectory("pdf", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        File pdfFile = response.getBody();
                        try {
                            pdfView.fromFile(pdfFile)
                                    .defaultPage(1)
                                    .enableAnnotationRendering(true)
                                    .onLoad(PdfActivity.this)
                                    .scrollHandle(new DefaultScrollHandle(PdfActivity.this))
                                    .spacing(10)
                                    .onPageError(PdfActivity.this)
                                    .load();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Toast.makeText(PdfActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(PdfActivity.this, String.valueOf(nbPages),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Toast.makeText(PdfActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}

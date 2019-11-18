package com.example.project;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project.SharedPref.SharedPrefManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class profilePengguna extends AppCompatActivity {
    private static final String TAG = profilePengguna.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    ImageView imgProfile;
    TextView prof_nama, prof_username, prof_email;
    Button btnProf_ubah;
    Beranda beranda;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pengguna);
        sharedPrefManager = new SharedPrefManager(this);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        inits();
        beranda = new Beranda();
        setToolbar();
    }

    private void inits() {
        prof_nama = findViewById(R.id.prof_nama);
        prof_email = findViewById(R.id.prof_email);
        prof_username = findViewById(R.id.prof_username);
        btnProf_ubah = findViewById(R.id.btnProf_ubah);
        imgProfile = findViewById(R.id.prof_poto);
        prof_nama.setText(": "+sharedPrefManager.getSPNama());
        prof_email.setText(": "+sharedPrefManager.getSPEmail());
        prof_username.setText(": "+sharedPrefManager.getUsername());

        String email = "http://192.168.43.236/perpus_db/uploads/" + sharedPrefManager.getId() + ".png";
        String shared = sharedPrefManager.getSPImage();

        if (shared.equals(email)){
            URL url = null;
            try {
                url = new URL(sharedPrefManager.getSPImage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgProfile.setImageBitmap(null);
            imgProfile.setImageBitmap(bmp);
        }else{
            imgProfile.setImageResource(R.drawable.images);
        }


        btnProf_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profilePengguna.this, editUser.class));
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile Pengguna");
        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profilePengguna.this, Beranda.class );
                profilePengguna.this.startActivity(intent);
            }
        });
    }
}


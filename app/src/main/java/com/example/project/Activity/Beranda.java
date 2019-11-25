package com.example.project.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.UserModel;
import com.example.project.fragment.fragment_beranda;
import com.example.project.fragment.fragment_kategori;
import com.example.project.fragment.fragment_rakbuk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class Beranda extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout ndrawer;
    private NavigationView nVdrawer;
    public ImageView potoPropil;
    TextView emailuser;
    TextView namauser;
    SharedPrefManager sharedPrefManager;
    ApiInterface apiInterface;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        userModel = new UserModel();
        setToolbar();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        setFragment(new fragment_beranda());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav2);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        ndrawer = findViewById(R.id.drawer_layout);
        nVdrawer = findViewById(R.id.nvView);
        content(nVdrawer);

        sharedPrefManager = new SharedPrefManager(this);
        init();

        apiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        if (sharedPrefManager.getSPSudahLogin()) {
            hideLogin();
            namauser.setText(sharedPrefManager.getSPNama());
            emailuser.setText(sharedPrefManager.getSPEmail());
            String email = "http://192.168.43.236/perpus_db/uploads/" + sharedPrefManager.getId() + ".png";
            String shared = sharedPrefManager.getSPImage();

            if (shared.equals(email)) {
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
                potoPropil.setImageBitmap(null);
                potoPropil.setImageBitmap(bmp);
            } else {
                potoPropil.setImageResource(R.drawable.images);
            }
        } else {
            showNavMenu();
            namauser.setText("Pengguna");
            emailuser.setText("example@gmail.com");
        }
        if (!sharedPrefManager.getCekIntent()) {
            Fragment fragment = new fragment_beranda();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment).commit();
            }
        }else {
            String s1 = getIntent().getStringExtra("backTo");
            if (s1.equals("1")) {
                setTitle("List Bacaan");
                Fragment fragmentRakbuk = new fragment_rakbuk();
                if (fragmentRakbuk != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, fragmentRakbuk).commit();
                    sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekIntent, false);
                }
            }if (s1.equals("2")){
                Fragment fragment = new fragment_beranda();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, fragment).commit();
                    sharedPrefManager.simpanSPBoolean(SharedPrefManager.cekIntent, false);
                }
            }
        }
    }

    public void init() {
        View headerView = nVdrawer.getHeaderView(0);
        potoPropil = headerView.findViewById(R.id.potoPropil);
        emailuser = headerView.findViewById(R.id.emailUser);
        namauser = headerView.findViewById(R.id.namaUser);

        Button drawLogin = headerView.findViewById(R.id.drawLogin);
        drawLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Beranda.this, MainActivity.class);
                startActivity(intent);
            }
        });

        potoPropil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Beranda.this, profilePengguna.class);
                startActivity(intent);
            }
        });

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.Tooolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_menu_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFragment( Fragment fragment2) {
            Class FragmentClass = fragment_beranda.class;
            setTitle("Beranda");
        try {
            fragment2 = (Fragment) FragmentClass.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment2).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
                ndrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if (item.getItemId() == R.id.menu_search){
            Intent intent = new Intent(Beranda.this, searchCoba.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void content ( NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                pilihMenu(menuItem);
                return true;
            }
        });
    }
    public void pilihMenu ( MenuItem item){
        Fragment fragment = null;
        Class FragmentClass = null;
        switch (item.getItemId()){
            case R.id.profile_pengguna:
                Intent iProfil = new Intent(Beranda.this, profilePengguna.class );
                Beranda.this.startActivity(iProfil);
                break;
            case R.id.catatan_pribadi:
                Intent iCatatan = new Intent(Beranda.this, NotesActivity.class);
                startActivity(iCatatan);
                break;
            case R.id.terdownload:
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +"Ebook Download/");
//                intent.setDataAndType(uri, "application/pdf");
//                startActivity(Intent.createChooser(intent, "Open Folder Download"));
                Intent intent = new Intent(Beranda.this, downloadedActivity.class);
                startActivity(intent);
                break;
            case R.id.keluar:
                sharedPrefManager.simpanSPBoolean(SharedPrefManager.CEK_SESSION, false);
                sharedPrefManager.simpanSPSring(SharedPrefManager.NAMA, "");
                sharedPrefManager.simpanSPSring(SharedPrefManager.USERNAME, "");
                sharedPrefManager.simpanSPSring(SharedPrefManager.PASSWORD, "");
                sharedPrefManager.simpanSPSring(SharedPrefManager.ID, "");
                sharedPrefManager.simpanSPSring(SharedPrefManager.EMAIL, "");
                sharedPrefManager.simpanSPSring(SharedPrefManager.IMAGE, "");
                startActivity(new Intent(Beranda.this, Beranda.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            default:
                    FragmentClass = fragment_beranda.class;
        }
        try {
            fragment = (Fragment) FragmentClass.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
        item.setChecked(true);
        if (fragment !=  null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            setTitle(item.getTitle());
        }
        ndrawer.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId() ){
            case R.id.menu_rakBuku:
                if (sharedPrefManager.getSPSudahLogin()){
                    fragmentClass = fragment_rakbuk.class;    
                }else{
                    Toast.makeText(this, "Harap login terlebih dahulu", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(Beranda.this, MainActivity.class);
                 startActivity(intent);
                }
                break;
            case R.id.menu_kategori:
                fragmentClass = fragment_kategori.class;
                break;
            default:
                fragmentClass = fragment_beranda.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }

        if (fragment !=  null){
           FragmentManager fragmentManager = getSupportFragmentManager();
           fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
           setTitle(menuItem.getTitle());
           return true;
        }
        menuItem.setChecked(true);
        return onOptionsItemSelected(menuItem);
    }

    private void hideLogin()
    {
        Menu nav_Menu = nVdrawer.getMenu();
//        nav_Menu.findItem(R.id.login).setVisible(false);
        View headerView = nVdrawer.getHeaderView(0);
        headerView.findViewById(R.id.drawLogin).setVisibility(View.GONE);
        headerView.findViewById(R.id.textlogin).setVisibility(View.GONE);

    }
    private void showNavMenu(){
        Menu nav_menu = nVdrawer.getMenu();
        nav_menu.findItem(R.id.profile_pengguna).setVisible(false);
        nav_menu.findItem(R.id.catatan_pribadi).setVisible(false);
        nav_menu.findItem(R.id.keluar).setVisible(false);
        nav_menu.findItem(R.id.terdownload).setVisible(false);
        View headerView = nVdrawer.getHeaderView(0);
        headerView.findViewById(R.id.namaUser).setVisibility(View.GONE);
        headerView.findViewById(R.id.emailUser).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}

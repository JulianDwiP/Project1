package com.example.project;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.entity.UserModel;
import com.example.project.fragment.fragment_beranda;
import com.example.project.fragment.fragment_kategori;
import com.example.project.fragment.fragment_rakbuk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class Beranda extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener { private static final String TAG = Beranda.class.getSimpleName();

    private DrawerLayout ndrawer;
    private NavigationView nVdrawer;
    int foto;
    public ImageView potoPropil;
    String nama,email;
    TextView emailuser;
    TextView namauser;
    SharedPrefManager sharedPrefManager;
    ApiInterface apiInterface;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        setToolbar();
        setFragment(new fragment_beranda());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav2);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        ndrawer = findViewById(R.id.drawer_layout);
        nVdrawer = findViewById(R.id.nvView);
        content(nVdrawer);
        sharedPrefManager = new SharedPrefManager(this);
        apiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        init();
    }


    public void init() {
        View headerView = nVdrawer.getHeaderView(0);
        potoPropil = headerView.findViewById(R.id.potoPropil);
        emailuser = headerView.findViewById(R.id.emailUser);
        namauser = headerView.findViewById(R.id.namaUser);
        namauser.setText(sharedPrefManager.getSPNama());
        emailuser.setText(sharedPrefManager.getSPEmail());
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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_search) ;
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
                ndrawer.openDrawer(GravityCompat.START);
                return true;
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
            case R.id.keluar:
                sharedPrefManager.simpanSPBoolean(SharedPrefManager.CEK_SESSION, false);
                startActivity(new Intent(Beranda.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
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
        Class fragmentClass;
        switch (menuItem.getItemId() ){
            case R.id.menu_rakBuku:
                fragmentClass = fragment_rakbuk.class;
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

}

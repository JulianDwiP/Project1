package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GantiPassword extends AppCompatActivity {

    EditText etLupaPassword, etLupaPassword2;
    Button btnLupaPassword;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        sharedPrefManager = new SharedPrefManager(this);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        init();
        btnLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pasAwl = etLupaPassword.getText().toString();
                String pasAkhir = etLupaPassword2.getText().toString();
                if (pasAwl.equals(pasAkhir)){
                    mApiInterface.setPassword(sharedPrefManager.getId(), pasAwl).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    if (jsonObject.getString("error").equals("false")){
                                        sharedPrefManager.simpanSPSring(SharedPrefManager.ID, "");
                                        Toast.makeText(GantiPassword.this, "Berhasil Mengganti password", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(GantiPassword.this, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(GantiPassword.this, "Gagal Mengganti Password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(GantiPassword.this, "Cek Koneksi Anda", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    etLupaPassword2.setError("Harus sama");
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GantiPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        etLupaPassword = findViewById(R.id.etLupaPassword);
        etLupaPassword2 = findViewById(R.id.etLupaPassword2);
        toolbar = findViewById(R.id.ToolbarLupaPassword);
        btnLupaPassword = findViewById(R.id.btnLupaPassword);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ganti Password");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GantiPassword.this, MainActivity.class);
        startActivity(intent);
    }
}

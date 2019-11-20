package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class LupaPasswordVerifiAcc extends AppCompatActivity {
    EditText etEmail, etUsername;
    Button kirimLP;
    ApiInterface mApiInterface;
    Toolbar toolbar;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        init();
        sharedPrefManager = new SharedPrefManager(this);

        kirimLP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiInterface.getAkun(etEmail.getText().toString(), etUsername.getText().toString()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.getString("error").equals("false")){
                                    String id_user = jsonObject.getJSONObject("list").getString("id");
                                    sharedPrefManager.simpanSPSring(SharedPrefManager.ID, id_user);
                                    Intent intent = new Intent(LupaPasswordVerifiAcc.this, GantiPassword.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LupaPasswordVerifiAcc.this, "Email atau Username Salah", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(LupaPasswordVerifiAcc.this, "Cek Koneksi Anda", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LupaPasswordVerifiAcc.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        etEmail = findViewById(R.id.etLPEmail);
        etUsername = findViewById(R.id.etLPUsername);
        kirimLP = findViewById(R.id.btnLPKirim);
        toolbar = findViewById(R.id.ToolbarLP);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Verifikasi Akun");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LupaPasswordVerifiAcc.this, MainActivity.class);
        startActivity(intent);
    }
}

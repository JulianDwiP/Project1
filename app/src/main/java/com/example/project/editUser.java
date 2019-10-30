package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.SharedPref.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editUser extends AppCompatActivity {
    EditText edtUser_nama, edtUser_username;
    Button simpan;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Context mContext;
    Toolbar toolbar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        mContext = this;
        sharedPrefManager = new SharedPrefManager(this);
        init();
    }

    private void init() {
        toolbar4 = findViewById(R.id.toolbarEdtUser);
        setSupportActionBar(toolbar4);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Pengguna");
        edtUser_nama = findViewById(R.id.edtUser_nama);
        edtUser_username = findViewById(R.id.edtUser_Username);
        simpan = findViewById(R.id.btnUser_simpan);

        toolbar4.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, profilePengguna.class));
            }
        });
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqUpdate();
            }
        });
    }

    private void reqUpdate() {
        mApiInterface.updateRequest(edtUser_nama.getText().toString(),
                edtUser_username.getText().toString(), sharedPrefManager.getSPEmail())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULT = new JSONObject(response.body().string());
                                if (jsonRESULT.getString("error").equals("false")){
                                    String nama = jsonRESULT.getJSONObject("user").getString("nama");
                                    String username = jsonRESULT.getJSONObject("user").getString("username");
                                    sharedPrefManager.simpanSPSring(SharedPrefManager.NAMA, nama);
                                    sharedPrefManager.simpanSPSring(SharedPrefManager.USERNAME, username);
                                    Toast.makeText(mContext, "Berhasil Mengubah",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(mContext, profilePengguna.class));
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            Log.i("debug", "onResponse: GA BERHASIL");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

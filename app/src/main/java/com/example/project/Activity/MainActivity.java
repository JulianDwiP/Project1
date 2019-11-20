package com.example.project.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private ProgressDialog loading;
    EditText emailText, passwordText;
    Button btnMasuk;
    TextView daftarText, lupaText;
    Context mContext;
    ApiInterface mApiInterface;
    SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        sharedPrefManager = new SharedPrefManager(this);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        init();
    }

    private void init() {
        emailText = findViewById(R.id.etEmail);
        passwordText = findViewById(R.id.etPassword);
        btnMasuk = findViewById(R.id.btnMasuk);
        daftarText = findViewById(R.id.tvDaftar);
        lupaText = findViewById(R.id.tvLupaPassword);
        daftarText.setPaintFlags(daftarText.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);

        lupaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this, LupaPasswordVerifiAcc.class);
                startActivity(intent);
            }
        });
        daftarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, daftar.class));
            }
        });
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordText.length()==0 || passwordText.length() <4 || passwordText.length()>14){
                    passwordText.setError("Field harus terisi 4 sampai 14 huruf");
                }if (emailText.length()==0|| !isValidEmail(emailText.getText().toString())){
                        emailText.setError("Email tidak valid");
                }else{
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu..", true, false);
                    reqLogin();
                }
            }
        });
    }
    private void reqLogin(){
        mApiInterface.loginRequest(
                emailText.getText().toString(),
                passwordText.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getString("error").equals("false")){
                            String id = jsonObject.getJSONObject("list").getString("id");
                            String nama = jsonObject.getJSONObject("list").getString("nama");
                            String username = jsonObject.getJSONObject("list").getString("username");
                            String email = jsonObject.getJSONObject("list").getString("email");
                            String password = jsonObject.getJSONObject("list").getString("en_password");
                            String image = jsonObject.getJSONObject("list").getString("image");
                            sharedPrefManager.simpanSPBoolean(SharedPrefManager.CEK_SESSION, true);
                            sharedPrefManager.simpanSPSring(SharedPrefManager.ID, id);
                            sharedPrefManager.simpanSPSring(SharedPrefManager.NAMA, nama);
                            sharedPrefManager.simpanSPSring(SharedPrefManager.USERNAME, username);
                            sharedPrefManager.simpanSPSring(SharedPrefManager.EMAIL, email);
                            sharedPrefManager.simpanSPSring(SharedPrefManager.PASSWORD,password);
                            sharedPrefManager.simpanSPSring(SharedPrefManager.IMAGE, image);
                            Toast.makeText(mContext, "Login Berhasil",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(mContext, Beranda.class));
                        }else{
                            loading.dismiss();
                            Toast.makeText(mContext, "Login Gagal, Email atau Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(mContext, "Gagal Login ",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static boolean isValidEmail(String email) {
        boolean validate;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern)) {
            validate = true;
        } else if (email.matches(emailPattern2)) {
            validate = true;
        } else {
            validate = false;
        }

        return validate;
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(MainActivity.this, Beranda.class);
        startActivity(intent);
    }
}
package com.example.project;

import android.app.ProgressDialog;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class daftar extends AppCompatActivity {
    EditText namaText;
    EditText emailText;
    EditText passwordText;
    EditText usernameText;
    Button daftarButton;
    Context mContext;
    ApiInterface mApiInterface;
    SharedPrefManager sharedPrefManager;
    private ProgressDialog loading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);
        mContext = this;
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);
        init();
}
    private void init() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.daf_toolbar);
        namaText = findViewById(R.id.etdNama);
        emailText = findViewById(R.id.etdEmail);
        passwordText = findViewById(R.id.etdPassword);
        usernameText = findViewById(R.id.etdUsername);
        daftarButton = findViewById(R.id.btndDaftar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordText.length()==0 || passwordText.length() <4 || passwordText.length()>14){
                    passwordText.setError("Field harus terisi 4 sampai 14 huruf");
                }if (emailText.length()==0|| !isValidEmail(emailText.getText().toString())){
                    emailText.setError("Email tidak valid");
                }if (namaText.length()==0){
                    namaText.setError("Field harus diisi");
                }if (usernameText.length()==0){
                    usernameText.setError("Field harus diisi");
                }else{
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu..", true, false);
                    reqRegister();
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void reqRegister(){
        mApiInterface.registerRequest(
                namaText.getText().toString(),
                usernameText.getText().toString(),
                emailText.getText().toString(),
                passwordText.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULT = new JSONObject(response.body().string());
                                if (jsonRESULT.getString("error").equals("false")){
                                    String nama = jsonRESULT.getJSONObject("user").getString("nama");
                                    String email = jsonRESULT.getJSONObject("user").getString("email");
                                    String username = jsonRESULT.getJSONObject("user").getString("username");
                                    String password = jsonRESULT.getJSONObject("user").getString("password");
                                    sharedPrefManager.simpanSPSring(SharedPrefManager.NAMA, nama);
                                    sharedPrefManager.simpanSPSring(SharedPrefManager.EMAIL, email);
                                    sharedPrefManager.simpanSPSring(SharedPrefManager.USERNAME, username);
                                    sharedPrefManager.simpanSPSring(SharedPrefManager.PASSWORD, password);
                                    Toast.makeText(mContext, "Berhasil Daftar",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(mContext, MainActivity.class));
                                }else {
                                    loading.dismiss();
                                    String error_message = jsonRESULT.getString("error_msg");
                                    Toast.makeText(mContext, error_message,Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        } else{
                            Log.i("debug", "onResponse: GA BERHASIL");
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
//        mApiInterface.registerRequest(
//                namaText.getText().toString(),
//                usernameText.getText().toString(),
//                emailText.getText().toString(),
//                passwordText.getText().toString()).enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                if (response.isSuccessful()){
//                    Toast.makeText(mContext, "Berhasil Daftar",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(mContext, MainActivity.class));
//                }
//            }
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
//            }
//        });
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
}


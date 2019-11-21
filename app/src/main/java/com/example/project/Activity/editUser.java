package com.example.project.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project.Activity.profilePengguna;
import com.example.project.Api.ApiClient;
import com.example.project.Api.ApiInterface;
import com.example.project.R;
import com.example.project.SharedPref.SharedPrefManager;
import com.example.project.utils.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editUser extends AppCompatActivity implements View.OnClickListener {
    public static final String UPLOAD_URL = "http://192.168.43.236/perpus_db/Upload.php";
    public static final String UPLOAD_KEY = "image";

    private int PICK_IMAGE_REQUEST = 1;
    EditText edtUser_nama, edtUser_username;
    Button simpan;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;
    Context mContext;
    Toolbar toolbarEdtUser;
    ImageView edt_prof_poto, edit_prof_poto_plus;

    private Bitmap bitmap;

    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        mApiInterface = ApiClient.getClient(ApiClient.BASE_URL).create(ApiInterface.class);
        mContext = this;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedPrefManager = new SharedPrefManager(this);
        init();
    }

    private void init() {
        toolbarEdtUser = findViewById(R.id.toolbarEdtUser);
        setSupportActionBar(toolbarEdtUser);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Pengguna");
        edit_prof_poto_plus = findViewById(R.id.edit_prof_poto_plus);
        edtUser_nama = findViewById(R.id.edtUser_nama);
        edtUser_username = findViewById(R.id.edtUser_Username);
        simpan = findViewById(R.id.btnUser_simpan);
        edt_prof_poto = findViewById(R.id.edit_prof_poto);

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
            edt_prof_poto.setImageBitmap(bmp);
        }else{
            edt_prof_poto.setImageResource(R.drawable.images);
        }


        edt_prof_poto.setOnClickListener(this);
        toolbarEdtUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(editUser.this, profilePengguna.class));
            }
        });
        simpan.setOnClickListener(this);
    }

    private void reqUpdate() {
        mApiInterface.updateRequest(edtUser_nama.getText().toString(), edtUser_username.getText().toString(),
                sharedPrefManager.getSPEmail()).enqueue(new Callback<ResponseBody>() {
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
                            sharedPrefManager.simpanSPSring(SharedPrefManager.IMAGE,
                                    "http://192.168.43.236/perpus_db/uploads/"+sharedPrefManager.getId()+".png");
                            Toast.makeText(mContext, "Berhasil Merubah",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(editUser.this, profilePengguna.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(mContext, "Gagal Merubah",Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Cek Koneksi Internet Anda",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Photo"), PICK_IMAGE_REQUEST);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(editUser.this, "Mengubah...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                HashMap<String,String> data = new HashMap<>();
                data.put("id", sharedPrefManager.getId());
                data.put(UPLOAD_KEY, uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                edt_prof_poto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if( v == edt_prof_poto){
            showFileChooser();
        }
        if ( v == toolbarEdtUser){
            Intent iProfil = new Intent(editUser.this, profilePengguna.class );
            editUser.this.startActivity(iProfil);
        }
        if (v== simpan){
            if (edtUser_nama.length()==0){
                edtUser_nama.setError("Field harus diisi");
            }if (edtUser_username.length()==0){
                edtUser_username.setError("Field harus diisi");
            }else{
                if (bitmap != null){
                    uploadImage();
                    reqUpdate();
                    }else{
                    reqUpdate();
                }
            }
        }
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(editUser.this, profilePengguna.class);
        startActivity(intent);
    }
}

package com.example.project.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class LupaPassword extends AppCompatActivity {
    private static final String TAG = "KirimEmail";
    private static final int REQUEST_SIGNUP = 0;
    EditText emailText;
    Button kirimBtn;
    Button kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        emailText = findViewById(R.id.etEmail);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LupaPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
        kirimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirim();
            }
        });
    }
    private void kirim() {
        Log.d(TAG, "Kirim");
        if (!validate()) {
            onLoginFailed();
            return;
        }
        kirimBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LupaPassword.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Mengirim Permintaan...");
        progressDialog.show();

        String email = emailText.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Gagal Mengirim", Toast.LENGTH_LONG).show();
        kirimBtn.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Masukan Email yg Valid");
            valid = false;
        } else {
            emailText.setError(null);
        }
        return valid;
    }
    private void onLoginSuccess() {
        kirimBtn.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "Pesan Berhasil Terkirim!!", Toast.LENGTH_LONG).show();

    }
}

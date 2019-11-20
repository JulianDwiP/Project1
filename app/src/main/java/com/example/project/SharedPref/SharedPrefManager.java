package com.example.project.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String PERPUS_APP = "PerpusApp";
    public static final String NAMA = "nama";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String ID = "id";
    public static final String PASSWORD = "password";
    public static final String IMAGE = "image";



    public static final String CEK_SESSION = "CekSession";
    public static final String CEK_GAMBAR = "CekGambar";
    public static final String CEK_BACA = "CekBaca";

    SharedPreferences sp;
    SharedPreferences.Editor loginEditor;

    public void deleteSting(String keySP, String value){
        loginEditor.putString("", "");
        loginEditor.commit();
    }

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(PERPUS_APP, context.MODE_PRIVATE);
        loginEditor = sp.edit();
    }

    public void simpanSPSring(String keySP, String value){
        loginEditor.putString(keySP, value);
        loginEditor.commit();
    }

    public void simpanSPBoolean(String keySP, boolean value){
        loginEditor.putBoolean(keySP, value);
        loginEditor.commit();
    }
    public String getSPNama(){
        return sp.getString(NAMA, "");
    }

    public String getId(){
        return sp.getString(ID, "");
    }

    public String getSPEmail(){
        return sp.getString(EMAIL, "");
    }
    public String getSPImage(){
        return sp.getString(IMAGE, "");
    }
    public String getUsername(){
        return sp.getString(USERNAME, "");
    }
    public String getPassword(){
        return  sp.getString(PASSWORD, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(CEK_SESSION, false);
    }

    public Boolean AdaGambarApaNgga(){
        return sp.getBoolean(CEK_GAMBAR, false);
    }

    public Boolean udahBacaBlum(){
        return sp.getBoolean(CEK_BACA, false);
    }
}

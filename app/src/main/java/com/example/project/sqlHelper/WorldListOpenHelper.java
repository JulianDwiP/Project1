package com.example.project.sqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.project.R;
import com.example.project.Model.model_DBbuku;

public class WorldListOpenHelper extends SQLiteOpenHelper {
    private static  final  String TAG = WorldListOpenHelper.class.getSimpleName();

    private static final  int versiDB = 1;
    private static final String TABEL_BUKU = "buku_entries";
    private static final  String namaDB = "dbbuku";

    public static final String key_judul = "_judul";
    public static final String key_penulis = "_penulis";
    public static final String key_foto_buku = "_fotoBuku";
    public static final String key_sinopsis = "_sinopsis";
    public static final  String key_idBuku = "_idBuku";

    private static final String[] column = {
            key_judul, key_foto_buku, key_penulis,key_idBuku, key_sinopsis
    };

    private static final String buatTabel =
            "CREATE TABLE "+ TABEL_BUKU + " ("
            + key_idBuku + " INTEGER PRIMARY KEY , "
            + key_sinopsis + " TEXT, "
            + key_penulis + " TEXT, "
            + key_foto_buku + " INTEGER, "
            + key_judul + " TEXT );";

    private SQLiteDatabase bacaDB;
    private SQLiteDatabase nulisDB;

    public WorldListOpenHelper(@Nullable Context context) {
        super(context, namaDB, null, versiDB);
        Log.d(TAG, "Construct WorldListOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(buatTabel);
        isiData(db);
    }

    private void isiData(SQLiteDatabase db) {
        String[] penulis = {
                "RA Kartini",
                "Andrea Hirata"
        };
        String[] sinopsis = {
                "hahdshjhjkhuiadfjhuihuihuhfdu",
                "jhdhadkjahiajjkafjd"
        };
        String[] judul = {
                "Habis Gelap Terbitlah Terang",
                "Laskar Pelangi"
        };
        int[] fotoBuku = {
                R.drawable.habis_gelap_terbitlah_terang,
                R.drawable.laskar_pelangi
        };

        ContentValues values = new ContentValues();
        for (int i = 0; i<judul.length; i++){
            values.put(key_foto_buku, fotoBuku[i]);
            values.put(key_judul, judul[i]);
            values.put(key_sinopsis, sinopsis[i]);
            values.put(key_penulis, penulis[i]);
            db.insert(TABEL_BUKU, null, values);

            Log.d(TAG, "DATA MASUK = " + judul[i]);
            }
        }

        public model_DBbuku query (int position){
        String query = "SELECT * FROM "
                + TABEL_BUKU
                + " ORDER BY "
                + key_idBuku +
                " ASC " + "LIMIT " + position + ",1";
            Cursor cursor = null;
            model_DBbuku entry = new model_DBbuku();

            try {
                if (bacaDB == null){bacaDB = getReadableDatabase();}
                cursor = bacaDB.rawQuery(query, null);
                cursor.moveToFirst();
                entry.setmId(cursor.getInt(cursor.getColumnIndex(key_idBuku)));
                entry.setFoto(cursor.getInt(cursor.getColumnIndex(key_foto_buku)));
                entry.setJudul(cursor.getString(cursor.getColumnIndex(key_judul)));
                entry.setPenulis(cursor.getString(cursor.getColumnIndex(key_penulis)));
                entry.setSinopsis(cursor.getString(cursor.getColumnIndex(key_sinopsis)));
            } catch (Exception e){
                Log.d(TAG, "QUERY EXEPTION!" + e.getMessage());
            }finally {
                cursor.close();
                return entry;
            }
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versiLama, int versiBaru) {
        Log.w(WorldListOpenHelper.class.getSimpleName(), "Update data dari"
        + versiLama + " ke " + versiBaru + "dimana akan menghapus semua data lama");
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_BUKU);
        onCreate(db);
    }
    public long count(){
        if (bacaDB == null){
            bacaDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(bacaDB, TABEL_BUKU);
    }
}

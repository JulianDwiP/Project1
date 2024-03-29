package com.example.project.db_note;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.project.Model.Note;

@Database(entities = Note.class, version = 1)
public abstract class NotesDB extends RoomDatabase {
    public abstract NotesDao notesDao();

    public static final String DATABSE_NAME = "notesDb";
    private static NotesDB instance;

    public static NotesDB getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, NotesDB.class, DATABSE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}

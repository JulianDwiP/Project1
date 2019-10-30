package com.example.project.callbacks;


import com.example.project.Model.Note;


public interface NoteEventListener {

    void onNoteClick(Note note);

    void onNoteLongClick(Note note);
}

package com.test.viewpagerfun.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.test.viewpagerfun.model.entity.Note;

import java.util.List;


@Dao
public abstract class NoteDao {

    @Insert
    public abstract long insert(Note note);

    @Update
    public abstract void update(Note note);

    @Delete
    public abstract void delete(Note note);

    @Query("DELETE FROM note_table")
    public abstract void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY note_title DESC")
    public abstract LiveData<List<Note>> getAllNotes();

}
package com.test.viewpagerfun.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.test.viewpagerfun.model.repository.NoteRepository;
import com.test.viewpagerfun.model.entity.Note;

import java.util.List;

public class StartingScreenViewModel extends AndroidViewModel {

    private final NoteRepository repository;
    private final LiveData<List<Note>> notes;


    public StartingScreenViewModel(Application application) {
        super(application);
        repository = new NoteRepository(application);
        notes = repository.getAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public int getNotesCount() {
        return notes.getValue().size();
    }
}



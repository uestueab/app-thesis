package com.test.viewpagerfun;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.test.viewpagerfun.model.datasource.NoteRepository;
import com.test.viewpagerfun.model.entity.Note;

import java.util.List;

public class StartingScreenViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private final LiveData<List<Note>> notes;


    public StartingScreenViewModel(Application application){
        super(application);
        repository = new NoteRepository(application);
        notes = repository.getAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }
}



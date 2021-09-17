package com.test.viewpagerfun.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.test.viewpagerfun.model.datasource.NoteRepository;
import com.test.viewpagerfun.model.entity.Note;

import java.util.List;

public class StartingScreenViewModel extends AndroidViewModel {

    private final LiveData<List<Note>> notes;


    public StartingScreenViewModel(Application application){
        super(application);
        NoteRepository repository = new NoteRepository(application);
        notes = repository.getAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }
}



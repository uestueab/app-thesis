package com.test.viewpagerfun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.test.viewpagerfun.model.entity.Note;

import java.util.List;

public class SharedViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Application application;

    private List<Note> remainingNotes;

    public SharedViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    public SharedViewModelFactory(@NonNull Application application, List<Note> notes) {
        this.application = application;
        this.remainingNotes = notes;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SharedViewModel.class) {
            if(remainingNotes == null)
                return (T) new SharedViewModel(application);
            else
                return (T) new SharedViewModel(application,remainingNotes);

        }
        return null;
    }
}
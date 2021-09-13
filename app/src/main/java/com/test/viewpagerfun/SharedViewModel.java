package com.test.viewpagerfun;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.viewpagerfun.model.datasource.NoteRepository;
import com.test.viewpagerfun.model.entity.Note;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Note>> notesList = new MutableLiveData<>();
    private MutableLiveData<Integer>  position = new MutableLiveData<>(0);


    public void setNotesList(List<Note> notes){
        notesList.setValue(notes);
    }

    // Position: Geter and Setter
    public MutableLiveData<Integer> getPosition(){ return position; }
    public void setPosition(int x){ position.setValue(x);}

    public LiveData<Note> getNote() {
        Note noteAtCurrentPosition = notesList.getValue().get(position.getValue());
        return new MutableLiveData<>(noteAtCurrentPosition);
    }

    public boolean hasNextNote() {
        Integer currentPosition = position.getValue();
        Integer lastItemIndex = notesList.getValue().size()-1;

        if (currentPosition < lastItemIndex){
            // Increment our position. Makes it possible to load next note when called before getNote()
            getPosition().setValue(++currentPosition);
            return true;
        }else{
            return false;
        }
    }
}



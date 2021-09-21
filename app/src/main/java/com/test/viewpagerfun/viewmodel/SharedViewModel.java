package com.test.viewpagerfun.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.test.viewpagerfun.model.repository.NoteRepository;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.sm2.Review;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {

    private final LiveData<List<Note>> notes;
    private MutableLiveData<Integer> position = new MutableLiveData<>(0);
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>(new ArrayList<>());

    //constructor for loading from database
    public SharedViewModel(Application application) {
        super(application);
        NoteRepository repository = new NoteRepository(application);
        notes = repository.getAllNotes();
    }

    //constructor for storing remaining notes from a previous review
    public SharedViewModel(Application application, List<Note> previousNotes) {
        super(application);
        notes = new MutableLiveData<>(previousNotes);
    }

    // Notes List
    public LiveData<List<Note>> getNotes() { return notes; }

    //get those notes which have not been reviewed by the user
    public List<Note> getRemainingNotes() {
        return notes.getValue();
    }

    //  Note (Only available if getNotes is observed!)
    public Note getNote() { return notes.getValue().get(0); }

    /**
     * @return Works like an iterator. Returns true if there are notes left to show.
     * Returns false when reaching last livedata index.
     */
    public boolean hasNextNote() { return notes.getValue().size() > 0; }

    // Reviews
    public void setReview(Review review){ reviews.getValue().add(review); }

    // Review
    public LiveData<Review> getReview() {
        int lastIndex = reviews.getValue().size()-1;
        Review review = reviews.getValue().get(lastIndex);
        return new MutableLiveData<>(review);
    }

}



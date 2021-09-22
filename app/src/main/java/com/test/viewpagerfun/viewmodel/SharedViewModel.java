package com.test.viewpagerfun.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.test.viewpagerfun.model.repository.NoteRepository;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.sm2.Review;
import com.test.viewpagerfun.sm2.Session;

import java.util.List;

public class SharedViewModel extends AndroidViewModel {

    private final NoteRepository repository;

    private final LiveData<List<Note>> notes;
    private final MutableLiveData<Review> mostRecentReview = new MutableLiveData<>();
    private final MutableLiveData<Session> session = new MutableLiveData<>(new Session());

    //constructor for loading from database
    public SharedViewModel(Application application) {
        super(application);
        repository = new NoteRepository(application);
        notes = repository.getAllNotes();
    }

    //constructor for storing remaining notes from a previous review
    public SharedViewModel(Application application, List<Note> previousNotes) {
        super(application);
        repository = new NoteRepository(application);
        notes = new MutableLiveData<>(previousNotes);
    }

    // Notes List
    public LiveData<List<Note>> getNotes() { return notes; }
    public List<Note> getRemainingNotes() { return notes.getValue(); }

    //  Note (Only available if getNotes is observed!)
    public Note getNote() { return notes.getValue().get(0); }

    /**
     * @return Works like an iterator. Returns true if there are notes left to show.
     * Returns false when reaching last livedata index.
     */
    public boolean hasNextNote() { return notes.getValue().size() > 0; }

    // Review
    public Review getMostRecentReview() { return mostRecentReview.getValue(); }
    public void setMostRecentReview(Review review){ mostRecentReview.setValue(review); }


    public void applyReview(Review review){
        session.getValue().applyReview(review);
    }

}



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

<<<<<<< HEAD
    private final NoteRepository repository;
=======
>>>>>>> parent of 61f6e84 (answer handling works)

    private final LiveData<List<Note>> notes;
    private MutableLiveData<Integer> position = new MutableLiveData<>(0);
    private List<Note> remainingNotes;

    private MutableLiveData<List<Review>> reviews = new MutableLiveData<>(new ArrayList<>());

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

<<<<<<< HEAD
    // Notes List
    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void addNote(Note note){
        notes.getValue().add(note);
    }

    //get those notes which have not been reviewed by the user
    public List<Note> getRemainingNotes() {
        return notes.getValue();
    }

    //  Note (Only available if getNotes is observed!)
    public Note getNote() {
        return notes.getValue().get(0);
    }

=======
    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    // Getter and Setter for position
    public MutableLiveData<Integer> getPosition() {
        return position;
    }

    public void setPosition(int x) {
        position.setValue(x);
    }

    public LiveData<Note> getNote() {
        Note noteAtCurrentPosition = notes.getValue().get(position.getValue());
        return new MutableLiveData<>(noteAtCurrentPosition);
    }

    public Note getNoteAtPosition(int position) {
        return notes.getValue().get(position);
    }

>>>>>>> parent of 61f6e84 (answer handling works)
    /**
     * @return Works like an iterator. Returns true if there are notes left to show.
     * Returns false when reaching last livedata index.
     */
    public boolean hasNextNote() {
        return notes.getValue().size() > 0;
    }

    // Reviews
    public void setReview(Review review) {
        reviews.getValue().add(review);
    }

<<<<<<< HEAD

    // Review
    public LiveData<Review> getReview() {
        int lastIndex = reviews.getValue().size() > 0 ? reviews.getValue().size()-1 : 0;
        Review review = reviews.getValue().get(lastIndex);
        return new MutableLiveData<>(review);
    }

    // Database
    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
=======
    public void setReview(Review review){
        reviews.getValue().add(review);
>>>>>>> parent of 61f6e84 (answer handling works)
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    // Getter and Setter for position
    public MutableLiveData<List<Review>> getReviews() { return reviews; }
}



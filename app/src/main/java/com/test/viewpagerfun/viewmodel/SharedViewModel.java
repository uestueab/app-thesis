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
=======
    private final NoteRepository repository;

>>>>>>> parent of a30fc9b (pre revert commit -m)
    private final LiveData<List<Note>> notes;
    private final List<Note> remainingNotes;
    private MutableLiveData<Integer> position = new MutableLiveData<>(0);
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>(new ArrayList<>());

    //constructor for loading from database
    public SharedViewModel(Application application) {
        super(application);
        NoteRepository repository = new NoteRepository(application);
        notes = repository.getAllNotes();
        remainingNotes = new ArrayList<>();
    }

    //constructor for storing remaining notes from a previous review
    public SharedViewModel(Application application, List<Note> previousNotes) {
        super(application);
        notes = new MutableLiveData<>(previousNotes);
        remainingNotes = new ArrayList<>();
    }

<<<<<<< HEAD
    // Position
    public MutableLiveData<Integer> getPosition() {
        return position;
    }
    public void setPosition(int x) {
        position.setValue(x);
    }

    // Notes List
    public LiveData<List<Note>> getNotes() { return notes; }


    //  Note (Only available if getNotes is observed!)
    public LiveData<Note> getNoteLiveData() {
        Note noteAtCurrentPosition = notes.getValue().get(position.getValue());
        return new MutableLiveData<>(noteAtCurrentPosition);
    }

    public Note getNoteAtPosition() { return notes.getValue().get(position.getValue()); }
    public Note getNoteAtPosition(int position) { return notes.getValue().get(position); }

=======
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

>>>>>>> parent of a30fc9b (pre revert commit -m)
    /**
     * @return Works like an iterator. Returns true if there are notes left to show.
     * Returns false when reaching last livedata index.
     */
    public boolean hasNextNote() {
<<<<<<< HEAD
        Integer currentPosition = position.getValue();
        Integer lastItemIndex = notes.getValue().size() - 1;
=======
        return notes.getValue().size() > 0;
    }

    // Reviews
    public void setReview(Review review) {
        reviews.getValue().add(review);
    }

>>>>>>> parent of a30fc9b (pre revert commit -m)

        if (currentPosition < lastItemIndex) {
            // Increment our position. Makes it possible to load next note when called before getNote()
            setPosition(++currentPosition);
            return true;
        } else {
            return false;
        }
    }

    //get those notes which have not been reviewed by the user
    public List<Note> getRemainingNotes() {
        if (remainingNotes.size() == 0) {
            for (int i = position.getValue(); i < notes.getValue().size(); i++) {
                remainingNotes.add(getNoteAtPosition(i));
            }
        }
        return remainingNotes;
    }

<<<<<<< HEAD
    // Review
    public MutableLiveData<List<Review>> getReviews() { return reviews; }
    public void setReview(Review review){
        reviews.getValue().add(review);
=======
    public void update(Note note) {
        repository.update(note);
>>>>>>> parent of a30fc9b (pre revert commit -m)
    }

    public LiveData<Review> getReviewAtPosition() {
        Review reviewAtCurrentPosition = reviews.getValue().get(position.getValue());
        return new MutableLiveData<>(reviewAtCurrentPosition);
    }

}



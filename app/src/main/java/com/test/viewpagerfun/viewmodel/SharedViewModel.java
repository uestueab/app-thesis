package com.test.viewpagerfun.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.test.viewpagerfun.model.repository.FlashCardRepository;
import com.test.viewpagerfun.model.entity.FlashCard;
import com.test.viewpagerfun.sm2.Review;
import com.test.viewpagerfun.sm2.Session;

import java.util.List;

public class SharedViewModel extends AndroidViewModel {

    private final FlashCardRepository repository;

    private final LiveData<List<FlashCard>> flashCards;
    private int totalFlashCards;
    private final MutableLiveData<Review> mostRecentReview = new MutableLiveData<>();
    private final MutableLiveData<Session> session = new MutableLiveData<>(new Session());
    private int correctCount;

    //constructor for loading from database
    public SharedViewModel(Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        flashCards = repository.getFlashCardsDue();
    }

    //constructor for storing remaining flashCards from a previous review
    public SharedViewModel(Application application, List<FlashCard> previousFlashCards) {
        super(application);
        repository = new FlashCardRepository(application);
        flashCards = new MutableLiveData<>(previousFlashCards);
    }

    // FlashCards List
    public LiveData<List<FlashCard>> getFlashCards() { return flashCards; }
    public List<FlashCard> getRemainingFlashCards() { return flashCards.getValue(); }
    public int getTotalFlashCards() {
        if (totalFlashCards == 0)
            totalFlashCards =  flashCards.getValue().size();
        return totalFlashCards;
    }

    //  FlashCard (Only available if getFlashCards is observed!)
    public FlashCard getFlashCard() {
        if(flashCards.getValue().size() > 0)
            return flashCards.getValue().get(0);

        return null;
    }

    /**
     * @return Works like an iterator. Returns true if there are flashCards left to show.
     * Returns false when reaching last livedata index.
     */
    public boolean hasNextFlashCard() { return flashCards.getValue().size() > 0; }

    // Review
    public Review getMostRecentReview() { return mostRecentReview.getValue(); }
    public void setMostRecentReview(Review review){ mostRecentReview.setValue(review); }


    // Session
    public Session getSession(){ return session.getValue();}
    public void applyReview(Review review){ session.getValue().applyReview(review); }

    public int getCorrectCount(boolean onDetails) {
        if (mostRecentReview.getValue() == null){
            correctCount = 0;
        }else{
            if (!getMostRecentReview().hasFailed())
                //Increment counter only when focus is on input fragment
                if (onDetails)
                    correctCount++;
        }

        return correctCount;
    }

    // Database
    public void insert(FlashCard flashCard) {
        repository.insert(flashCard);
    }

    public void update(FlashCard flashCard) {
        repository.update(flashCard);
    }

    public void delete(FlashCard flashCard) {
        repository.delete(flashCard);
    }

}



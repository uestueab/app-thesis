package com.thesis.yatta.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thesis.yatta.model.entity.PastReview;
import com.thesis.yatta.model.repository.FlashCardRepository;
import com.thesis.yatta.model.entity.FlashCard;
import com.thesis.yatta.model.repository.PastReviewRepository;

import java.util.List;

public class StartingScreenViewModel extends AndroidViewModel {

    private final FlashCardRepository flashCardRepository;
    private final LiveData<List<FlashCard>> flashCards;

    private final PastReviewRepository pastReviewRepository;
    private final LiveData<List<PastReview>> pastReviews;

    public StartingScreenViewModel(Application application) {
        super(application);
        flashCardRepository = new FlashCardRepository(application);
        pastReviewRepository = new PastReviewRepository(application);

        flashCards = flashCardRepository.getFlashCardsDue();
        pastReviews = pastReviewRepository.getFiveLatestReviews();
    }

    public LiveData<List<FlashCard>> getFlashCards() { return flashCards; }
    public LiveData<List<PastReview>> getPastReviews() { return pastReviews; }

    public int getFlashCardsCount() {
        return flashCards.getValue().size();
    }

    // Database
    public void insert(PastReview pastReview) { pastReviewRepository.insert(pastReview); }
    public void update(PastReview pastReview) { pastReviewRepository.update(pastReview); }
    public void delete(PastReview pastReview) { pastReviewRepository.delete(pastReview);}

    public void updateReviewHasEnded(long dateEnded){
        pastReviewRepository.updateReviewHasEnded(dateEnded);
    }

}



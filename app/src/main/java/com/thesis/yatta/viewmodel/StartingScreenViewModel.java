package com.thesis.yatta.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.thesis.yatta.model.repository.FlashCardRepository;
import com.thesis.yatta.model.entity.FlashCard;

import java.util.List;

public class StartingScreenViewModel extends AndroidViewModel {

    private final FlashCardRepository repository;
    private final LiveData<List<FlashCard>> flashCards;


    public StartingScreenViewModel(Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        flashCards = repository.getFlashCardsDue();
    }

    public LiveData<List<FlashCard>> getFlashCards() {
        return flashCards;
    }

    public int getFlashCardsCount() {
        return flashCards.getValue().size();
    }
}



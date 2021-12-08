package com.thesis.yokatta.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.thesis.yokatta.model.entity.FlashCard;
import com.thesis.yokatta.model.repository.FlashCardRepository;

import java.util.List;


public class ManageFlashCardViewModel extends AndroidViewModel {
    private final FlashCardRepository repository;
    private final LiveData<List<FlashCard>> allFlashCards;

    public ManageFlashCardViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        allFlashCards = repository.getAllFlashCards();
    }

    public void insert(FlashCard flashCard) {
        repository.insert(flashCard);
    }

    public void update(FlashCard flashCard) {
        repository.update(flashCard);
    }

    public void delete(FlashCard flashCard) {
        repository.delete(flashCard);
    }

    public void deleteAllFlashCards() {
        repository.deleteAllFlashCards();
    }

    public LiveData<List<FlashCard>> getAllFlashCards() {
        return allFlashCards;
    }
}
package com.test.viewpagerfun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.test.viewpagerfun.model.entity.FlashCard;

import java.util.List;

public class SharedViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Application application;

    private List<FlashCard> remainingFlashCards;

    public SharedViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    public SharedViewModelFactory(@NonNull Application application, List<FlashCard> flashCards) {
        this.application = application;
        this.remainingFlashCards = flashCards;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SharedViewModel.class) {
            if(remainingFlashCards == null)
                return (T) new SharedViewModel(application);
            else
                return (T) new SharedViewModel(application,remainingFlashCards);

        }
        return null;
    }
}
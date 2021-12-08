package com.thesis.yokatta.model.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.thesis.yokatta.model.dao.FlashCardDao;
import com.thesis.yokatta.model.database.FlashCardDatabase;
import com.thesis.yokatta.model.entity.FlashCard;

import java.util.List;

public class FlashCardRepository {
    private FlashCardDao flashCardDao;
    private LiveData<List<FlashCard>> allFlashCards;
    private LiveData<List<FlashCard>> dueFlashCards;

    public FlashCardRepository(Application application) {
        FlashCardDatabase database = FlashCardDatabase.getInstance(application);
        flashCardDao = database.flashCardDao();
        allFlashCards = flashCardDao.getAllFlashCards();
        dueFlashCards = flashCardDao.getFlashCardsDue();
    }

    public void insert(FlashCard flashCard) {
        new InsertFlashCardAsyncTask(flashCardDao).execute(flashCard);
    }

    public void update(FlashCard flashCard) {
        new UpdateFlashCardAsyncTask(flashCardDao).execute(flashCard);
    }

    public void delete(FlashCard flashCard) {
        new DeleteFlashCardAsyncTask(flashCardDao).execute(flashCard);
    }

    public void deleteAllFlashCards() {
        new DeleteAllFlashCardsAsyncTask(flashCardDao).execute();
    }

    public LiveData<List<FlashCard>> getAllFlashCards() { return allFlashCards; }
    public LiveData<List<FlashCard>> getFlashCardsDue() { return dueFlashCards; }

    private static class InsertFlashCardAsyncTask extends AsyncTask<FlashCard, Void, Void> {
        private FlashCardDao flashCardDao;

        private InsertFlashCardAsyncTask(FlashCardDao flashCardDao) {
            this.flashCardDao = flashCardDao;
        }

        @Override
        protected Void doInBackground(FlashCard... flashCards) {
            flashCardDao.insert(flashCards[0]);
            return null;
        }
    }

    private static class UpdateFlashCardAsyncTask extends AsyncTask<FlashCard, Void, Void> {
        private FlashCardDao flashCardDao;

        private UpdateFlashCardAsyncTask(FlashCardDao flashCardDao) {
            this.flashCardDao = flashCardDao;
        }

        @Override
        protected Void doInBackground(FlashCard... flashCards) {
            flashCardDao.update(flashCards[0]);
            return null;
        }
    }

    private static class DeleteFlashCardAsyncTask extends AsyncTask<FlashCard, Void, Void> {
        private FlashCardDao flashCardDao;

        private DeleteFlashCardAsyncTask(FlashCardDao flashCardDao) {
            this.flashCardDao = flashCardDao;
        }

        @Override
        protected Void doInBackground(FlashCard... flashCards) {
            flashCardDao.delete(flashCards[0]);
            return null;
        }
    }

    private static class DeleteAllFlashCardsAsyncTask extends AsyncTask<Void, Void, Void> {
        private FlashCardDao flashCardDao;

        private DeleteAllFlashCardsAsyncTask(FlashCardDao flashCardDao) {
            this.flashCardDao = flashCardDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            flashCardDao.deleteAllFlashCards();
            return null;
        }
    }
}
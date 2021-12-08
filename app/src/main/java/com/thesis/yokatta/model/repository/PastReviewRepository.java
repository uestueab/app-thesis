package com.thesis.yokatta.model.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.thesis.yokatta.model.dao.PastReviewDao;
import com.thesis.yokatta.model.database.FlashCardDatabase;
import com.thesis.yokatta.model.entity.PastReview;

import java.util.List;

public class PastReviewRepository {
    private PastReviewDao pastReviewDao;
    private LiveData<List<PastReview>> pastReviews;

    public PastReviewRepository(Application application) {
        FlashCardDatabase database = FlashCardDatabase.getInstance(application);
        pastReviewDao = database.pastReviewDao();
        pastReviews = pastReviewDao.getFiveLatestReviews();
    }

    public void insert(PastReview pastReview) {
        new InsertPastReviewAsyncTask(pastReviewDao).execute(pastReview);
    }

    public void update(PastReview pastReview) {
        new UpdatePastReviewAsyncTask(pastReviewDao).execute(pastReview);
    }

    public void updateReviewHasEnded(long ended) {
        new CustomUpdatePastReviewAsyncTask(pastReviewDao).execute(ended);
    }

    public void delete(PastReview pastReview) {
        new DeletePastReviewAsyncTask(pastReviewDao).execute(pastReview);
    }

    public void deleteAllPastReviews() {
        new DeleteAllPastReviewsAsyncTask(pastReviewDao).execute();
    }

    public LiveData<List<PastReview>> getFiveLatestReviews() { return pastReviews; }

    private static class InsertPastReviewAsyncTask extends AsyncTask<PastReview, Void, Void> {
        private PastReviewDao pastReviewDao;

        private InsertPastReviewAsyncTask(PastReviewDao pastReviewDao) {
            this.pastReviewDao = pastReviewDao;
        }

        @Override
        protected Void doInBackground(PastReview... pastReviews) {
            pastReviewDao.insert(pastReviews[0]);
            return null;
        }
    }

    private static class UpdatePastReviewAsyncTask extends AsyncTask<PastReview, Void, Void> {
        private PastReviewDao pastReviewDao;

        private UpdatePastReviewAsyncTask(PastReviewDao pastReviewDao) {
            this.pastReviewDao = pastReviewDao;
        }

        @Override
        protected Void doInBackground(PastReview... pastReviews) {
            pastReviewDao.update(pastReviews[0]);
            return null;
        }
    }

    private static class CustomUpdatePastReviewAsyncTask extends AsyncTask<Long, Void, Void> {
        private PastReviewDao pastReviewDao;

        private CustomUpdatePastReviewAsyncTask(PastReviewDao pastReviewDao) {
            this.pastReviewDao = pastReviewDao;
        }

        @Override
        protected Void doInBackground(Long... ended) {
            pastReviewDao.updateReviewHasEnded(ended[0]);
            return null;
        }
    }

    private static class DeletePastReviewAsyncTask extends AsyncTask<PastReview, Void, Void> {
        private PastReviewDao pastReviewDao;

        private DeletePastReviewAsyncTask(PastReviewDao pastReviewDao) {
            this.pastReviewDao = pastReviewDao;
        }

        @Override
        protected Void doInBackground(PastReview... pastReviews) {
            pastReviewDao.delete(pastReviews[0]);
            return null;
        }
    }

    private static class DeleteAllPastReviewsAsyncTask extends AsyncTask<Void, Void, Void> {
        private PastReviewDao pastReviewDao;

        private DeleteAllPastReviewsAsyncTask(PastReviewDao pastReviewDao) {
            this.pastReviewDao = pastReviewDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            pastReviewDao.deleteAllPastReviews();
            return null;
        }
    }
}
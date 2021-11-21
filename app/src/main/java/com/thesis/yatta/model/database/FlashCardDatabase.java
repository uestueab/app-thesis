package com.thesis.yatta.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.thesis.yatta.model.dao.FlashCardDao;
import com.thesis.yatta.model.dao.PastReviewDao;
import com.thesis.yatta.model.dataconverter.DataConverter;
import com.thesis.yatta.model.entity.FlashCard;
import com.thesis.yatta.model.entity.PastReview;
import com.thesis.yatta.toolbox.TimeProvider;

import java.util.ArrayList;
import java.util.Arrays;


@Database(entities = {FlashCard.class, PastReview.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class FlashCardDatabase extends RoomDatabase {

    private static FlashCardDatabase instance;

    public abstract FlashCardDao flashCardDao();
    public abstract PastReviewDao pastReviewDao();

    public static synchronized FlashCardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FlashCardDatabase.class, "flashCard_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private FlashCardDao flashCardDao;
        private PastReviewDao pastReviewDao;

        private PopulateDbAsyncTask(FlashCardDatabase db) {
            flashCardDao = db.flashCardDao();
            pastReviewDao = db.pastReviewDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            flashCardDao.insert(FlashCard.builder()
                    .prompt("light").mnemonic("Etwas ist nicht gerade schwer, sondern...").meaning("licht").synonyms( new ArrayList<String>(Arrays.asList("hell","leicht"))).build());
            flashCardDao.insert(FlashCard.builder().prompt("church").mnemonic("Ein Gotteshaus").meaning("kirche").build());
            flashCardDao.insert(FlashCard.builder().prompt("plant").mnemonic("Denke an Flora, Botanik, Vegetation...").meaning("pflanze").build());
            flashCardDao.insert(FlashCard.builder().prompt("water").meaning("wasser").build());

            pastReviewDao.insert(PastReview.builder().ended(TimeProvider.now()).itemCount(23).build());
            pastReviewDao.insert(PastReview.builder().ended(TimeProvider.now()).itemCount(5).build());
            pastReviewDao.insert(PastReview.builder().ended(TimeProvider.now()).itemCount(10).build());
            pastReviewDao.insert(PastReview.builder().ended(TimeProvider.now()).itemCount(40).build());
            pastReviewDao.insert(PastReview.builder().ended(TimeProvider.now()).itemCount(16).build());

            return null;
        }
    }
}
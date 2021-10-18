package com.test.viewpagerfun.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.test.viewpagerfun.model.dao.FlashCardDao;
import com.test.viewpagerfun.model.dataconverter.DataConverter;
import com.test.viewpagerfun.model.entity.FlashCard;

import java.util.ArrayList;


@Database(entities = {FlashCard.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class FlashCardDatabase extends RoomDatabase {

    private static FlashCardDatabase instance;

    public abstract FlashCardDao flashCardDao();

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

        private PopulateDbAsyncTask(FlashCardDatabase db) {
            flashCardDao = db.flashCardDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            flashCardDao.insert(FlashCard.builder()
                    .prompt("light").meaning("licht").synonyms( new ArrayList<String>() { { add("hell"); } } ).build());
            flashCardDao.insert(FlashCard.builder().prompt("church").meaning("kirche").build());
            flashCardDao.insert(FlashCard.builder().prompt("plant").meaning("pflanze").build());
            flashCardDao.insert(FlashCard.builder().prompt("water").meaning("wasser").build());
//            flashCardDao.insertFlashCardWithMetaData(new FlashCard("FlashCard", "with status", 10),
//                    new MetaData("now"));
            return null;
        }
    }
}
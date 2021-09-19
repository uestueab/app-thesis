package com.test.viewpagerfun.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.test.viewpagerfun.model.dao.NoteDao;
import com.test.viewpagerfun.model.dataconverter.DataConverter;
import com.test.viewpagerfun.model.entity.Note;

import java.util.ArrayList;


@Database(entities = {Note.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
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
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(Note.builder()
                    .prompt("light").meaning("licht").synonyms( new ArrayList<String>() { { add("hell"); } } ).build());
            noteDao.insert(Note.builder().prompt("church").meaning("kirche").build());
            noteDao.insert(Note.builder().prompt("plant").meaning("pflanze").build());

//            noteDao.insertNoteWithMetaData(new Note("Note", "with status", 10),
//                    new MetaData("now"));
            return null;
        }
    }
}
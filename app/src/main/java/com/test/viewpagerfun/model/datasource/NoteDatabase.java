package com.test.viewpagerfun.model.datasource;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.test.viewpagerfun.model.dao.NoteDao;
import com.test.viewpagerfun.model.entity.Note;


@Database(entities = {Note.class}, version = 1)
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
            noteDao.insert(Note.builder().title("debug").description("Test").priority(10).build());
            noteDao.insert(Note.builder().title("coca cola").description("Test").priority(10).build());
            noteDao.insert(Note.builder().title("kirche").description("Test").priority(10).build());
            noteDao.insert(Note.builder().title("licht").description("Test").priority(10).build());
            noteDao.insert(Note.builder().title("oxidation").description("Test").priority(10).build());

//            noteDao.insertNoteWithMetaData(new Note("Note", "with status", 10),
//                    new MetaData("now"));
            return null;
        }
    }
}
package com.thesis.yatta.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thesis.yatta.model.entity.FlashCard;

import java.util.List;


@Dao
public abstract class FlashCardDao {

    @Insert
    public abstract long insert(FlashCard flashCard);

    @Update
    public abstract void update(FlashCard flashCard);

    @Delete
    public abstract void delete(FlashCard flashCard);

    @Query("DELETE FROM flashCard_table")
    public abstract void deleteAllFlashCards();

    @Query("SELECT * FROM flashCard_table ORDER BY flashCard_title DESC")
    public abstract LiveData<List<FlashCard>> getAllFlashCards();

    @Query("SELECT * FROM flashCard_table where DATE(dueDate) <= DATE('now') ORDER BY flashCard_title DESC")
    public abstract LiveData<List<FlashCard>> getFlashCardsDue();

}
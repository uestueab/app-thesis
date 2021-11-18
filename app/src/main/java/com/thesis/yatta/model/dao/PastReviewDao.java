package com.thesis.yatta.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thesis.yatta.model.entity.FlashCard;
import com.thesis.yatta.model.entity.PastReview;

import java.util.List;


@Dao
public abstract class PastReviewDao {

    @Insert
    public abstract long insert(PastReview pastReview);

    @Update
    public abstract void update(PastReview pastReview);

    @Delete
    public abstract void delete(PastReview pastReview);

    @Query("DELETE FROM pastReview_table")
    public abstract void deleteAllPastReviews();

    //get the five latest reviews
    @Query("SELECT * FROM pastReview_table ORDER BY ended DESC LIMIT 5")
    public abstract LiveData<List<PastReview>> getFiveLatestReviews();
}
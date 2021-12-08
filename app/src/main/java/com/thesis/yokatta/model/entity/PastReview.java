package com.thesis.yokatta.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.thesis.yokatta.toolbox.TimeProvider;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "pastReview_table")
public class PastReview implements Cloneable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private long pastReviewId;

    @Builder.Default
    private long started = TimeProvider.now();  //the date when a review has started
    private long ended;                         //gets set once review is finished
    private int itemCount;                      //items learning during session

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
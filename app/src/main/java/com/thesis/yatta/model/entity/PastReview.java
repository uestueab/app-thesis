package com.thesis.yatta.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.thesis.yatta.toolbox.TimeProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private long ended = TimeProvider.now();    //the date when a review has ended
    private int itemCount;                      //items learning during session

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
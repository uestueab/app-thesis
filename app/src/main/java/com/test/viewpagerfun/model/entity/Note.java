package com.test.viewpagerfun.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import com.test.viewpagerfun.toolbox.TimeProvider;

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
@Entity(tableName = "note_table")
public class Note implements Cloneable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private long noteId;
    private String title;
    private String description;
    private int priority;

    private int consecutiveCorrectCount;

    @Builder.Default
    private long lastReviewedDate = TimeProvider.now();
    private long dueDate;
    private float interval;

    @Builder.Default
    private float easinessFactor = 2.5f;


    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
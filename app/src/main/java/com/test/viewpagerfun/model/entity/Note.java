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
@Entity(tableName = "note_table")
public class Note implements Cloneable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private long noteId;

    @ColumnInfo(name = "note_title")
    private String prompt;              // represents the question. the input the user wants to learn
    private String meaning;             // the meaning of the prompt in the language the user speaks.
    private List<String> synonyms;      // treated as valid answers
    private String mnemonic;            // represents a hint text, as specified by the user.


    /*
     * SM2-Algorithm based fields:
     */
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
package com.thesis.yokatta.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.thesis.yokatta.toolbox.TimeProvider;

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
@Entity(tableName = "flashCard_table")
public class FlashCard implements Cloneable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private long flashCardId;

    @ColumnInfo(name = "flashCard_title")
    private String prompt;              // represents the question. the input the user wants to learn
    private String meaning;             // the meaning of the prompt in the language the user speaks.
    @Builder.Default
    private List<String> synonyms = new ArrayList<>();      // treated as valid answers
    private String mnemonic;            // represents a hint text, as specified by the user.
    private String pronunciation;       // filepath of the recording.


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
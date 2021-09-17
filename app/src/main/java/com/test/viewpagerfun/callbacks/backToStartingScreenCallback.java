package com.test.viewpagerfun.callbacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import com.test.viewpagerfun.PrefManager;
import com.test.viewpagerfun.ReviewActivity;
import com.test.viewpagerfun.databinding.ActivityStartingScreenBinding;
import com.test.viewpagerfun.model.entity.Note;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class backToStartingScreenCallback implements ActivityResultCallback<ActivityResult> {

    private Context context;
    private ActivityStartingScreenBinding binding;

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {

            Intent intent = result.getData();
            Bundle bundle = intent.getBundleExtra(ReviewActivity.EXTRA_REMAINING_REVIEWS);
            List<Note> notes = (List<Note>) bundle.getSerializable("notes");

            //persist any remaining items to shared preferences
            if (notes.size() > 0) {
                new PrefManager<>(getContext()).setNotes("REMAINING_NOTES", notes);
            } else {
                //or clear the key-value pair
                new PrefManager<>(getContext()).remove("REMAINING_NOTES");
            }
            binding.tvReviewItemCount.setText("Review: " + notes.size());
        }
    }

}

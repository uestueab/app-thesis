package com.test.viewpagerfun.callbacks;

import android.app.Activity;
import android.content.Context;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import com.test.viewpagerfun.PrefManager;
import com.test.viewpagerfun.databinding.ActivityStartingScreenBinding;
import com.test.viewpagerfun.model.entity.Note;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static com.test.viewpagerfun.constants.ConstantsHolder.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * callback to be made whenever the the back-button is pressed, basically:
 * - get data from the intent of the review activity. These are the remaining review items.
 * - store those remaining items in shared preferences, as to avoid unnecessary database calls.
 */
public class backToStartingScreenCallback implements ActivityResultCallback<ActivityResult> {

    private Context context;
    private ActivityStartingScreenBinding binding;

    //unchecked casts can be ignored, since the bundle is guaranteed to return a List<Note>
    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {

            PrefManager.init(getContext());
            List<Note> notes = PrefManager.getNotes(PREFS_REMAINING_NOTES);

            // clear the key-value pair
            if (notes == null || notes.size() == 0) {
                PrefManager.remove(PREFS_REMAINING_NOTES);
            }
            binding.tvReviewItemCount.setText("Review: " + notes.size());
        }
    }

}

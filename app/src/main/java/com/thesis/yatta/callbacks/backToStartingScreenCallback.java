package com.thesis.yatta.callbacks;

import android.app.Activity;
import android.content.Context;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import com.thesis.yatta.PrefManager;
import com.thesis.yatta.databinding.ActivityStartingScreenBinding;
import com.thesis.yatta.model.entity.FlashCard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static com.thesis.yatta.constants.ConstantsHolder.*;

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

    //unchecked casts can be ignored, since the bundle is guaranteed to return a List<FlashCard>
    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {

            PrefManager.init(getContext());
            List<FlashCard> flashCards = PrefManager.getFlashCards(PREFS_REMAINING_FLASH_CARDS);

            // clear the key-value pair
            if (flashCards == null || flashCards.size() == 0) {
                PrefManager.remove(PREFS_REMAINING_FLASH_CARDS);
            }
            binding.tvReviewItemCount.setText("Review: " + flashCards.size());
        }
    }

}

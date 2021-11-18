package com.thesis.yatta.listeners.onClick;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.thesis.yatta.PrefManager;
import com.thesis.yatta.ReviewActivity;
import com.thesis.yatta.StartingScreenActivity;
import com.thesis.yatta.databinding.ReviewDetailedResultFragmentBinding;
import com.thesis.yatta.viewmodel.SharedViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static com.thesis.yatta.constants.ConstantsHolder.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
/*
 *  Determines what happens when a review item lapsed. Either:
 *  - show the next item
 *  - end the review when no items are left.
 */
public class NextReviewItemListener implements View.OnClickListener{

    private SharedViewModel model;
    private Activity activity;
    private ReviewDetailedResultFragmentBinding binding;

    @Override
    public void onClick(View v) {
        //let move to the review user input fragment if flashCards are left
        if (model.hasNextFlashCard()) {

            ((ReviewActivity) activity).previous_fragment();
            //remove flicker
            binding.tvQuestion.setText("");
        } else {
            PrefManager.init(getActivity());
            PrefManager.remove(PREFS_REMAINING_FLASH_CARDS);

            // all items passed, quit by moving to another activity
            Intent intent = new Intent(getActivity(), StartingScreenActivity.class);
            activity.startActivity(intent);
        }
    }

}

package com.test.viewpagerfun.listeners.onClick;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.test.viewpagerfun.PrefManager;
import com.test.viewpagerfun.ReviewActivity;
import com.test.viewpagerfun.StartingScreenActivity;
import com.test.viewpagerfun.databinding.ReviewDetailedResultFragmentBinding;
import com.test.viewpagerfun.viewmodel.SharedViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        //let move to the review user input fragment if remaining notes exist
        if (model.hasNextNote()) {
            ((ReviewActivity) activity).previous_fragment();
            //remove flicker
            binding.tvQuestion.setText("");
        } else { // all items passed, quit by moving to another activity
            new PrefManager<>(getActivity()).remove("REMAINING_NOTES");

            Intent intent = new Intent(getActivity(), StartingScreenActivity.class);
            activity.startActivity(intent);
        }
    }

}

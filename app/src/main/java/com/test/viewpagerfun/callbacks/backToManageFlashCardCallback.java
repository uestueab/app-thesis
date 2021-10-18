package com.test.viewpagerfun.callbacks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import com.test.viewpagerfun.model.entity.FlashCard;
import com.test.viewpagerfun.viewmodel.ManageFlashCardViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class backToManageFlashCardCallback implements ActivityResultCallback<ActivityResult> {

    private Context context;
    private ManageFlashCardViewModel viewModel;


    @Override
    public void onActivityResult(ActivityResult result) {
        int requestCode;
        if (result.getData() == null)
            requestCode = 0;
        else
            requestCode = result.getData().getIntExtra(REQUEST_CODE, 0);

        if (requestCode == ADD_NOTE_REQUEST && result.getResultCode() == Activity.RESULT_OK) {
            Bundle bundle = result.getData().getBundleExtra(EXTRA_ADD_NOTE);
            FlashCard flashCard = (FlashCard) bundle.getSerializable(BUNDLE_ADD_NOTE);

            viewModel.insert(flashCard);
            Toast.makeText(getContext(), "FlashCard saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && result.getResultCode() == Activity.RESULT_OK) {
            Bundle bundle = result.getData().getBundleExtra(EXTRA_EDIT_NOTE);
            FlashCard flashCard = (FlashCard) bundle.getSerializable(BUNDLE_ADD_NOTE);

            viewModel.update(flashCard);
            Toast.makeText(getContext(), "FlashCard updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "FlashCard not saved", Toast.LENGTH_SHORT).show();
        }
    }
}

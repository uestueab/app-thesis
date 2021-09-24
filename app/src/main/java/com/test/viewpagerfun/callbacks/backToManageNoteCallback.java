package com.test.viewpagerfun.callbacks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.viewmodel.ManageNoteViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class backToManageNoteCallback implements ActivityResultCallback<ActivityResult> {

    private Context context;
    private ManageNoteViewModel viewModel;


    @Override
    public void onActivityResult(ActivityResult result) {
        int requestCode;
        if (result.getData() == null)
            requestCode = 0;
        else
            requestCode = result.getData().getIntExtra(REQUEST_CODE, 0);

        if (requestCode == ADD_NOTE_REQUEST && result.getResultCode() == Activity.RESULT_OK) {
            Bundle bundle = result.getData().getBundleExtra(EXTRA_ADD_NOTE);
            Note note = (Note) bundle.getSerializable(BUNDLE_ADD_NOTE);

            viewModel.insert(note);
            Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && result.getResultCode() == Activity.RESULT_OK) {
            Bundle bundle = result.getData().getBundleExtra(EXTRA_EDIT_NOTE);
            Note note = (Note) bundle.getSerializable(BUNDLE_ADD_NOTE);

            viewModel.update(note);
            Toast.makeText(getContext(), "Note updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}

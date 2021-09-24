package com.test.viewpagerfun.callbacks;

import android.app.Activity;
import android.content.Context;
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
            String title = result.getData().getStringExtra(EXTRA_TITLE);

            Note note = Note.builder().prompt(title).build();
            viewModel.insert(note);
            Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && result.getResultCode() == Activity.RESULT_OK) {
            long id = result.getData().getLongExtra(EXTRA_ID, -1);
            //invalid id
            if (id == -1) {
                Toast.makeText(getContext(), "note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = result.getData().getStringExtra(EXTRA_TITLE);

            Note note = Note.builder().noteId(id).prompt(title).build();
            viewModel.update(note);

            Toast.makeText(getContext(), "Note updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}

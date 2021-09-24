package com.test.viewpagerfun.callbacks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.test.viewpagerfun.AddEditNoteActivity;
import com.test.viewpagerfun.NoteAdapter;
import com.test.viewpagerfun.R;
import com.test.viewpagerfun.databinding.ActivityManageNoteBinding;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.viewmodel.ManageNoteViewModel;

import java.io.Serializable;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class SwipeRecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private NoteAdapter adapter;
    private ManageNoteViewModel noteViewModel;
    private ActivityManageNoteBinding binding;
    private Activity activity;
    private ActivityResultLauncher<Intent> addEditNoteResultLauncher;

    public SwipeRecyclerViewTouchHelper(int dragDirs, int swipeDirs,
                                        NoteAdapter adapter, ManageNoteViewModel noteViewModel, ActivityManageNoteBinding binding,
                                        Activity activity, ActivityResultLauncher<Intent> addEditNoteResultLauncher) {
        super(dragDirs, swipeDirs);
        this.adapter = adapter;
        this.noteViewModel = noteViewModel;
        this.binding = binding;
        this.activity = activity;
        this.addEditNoteResultLauncher = addEditNoteResultLauncher;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        switch (direction) {
            case ItemTouchHelper.LEFT:
                String deletedNoteTitle = adapter.getNoteAt(position).getPrompt();
                Note cloneNote = null;
                try {
                    cloneNote = (Note) adapter.getNoteAt(position).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                noteViewModel.delete(adapter.getNoteAt(position));
                Note finalCloneNote = cloneNote;
                Snackbar.make(binding.recyclerView, "Deleted: " + deletedNoteTitle, BaseTransientBottomBar.LENGTH_LONG)
                        .setAction("Restore", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                noteViewModel.insert(finalCloneNote);
                            }
                        }).show();
                        Toast.makeText(activity, "Note deleted: "+ deletedNoteTitle, Toast.LENGTH_SHORT).show();
                break;
            case ItemTouchHelper.RIGHT:
                editNote(adapter.getNoteAt(position));
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(this.activity, R.color.deleteNoteOnSwipe))
                .addSwipeLeftActionIcon(R.drawable.ic_delete)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(this.activity, R.color.editNoteOnSwipe))
                .addSwipeRightActionIcon(R.drawable.ic_edit)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void editNote(Note note) {
        Intent intent = new Intent(activity, AddEditNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_EDIT_NOTE, (Serializable) note);
        intent.putExtra(EXTRA_EDIT_NOTE,bundle);

        addEditNoteResultLauncher.launch(intent);
    }
}

package com.thesis.yatta.callbacks;

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
import com.thesis.yatta.AddEditFlashCardActivity;
import com.thesis.yatta.FlashCardAdapter;
import com.thesis.yatta.R;
import com.thesis.yatta.databinding.ActivityManageFlashcardBinding;
import com.thesis.yatta.model.entity.FlashCard;
import com.thesis.yatta.viewmodel.ManageFlashCardViewModel;

import java.io.Serializable;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.thesis.yatta.constants.ConstantsHolder.*;

public class SwipeRecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private FlashCardAdapter adapter;
    private ManageFlashCardViewModel flashCardViewModel;
    private ActivityManageFlashcardBinding binding;
    private Activity activity;
    private ActivityResultLauncher<Intent> addEditFlashCardResultLauncher;

    public SwipeRecyclerViewTouchHelper(int dragDirs, int swipeDirs,
                                        FlashCardAdapter adapter, ManageFlashCardViewModel flashCardViewModel, ActivityManageFlashcardBinding binding,
                                        Activity activity, ActivityResultLauncher<Intent> addEditFlashCardResultLauncher) {
        super(dragDirs, swipeDirs);
        this.adapter = adapter;
        this.flashCardViewModel = flashCardViewModel;
        this.binding = binding;
        this.activity = activity;
        this.addEditFlashCardResultLauncher = addEditFlashCardResultLauncher;
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
                String deletedFlashCardTitle = adapter.getFlashCardAt(position).getPrompt();
                FlashCard cloneFlashCard = null;
                try {
                    cloneFlashCard = (FlashCard) adapter.getFlashCardAt(position).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                flashCardViewModel.delete(adapter.getFlashCardAt(position));
                FlashCard finalCloneFlashCard = cloneFlashCard;
                Snackbar.make(binding.recyclerView, "Deleted: " + deletedFlashCardTitle, BaseTransientBottomBar.LENGTH_LONG)
                        .setAction("Restore", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                flashCardViewModel.insert(finalCloneFlashCard);
                            }
                        }).show();
                break;
            case ItemTouchHelper.RIGHT:
                editFlashCard(adapter.getFlashCardAt(position));
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(this.activity, R.color.deleteFlashCardOnSwipe))
                .addSwipeLeftActionIcon(R.drawable.ic_delete)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(this.activity, R.color.editFlashCardOnSwipe))
                .addSwipeRightActionIcon(R.drawable.ic_edit)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void editFlashCard(FlashCard flashCard) {
        Intent intent = new Intent(activity, AddEditFlashCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_CODE, EDIT_NOTE_REQUEST);
        bundle.putSerializable(BUNDLE_EDIT_NOTE, (Serializable) flashCard);
        intent.putExtra(EXTRA_EDIT_NOTE, bundle);

        addEditFlashCardResultLauncher.launch(intent);
    }
}

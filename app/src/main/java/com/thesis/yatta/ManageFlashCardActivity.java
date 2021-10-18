package com.thesis.yatta;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thesis.yatta.callbacks.SwipeRecyclerViewTouchHelper;
import com.thesis.yatta.callbacks.backToManageFlashCardCallback;
import com.thesis.yatta.databinding.ActivityManageFlashcardBinding;
import com.thesis.yatta.listeners.onClick.AddFlashCardListener;
import com.thesis.yatta.listeners.onQueryTextListener.SearchViewTextListener;
import com.thesis.yatta.model.entity.FlashCard;
import com.thesis.yatta.viewmodel.ManageFlashCardViewModel;

import java.io.Serializable;
import java.util.List;

import static com.thesis.yatta.constants.ConstantsHolder.*;
public class ManageFlashCardActivity extends BaseActivity {

    private ActivityManageFlashcardBinding binding;

    private ManageFlashCardViewModel flashCardViewModel;
    private ActivityResultLauncher<Intent> addEditFlashCardResultLauncher;

    public static FlashCardAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageFlashcardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);

        adapter = new FlashCardAdapter();
        binding.recyclerView.setAdapter(adapter);

        flashCardViewModel = new ViewModelProvider(this).get(ManageFlashCardViewModel.class);
        flashCardViewModel.getAllFlashCards().observe(this, new Observer<List<FlashCard>>() {
            @Override
            public void onChanged(@Nullable List<FlashCard> flashCards) {
                adapter.submitList(flashCards);
            }
        });

        //Decides what to do when returning to this Activity
        addEditFlashCardResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                backToManageFlashCardCallback.builder()
                        .context(this)
                        .viewModel(flashCardViewModel)
                        .build()
                );

        binding.buttonAddFlashCard.setOnClickListener(
                AddFlashCardListener.builder()
                        .currentActivity(this).targetActivity(AddEditFlashCardActivity.class)
                        .requestCode(ADD_NOTE_REQUEST)
                        .resultLauncher(addEditFlashCardResultLauncher)
                        .build()
        );

        //Make the RecyclerView react to swipes
        new ItemTouchHelper(new SwipeRecyclerViewTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                adapter,flashCardViewModel,binding,this,addEditFlashCardResultLauncher)
        ).attachToRecyclerView(binding.recyclerView);

        //Edit flashCard on click
        adapter.setOnItemClickListener(new FlashCardAdapter.OnItemclickListener() {
            @Override
            public void onItemClick(FlashCard flashCard) { editFlashCard(flashCard); }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.manage_flashcards_menu,menu);

        MenuItem actionSearch= menu.findItem( R.id.search_cards);
        final SearchView searchViewEditText = (SearchView) actionSearch.getActionView();
        searchViewEditText.setQueryHint("search flashCards...");
        searchViewEditText.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchViewEditText.setMaxWidth(Integer.MAX_VALUE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                FloatingActionButton buttonAddFlashCard = findViewById(R.id.button_add_flashCard);
                buttonAddFlashCard.setVisibility(View.INVISIBLE);
            }
        });

        searchViewEditText.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                FloatingActionButton buttonAddFlashCard = findViewById(R.id.button_add_flashCard);
                buttonAddFlashCard.setVisibility(View.VISIBLE);
                return false;
            }
        });

        // Whenever the text of the searchView changes
        searchViewEditText.setOnQueryTextListener(SearchViewTextListener.builder()
                .context(this)
                .adapter(adapter)
                .build()
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.delete_all_flashCards:
//                flashCardViewModel.deleteAllFlashCards();
                Toast.makeText(this, "[fake]: all flashCards deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editFlashCard(FlashCard flashCard){
        Intent intent = new Intent(ManageFlashCardActivity.this, AddEditFlashCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_CODE, EDIT_NOTE_REQUEST);
        bundle.putSerializable(BUNDLE_EDIT_NOTE, (Serializable) flashCard);
        intent.putExtra(EXTRA_EDIT_NOTE,bundle);

        addEditFlashCardResultLauncher.launch(intent);
    }
}
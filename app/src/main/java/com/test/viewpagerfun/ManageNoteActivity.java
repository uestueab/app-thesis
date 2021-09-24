package com.test.viewpagerfun;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.test.viewpagerfun.callbacks.SwipeRecyclerViewTouchHelper;
import com.test.viewpagerfun.callbacks.backToManageNoteCallback;
import com.test.viewpagerfun.databinding.ActivityManageNoteBinding;
import com.test.viewpagerfun.listeners.onClick.AddNoteListener;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.toolbox.TimeProvider;
import com.test.viewpagerfun.viewmodel.ManageNoteViewModel;

import java.time.LocalDateTime;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import static com.test.viewpagerfun.constants.ConstantsHolder.*;
public class ManageNoteActivity extends AppCompatActivity {

    private ActivityManageNoteBinding binding;

    private ManageNoteViewModel noteViewModel;
    public static NoteAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Overview total notes");


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);

        adapter = new NoteAdapter();
        binding.recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(ManageNoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        ActivityResultLauncher<Intent> addEditNoteResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                backToManageNoteCallback.builder()
                        .context(this)
                        .viewModel(noteViewModel)
                        .build()
                );

        binding.buttonAddNote.setOnClickListener(
                AddNoteListener.builder()
                        .currentActivity(this).targetActivity(AddEditNoteActivity.class)
                        .requestCode(ADD_NOTE_REQUEST)
                        .resultLauncher(addEditNoteResultLauncher)
                        .build()
        );

        //Make the RecyclerView swipeable
        new ItemTouchHelper(new SwipeRecyclerViewTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                adapter,noteViewModel,ManageNoteActivity.this)
        ).attachToRecyclerView(binding.recyclerView);

        //Edit note on click
        adapter.setOnItemClickListener(new NoteAdapter.OnItemclickListener() {
            @Override
            public void onItemClick(Note note) { editNote(note); }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.manage_notes_menu,menu);

        MenuItem actionSearch= menu.findItem( R.id.search_cards);
        final SearchView searchViewEditText = (SearchView) actionSearch.getActionView();
        searchViewEditText.setQueryHint("search notes...");
        searchViewEditText.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchViewEditText.setMaxWidth(Integer.MAX_VALUE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
                buttonAddNote.setVisibility(View.INVISIBLE);
            }
        });

        searchViewEditText.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
                buttonAddNote.setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchViewEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //when SearchView gets cleared.
                if(query.equals("emptyQuery")){
                    //makes sure notes exist in adapter field
                    if(adapter.getNoteCount() == 0){
                        return false;
                    }
                    List<Note> notesListFull = adapter.getNotes();
                    //refresh screen with the full list again.
                    adapter.submitList(notesListFull);
                    return false;
                }

                if (!adapter.filter(query))
                    Toast.makeText(ManageNoteActivity.this, "couldn't find note matching query", Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    this.onQueryTextSubmit("emptyQuery");
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "all notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editNote(Note note){
        Intent intent = new Intent(ManageNoteActivity.this, AddEditNoteActivity.class);

        intent.putExtra(EXTRA_ID, note.getNoteId());
        intent.putExtra(EXTRA_TITLE, note.getPrompt());
//        intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
//        intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
        startActivityForResult(intent,EDIT_NOTE_REQUEST);
    }
}
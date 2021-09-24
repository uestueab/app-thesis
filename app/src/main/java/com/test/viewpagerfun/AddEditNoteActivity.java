package com.test.viewpagerfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.test.viewpagerfun.databinding.ActivityAddNoteBinding;
import com.test.viewpagerfun.databinding.ActivityManageNoteBinding;
import com.test.viewpagerfun.model.entity.Note;

import java.io.Serializable;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class AddEditNoteActivity extends AppCompatActivity {

    private int requestCode;
    private ActivityAddNoteBinding binding;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_EDIT_NOTE)){
            setTitle("Edit Note");

            Bundle bundle = intent.getBundleExtra(EXTRA_EDIT_NOTE);
            note = (Note) bundle.getSerializable(BUNDLE_EDIT_NOTE);
            requestCode = bundle.getInt(REQUEST_CODE,0);

            binding.editTextTitle.setText(note.getPrompt());
            binding.editTextDescription.setText(note.getMeaning());
        }else{
            setTitle("Add Note");
            requestCode = ADD_NOTE_REQUEST;
        }
    }

    private void saveNote(){
        //get input of edittext fields
        String title = binding.editTextTitle.getText().toString();

        //restrict empty fields
        if(title.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }

        //set the new inputs the the note attributes
        if(requestCode == ADD_NOTE_REQUEST){
            note = Note.builder()
                    .prompt(title)
                    .build();
        }else if(requestCode == EDIT_NOTE_REQUEST){
            note.setPrompt(title);
        }

        String extraKey = requestCode == ADD_NOTE_REQUEST ? EXTRA_ADD_NOTE : EXTRA_EDIT_NOTE;

        //prepare intent and send note too
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_ADD_NOTE,(Serializable) note);
        intent.putExtra(extraKey,bundle);

//        long id = getIntent().getLongExtra(EXTRA_ID,-1);
//        if(id != -1){
//           intent.putExtra(EXTRA_ID,id);
//        }

        intent.putExtra(REQUEST_CODE, requestCode);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
     }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
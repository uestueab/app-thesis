package com.test.viewpagerfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.test.viewpagerfun.databinding.ActivityAddNoteBinding;
import com.test.viewpagerfun.model.entity.Note;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class AddEditNoteActivity extends BaseActivity {

    private int requestCode;
    private ActivityAddNoteBinding binding;
    private Note note;

    private final List<EditText> et_synonyoms = new ArrayList<EditText>();
    private List<String> synonyms = new ArrayList<String>();


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

        // the inserted edittext becomes the parent of all programmatically created edittext fields.
        et_synonyoms.add(binding.editTextSynonym);

        if(intent.hasExtra(EXTRA_EDIT_NOTE)){
            setTitle("Edit Note");

            Bundle bundle = intent.getBundleExtra(EXTRA_EDIT_NOTE);
            note = (Note) bundle.getSerializable(BUNDLE_EDIT_NOTE);
            requestCode = bundle.getInt(REQUEST_CODE,0);

            binding.editTextTitle.setText(note.getPrompt());
            binding.editTextMeaning.setText(note.getMeaning());

            for(String synonym: note.getSynonyms()){
                createSynonymFields(synonym);
            }

        }else{
            setTitle("Add Note");
            requestCode = ADD_NOTE_REQUEST;
        }


        binding.btnAddSynonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSynonymFields(null);
            }
        });

        binding.btnRemoveSynonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_synonyoms.size() == 2){
                    // there are two edittext fields left. remove one more..
                    binding.clRootLayout.removeView(et_synonyoms.get(et_synonyoms.size()-1));
                    et_synonyoms.remove(et_synonyoms.size()-1);
                    // and make the button disappear
                    binding.btnRemoveSynonym.setVisibility(View.INVISIBLE);
                    binding.tvRemoveSynonym.setVisibility(View.INVISIBLE);

                    return;
                }
                binding.clRootLayout.removeView(et_synonyoms.get(et_synonyoms.size()-1));
                et_synonyoms.remove(et_synonyoms.size()-1);
            }
        });
    }

    private void createSynonymFields(String synonym){
        ConstraintSet set = new ConstraintSet();

        EditText editText = new EditText(AddEditNoteActivity.this);
        editText.setId(View.generateViewId());
        if(synonym == null){
            editText.setHint("add synonym...");
            editText.setHintTextColor(binding.editTextSynonym.getHintTextColors());
        }else{
            editText.setText(synonym);
        }
        editText.setTextColor(binding.editTextSynonym.getTextColors());


        et_synonyoms.add(editText);

        binding.clRootLayout.addView(editText, et_synonyoms.size());

        set.clone(binding.clRootLayout);
        // connect start and end point of views, in this case top of child to top of parent.
        set.connect(editText.getId(), ConstraintSet.TOP, et_synonyoms.get(et_synonyoms.size()-2).getId(), ConstraintSet.BOTTOM);
        set.connect(editText.getId(), ConstraintSet.START, et_synonyoms.get(et_synonyoms.size()-2).getId(), ConstraintSet.START);
        // ... similarly add other constraints
        set.applyTo(binding.clRootLayout);

        //since we now have an additional field
        //show a button to remove the created view
        binding.btnRemoveSynonym.setVisibility(View.VISIBLE);
        binding.tvRemoveSynonym.setVisibility(View.VISIBLE);

    }

    private void saveSynonyms(){
        for(int i=0; i < et_synonyoms.size(); i++){
            synonyms.add(et_synonyoms.get(i).getText().toString());
        }
    }

    private void saveNote(){
        saveSynonyms();

        //get input of edittext fields
        String title = binding.editTextTitle.getText().toString();
        String meaning = binding.editTextMeaning.getText().toString();

        //restrict empty fields
        if(title.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }

        //set the new inputs the the note attributes
        if(requestCode == ADD_NOTE_REQUEST){
            note = Note.builder()
                    .prompt(title)
                    .meaning(meaning)
                    .synonyms(synonyms)
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
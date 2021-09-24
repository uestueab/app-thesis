package com.test.viewpagerfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.test.viewpagerfun.databinding.ActivityAddNoteBinding;
import com.test.viewpagerfun.databinding.ActivityManageNoteBinding;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class AddEditNoteActivity extends AppCompatActivity {

    private int requestCode;

    private ActivityAddNoteBinding binding;

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
        requestCode = intent.getIntExtra(REQUEST_CODE,0);

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        }else{
            setTitle("Add Note");
        }
    }

    private void saveNote(){
        String title = binding.editTextTitle.getText().toString();

        if(title.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE,title);

        long id = getIntent().getLongExtra(EXTRA_ID,-1);
        if(id != -1){
           data.putExtra(EXTRA_ID,id);
        }

        data.putExtra(REQUEST_CODE, requestCode);
        setResult(RESULT_OK, data);
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
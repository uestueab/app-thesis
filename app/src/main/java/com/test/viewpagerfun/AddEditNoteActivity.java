package com.test.viewpagerfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.test.viewpagerfun.constants.Permissions;
import com.test.viewpagerfun.databinding.ActivityAddNoteBinding;
import com.test.viewpagerfun.listeners.onClick.RemoveSynonymListener;
import com.test.viewpagerfun.model.entity.Note;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class AddEditNoteActivity extends BaseActivity {

    private static final String TAG = "AddEditNoteActivity";

    private int requestCode;
    private ActivityAddNoteBinding binding;
    private Note note;

    private final List<EditText> et_synonyms = new ArrayList<EditText>();
    private List<String> synonyms = new ArrayList<String>();

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private boolean recordingExists = false;
    private String temp_recordingName = "rec_" + UUID.randomUUID().toString() + ".ogg";
    private String notePronunciation;

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

        // ask for permission
        if (isMicrophonePresent())
            getMicrophonePermission();

        // the inserted edittext becomes the parent of all programmatically created edittext fields.
        et_synonyms.add(binding.editTextSynonym);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_EDIT_NOTE)) {
            setTitle("Edit Note");

            Bundle bundle = intent.getBundleExtra(EXTRA_EDIT_NOTE);
            //get the note to be edited
            note = (Note) bundle.getSerializable(BUNDLE_EDIT_NOTE);
            //get the request-code
            requestCode = bundle.getInt(REQUEST_CODE, 0);

            //fill in all the fields
            binding.editTextTitle.setText(note.getPrompt());
            binding.editTextMeaning.setText(note.getMeaning());

            //get the pronunciation of note and store globally
            notePronunciation = note.getPronunciation();

            //show name of prev recording.
            if (notePronunciation != null) {
                File pronunciation = new File(note.getPronunciation());
                binding.tvAddRecording.setText(pronunciation.getName());
                recordingExists = true;
            }

            //get synonyms of note and store globally
            synonyms = note.getSynonyms();

            //if the note has synonyms show them on screen too
            if (synonyms != null && synonyms.size() > 0) {
                for (int i = 0; i < synonyms.size(); i++) {
                    if (i == 0) binding.editTextSynonym.setText(synonyms.get(0));
                    else
                        createSynonymField(synonyms.get(i));
                }
            }

        } else {
            setTitle("Add Note");
            requestCode = ADD_NOTE_REQUEST;
        }


        binding.btnAddSynonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSynonymField(null);
            }
        });

        binding.btnRemoveSynonym.setOnClickListener(
                RemoveSynonymListener.builder()
                        .binding(binding).editTexts(et_synonyms).build()
        );

        binding.btnRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRecordPressed();
            }
        });

        binding.btnStopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStopPressed();
            }
        });

        binding.btnPlayRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlayPressed();
            }
        });

        binding.tvAddRecording.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String fileName = null;

                //are we editing note's pronunciation? -> notePronunciation
                // are we adding pronunciation to a new note? -> temp_recordingName
                if(notePronunciation != null)
                    fileName = notePronunciation;
                else if(recordingExists)
                    fileName = temp_recordingName;

                if (fileName != null) {
                    File file = new File(fileName);
                    Log.d(TAG, "getRecordingFilePath: " + file.getPath());

                    //does file exist on device?
                    if(file.isFile()){
                        //was the file deleted?
                        if(file.delete()){
                            //remove link note <-> pronunciation location, in case we are editing
                            if(note != null)
                                note.setPronunciation(null);
                            //update ui
                            binding.tvAddRecording.setText("Nothing recorded yet");
                            //reset
                            recordingExists = false;
                        }
                    }
                }
                return false;
            }
        });

    }

    private void createSynonymField(String synonym) {
        ConstraintSet set = new ConstraintSet();

        EditText editText = new EditText(AddEditNoteActivity.this);
        editText.setId(View.generateViewId());
        if (synonym == null) {
            editText.setHint("add synonym...");
            editText.setHintTextColor(binding.editTextSynonym.getHintTextColors());
        } else {
            editText.setText(synonym);
        }
        editText.setTextColor(binding.editTextSynonym.getTextColors());


        et_synonyms.add(editText);

        binding.clRootLayout.addView(editText, et_synonyms.size());

        set.clone(binding.clRootLayout);
        // connect start and end point of views, in this case top of child to top of parent.
        set.connect(editText.getId(), ConstraintSet.TOP, et_synonyms.get(et_synonyms.size() - 2).getId(), ConstraintSet.BOTTOM);
        set.connect(editText.getId(), ConstraintSet.START, et_synonyms.get(et_synonyms.size() - 2).getId(), ConstraintSet.START);
        // ... similarly add other constraints
        set.applyTo(binding.clRootLayout);

        //since we now have an additional field
        //show a button to remove the created view
        binding.btnRemoveSynonym.setVisibility(View.VISIBLE);
        binding.tvRemoveSynonym.setVisibility(View.VISIBLE);

    }

    private void saveSynonyms() {
        //there are already the synonyms within the list. Adding synonyms from the edittext fields causes
        //the list to have duplicates. So we need to clear the list.
        if (synonyms != null && synonyms.size() > 0)
            synonyms.clear();
        else
            //in case the note doesn't have synonyms we give the user a chance to add some.
            synonyms = new ArrayList<>();

        for (int i = 0; i < et_synonyms.size(); i++) {
            String synonym = et_synonyms.get(i).getText().toString();
            if (synonym != null && !synonym.trim().isEmpty())
                synonyms.add(synonym);
        }
    }

    private void saveNote() {
        //get input of edittext fields
        String title = binding.editTextTitle.getText().toString();
        String meaning = binding.editTextMeaning.getText().toString();

        //restrict that some fields may not be empty
        if (title.trim().isEmpty() || meaning.trim().isEmpty()) {
            Toast.makeText(this, "Insert a title and meaning!", Toast.LENGTH_SHORT).show();
            return;
        }

        //store the synonyms in a list
        saveSynonyms();

        //create a new note (generates an id by default)
        //..or set new values to the existing note, which already has an id
        if (requestCode == ADD_NOTE_REQUEST) {
            note = Note.builder()
                    .prompt(title)
                    .meaning(meaning)
                    .synonyms(synonyms)
                    .build();
        } else if (requestCode == EDIT_NOTE_REQUEST) {
            note.setPrompt(title);
            note.setMeaning(meaning);
            note.setSynonyms(synonyms);
        }

        // add pronunciation too if exist, or null when no recording was done
        note.setPronunciation(prepareRecording());

        String extraKey = requestCode == ADD_NOTE_REQUEST ? EXTRA_ADD_NOTE : EXTRA_EDIT_NOTE;

        //prepare intent and send note to ManageNoteActivity again
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_ADD_NOTE, (Serializable) note);
        intent.putExtra(extraKey, bundle);

        intent.putExtra(REQUEST_CODE, requestCode);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isMicrophonePresent() {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, Permissions.REQUEST_MICROPHONE);
        }
    }

    private void btnRecordPressed() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();

            try {
                notePronunciation = getRecordingFilePath();

                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.OGG);
                mediaRecorder.setOutputFile(notePronunciation);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS);
                mediaRecorder.setAudioEncodingBitRate(128000);
                mediaRecorder.setAudioSamplingRate(44100);
                mediaRecorder.prepare();
                mediaRecorder.start();

                Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void btnStopPressed() {
        // if there is a recording going on...
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();

            mediaRecorder = null;
            recordingExists = true;

            //update ui
            binding.tvAddRecording.setText("recording available");
            Toast.makeText(this, "Recording stopped...", Toast.LENGTH_SHORT).show();
        }
    }

    private void btnPlayPressed() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();

        // if there was a recording...
        if (mediaRecorder == null && recordingExists) {
            if (!mediaPlayer.isPlaying()) {
                try {
                    //when a note already had a pronunciation recording.. play that
                    //or else play the
                    mediaPlayer.setDataSource(notePronunciation);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    mediaPlayer = null;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File recordingDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(recordingDir, temp_recordingName);
        Log.d(TAG, "getRecordingFilePath: " + file.getPath());
        return file.getPath();
    }

    //renames the temporary filename to the final one, containing the name of the note.
    private String prepareRecording() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File recordingDir = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File old_file = new File(recordingDir, temp_recordingName);

        if (old_file.isFile()) {
            temp_recordingName = note.getNoteId()+"_"+note.getPrompt() +".ogg";
            File new_file = new File(recordingDir, temp_recordingName);
            if (old_file.renameTo(new_file)) {
                Log.d(TAG, "prepareRecording: [New filename: " + new_file.getName() + "]");
                return temp_recordingName;
            }
        }
        Log.d(TAG, "prepareRecording: Processing failed");

        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
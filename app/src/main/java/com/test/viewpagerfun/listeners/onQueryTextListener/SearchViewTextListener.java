package com.test.viewpagerfun.listeners.onQueryTextListener;

import android.content.Context;
import android.text.TextUtils;
import android.widget.SearchView;
import android.widget.Toast;

import com.test.viewpagerfun.ManageNoteActivity;
import com.test.viewpagerfun.NoteAdapter;
import com.test.viewpagerfun.databinding.ManageNoteBinding;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.viewmodel.ManageNoteViewModel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchViewTextListener implements SearchView.OnQueryTextListener {

    private Context context;
    private NoteAdapter adapter;

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
            Toast.makeText(getContext(), "couldn't find note matching query", Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(TextUtils.isEmpty(newText)){
            this.onQueryTextSubmit("emptyQuery");
        }
        return false;
    }
}

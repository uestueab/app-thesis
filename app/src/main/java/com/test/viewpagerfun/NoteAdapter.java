package com.test.viewpagerfun;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.test.viewpagerfun.model.entity.Note;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    private OnItemclickListener listener;
    private List<Note> noteListFull = new ArrayList<>();

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull  Note oldItem, @NonNull Note newItem) {
            return oldItem.getNoteId() == newItem.getNoteId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull  Note oldItem, @NonNull  Note newItem) {
            return oldItem.getPrompt().equals(newItem.getPrompt());
//                    && oldItem.getDescription().equals(newItem.getDescription()) &&
//                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_note, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getPrompt());
        holder.textViewMeaning.setText(currentNote.getMeaning());
    }


    public Note getNoteAt(int position){
        return getItem(position);
    }


    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewMeaning;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewMeaning = itemView.findViewById(R.id.text_view_meaning);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemclickListener{
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemclickListener listener){
        this.listener = listener;
    }

    public boolean filter(String query) {
        String searchQuery = query.toLowerCase();
        List<Note> filteredList = new ArrayList<>();
        List<Note> currentList  = getCurrentList();

        for(Note note : currentList){
            String currentNoteTitle = note.getPrompt().toLowerCase();
            // Found a note that matches the query! -> add to list
            if(currentNoteTitle.contains(searchQuery))
                filteredList.add(note);
        }

        //nothing found -> do nothing!
        if (filteredList.isEmpty())
            return false;

        /* Here we have a filteredList: Our query was successful
         * We want to save the full list, case the user empties the search field:
         * - display the original list with all the items again!
         * - if statement makes sure the getCurrentList() call runs only once.
         */
        if (this.noteListFull.size() == 0)
            this.noteListFull = new ArrayList<>(currentList);

        // Sort the list to ensure the user gets his desired match
        filteredList.sort(new Comparator<Note>() {
            @Override
            public int compare(Note firstNote, Note secondNote) {
                /* return -1 if first argument should be before second argument
                 * return 1 if second should be before first argument
                 * return 0 otherwise (meaning the order stays the same)
                 */
                if (firstNote.getPrompt().toLowerCase().startsWith(searchQuery)) {
                    return -1;
                } else if (secondNote.getPrompt().toLowerCase().startsWith(searchQuery)) {
                    return 1;
                } else return 0;
            }
        });

        //update the view
        submitList(filteredList);
        return true;
    }

    public int getNoteCount(){
        return this.noteListFull.size();
    }

    public List<Note> getNotes(){
        return this.noteListFull;
    }
}
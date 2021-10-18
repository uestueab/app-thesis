package com.thesis.yatta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.thesis.yatta.model.entity.FlashCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class FlashCardAdapter extends ListAdapter<FlashCard, FlashCardAdapter.FlashCardHolder> {
    private OnItemclickListener listener;
    private List<FlashCard> flashCardListFull = new ArrayList<>();

    public FlashCardAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<FlashCard> DIFF_CALLBACK = new DiffUtil.ItemCallback<FlashCard>() {
        @Override
        public boolean areItemsTheSame(@NonNull  FlashCard oldItem, @NonNull FlashCard newItem) {
            return oldItem.getFlashCardId() == newItem.getFlashCardId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull  FlashCard oldItem, @NonNull  FlashCard newItem) {
            return oldItem.getPrompt().equals(newItem.getPrompt());
//                    && oldItem.getDescription().equals(newItem.getDescription()) &&
//                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public FlashCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_flashcard, parent, false);
        return new FlashCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardHolder holder, int position) {
        FlashCard currentFlashCard = getItem(position);
        holder.textViewTitle.setText(currentFlashCard.getPrompt());
        holder.textViewMeaning.setText(currentFlashCard.getMeaning());
    }


    public FlashCard getFlashCardAt(int position){
        return getItem(position);
    }


    class FlashCardHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewMeaning;

        public FlashCardHolder(View itemView) {
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
        void onItemClick(FlashCard flashCard);
    }

    public void setOnItemClickListener(OnItemclickListener listener){
        this.listener = listener;
    }

    public boolean filter(String query) {
        String searchQuery = query.toLowerCase();
        List<FlashCard> filteredList = new ArrayList<>();
        List<FlashCard> currentList  = getCurrentList();

        for(FlashCard flashCard : currentList){
            String currentFlashCardTitle = flashCard.getPrompt().toLowerCase();
            // Found a flashCard that matches the query! -> add to list
            if(currentFlashCardTitle.contains(searchQuery))
                filteredList.add(flashCard);
        }

        //nothing found -> do nothing!
        if (filteredList.isEmpty())
            return false;

        /* Here we have a filteredList: Our query was successful
         * We want to save the full list, case the user empties the search field:
         * - display the original list with all the items again!
         * - if statement makes sure the getCurrentList() call runs only once.
         */
        if (this.flashCardListFull.size() == 0)
            this.flashCardListFull = new ArrayList<>(currentList);

        // Sort the list to ensure the user gets his desired match
        filteredList.sort(new Comparator<FlashCard>() {
            @Override
            public int compare(FlashCard firstFlashCard, FlashCard secondFlashCard) {
                /* return -1 if first argument should be before second argument
                 * return 1 if second should be before first argument
                 * return 0 otherwise (meaning the order stays the same)
                 */
                if (firstFlashCard.getPrompt().toLowerCase().startsWith(searchQuery)) {
                    return -1;
                } else if (secondFlashCard.getPrompt().toLowerCase().startsWith(searchQuery)) {
                    return 1;
                } else return 0;
            }
        });

        //update the view
        submitList(filteredList);
        return true;
    }

    public int getFlashCardCount(){
        return this.flashCardListFull.size();
    }

    public List<FlashCard> getFlashCards(){
        return this.flashCardListFull;
    }
}
package com.test.viewpagerfun.listeners.onQueryTextListener;

import android.content.Context;
import android.text.TextUtils;
import android.widget.SearchView;
import android.widget.Toast;

import com.test.viewpagerfun.FlashCardAdapter;
import com.test.viewpagerfun.model.entity.FlashCard;

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
    private FlashCardAdapter adapter;

    @Override
    public boolean onQueryTextSubmit(String query) {
        //when SearchView gets cleared.
        if(query.equals("emptyQuery")){
            //makes sure flashCards exist in adapter field
            if(adapter.getFlashCardCount() == 0){
                return false;
            }
            List<FlashCard> flashCardsListFull = adapter.getFlashCards();
            //refresh screen with the full list again.
            adapter.submitList(flashCardsListFull);
            return false;
        }

        if (!adapter.filter(query))
            Toast.makeText(getContext(), "couldn't find flashCard matching query", Toast.LENGTH_SHORT).show();

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

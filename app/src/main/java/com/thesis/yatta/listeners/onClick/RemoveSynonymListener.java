package com.thesis.yatta.listeners.onClick;

import android.view.View;
import android.widget.EditText;

import com.thesis.yatta.databinding.ActivityAddFlashcardBinding;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveSynonymListener implements View.OnClickListener {

    private ActivityAddFlashcardBinding binding;
    private List<EditText> editTexts = new ArrayList<EditText>();

    @Override
    public void onClick(View v) {
        if (editTexts.size() == 2) {
            // there are two edittext fields left. remove one more..
            binding.clRootLayout.removeView(editTexts.get(editTexts.size() - 1));
            editTexts.remove(editTexts.size() - 1);
            // and make the button disappear
            binding.btnRemoveSynonym.setVisibility(View.INVISIBLE);
            binding.tvRemoveSynonym.setVisibility(View.INVISIBLE);

            return;
        }
        binding.clRootLayout.removeView(editTexts.get(editTexts.size() - 1));
        editTexts.remove(editTexts.size() - 1);
    }
}

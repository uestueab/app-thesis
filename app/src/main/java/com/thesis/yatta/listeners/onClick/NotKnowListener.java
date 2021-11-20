package com.thesis.yatta.listeners.onClick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.thesis.yatta.PrefManager;
import com.thesis.yatta.R;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thesis.yatta.constants.ConstantsHolder.PREFS_DISPLAY_THEME;
import static com.thesis.yatta.constants.ConstantsHolder.THEME_GRUVBOX;
import static com.thesis.yatta.constants.ConstantsHolder.THEME_LIGHT;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotKnowListener implements View.OnClickListener {

    private Context context;
    private String mnemonic;

    @Override
    public void onClick(View v) {
        if(mnemonic == null){
            Toast.makeText(getContext(), "Flashcard has no mnemonic!", Toast.LENGTH_SHORT).show();
        }else{
            //This is needed to style the dialog color so that it matches the current theme
            PrefManager.init(getContext());
            String theme =  PrefManager.get(PREFS_DISPLAY_THEME,THEME_LIGHT);
            int dialogTheme;

            if(theme.equals(THEME_GRUVBOX)){
                dialogTheme = R.style.gruvbox_dialogTheme;
            }else {
                dialogTheme = R.style.light_dialogTheme;
            }


            AlertDialog alert = new AlertDialog.Builder(getContext(), dialogTheme)
                    .setTitle("Mnemonic:")
                    .setMessage(getMnemonic())
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();

            alert.setOnShowListener(arg0 -> {
                //This is more or less a workaround to set the button text color
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getAttributeColor(R.attr.textColor));
            });
            alert.show();


        }
    }

    private int getAttributeColor(int resId) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(resId, typedValue, true);

        //color value as int
        return typedValue.data;
    }
}

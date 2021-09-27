package com.test.viewpagerfun;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

/*
    BaseActivity:
    Super class of most Activities. Adapts theme changes for every activity.
 */
public abstract class BaseActivity extends AppCompatActivity {

    String current_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PrefManager.init(this);
        String new_theme =  PrefManager.get(PREFS_DISPLAY_THEME,THEME_LIGHT);

        if (!current_theme.equals(new_theme)) {
            this.recreate();
        }
    }

    protected void applyTheme(Context context){
        PrefManager.init(context);
        current_theme =  PrefManager.get(PREFS_DISPLAY_THEME,THEME_LIGHT);
        if (current_theme.equals(THEME_GRUVBOX)) {
            setTheme(R.style.gruvbox);
        } else {
            setTheme(R.style.light);
        }

    }
}

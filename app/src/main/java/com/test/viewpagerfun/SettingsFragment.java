package com.test.viewpagerfun;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preference_screen, rootKey);

        loadSettings();
    }

    private void loadSettings() {
        PrefManager.init(getContext());

        SwitchPreferenceCompat prefs_general_notifications = (SwitchPreferenceCompat) findPreference(PREFS_GENERAL_NOTIFICATIONS);
        boolean notifications_enabled = PrefManager.get(PREFS_GENERAL_NOTIFICATIONS, false);
        if (notifications_enabled) {
            prefs_general_notifications.setChecked(true);
            prefs_general_notifications.setIcon(R.drawable.notifications_on);
        } else {
            prefs_general_notifications.setChecked(false);
            prefs_general_notifications.setIcon(R.drawable.notifications_off);
        }

        prefs_general_notifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean notifications_enabled = (boolean) newValue;
                if (notifications_enabled) {
                    prefs_general_notifications.setIcon(R.drawable.notifications_on);
                } else {
                    prefs_general_notifications.setIcon(R.drawable.notifications_off);
                }
                PrefManager.set(PREFS_GENERAL_NOTIFICATIONS, notifications_enabled);
                return true;
            }
        });

        ListPreference pref_display_theme = (ListPreference) findPreference(PREFS_DISPLAY_THEME);
        pref_display_theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String theme_name = (String) newValue;
                switch (theme_name.toLowerCase()) {
                    case "gruvbox":
                        PrefManager.set(PREFS_DISPLAY_THEME, THEME_GRUVBOX);
                        getActivity().recreate();
                        break;
                    case "breeze":
                        PrefManager.set(PREFS_DISPLAY_THEME, THEME_BREEZE);
                        getActivity().recreate();
                        break;
                    default:
                        PrefManager.set(PREFS_DISPLAY_THEME, THEME_LIGHT);
                        getActivity().recreate();
                        break;
                }

                return true;
            }
        });

        Preference prefs_feedback = (Preference) findPreference(PREFS_FEEDBACK);
        prefs_feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URI_REPO_ISSUES)));
                return true;
            }
        });

        SwitchPreferenceCompat prefs_review_shuffle = (SwitchPreferenceCompat) findPreference(PREFS_REVIEW_SHUFFLE);
        //show check state
        boolean shuffle_enabled = PrefManager.get(PREFS_REVIEW_SHUFFLE, false);
        prefs_review_shuffle.setChecked(shuffle_enabled);

        prefs_review_shuffle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean shuffle_preference = (boolean) newValue;
                PrefManager.set(PREFS_REVIEW_SHUFFLE, shuffle_preference);
                return true;
            }
        });

        SwitchPreferenceCompat prefs_display_animation = (SwitchPreferenceCompat) findPreference(PREFS_DISPLAY_ANIMATION);
        //show check state
        boolean animation_enabled = PrefManager.get(PREFS_DISPLAY_ANIMATION, false);
        prefs_display_animation.setChecked(animation_enabled);

        prefs_display_animation.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean animation_preference = (boolean) newValue;
                PrefManager.set(PREFS_DISPLAY_ANIMATION, animation_preference);
                return true;
            }
        });

        SwitchPreferenceCompat prefs_review_back = (SwitchPreferenceCompat) findPreference(PREFS_REVIEW_BACK);
        //show check state
        boolean back_enabled = PrefManager.get(PREFS_REVIEW_BACK, false);
        prefs_review_back.setChecked(back_enabled);
        prefs_review_back.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean backPress_preference = (boolean) newValue;
                PrefManager.set(PREFS_REVIEW_BACK,backPress_preference);
                return true;
            }
        });


    }
}

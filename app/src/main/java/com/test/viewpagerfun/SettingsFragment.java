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

    }
}

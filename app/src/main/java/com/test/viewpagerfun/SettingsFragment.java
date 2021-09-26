package com.test.viewpagerfun;

import android.graphics.Color;
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

    private void loadSettings(){
        PrefManager.init(getContext());

        SwitchPreferenceCompat prefs_notifications = (SwitchPreferenceCompat) findPreference("prefs_notifications");
        boolean notications_enabled = PrefManager.get("prefs_notification",false);
        if(notications_enabled){
            prefs_notifications.setChecked(true);
            prefs_notifications.setIcon(R.drawable.notifications_on);
        }
        else{
            prefs_notifications.setChecked(false);
            prefs_notifications.setIcon(R.drawable.notifications_off);
        }

        prefs_notifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean notifications_enabled = (boolean) newValue;
                if(notifications_enabled){
                    prefs_notifications.setIcon(R.drawable.notifications_on);
                }else{
                    prefs_notifications.setIcon(R.drawable.notifications_off);
                }
                PrefManager.set("prefs_notification",notifications_enabled);
                return true;
            }
        });

        ListPreference pref_theme = (ListPreference) findPreference("pref_theme");
        pref_theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String theme_name = (String) newValue;
                    switch (theme_name.toLowerCase()){
                        case "gruvbox":
                            PrefManager.set(PREFS_THEME,THEME_GRUVBOX);
                            getActivity().recreate();
                            break;
                        case "breeze":
                            PrefManager.set(PREFS_THEME,THEME_BREEZE);
                            getActivity().recreate();
                            break;
                        default:
                            PrefManager.set(PREFS_THEME,THEME_LIGHT);
                            getActivity().recreate();
                            break;
                    }

                return true;
            }
        });

    }
}

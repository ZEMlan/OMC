package com.example.onemorechapter.settings;


import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.onemorechapter.R;


public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(getContext(), R.xml.advanced_preferences, false);
    }
}

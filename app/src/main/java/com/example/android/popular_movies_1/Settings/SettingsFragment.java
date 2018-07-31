package com.example.android.popular_movies_1.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.android.popular_movies_1.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        int count = getPreferenceScreen().getPreferenceCount();

        for (int i=0; i<count; i++){
            Preference preference = getPreferenceScreen().getPreference(i);

            if (preference instanceof ListPreference){
                String value = sharedPreferences.getString(preference.getKey(), "");

                setSummaryPreference(preference, value);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (preference != null){
            if (preference instanceof ListPreference){
                String value = sharedPreferences.getString(preference.getKey(), "");
                setSummaryPreference(preference, value);
            }
        }
    }

    private void setSummaryPreference(Preference preference, String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;

            int index = listPreference.findIndexOfValue(value);
            if (index >= 0)
                listPreference.setSummary(listPreference.getEntries()[index]);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}

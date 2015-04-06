package com.example.mydemos.view;


import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;


public class PreferenceTest extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPreferenceScreen(createPreferenceHierarchy());
    }

    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);


        // Dialog based preferences
        PreferenceCategory dialogBasedPrefCat = new PreferenceCategory(this);
        dialogBasedPrefCat.setTitle("dialog_based_preferences");
        root.addPreference(dialogBasedPrefCat);

        // List preference
        IconDecorListPreference listPref = new IconDecorListPreference(this);
        listPref.setKey("IconDecorListPreference");
        listPref.setSummary(IconDecorListPreference.getSummaryInfo(this));
        dialogBasedPrefCat.addPreference(listPref);


        return root;
    }
}

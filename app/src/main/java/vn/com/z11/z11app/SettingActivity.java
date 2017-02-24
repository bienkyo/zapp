package vn.com.z11.z11app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Iterator;
import java.util.List;

import vn.com.z11.z11app.ApiResponseModel.LanguageResponse;
import vn.com.z11.z11app.Database.Query.LocalDb;

/**
 * Created by Bien-kun on 21/02/2017.
 */

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction()
                .replace(R.id.content,new SettingFragment())
                .commit();
    }

    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
        ListPreference mPrefLanguage;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);
            mPrefLanguage = (ListPreference) findPreference(getString(R.string.pref_native_language_key));
            if (mPrefLanguage != null){
                final List<LanguageResponse.Language> languages = (new  LocalDb(getActivity())).getLanguages();
                String[] entries = new String[languages.size()];
                String[] values = new String[languages.size()];

                Iterator<LanguageResponse.Language> iter = languages.iterator();
                int i = 0;
                while (iter.hasNext()){
                    LanguageResponse.Language e = iter.next();
                    entries[i] = e.description;
                    values[i] = e.languageCode;
                    i++;
                }
                String defaultValue = entries[0];
                mPrefLanguage.setDefaultValue(defaultValue);
                mPrefLanguage.setEntries(entries);
                mPrefLanguage.setEntryValues(values);
                mPrefLanguage.setOnPreferenceChangeListener(this);
                onPreferenceChange(mPrefLanguage,
                        PreferenceManager.getDefaultSharedPreferences(getActivity())
                                .getString(getString(R.string.pref_native_language_key),defaultValue));

            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                int preIndex = mPrefLanguage.findIndexOfValue(stringValue);
                if (preIndex >= 0) {
                    preference.setSummary(mPrefLanguage.getEntries()[preIndex]);
                }

            }
            return true;
        }

        @Override
        public void onResume() {
            super.onResume();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onStop() {
            super.onStop();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    }


}

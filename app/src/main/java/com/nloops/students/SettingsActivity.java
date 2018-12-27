package com.nloops.students;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.cloud.CloudOperations;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

  @BindView(R.id.general_toolbar)
  Toolbar mToolBar;
  @BindView(R.id.tv_general_toolbar)
  TextView mToolBarTV;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    // link views using ButterKnife
    ButterKnife.bind(this);
    // Setup toolbar
    setSupportActionBar(mToolBar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
  }


  public static class TasksPreferenceFragment extends PreferenceFragment
      implements Preference.OnPreferenceChangeListener, OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.main_settings);

      // get first preference
      Preference absnteem = findPreference(getString(R.string.absnteem_key));
      bindSummaryToValue(absnteem);
      // get the second one
      Preference scheduleBackup = findPreference(getString(R.string.settings_sync_time_key));
      bindSummaryToValue(scheduleBackup);
    }

    @Override
    public void onResume() {
      super.onResume();
      // register listener
      getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
      super.onDestroy();
      getPreferenceManager().getSharedPreferences()
          .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
      String stringValue = newValue.toString();
      if (preference instanceof ListPreference) {
        ListPreference listPreference = (ListPreference) preference;
        int prefIndex = listPreference.findIndexOfValue(stringValue);
        if (prefIndex >= 0) {
          CharSequence[] labels = listPreference.getEntries();
          preference.setSummary(labels[prefIndex]);
        }
      } else {
        preference.setSummary(stringValue);
      }
      return true;
    }

    private void bindSummaryToValue(Preference preference) {
      preference.setOnPreferenceChangeListener(this);
      SharedPreferences sharedPreferences = PreferenceManager
          .getDefaultSharedPreferences(preference.getContext());
      String prefString = sharedPreferences.getString(preference.getKey(), "");
      onPreferenceChange(preference, prefString);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
      if (key.equals(getString(R.string.settings_sync_time_key))) {
        CloudOperations.getInstance(getActivity().getApplicationContext()).initialize();
      }
    }
  }

}

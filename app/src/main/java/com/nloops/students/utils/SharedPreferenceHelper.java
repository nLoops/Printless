package com.nloops.students.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nloops.students.R;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceHelper {

  private static SharedPreferenceHelper instance = null;
  private static SharedPreferences preferences;
  private static SharedPreferences.Editor editor;
  private static Gson gson;
  private static final String SCHEDULE_SUBJECT_ATTENDANCE = "schedule_attendance";
  private static Context mContext;

  private SharedPreferenceHelper() {
  }


  public static SharedPreferenceHelper getInstance(Context context) {
    if (instance == null) {
      mContext = context;
      instance = new SharedPreferenceHelper();
      preferences = context.getSharedPreferences(SCHEDULE_SUBJECT_ATTENDANCE, Context.MODE_PRIVATE);
      editor = preferences.edit();
      gson = new Gson();
    }
    return instance;
  }

  public void saveScheduled(ArrayList<Long> list, String subjectKey) {
    String key = String.valueOf(subjectKey);
    String json = gson.toJson(list);
    editor.remove(key).commit();
    editor.putString(key, json);
    editor.commit();
  }

  public ArrayList<Long> readScheduled(String subjectKey) {
    String response = preferences.getString(subjectKey, "");
    return gson.fromJson(response,
        new TypeToken<List<Long>>() {
        }.getType());
  }

  public void saveUserEmail(String email) {
    editor.remove(UtilsConstants.USER_EMAIL).commit();
    editor.putString(UtilsConstants.USER_EMAIL, email).commit();
    editor.commit();
  }

  public String getUserEmail() {
    return preferences.getString(UtilsConstants.USER_EMAIL, "");
  }


  public void setPermissionsState(Boolean state) {
    editor.remove(UtilsConstants.PERMISSIONS_GRANTED).commit();
    editor.putBoolean(UtilsConstants.PERMISSIONS_GRANTED, state).commit();
    editor.commit();
  }

  public boolean getPermissionsState() {
    return preferences.getBoolean(UtilsConstants.PERMISSIONS_GRANTED, false);
  }

  public String getAbsnteemPerc() {
    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    String perc = mPreferences.getString(mContext.getString(R.string.absnteem_key), "50");
    Log.d("TESA", perc);
    return perc;
  }


}

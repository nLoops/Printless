package com.nloops.students.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceHelper {

  private static SharedPreferenceHelper instance = null;
  private static SharedPreferences preferences;
  private static SharedPreferences.Editor editor;
  private static Gson gson;
  private static final String SCHEDULE_SUBJECT_ATTENDANCE = "schedule_attendance";

  private SharedPreferenceHelper() {
  }


  public static SharedPreferenceHelper getInstance(Context context) {
    if (instance == null) {
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


}

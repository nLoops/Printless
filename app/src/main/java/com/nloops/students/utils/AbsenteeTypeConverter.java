package com.nloops.students.utils;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class AbsenteeTypeConverter {

  static Gson gson = new Gson();

  @TypeConverter
  public static List<StudentModel> stringToStudentModelList(String data) {
    if (data == null) {
      return Collections.emptyList();
    }

    Type listType = new TypeToken<List<StudentModel>>() {
    }.getType();

    return gson.fromJson(data, listType);
  }

  @TypeConverter
  public static String someObjectListToString(List<StudentModel> students) {
    return gson.toJson(students);
  }

}

package com.nloops.students.utils;

public class UtilsConstants {

  private UtilsConstants() {
    // private constructor to prevent calling or create a new object
  }

  public static final String EXTRA_SUBJECT_ID_INTENT = "com.nloops.students.EXTRA_INTENT_id";
  public static final String EXTRA_SUBJECT_ID_TO_CLASSES = "com.nloops.studetns.EXTRA_INTENT_subjectID";
  public static final String EXTRA_CLASS_ID_INTENT = "com.nloops.students.EXTRA_INTENT_CLASS_id";
  public static final String EXTRA_CLASS_TO_EDIT_SUBJECT_ID = "com.nloops.students.EXTRA_INTENT_CLASS_EDIT_id";
  public static final String EXTRA_STUDENT_ID_INTENT = "com.nloops.students.EXTRA_STUDENT_INTENT_ID";
  public static final String EXTRA_CLASS_TO_STUDENT_ID = "com.nloops.students.EXTRA_CLASS_TO_STUDENT";
  public static final String EXTRA_CLASS_ID_TO_ATTENDANCE = "com.nloops.students.EXTRA_CLASS_TO_ATT";
  public static final String EXTRA_SET_ATTENDANCE_EDIT_MODE = "com.nloops.students.EXTRA_ATT_EDIT_MODE";
  public static final String EXTRA_SUBJECT_ID_CLASS_TO_STUDENT = "com.nloops.students.EXTRA_SUBJECT_TO_STUDENT";
  public static final String EXTRA_SUBJECT_ID_CLASS_TO_ATTENDANCE = "com.nloops.students.EXTRA_SUBJECT_TO_ATTENDANCE";
  public static final String EXTRA_SUBJECT_TO_CLASS_REPORT = "com.nloops.students.EXTRA_SUBJECT_Class_report";
  public static final String EXTRA_SUBJECT_NAME_TO_CLASS_REPORT = "com.nloops.students.EXTRA_SUBJECT_NAME_CLASS_REPORT";
  public static final String EXTRA_CLASS_TO_STUDENT_REPORT = "com.nloops.students.EXTRA_CLASS_Student_report";
  public static final String EXTRA_CLASS_NAME_TO_STUDENT_REPORT = "com.nloops.students.EXTRA_CLASS_NAME_STUDENT_REPORT";
  // values for toolbars
  public static final String EXTRA_SUBJECT_NAME_TO_CLASS = "com.nloops.students.EXTRA_SUBJECT_NAME";
  public static final String EXTRA_CLASS_NAME_TO_STUDENT = "com.nloops.students.EXTRA_CLASS_NAME";
  // for Activity RESULTS messages
  public static final int RESULT_ADD_ITEM = 100;
  public static final int RESULT_EDIT_ITEM = 101;
  public static final int RESULT_DELETE_ITEM = 102;

  public static final String PERMISSIONS_GRANTED = "permissions_stat";

  // for Student Model State
  public static final int STUDENT_ABSENTEE_OKAY = 1;
  public static final int STUDENT_ABSENTEE_NO = 0;

  // week days
  public static final String DAY_SATURDAY = "Saturday";
  public static final String DAY_SUNDAY = "Sunday";
  public static final String DAY_MONDAY = "Monday";
  public static final String DAY_TUESDAY = "Tuesday";
  public static final String DAY_WEDNESDAY = "Wednesday";
  public static final String DAY_THURDAY = "Thursday";
  public static final String DAY_FRIDAY = "Friday";

  // hours
  public static final String HOUR_TEN = "10:00 AM";
  public static final String HOUR_ONE = "01:00 PM";
  public static final String HOUR_THREE = "03:00 PM";
  public static final String HOUR_SEVEN = "07:00 PM";

  // for Notification ID
  public static final String NOTIFICATION_ID = "notify_id";

  // for Firebase
  public static final String USERS_DATABASE_REFERENCE = "users";
  public static final String ATTENDANCE_DATABASE_REFERENCE = "attendance";
  public static final String USER_EMAIL = "user_email";

}

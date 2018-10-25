package com.nloops.students.utils;

public class UtilsConstants {

  private UtilsConstants() {
    // private constructor to prevent calling or create a new object
  }

  public static final String EXTRA_SUBJECT_ID_INTENT = "com.nloops.students.EXTRA_INTENT_id";
  public static final String EXTRA_SUBJECT_ID_TO_CLASSES = "com.nloops.studetns.EXTRA_INTENT_subjectID";
  public static final String EXTRA_CLASS_ID_INTENT = "com.nloops.students.EXTRA_INTENT_CLASS_id";
  public static final String EXTRA_CLASS_TO_EDIT_SUBJECT_ID = "com.nloops.students.EXTRA_INTENT_CLASS_EDIT_id";
  // for Activity RESULTS messages
  public static final int RESULT_ADD_ITEM = 100;
  public static final int RESULT_EDIT_ITEM = 101;
  public static final int RESULT_DELETE_ITEM = 102;

  // for Student Model State
  public static final int STUDENT_ABSENTEE_OKAY = 1;
  public static final int STUDENT_ABSENTEE_NO = 0;

}

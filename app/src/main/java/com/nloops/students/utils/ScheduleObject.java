package com.nloops.students.utils;

public class ScheduleObject {

  private String mDateString;
  private String mTimeString;

  public ScheduleObject(String mDateString, String mTimeString) {
    this.mDateString = mDateString;
    this.mTimeString = mTimeString;
  }

  public String getmDateString() {
    return mDateString;
  }

  public void setmDateString(String mDateString) {
    this.mDateString = mDateString;
  }

  public String getmTimeString() {
    return mTimeString;
  }

  public void setmTimeString(String mTimeString) {
    this.mTimeString = mTimeString;
  }
}

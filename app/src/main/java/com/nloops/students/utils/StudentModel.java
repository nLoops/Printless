package com.nloops.students.utils;

import android.os.Parcel;

public class StudentModel implements android.os.Parcelable {

  private int mStudentID;

  private String mStudentUniID;

  private String mStudentName;

  private int mAbsenteeState;


  public StudentModel(int mStudentID, String mStudentUniID, String mStudentName,
      int mAbsenteeState) {
    this.mStudentID = mStudentID;
    this.mStudentUniID = mStudentUniID;
    this.mStudentName = mStudentName;
    this.mAbsenteeState = mAbsenteeState;
  }

  public int getmStudentID() {
    return mStudentID;
  }

  public void setmStudentID(int mStudentID) {
    this.mStudentID = mStudentID;
  }

  public String getmStudentUniID() {
    return mStudentUniID;
  }

  public void setmStudentUniID(String mStudentUniID) {
    this.mStudentUniID = mStudentUniID;
  }

  public String getmStudentName() {
    return mStudentName;
  }

  public void setmStudentName(String mStudentName) {
    this.mStudentName = mStudentName;
  }

  public int getmAbsenteeState() {
    return mAbsenteeState;
  }

  public void setmAbsenteeState(int mAbsenteeState) {
    this.mAbsenteeState = mAbsenteeState;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.mStudentID);
    dest.writeString(this.mStudentUniID);
    dest.writeString(this.mStudentName);
    dest.writeInt(this.mAbsenteeState);
  }

  protected StudentModel(Parcel in) {
    this.mStudentID = in.readInt();
    this.mStudentUniID = in.readString();
    this.mStudentName = in.readString();
    this.mAbsenteeState = in.readInt();
  }

  public static final Creator<StudentModel> CREATOR = new Creator<StudentModel>() {
    @Override
    public StudentModel createFromParcel(Parcel source) {
      return new StudentModel(source);
    }

    @Override
    public StudentModel[] newArray(int size) {
      return new StudentModel[size];
    }
  };
}

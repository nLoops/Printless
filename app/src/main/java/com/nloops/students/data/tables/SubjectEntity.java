package com.nloops.students.data.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Using Room Library this object will mapped as a table to insert, query, update, delete subject
 * data from and to Database.
 */

@Entity(tableName = "subjects")
public class SubjectEntity {

  // Declare Table columns and their data types
  @PrimaryKey(autoGenerate = true)
  private int subjectID;
  @ColumnInfo(name = "subject_name")
  private String subjectName;
  @ColumnInfo(name = "school_name")
  private String schoolName;
  private String userUID;

  // this constructor will automatically used by Room in this object construction.
  public SubjectEntity(int subjectID, String subjectName, String schoolName, String userUID) {
    this.subjectID = subjectID;
    this.subjectName = subjectName;
    this.schoolName = schoolName;
    this.userUID = userUID;
  }

  // marked this constructor with @Ignore annotation because we will use it in our
  // CRUD operations (insert,update,delete etc...)
  @Ignore
  public SubjectEntity(String subjectName, String schoolName, String userUID) {
    this.subjectName = subjectName;
    this.schoolName = schoolName;
    this.userUID = userUID;
  }

  @Ignore
  public SubjectEntity() {
  }

  // object getters and setters.
  public int getSubjectID() {
    return subjectID;
  }

  public void setSubjectID(int subjectID) {
    this.subjectID = subjectID;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  public String getSchoolName() {
    return schoolName;
  }

  public void setSchoolName(String schoolName) {
    this.schoolName = schoolName;
  }

  public String getUserUID() {
    return userUID;
  }

  public void setUserUID(String userUID) {
    this.userUID = userUID;
  }
}

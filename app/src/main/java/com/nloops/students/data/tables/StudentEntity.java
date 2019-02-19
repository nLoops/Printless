package com.nloops.students.data.tables;


import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import com.nloops.students.utils.UtilsConstants;

@Entity(tableName = "students", foreignKeys = {
    @ForeignKey(entity = ClassEntity.class,
        parentColumns = "classID",
        childColumns = "foreignClassID",
        onDelete = CASCADE),
    @ForeignKey(entity = SubjectEntity.class,
        parentColumns = "subjectID",
        childColumns = "foreignSubjectID",
        onDelete = CASCADE)},
    indices = {@Index("foreignClassID"), @Index("foreignSubjectID")})
public class StudentEntity {

  @PrimaryKey(autoGenerate = true)
  private int studentID;

  private String studentName;

  private String studentUniID;

  private int foreignClassID;

  private int attendanceState;

  private int foreignSubjectID;

  private String userUID;

  public StudentEntity(int studentID, String studentName, String studentUniID, int foreignClassID,
      int attendanceState, int foreignSubjectID, String userUID) {
    this.studentID = studentID;
    this.studentName = studentName;
    this.studentUniID = studentUniID;
    this.foreignClassID = foreignClassID;
    this.attendanceState = attendanceState;
    this.foreignSubjectID = foreignSubjectID;
    this.userUID = userUID;
  }

  @Ignore
  public StudentEntity(String studentName, String studentUniID, int foreignClassID,
      int attendanceState, int foreignSubjectID, String userUID) {
    this.studentName = studentName;
    this.studentUniID = studentUniID;
    this.foreignClassID = foreignClassID;
    this.attendanceState = attendanceState;
    this.foreignSubjectID = foreignSubjectID;
    this.userUID = userUID;
  }

  @Ignore
  public StudentEntity(String studentName, int foreignClassID,
      int attendanceState, int foreignSubjectID, String userUID) {
    this.studentName = studentName;
    this.foreignClassID = foreignClassID;
    this.attendanceState = attendanceState;
    this.foreignSubjectID = foreignSubjectID;
    this.userUID = userUID;
  }

  @Ignore
  public StudentEntity() {
  }

  public int getStudentID() {
    return studentID;
  }

  public void setStudentID(int studentID) {
    this.studentID = studentID;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getStudentUniID() {
    return studentUniID;
  }

  public void setStudentUniID(String studentUniID) {
    this.studentUniID = studentUniID;
  }

  public int getForeignClassID() {
    return foreignClassID;
  }

  public void setForeignClassID(int foreignClassID) {
    this.foreignClassID = foreignClassID;
  }

  public int getAttendanceState() {
    return attendanceState;
  }

  public void setAttendanceState(int attendanceState) {
    this.attendanceState = attendanceState;
  }

  public boolean isAttendanceOkay() {
    return attendanceState == UtilsConstants.STUDENT_ABSENTEE_OKAY;
  }

  public int getForeignSubjectID() {
    return foreignSubjectID;
  }

  public void setForeignSubjectID(int foreignSubjectID) {
    this.foreignSubjectID = foreignSubjectID;
  }

  public String getUserUID() {
    return userUID;
  }

  public void setUserUID(String userUID) {
    this.userUID = userUID;
  }
}

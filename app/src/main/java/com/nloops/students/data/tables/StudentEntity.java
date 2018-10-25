package com.nloops.students.data.tables;


import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "students", foreignKeys = @ForeignKey(entity = ClassEntity.class,
    parentColumns = "classID",
    childColumns = "foreignClassID",
    onDelete = CASCADE),
    indices = {@Index("foreignClassID")})
public class StudentEntity {

  @PrimaryKey(autoGenerate = true)
  private int studentID;

  private String studentName;

  private String studentUniID;

  private int foreignClassID;

  public StudentEntity(int studentID, String studentName, String studentUniID, int foreignClassID) {
    this.studentID = studentID;
    this.studentName = studentName;
    this.studentUniID = studentUniID;
    this.foreignClassID = foreignClassID;
  }

  @Ignore
  public StudentEntity(String studentName, String studentUniID, int foreignClassID) {
    this.studentName = studentName;
    this.studentUniID = studentUniID;
    this.foreignClassID = foreignClassID;
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
}

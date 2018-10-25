package com.nloops.students.data.tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import com.nloops.students.utils.AbsenteeTypeConverter;
import com.nloops.students.utils.StudentModel;
import java.util.List;

@Entity(tableName = "absentees")
public class AbsenteeEntity {

  @PrimaryKey(autoGenerate = true)
  private int absenteeID;

  private long absenteeDate;

  @TypeConverters(AbsenteeTypeConverter.class)
  private List<StudentModel> studentsList;

  public AbsenteeEntity(int absenteeID, long absenteeDate,
      List<StudentModel> studentsList) {
    this.absenteeID = absenteeID;
    this.absenteeDate = absenteeDate;
    this.studentsList = studentsList;
  }

  @Ignore
  public AbsenteeEntity(long absenteeDate,
      List<StudentModel> students) {
    this.absenteeDate = absenteeDate;
    this.studentsList = students;
  }

  public int getAbsenteeID() {
    return absenteeID;
  }

  public void setAbsenteeID(int absenteeID) {
    this.absenteeID = absenteeID;
  }

  public long getAbsenteeDate() {
    return absenteeDate;
  }

  public void setAbsenteeDate(long absenteeDate) {
    this.absenteeDate = absenteeDate;
  }

  public List<StudentModel> getStudentsList() {
    return studentsList;
  }

  public void setStudentsList(List<StudentModel> students) {
    this.studentsList = students;
  }
}

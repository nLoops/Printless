package com.nloops.students.data.tables;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "students_join",
    primaryKeys = {"student_id", "absentee_id"},
    foreignKeys = {
        @ForeignKey(entity = StudentEntity.class,
            parentColumns = "studentID",
            childColumns = "student_id"),
        @ForeignKey(entity = AbsenteeEntity.class,
            parentColumns = "absenteeID",
            childColumns = "absentee_id")
    })
public class StudentAbsJoin {

  public final int student_id;

  public final int absentee_id;

  public StudentAbsJoin(int student_id, int absentee_id) {
    this.student_id = student_id;
    this.absentee_id = absentee_id;
  }
}

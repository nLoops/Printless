package com.nloops.students.data.tables;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import com.nloops.students.utils.AbsenteeTypeConverter;
import java.util.List;

@Entity(tableName = "absentees", foreignKeys = {
    @ForeignKey(entity = ClassEntity.class,
        parentColumns = "classID",
        childColumns = "foreignAttClassID",
        onDelete = CASCADE),
    @ForeignKey(entity = SubjectEntity.class,
        parentColumns = "subjectID",
        childColumns = "foreignAttSubjectID",
        onDelete = CASCADE)},
    indices = {@Index("foreignAttClassID"), @Index("foreignAttSubjectID")})
public class AbsenteeEntity {

  @PrimaryKey(autoGenerate = true)
  private int absenteeID;

  private long absenteeDate;

  @TypeConverters(AbsenteeTypeConverter.class)
  private List<StudentEntity> studentsList;

  private int foreignAttClassID;

  private int foreignAttSubjectID;

  public AbsenteeEntity(int absenteeID, long absenteeDate,
      List<StudentEntity> studentsList, int foreignAttClassID, int foreignAttSubjectID) {
    this.absenteeID = absenteeID;
    this.absenteeDate = absenteeDate;
    this.studentsList = studentsList;
    this.foreignAttClassID = foreignAttClassID;
    this.foreignAttSubjectID = foreignAttSubjectID;
  }

  @Ignore
  public AbsenteeEntity(long absenteeDate,
      List<StudentEntity> students, int classID, int foreignAttSubjectID) {
    this.absenteeDate = absenteeDate;
    this.studentsList = students;
    this.foreignAttClassID = classID;
    this.foreignAttSubjectID = foreignAttSubjectID;
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

  public List<StudentEntity> getStudentsList() {
    return studentsList;
  }

  public void setStudentsList(List<StudentEntity> students) {
    this.studentsList = students;
  }

  public int getForeignAttClassID() {
    return foreignAttClassID;
  }

  public void setForeignAttClassID(int foreignAttClassID) {
    this.foreignAttClassID = foreignAttClassID;
  }

  public int getForeignAttSubjectID() {
    return foreignAttSubjectID;
  }

  public void setForeignAttSubjectID(int foreignAttSubjectID) {
    this.foreignAttSubjectID = foreignAttSubjectID;
  }
}

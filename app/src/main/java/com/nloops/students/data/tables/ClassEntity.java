package com.nloops.students.data.tables;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "class_table", foreignKeys = @ForeignKey(entity = SubjectEntity.class,
    parentColumns = "subjectID",
    childColumns = "foreignSubjectID",
    onDelete = CASCADE)
    , indices = {@Index("foreignSubjectID")})
public class ClassEntity {

  @PrimaryKey(autoGenerate = true)
  private int classID;

  private String className;

  // this variable will holds the parent subject id to allow query all classes under this id from DB
  private int foreignSubjectID;

  private String userUID;


  public ClassEntity(int classID, String className, int foreignSubjectID, String userUID) {
    this.classID = classID;
    this.className = className;
    this.foreignSubjectID = foreignSubjectID;
    this.userUID = userUID;
  }

  /**
   * This constructor will use in CRUD operations to make a new ClassEntity.
   *
   * @param className {@link #className}
   */
  @Ignore
  public ClassEntity(String className, int subjectID, String userUID) {
    this.className = className;
    this.foreignSubjectID = subjectID;
    this.userUID = userUID;
  }

  @Ignore
  public ClassEntity() {
  }

  // Getters and Setters for Class variables.
  public int getClassID() {
    return classID;
  }

  public void setClassID(int classID) {
    this.classID = classID;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
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

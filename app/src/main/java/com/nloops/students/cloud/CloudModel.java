package com.nloops.students.cloud;

import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

public class CloudModel {

  private List<SubjectEntity> subjectEntities;
  private List<ClassEntity> classEntities;
  private List<StudentEntity> studentEntities;
  private List<AbsenteeEntity> absenteeEntities;

  public CloudModel(List<SubjectEntity> subjectEntities,
      List<ClassEntity> classEntities,
      List<StudentEntity> studentEntities,
      List<AbsenteeEntity> absenteeEntities) {
    this.subjectEntities = subjectEntities;
    this.classEntities = classEntities;
    this.studentEntities = studentEntities;
    this.absenteeEntities = absenteeEntities;
  }

  public CloudModel(List<SubjectEntity> subjectEntities, List<AbsenteeEntity> absenteeEntities) {
    this.subjectEntities = subjectEntities;
    this.absenteeEntities = absenteeEntities;
  }

  public CloudModel() {
  }

  public List<SubjectEntity> getSubjectEntities() {
    return subjectEntities;
  }

  public void setSubjectEntities(
      List<SubjectEntity> subjectEntities) {
    this.subjectEntities = subjectEntities;
  }

  public List<ClassEntity> getClassEntities() {
    return classEntities;
  }

  public void setClassEntities(List<ClassEntity> classEntities) {
    this.classEntities = classEntities;
  }

  public List<StudentEntity> getStudentEntities() {
    return studentEntities;
  }

  public void setStudentEntities(
      List<StudentEntity> studentEntities) {
    this.studentEntities = studentEntities;
  }

  public List<AbsenteeEntity> getAbsenteeEntities() {
    return absenteeEntities;
  }

  public void setAbsenteeEntities(
      List<AbsenteeEntity> absenteeEntities) {
    this.absenteeEntities = absenteeEntities;
  }
}

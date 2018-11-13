package com.nloops.students.utils;

public class StudentReportModel {

  String studentName;
  String studentUID;
  String totalLectures;
  String absenteeCount;
  String absenteePercantge;

  public StudentReportModel(String studentName, String studentUID, String totalLectures,
      String absenteeCount, String absenteePercantge) {
    this.studentName = studentName;
    this.studentUID = studentUID;
    this.totalLectures = totalLectures;
    this.absenteeCount = absenteeCount;
    this.absenteePercantge = absenteePercantge;
  }

  public String getStudentName() {
    return studentName;
  }

  public String getStudentUID() {
    return studentUID;
  }

  public String getTotalLectures() {
    return totalLectures;
  }

  public String getAbsenteeCount() {
    return absenteeCount;
  }

  public String getAbsenteePercantge() {
    return absenteePercantge;
  }
}

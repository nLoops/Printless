package com.nloops.students.utils;

import java.io.Serializable;

public class SubjectModel implements Serializable {

  public final String subjectName;
  public final int subjectID;

  public SubjectModel(String subjectName, int subjectID) {
    this.subjectName = subjectName;
    this.subjectID = subjectID;
  }
}

package com.nloops.students.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

/**
 * This interface will holds {@link SubjectEntity} Database operations like query, insert, update,
 * delete data.
 */

@Dao
public interface SubjectDAO {

  @Query("SELECT * from subjects WHERE userUID = :userUID")
  List<SubjectEntity> loadAllSubjects(String userUID);

  @Query("SELECT * from subjects WHERE subjectID = :id")
  SubjectEntity loadSingleSubject(int id);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertSubject(SubjectEntity subjectEntity);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateSubject(SubjectEntity subjectEntity);

  @Delete
  void deleteSubject(SubjectEntity subjectEntity);

  // CRUD for Class Entity
  @Query("SELECT * from class_table WHERE userUID = :userUID")
  List<ClassEntity> loadAllClassesTable(String userUID);

  @Query("SELECT * from class_table WHERE foreignSubjectID = :subjectID AND userUID = :userUID")
  List<ClassEntity> loadAllClasses(int subjectID, String userUID);

  @Query("SELECT * from class_table WHERE classID = :id")
  ClassEntity loadSingleClass(int id);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertClass(ClassEntity classEntity);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateClass(ClassEntity classEntity);

  @Delete
  void deleteClass(ClassEntity classEntity);

  // CRUD for Student Entity
  @Query("SELECT * from students WHERE userUID = :userUID")
  List<StudentEntity> loadAllStudentsTable(String userUID);

  @Query("SELECT * from students WHERE foreignClassID = :classID AND userUID = :userUID")
  List<StudentEntity> loadAllStudents(int classID, String userUID);

  @Query("SELECT * from students WHERE foreignSubjectID = :subjectID AND userUID = :userUID")
  List<StudentEntity> loadAllStudentsBySubject(int subjectID, String userUID);

  @Query("SELECT * from students WHERE studentID = :id")
  StudentEntity loadSingleStudent(int id);

  @Query("SELECT * from students WHERE studentUniID LIKE :searchKeyword")
  List<StudentEntity> checkStudentUID(String searchKeyword);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertStudent(StudentEntity studentEntity);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateStudent(StudentEntity studentEntity);

  @Query("UPDATE students SET studentName =:newName WHERE studentID= :stuID")
  int updateStudentName(String newName, int stuID);

  @Delete
  void deleteStudent(StudentEntity studentEntity);

  // CRUD for Absentee Entity
  @Query("SELECT * from absentees WHERE userUID = :userUID")
  List<AbsenteeEntity> loadAllAbsentee(String userUID);

  @Query("SELECT * from absentees WHERE foreignAttSubjectID = :subjectID AND userUID = :userUID")
  List<AbsenteeEntity> loadAllAbsenteeBySubject(int subjectID, String userUID);

  @Query("SELECT * from absentees WHERE foreignAttClassID = :classID AND userUID = :userUID")
  List<AbsenteeEntity> loadAllAbsenteeByClass(int classID, String userUID);

  @Query("SELECT * from absentees WHERE absenteeID = :id AND foreignAttClassID = :classID AND userUID = :userUID")
  AbsenteeEntity loadSingleAbsentee(int id, int classID, String userUID);

  @Query("SELECT * from absentees WHERE absenteeDate = :dateValue AND foreignAttClassID = :classID AND userUID = :userUID ")
  AbsenteeEntity getAbsenteeByDate(long dateValue, int classID, String userUID);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAbsentee(AbsenteeEntity absenteeEntity);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateAbsentee(AbsenteeEntity absenteeEntity);

  @Delete
  void deleteAbsentee(AbsenteeEntity absenteeEntity);

  @Query("SELECT * FROM absentees INNER JOIN students_join "
      + "ON absentees.absenteeID = students_join.absentee_id "
      + "WHERE students_join.student_id = :studentID")
  List<AbsenteeEntity> getAbsenteeForStudent(final int studentID);


}

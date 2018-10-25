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

  @Query("SELECT * from subjects")
  List<SubjectEntity> loadAllSubjects();

  @Query("SELECT * from subjects WHERE subjectID = :id")
  SubjectEntity loadSingleSubject(int id);

  @Insert
  void insertSubject(SubjectEntity subjectEntity);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateSubject(SubjectEntity subjectEntity);

  @Delete
  void deleteSubject(SubjectEntity subjectEntity);

  // CRUD for Class Entity
  @Query("SELECT * from class_table WHERE foreignSubjectID = :subjectID")
  List<ClassEntity> loadAllClasses(int subjectID);

  @Query("SELECT * from class_table WHERE classID = :id")
  ClassEntity loadSingleClass(int id);

  @Insert
  void insertClass(ClassEntity classEntity);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateClass(ClassEntity classEntity);

  @Delete
  void deleteClass(ClassEntity classEntity);

  // CRUD for Student Entity
  @Query("SELECT * from students WHERE foreignClassID = :classID")
  List<StudentEntity> loadAllStudents(int classID);

  @Query("SELECT * from students WHERE studentID = :id")
  StudentEntity loadSingleStudent(int id);

  @Insert
  void insertStudent(StudentEntity studentEntity);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateStudent(StudentEntity studentEntity);

  @Delete
  void deleteStudent(StudentEntity studentEntity);

  // CRUD for Absentee Entity
  @Query("SELECT * from absentees")
  List<AbsenteeEntity> loadAllAbsentee();

  @Query("SELECT * from absentees WHERE absenteeID = :id")
  AbsenteeEntity loadSingleAbsentee(int id);

  @Insert
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

package com.nloops.students.attendance;

import com.nloops.students.data.mvp.BasePresenter;
import com.nloops.students.data.mvp.BaseView;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.StudentEntity;
import java.util.List;

public interface AttendanceContract {

  interface View extends BaseView<Presenter> {

    void showStudentsList(List<StudentEntity> data);

    void showAbsenteeList(AbsenteeEntity data);

    void showAllAbsentee(List<AbsenteeEntity> entities);

    void clearAdapter();

  }


  interface Presenter extends BasePresenter {

    void loadStudents();

    void insertAttendance(AbsenteeEntity entity);

    void updateAttendance(AbsenteeEntity entity);

    void loadAbsenteeByDate(long dateValue, int classID);

    void loadAllAbsentee(int classID);

  }

}

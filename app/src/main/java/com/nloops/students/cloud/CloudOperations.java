package com.nloops.students.cloud;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nloops.students.data.AppDatabase;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.utils.UtilsConstants;
import java.util.List;
import java.util.Objects;

public class CloudOperations {

  private static CloudOperations instance = null;
  private static AppDatabase mDB;

  private CloudOperations() {
  }

  public static CloudOperations getInstance(Context context, FragmentManager manager) {
    if (instance == null) {
      instance = new CloudOperations();
      mDB = AppDatabase.getInstance(context);
    }
    return instance;
  }

  private void syncDataWithServer(CloudModel model) {
    FirebaseDatabase.getInstance().getReference()
        .child(UtilsConstants.ATTENDANCE_DATABASE_REFERENCE)
        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
        .setValue(model);
  }

  class DataLoader extends AsyncTask<Void, Void, CloudModel> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected CloudModel doInBackground(Void... voids) {
      List<SubjectEntity> subjectEntities = mDB.subjectDAO().loadAllSubjects();
      List<AbsenteeEntity> absenteeEntities = mDB.subjectDAO().loadAllAbsentee();
      List<ClassEntity> classEntities = mDB.subjectDAO().loadAllClassesTable();
      List<StudentEntity> studentEntities = mDB.subjectDAO().loadAllStudentsTable();
      return new CloudModel(subjectEntities, classEntities, studentEntities, absenteeEntities);
    }

    @Override
    protected void onPostExecute(CloudModel cloudModel) {
      super.onPostExecute(cloudModel);
      syncDataWithServer(cloudModel);
    }
  }

  public synchronized void syncData() {
    new DataLoader().execute();
  }

}

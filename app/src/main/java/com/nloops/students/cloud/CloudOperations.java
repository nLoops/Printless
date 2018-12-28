package com.nloops.students.cloud;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nloops.students.R;
import com.nloops.students.data.AppDatabase;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.utils.UtilsConstants;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CloudOperations {

  private static CloudOperations instance = null;
  private static AppDatabase mDB;

  private static final String TAG = CloudOperations.class.getSimpleName();
  private static final int TWELVE_HOURS = 12;
  private static final int DAY_HOURS = 24;
  private static final int WEEK_HOURS = 168;
  private static final int FLEXTIME_MULTIPLIER = 3;
  private static final String TASKS_TAG = "sync-tasks";
  private static int SYNC_INTERVAL_HOURS;
  private static int SYNC_INTERVAL_SECONDS;
  private static int SYNC_FLEXTIME_SECONDS;
  private static Context mContext;

  private CloudOperations() {
  }

  public static CloudOperations getInstance(Context context) {
    if (instance == null) {
      instance = new CloudOperations();
      mDB = AppDatabase.getInstance(context);
      mContext = context;
    }
    return instance;
  }

  public void syncDataWithServer() {
    try {
      assert FirebaseAuth.getInstance().getCurrentUser() != null;
      FirebaseDatabase.getInstance().getReference()
          .child(UtilsConstants.ATTENDANCE_DATABASE_REFERENCE)
          .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
          .setValue(getDataModel());
    } catch (Exception e) {
      Log.d(TAG, "Cannot push data to the server \n" + e.getMessage());
    }

  }

  private static CloudModel getDataModel() {
    List<SubjectEntity> subjectEntities = mDB.subjectDAO().loadAllSubjects();
    List<AbsenteeEntity> absenteeEntities = mDB.subjectDAO().loadAllAbsentee();
    List<ClassEntity> classEntities = mDB.subjectDAO().loadAllClassesTable();
    List<StudentEntity> studentEntities = mDB.subjectDAO().loadAllStudentsTable();
    return new CloudModel(subjectEntities, classEntities, studentEntities, absenteeEntities);
  }

  /**
   * Helper Method to get SyncTime Preference that chosen By user.
   *
   * @param context Passed {@link Context}
   */
  private static void getSyncTime(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    String syncTime = preferences.getString(context.getString(R.string.settings_sync_time_key),
        context.getString(R.string.settings_sync_time_default));
    if (syncTime.equals(context.getString(R.string.settings_sync_twelve_key))) {
      SYNC_INTERVAL_HOURS = TWELVE_HOURS;
    } else if (syncTime.equals(context.getString(R.string.settings_sync_day_key))) {
      SYNC_INTERVAL_HOURS = DAY_HOURS;
    } else if (syncTime.equals(context.getString(R.string.settings_sync_week_key))) {
      SYNC_INTERVAL_HOURS = WEEK_HOURS;
    }
    // Convert Hours to Seconds
    SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    // Set FlexTime to make a frame to fire Job within
    SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / FLEXTIME_MULTIPLIER;
  }

  synchronized public void initialize() {
    scheduleFirebaseJobDispatcherSync();
  }

  private static void scheduleFirebaseJobDispatcherSync() {

    getSyncTime(mContext);

    Driver driver = new GooglePlayDriver(mContext);
    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

    Job tasksJob = dispatcher.newJobBuilder()
        /* The Service that will be used to sync Tasks's data */
        .setService(TasksFirebaseJobService.class)
        /* Set the UNIQUE tag used to identify this Job */
        .setTag(TASKS_TAG)
        /*
         * Network constraints on which this Job should run. We choose to run on any
         * network, but you can also choose to run only on un-metered networks or when the
         * device is charging. It might be a good idea to include a preference for this,
         * as some users may not want to download any data on their mobile plan. ($$$)
         */
        .setConstraints(Constraint.ON_ANY_NETWORK)
        /*
         * setLifetime sets how long this job should persist. The options are to keep the
         * Job "forever" or to have it die the next time the device boots up.
         */
        .setLifetime(Lifetime.FOREVER)
        .setRecurring(true)
        /*
         * When should our job start we add a frame with start and end time and the
         * job will be run within
         */
        .setTrigger(Trigger.executionWindow(
            SYNC_INTERVAL_SECONDS,
            SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
        /*
         * If a Job with the tag with provided already exists, this new job will replace
         * the old one.
         */
        .setReplaceCurrent(true)
        /* Once the Job is ready, call the builder's build method to return the Job */
        .build();
    /* Schedule the Job with the dispatcher */
    dispatcher.schedule(tasksJob);
  }

}

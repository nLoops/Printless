package com.nloops.students.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.nloops.students.classesdata.ClassesActivity;
import com.nloops.students.data.mvp.StructureDataSource.LoadSubjectsCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.utils.SharedPreferenceHelper;
import com.nloops.students.utils.SubjectModel;
import com.nloops.students.utils.UtilsConstants;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import timber.log.Timber;

/**
 * This Receiver will handle Broadcast of Scheduled Alarms, even after device restart.
 */
public class AlarmReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    Timber.tag("Alarm");
    Timber.d("Alarm Receiver called");
    if (intent.getAction() != null) {
      if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
        LocalDataSource.getInstance(context).getSubjects(new LoadSubjectsCallBack() {
          @Override
          public void onSubjectsLoaded(List<SubjectEntity> subjects) {
            Calendar calendar = Calendar.getInstance();
            for (SubjectEntity entity : subjects) {
              ArrayList<Long> attendance = SharedPreferenceHelper.getInstance(context)
                  .readScheduled(String.valueOf(entity.getSubjectID()));
              for (Long date : attendance) {
                calendar.setTimeInMillis(date);
                SubjectModel model = new SubjectModel(entity.getSubjectName(),
                    entity.getSubjectID());
                AlarmScheduler.scheduleAlarm(calendar.get(Calendar.DAY_OF_WEEK),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    context, model);
              }
            }
          }

          @Override
          public void onSubjectDataNotAvailable() {
            Timber.d("Cannot load Subject Data");
          }
        });
      }
    } else {
      String subjectName = intent.getStringExtra(UtilsConstants.EXTRA_SUBJECT_NAME_TO_CLASS);
      int subjectID = intent.getIntExtra(UtilsConstants.EXTRA_SUBJECT_ID_TO_CLASSES, -1);
      Intent classIntent = new Intent(context, ClassesActivity.class);
      classIntent.putExtra(UtilsConstants.EXTRA_SUBJECT_NAME_TO_CLASS, subjectName);
      classIntent.putExtra(UtilsConstants.EXTRA_SUBJECT_ID_TO_CLASSES, subjectID);

      AlarmScheduler.notifyUser(context, classIntent);
    }
  }
}

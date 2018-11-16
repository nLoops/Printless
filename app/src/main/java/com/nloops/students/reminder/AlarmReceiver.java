package com.nloops.students.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.nloops.students.classesdata.ClassesActivity;
import com.nloops.students.utils.UtilsConstants;

/**
 * This Receiver will handle Broadcast of Scheduled Alarms, even after device restart.
 */
public class AlarmReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    if (intent.getAction() != null) {
      if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
        // TODO: 16/11/2018 Solve on Restart scheduled alarms
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

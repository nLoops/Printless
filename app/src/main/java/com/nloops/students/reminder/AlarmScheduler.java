package com.nloops.students.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import com.nloops.students.R;
import com.nloops.students.utils.SubjectModel;
import com.nloops.students.utils.UtilsConstants;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import timber.log.Timber;

public class AlarmScheduler {

  // Notification Channel ID
  private static final String NOTIFICATION_CHANNEL_ID = "main_channel";

  private AlarmScheduler() {
    // private constructor to prevent init.
  }

  public static void scheduleAlarm(int dayOfWeek, int hourOfDay, Context context,
      SubjectModel model) {

    // Enable a receiver
    ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
    PackageManager pm = context.getPackageManager();
    pm.setComponentEnabledSetting(receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP);

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    // Accept the change here at this line to avoid skipping of current week.
    if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
      calendar.add(Calendar.DAY_OF_YEAR, new GregorianCalendar().get(Calendar.DAY_OF_WEEK) - 1);
    }

    // Need to add SubjectID , SubjectName.
    Intent classIntent = new Intent(context, AlarmReceiver.class);
    classIntent.putExtra(UtilsConstants.EXTRA_SUBJECT_NAME_TO_CLASS, model.subjectName);
    classIntent.putExtra(UtilsConstants.EXTRA_SUBJECT_ID_TO_CLASSES, model.subjectID);

    // create unique request ID
    int requestID = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);

    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
        requestID, classIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    // get AlarmManager
    AlarmManager alarmManager = AlarmManagerProvider.getAlarmManager(context);
    // Cancel previous Alarms
    alarmManager.cancel(pendingIntent);
    // log calendar time
    Timber.tag("Alarm");
    Timber.d("The Day User choose is %s", dayOfWeek);
    Timber.d("The Hour that user choose is %s", hourOfDay);
    Timber.d("Scheduled time is %s", calendar.getTimeInMillis());
    Timber.d("Pending Intent req code %s", requestID);
    // schedule new alarm
    alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis()
        /*System.currentTimeMillis()*/,
        AlarmManager.INTERVAL_DAY * 7, pendingIntent);
  }

  public static void notifyUser(Context context, Intent intent) {

    PendingIntent operation = TaskStackBuilder.create(context)
        .addNextIntentWithParentStack(intent)
        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    // define Notification Manager
    NotificationManager manager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);

    // Ref of Notification Channel
    NotificationChannel mChannel;

    // create unique notification ID
    int notificationID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    // add the id so we can cancel from activity
    intent.putExtra(UtilsConstants.NOTIFICATION_ID, notificationID);
    // create pending intent to listen to action.
    PendingIntent actionIntent = PendingIntent.getActivity(context, 1, intent,
        PendingIntent.FLAG_UPDATE_CURRENT);

    // Create Notification Channel this only for OS O and further.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence cName = context.getString(R.string.notification_channel_name);
      String cDescription = context.getString(R.string.notification_channel_desc);
      int importance = NotificationManager.IMPORTANCE_HIGH;
      mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, cName, importance);
      mChannel.setDescription(cDescription);
      mChannel.setShowBadge(true);
      assert manager != null;
      manager.createNotificationChannel(mChannel);

      long[] pattern = {500, 500, 500, 500};
      // Build a Notification.
      NotificationCompat.Builder note = new NotificationCompat.Builder(context)
          .setColor(ContextCompat.getColor(context, R.color.colorAccent))
          .setContentTitle(context.getString(R.string.notification_title))
          .setContentText(intent.getStringExtra(UtilsConstants.EXTRA_SUBJECT_NAME_TO_CLASS))
          .setSmallIcon(R.drawable.ic_empty_calender_small)
          .setContentIntent(operation)
          .setChannelId(NOTIFICATION_CHANNEL_ID)
          .addAction(R.drawable.ic_done_white, context.getString(R.string.notify_action_take),
              actionIntent)
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
          .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
          .setVibrate(pattern)
          .setAutoCancel(true);

      manager.notify(notificationID, note.build());


    }

  }


}

package com.nloops.students.cloud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

public class TasksFirebaseJobService extends JobService {

  private AsyncTask<Void, Void, Void> mStudentsData;

  /**
   * The entry point to your Job. Implementations should offload work to another thread of execution
   * as soon as possible. This is called by the Job Dispatcher to tell us we should start our job.
   * Keep in mind this method is run on the application's main thread, so we need to offload work to
   * a background thread.
   */
  @SuppressLint("StaticFieldLeak")
  @Override
  public boolean onStartJob(final JobParameters job) {
    mStudentsData = new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... voids) {
        Context context = getApplicationContext();
        CloudOperations.getInstance(context).syncDataWithServer();
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        jobFinished(job, false);
      }
    };
    mStudentsData.execute();
    return true;
  }


  /**
   * Called when the scheduling engine has decided to interrupt the execution of a running job, most
   * likely because the runtime constraints associated with the job are no longer satisfied.
   *
   * @return whether the job should be retried
   * @see Job.Builder#setRetryStrategy(RetryStrategy)
   * @see RetryStrategy
   */
  @Override
  public boolean onStopJob(JobParameters job) {
    if (mStudentsData != null) {
      mStudentsData.cancel(true);
    }
    return true;
  }
}

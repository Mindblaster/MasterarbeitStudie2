package ma.mimuc.com.masterarbeitstudie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

import ma.mimuc.com.masterarbeitstudie.Activities.PollActivity;
import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;

/**
 * Created by Raphael on 05.02.2016.
 */
public class NotificationHandler {

    private final long MAX_TIME_SINCE_LAST_RESTINGSTATE=1800000; //30mins in ms
    private final long MAX_TIME_SINCE_LAST_ANSWERED_POLL=7200000; //2h in ms
    private final long MAX_TIME_SINCE_LAST_DELETED_NOTIFICATION=3600000; //1h in ms
    private final long MAX_TIME_SINCE_LAST_POSTED_NOTIFICATION=2700000; //45mins in ms


    private Context context;
    private NotificationManager notificationManager;
    private static final String NOTIFICATION_DELETED_ACTION = "NOTIFICATION_DELETED";
    private SharedPreferencesManager sharedPreferencesManager;
    private DatabaseHandler databaseHandler;
    private Timestamp timestamp;

    public NotificationHandler(Context context){
        this.context=context;
        notificationManager = (NotificationManager)
                context.getSystemService(context.NOTIFICATION_SERVICE);
        sharedPreferencesManager=new SharedPreferencesManager(context);
        databaseHandler = new DatabaseHandler(context);
        timestamp=new Timestamp();
    }

    public void askUserToAnswerQuestion(){

        System.out.println("method called");
        // prepare intent which is triggered if the

        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Notification was deleted");
                sharedPreferencesManager.saveLastDeletedNotificationTime(System.currentTimeMillis());
                //Handle time based algorithm

            }
        };
// notification is selected

        Intent intent = new Intent(context, PollActivity.class);

        intent.putExtra("RestingStateTime",sharedPreferencesManager.getLastRestingStateTime());
// use System.currentTimeMillis() to have a unique ID for the pending intent

        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        Intent intent2 = new Intent(NOTIFICATION_DELETED_ACTION);
        PendingIntent pendintIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);
        context.registerReceiver(receiver, new IntentFilter(NOTIFICATION_DELETED_ACTION));

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Please Answer A Question for the User Study")
                .setContentTitle("User Study LMU")
                .setContentIntent(pIntent)
                .setDeleteIntent(pendintIntent)
                .setAutoCancel(true).build();
        notificationManager.notify(0, n);
        new WriteUserStudyPerformanceToDatabase().execute();
        sharedPreferencesManager.saveLastNotificationPostedTime(System.currentTimeMillis());
    }

    public boolean postNotification(){
        //If last Resting state was within last half hour
        if(System.currentTimeMillis()-sharedPreferencesManager.getLastRestingStateTime()<MAX_TIME_SINCE_LAST_RESTINGSTATE){
            //If last Answered Poll was more than two hours ago
            if(System.currentTimeMillis()-sharedPreferencesManager.getLastAnsweredPollTime()>MAX_TIME_SINCE_LAST_ANSWERED_POLL){
                //User Deleted last notification more than one hour ago
                if(System.currentTimeMillis()-sharedPreferencesManager.getLastDeletedNotificationTime()>MAX_TIME_SINCE_LAST_DELETED_NOTIFICATION){
                    //Last Posted Notification was longer than 45 minutes ago
                    if(System.currentTimeMillis()-sharedPreferencesManager.getLastNotificationPostedTime()>MAX_TIME_SINCE_LAST_POSTED_NOTIFICATION){
                        return true;
                    }
                }

            }

        }
        return false;
    }
    private class WriteUserStudyPerformanceToDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void ... params) {
            SensorMeasurement sensorMeasurement=new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
            sensorMeasurement.setSensor("USER_STUDY_PERFORMANCE");
            sensorMeasurement.setTag("NOTIFICATION POSTED");
            databaseHandler.insertUserStudyPerformanceIntoDatabase(sensorMeasurement);
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {

        }
    }


}

package ma.mimuc.com.masterarbeitstudie.OtherLoggers;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.SharedPreferencesManager;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;


public class NotificationLoggerService extends NotificationListenerService {

    private Context context;
    private DatabaseHandler databaseHandler;
    private SharedPreferencesManager sharedPreferencesManager;

    private TimedQueue timedQueue;
    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;
    private long savingTime;
    private final long ONE_DAY_IN_MILLISECONDS=86400000;



    @Override

    public void onCreate() {
        super.onCreate();
        databaseHandler= new DatabaseHandler(getApplicationContext());
        sharedPreferencesManager= new SharedPreferencesManager(getApplicationContext());
        context = getApplicationContext();
        Log.d("NotLoggerService", "Service started");
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
        savingTime=System.currentTimeMillis();
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        currentMeasurement = new SensorMeasurement(1, sbn.getId(), 0, System.currentTimeMillis(), timestamp.getCurrentTimeStamp());
        currentMeasurement.setSensor("NOTIFICATION_LOGGER");
        currentMeasurement.setTag("POSTED");
        currentMeasurement.setPackageName(sbn.getPackageName());
        currentMeasurement.setNotificationID(sbn.getKey());
        timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        currentMeasurement = new SensorMeasurement(0, sbn.getId(), 0, System.currentTimeMillis(), timestamp.getCurrentTimeStamp());
        currentMeasurement.setSensor("NOTIFICATION_LOGGER");
        currentMeasurement.setTag("REMOVED");
        currentMeasurement.setPackageName(sbn.getPackageName());
        currentMeasurement.setNotificationID(sbn.getKey());
        if(sbn.getPackageName().equals("ma.mimuc.com.masterarbeitstudie")){
            new WriteUserStudyPerformanceToDatabase().execute(currentMeasurement);
        }
        timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
        //Once per Day save Data to DB
        if(System.currentTimeMillis()-savingTime > ONE_DAY_IN_MILLISECONDS/12){
            new WriteNotificationDataToDatabase().execute();
            savingTime=System.currentTimeMillis();
        }


    }

    private class WriteNotificationDataToDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void ... params) {
            databaseHandler.addMeasurementsToDB(timedQueue.getSensorMeasurements(),"notifications");
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {
            timedQueue.emptyTimedQueue();
        }


    }

    private class WriteUserStudyPerformanceToDatabase extends AsyncTask<SensorMeasurement, Void, String> {
        @Override
        protected String doInBackground(SensorMeasurement ... params) {
            SensorMeasurement sensorMeasurement=new SensorMeasurement(params[0].getxValue(),params[0].getyValue(),params[0].getzValue(),params[0].getTimestampSysTime(),params[0].getTimeStampFormatted());
            sensorMeasurement.setSensor("USER_STUDY_PERFORMANCE");
            sensorMeasurement.setTag("NOTIFICATION DELETED");
            databaseHandler.insertUserStudyPerformanceIntoDatabase(sensorMeasurement);
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {

        }


    }


}

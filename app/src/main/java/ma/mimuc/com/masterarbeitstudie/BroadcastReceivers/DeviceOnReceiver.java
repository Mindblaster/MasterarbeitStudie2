package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.LoggingService;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.OtherLoggers.NotificationLoggerService;
import ma.mimuc.com.masterarbeitstudie.SystemValues;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 04.02.2016.
 */
public class DeviceOnReceiver extends BroadcastReceiver {


    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;
    private SystemValues systemValues;
    private DatabaseHandler databaseHandler;



    @Override
    public void onReceive(Context context, Intent intent) {

        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
        systemValues=new SystemValues(context);
        databaseHandler= new DatabaseHandler(context);
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            AlarmManager scheduler = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent3 = new Intent(context.getApplicationContext(), LoggingService.class);
            PendingIntent scheduledIntent = PendingIntent.getService(context.getApplicationContext(), 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, scheduledIntent);

            Intent intent2 = new Intent(context.getApplicationContext(), NotificationLoggerService.class);
            PendingIntent scheduledIntent2 = PendingIntent.getService(context.getApplicationContext(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, scheduledIntent2);


            currentMeasurement= new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
            currentMeasurement.setSensor("DEVICE_ON_OFF");
            currentMeasurement.setTag("DEVICE_ON");
            timedQueue.addToSensorMeasurements(currentMeasurement, 0);
            System.out.println("DEVICE_ON");
            new WriteDeviceOffToDatabase().execute();
        }
    }

    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }


    private class WriteDeviceOffToDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void ... params) {
            databaseHandler.addMeasurementsToDB(timedQueue.getSensorMeasurements(),"device_on_off");
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {
            timedQueue.emptyTimedQueue();
        }


    }
}

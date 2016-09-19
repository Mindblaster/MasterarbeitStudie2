package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.NotificationHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 29.10.2015.
 */
public class ScreenTimeLogger {
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

    private Context context;
    private double screentimeInMs;
    private double screenOnTime;
    private double screenOffTime;
    private double userPresentTime;

    private boolean screenIsOn=false;
    private NotificationHandler notificationHandler;

    private DatabaseHandler databaseHandler;

    public ScreenTimeLogger(Context context) {
        this.context = context;
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
        notificationHandler= new NotificationHandler(context);
        databaseHandler= new DatabaseHandler(context);

        BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                screenOnTime = System.currentTimeMillis();
                currentMeasurement= new SensorMeasurement((long)screenOnTime,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("SCREEN_ON_OFF");
                currentMeasurement.setTag("SCREEN_ON");
                timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                screenIsOn = true;
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(screenOnReceiver, filter);

        BroadcastReceiver userPresentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                userPresentTime =System.currentTimeMillis();

                //PostNotification if Sensible
                if(notificationHandler.postNotification()){
                    notificationHandler.askUserToAnswerQuestion();

                }
                if(screenIsOn){
                    screentimeInMs=userPresentTime- screenOnTime;
                    Log.d("ScreenTimeLogger", (screentimeInMs/1000)+ "ms");
                }
                else{
                    screentimeInMs=-1;  //DefaultValue if Screen Off Event is Detected without a Screen On Event
                }
                currentMeasurement= new SensorMeasurement((long)userPresentTime,(long)screentimeInMs,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("SCREEN_ON_OFF");
                currentMeasurement.setTag("USER_PRESENT");
                timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
            }
        };
        IntentFilter filter3 = new IntentFilter(Intent.ACTION_USER_PRESENT);
        context.registerReceiver(userPresentReceiver, filter3);


        BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                screenOffTime=System.currentTimeMillis();

                Log.d("ScreenTimeLogger", "Screen off: "+ screenOffTime);
                if(screenIsOn){
                    screentimeInMs=screenOffTime- screenOnTime;
                    Log.d("ScreenTimeLogger", (screentimeInMs/1000)+ "ms");
                }
                else{
                    screentimeInMs=-1;  //DefaultValue if Screen Off Event is Detected without a Screen On Event
                }
                currentMeasurement= new SensorMeasurement((long)screenOffTime,(long)screentimeInMs,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("SCREEN_ON_OFF");
                currentMeasurement.setTag("SCREEN_OFF");
                timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                screenIsOn=false;
            }
        };
        IntentFilter filter2 = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(screenOffReceiver, filter2);
    }

    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }


}

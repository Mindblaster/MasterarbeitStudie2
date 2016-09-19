package ma.mimuc.com.masterarbeitstudie.deprecated;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 11.11.2015.
 */
public class ApplicationLogger {
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms



    private Context context;
    private ActivityManager am;
    Timer timer=null;

    public ApplicationLogger(Context context) {
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();

        this.context = context;
        am= (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(final Context context, Intent intent) {
                timer= new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
                        currentMeasurement= new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                        currentMeasurement.setPackageName(packageName);
                        currentMeasurement.setSensor("APPLICATION_LOGGER");
                        currentMeasurement.setTag("PRE_LOLLIPOP");
                        timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);



                        Log.d("Timertask", "App Running: " + packageName);
                    }
                }, 5000, 5000);//put here time 1000 milliseconds=1 second

            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(screenOnReceiver, filter);


        BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(timer!=null) {
                    timer.cancel();
                    timer.purge();
                    timer=null;
                }
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

package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 26.11.2015.
 */
public class PluggedInOutLogger {

    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

    public PluggedInOutLogger(Context context) {
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();

        BroadcastReceiver powerConnectedReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                currentMeasurement= new SensorMeasurement(1,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("POWER_CONNECTION");

                int plugged=intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                if(plugged==BatteryManager.BATTERY_PLUGGED_USB){
                    currentMeasurement.setTag("POWER_CONNECTED_USB");
                }
                else if(plugged==BatteryManager.BATTERY_PLUGGED_AC){
                    currentMeasurement.setTag("POWER_CONNECTED_AC");
                }
                else{
                    currentMeasurement.setTag("POWER_NOT_RECOGNIZED");
                }

                timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        context.registerReceiver(powerConnectedReceiver, filter);

        BroadcastReceiver powerDisconnectedReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                currentMeasurement= new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("POWER_CONNECTION");
                currentMeasurement.setTag("POWER_DISCONNECTED");
                timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);

            }
        };
        IntentFilter filter2 = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        context.registerReceiver(powerDisconnectedReceiver, filter2);


    }

    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }
}

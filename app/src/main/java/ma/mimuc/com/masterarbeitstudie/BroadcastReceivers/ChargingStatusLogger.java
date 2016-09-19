package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 29.10.2015.
 */
public class ChargingStatusLogger {

    private Context context;
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms


    public ChargingStatusLogger(Context context) {
        this.context = context;
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            int scale = -1;
            int level = -1;
            int voltage = -1;
            int temp = -1;

            @Override
            public void onReceive(Context context, Intent intent) {
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                //Log.e("BatteryManager", "level is " + level + "/" + scale + ", temp is " + temp + ", voltage is " + voltage);
                currentMeasurement= new SensorMeasurement(level,scale,temp,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("BATTERY_STATUS");
                currentMeasurement.setTag("");
                timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(batteryReceiver, filter);
    }
    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }


}



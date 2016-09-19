package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.SystemValues;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 11.11.2015.
 */
public class BluetoothOnOffLogger {
    BluetoothAdapter btAdapter;
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;
    private SystemValues systemValues;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

    public BluetoothOnOffLogger(Context context){
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
        btAdapter=BluetoothAdapter.getDefaultAdapter();
        systemValues=new SystemValues(context);

        BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {


                if (btAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    currentMeasurement= new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                    currentMeasurement.setSensor("BLUETOOTH_ON_OFF");
                    currentMeasurement.setTag("BLUETOOTH_OFF");
                    timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                }
                if (btAdapter.getState()== BluetoothAdapter.STATE_ON){
                    currentMeasurement= new SensorMeasurement(1,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                    currentMeasurement.setSensor("BLUETOOTH_ON_OFF");
                    currentMeasurement.setTag("BLUETOOTH_ON");
                    timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);;
                }

            }
        };
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(bluetoothReceiver, filter);

    }



    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }
};



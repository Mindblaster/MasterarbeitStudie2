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
public class WiFiOnOffLogger {
    WifiManager wifiManager;
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;
    private int wifiEnabled=-1;
    private SystemValues systemValues;


    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

    public WiFiOnOffLogger(Context context){
        wifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
        systemValues=new SystemValues(context);

        BroadcastReceiver wifiReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(wifiEnabled==-1){
                    if(systemValues.wifiEnabled()){
                        wifiEnabled=1;
                    }
                    if(!systemValues.wifiEnabled()){
                        wifiEnabled=0;
                    }
                }
                else {
                    if (systemValues.wifiEnabled()) {
                        if (wifiEnabled == 0) {
                            currentMeasurement = new SensorMeasurement(1, 0, 0, System.currentTimeMillis(), timestamp.getCurrentTimeStamp());
                            currentMeasurement.setSensor("WIFI_ON_OFF");
                            currentMeasurement.setTag("WIFI_ENABLED");
                            timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                            wifiEnabled = 1;
                            System.out.println("WIFI_ENABLED");
                        }
                    }
                    if (!systemValues.wifiEnabled()) {
                        if (wifiEnabled == 1) {
                            currentMeasurement = new SensorMeasurement(0, 0, 0, System.currentTimeMillis(), timestamp.getCurrentTimeStamp());
                            currentMeasurement.setSensor("WIFI_ON_OFF");
                            currentMeasurement.setTag("WIFI_DISABLED");
                            timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                            wifiEnabled = 0;
                            System.out.println("WIFI_DISABLED");
                        }
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(wifiReceiver, filter);

    }
    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }
}

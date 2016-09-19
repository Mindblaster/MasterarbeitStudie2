package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 19.11.2015.
 */
public class AudioDevicePlugLogger {        //Log Outgoing Phonecalls
    private boolean headsetConnected = false;
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

    public AudioDevicePlugLogger(Context context) {

        timedQueue= new TimedQueue();
        timestamp=new Timestamp();

        BroadcastReceiver audioDeviceplugListener = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra("state")) {
                    if (headsetConnected && intent.getIntExtra("state", 0) == 0) {
                        headsetConnected = false;
                        currentMeasurement= new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                        currentMeasurement.setSensor("AUDIO_PLUG_IN_OUT");
                        currentMeasurement.setTag("DISCONNECTED");
                        timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);
                    } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1) {
                        headsetConnected = true;
                        currentMeasurement= new SensorMeasurement(1,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                        currentMeasurement.setSensor("AUDIO_PLUG_IN_OUT");
                        currentMeasurement.setTag("CONNECTED");
                        timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        context.registerReceiver(audioDeviceplugListener, filter);
    }

    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }
}

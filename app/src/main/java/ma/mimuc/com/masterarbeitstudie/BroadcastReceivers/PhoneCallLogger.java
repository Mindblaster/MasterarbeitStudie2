package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.SystemValues;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;


/**
 * Created by Raphael on 19.11.2015.
 */
public class PhoneCallLogger {


    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

    private Context context;


    public PhoneCallLogger(Context context){
        this.context =context;
        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();


        //Log Outgoing Phonecalls
        BroadcastReceiver madePhoneCallReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                currentMeasurement= new SensorMeasurement(1,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("PHONE_CALLS");
                currentMeasurement.setTag("OUTGOING_CALL");
                timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                System.out.println("outgoing phone call registered");

            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        context.registerReceiver(madePhoneCallReceiver, filter);


        //Log Incoming Phonecalls
        PhoneStateListener phoneStateListener= new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber){
                if(state==1) {
                    currentMeasurement= new SensorMeasurement(1,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                    currentMeasurement.setSensor("PHONE_CALLS");
                    currentMeasurement.setTag("INCOMING_CALL");
                    timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                    System.out.println("incoming phone call registered");
                }
            }
        };
        tmgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }

}

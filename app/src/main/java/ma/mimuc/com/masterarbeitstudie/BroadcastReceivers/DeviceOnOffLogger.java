package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.SystemValues;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 04.02.2016.
 */
public class DeviceOnOffLogger {
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;
    private SystemValues systemValues;
    private DatabaseHandler databaseHandler;
    private OnDeviceOff onDeviceOff;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

    public DeviceOnOffLogger(Context context,Service service){
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
        systemValues=new SystemValues(context);
        databaseHandler= new DatabaseHandler(context);
        onDeviceOff=(OnDeviceOff) service ;

        final BroadcastReceiver deviceOnReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                currentMeasurement= new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("DEVICE_ON_OFF");
                currentMeasurement.setTag("DEVICE_ON");
                timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                System.out.println("DEVICE_ON");
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        context.registerReceiver(deviceOnReceiver, filter);

        final BroadcastReceiver deviceOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                currentMeasurement= new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setSensor("DEVICE_ON_OFF");
                currentMeasurement.setTag("DEVICE_OFF");
                timedQueue.addToSensorMeasurements(currentMeasurement, MEASUREMENT_INTERVALL);
                new WriteDeviceOffToDatabase().execute();
                System.out.println("DEVICE_OFF");
            }
        };
        IntentFilter filter2 = new IntentFilter(Intent.ACTION_SHUTDOWN);
        context.registerReceiver(deviceOffReceiver, filter2);

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
            onDeviceOff.saveDataOnDeviceOff();
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {
            timedQueue.emptyTimedQueue();
        }


    }

    public interface OnDeviceOff{
        void saveDataOnDeviceOff();
    }
}

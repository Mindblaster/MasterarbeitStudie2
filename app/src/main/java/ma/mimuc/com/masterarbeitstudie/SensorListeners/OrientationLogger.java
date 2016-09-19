package ma.mimuc.com.masterarbeitstudie.SensorListeners;

import android.content.Context;
import android.view.OrientationEventListener;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 10.11.2015.
 */
public class OrientationLogger extends OrientationEventListener {
    private final long MEASUREMENT_INTERVALL=20000; //time that values are saved in ms
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private final int CUSTOM_SENSOR_DELAY=500000;//0.5 seconds in microseconds



    public OrientationLogger(Context context, int rate) {
        super(context, rate);
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
    }

    @Override
    public void onOrientationChanged(int orientation) {
        //Save value
        currentMeasurement= new SensorMeasurement(orientation,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
        currentMeasurement.setSensor("ORIENTATION");
        currentMeasurement.setTag("");
        timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);
    }
    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }


}

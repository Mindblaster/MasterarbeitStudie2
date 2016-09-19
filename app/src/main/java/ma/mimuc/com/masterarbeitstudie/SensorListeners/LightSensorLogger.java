package ma.mimuc.com.masterarbeitstudie.SensorListeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;

/**
 * Created by Raphael on 10.11.2015.
 */
public class LightSensorLogger implements SensorEventListener {
    private Context context;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorMeasurement currentMeasurement;
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private final long MEASUREMENT_INTERVALL=20000; //time that values are saved in ms


    public LightSensorLogger(Context context, int rate){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor==null){
            return;
        }
        timedQueue= new TimedQueue();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        timestamp=new Timestamp();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        //TODO Throttle Sensor Data
        currentMeasurement= new SensorMeasurement((long)event.values[0],0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
        currentMeasurement.setSensor("BRIGHTNESS");
        currentMeasurement.setTag("");
        timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);
        //System.out.println("Light Level: " + event.values[0] + " lx");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }
}

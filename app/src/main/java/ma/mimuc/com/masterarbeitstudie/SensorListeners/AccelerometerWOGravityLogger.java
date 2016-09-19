package ma.mimuc.com.masterarbeitstudie.SensorListeners;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 26.11.2015.
 */
public class AccelerometerWOGravityLogger implements SensorEventListener {
    private SensorManager sensorManager;
    private long lastUpdate;
    private Context context;
    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private final long MEASUREMENT_INTERVALL=20000; //time that values are saved in ms

    private final int CUSTOM_SENSOR_DELAY=500000;//0.5 seconds in microseconds



    public AccelerometerWOGravityLogger(Context context, Service service){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                CUSTOM_SENSOR_DELAY);
        this.context=context;
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            SensorEvent sensorEvent = event;

            float[] values = sensorEvent.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            currentMeasurement= new SensorMeasurement(x,y,z,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
            currentMeasurement.setSensor("ACCELEROMETER_WO_GRAVITY");
            currentMeasurement.setTag("");
            timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);
            //System.out.println("Linear ACC: " + x + ", " + y + ", " + z);

        }
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

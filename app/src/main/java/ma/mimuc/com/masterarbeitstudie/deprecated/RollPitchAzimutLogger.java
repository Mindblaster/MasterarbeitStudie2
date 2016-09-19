package ma.mimuc.com.masterarbeitstudie.deprecated;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Raphael on 29.10.2015.
 */
public class RollPitchAzimutLogger implements SensorEventListener {
    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    float[] mGravity;
    float[] mGeomagnetic;
    Float azimut,pitch,roll;
    float[] R;
    long timestamp;
    Context context;
    private final double TIMEINTERVALL_SENSOR_MEASURENT=1000000000.0; //1 second in nanoseconds
    private final double MOVEMENT_THRESHOLD=0.1;
    private long lastUpdate;
    private final long MEASUREMENT_INTERVALL=20000; //time that values are saved in ms


    private ArrayList<Double> loggingBufferAcc;
    public RollPitchAzimutLogger(Context context){
        this.context=context;
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        azimut=0.0f;
        roll=0.0f;
        pitch=0.0f;
        loggingBufferAcc=new ArrayList<Double>(100);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    private static final String DEBUG_TAG = "BaroLoggerService";

    private SensorManager sensorManager = null;
    private Sensor sensor = null;


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            R = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                new SensorEventLoggerTask().execute(event);
            }
        }

    }

    private class SensorEventLoggerTask extends
            AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent event = events[0];
            float orientation[] = new float[3];
            SensorManager.getOrientation(R, orientation);
            azimut = orientation[0]; // orientation contains: azimut, pitch and roll
            pitch = orientation[1];
            roll = orientation[2];
            long actualTime = event.timestamp;
            if (actualTime - lastUpdate < 200000) {
                return null;
            }
            //save the orientation
            System.out.println("Azimut: "+ azimut);
            return null;
        }
    }
}
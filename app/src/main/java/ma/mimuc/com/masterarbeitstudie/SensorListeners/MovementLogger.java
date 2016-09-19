package ma.mimuc.com.masterarbeitstudie.SensorListeners;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 29.10.2015.
 */
public class MovementLogger implements SensorEventListener {
    private SensorManager sensorManager;
    private long lastUpdate;
    private Context context;
    private boolean deviceIsResting=false;
    private long startNotMoving;
    private boolean deviceEnteredRestingState=false;

    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;

    private AsyncResponse asyncResponse;
    private final double TIMEINTERVALL_SENSOR_MEASURENT=1000000000.0; //1 second in nanoseconds
    private final double RESTING_STATE_TIME=10000000000.0; //10 Seconds


    private final int CUSTOM_SENSOR_DELAY=500000;//0.5 seconds in microseconds

    private final long MEASUREMENT_INTERVALL=20000; //time that values are saved in ms

    public MovementLogger(Context context, Service service){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                CUSTOM_SENSOR_DELAY);
        this.context=context;
        asyncResponse=(AsyncResponse) service ;
        startNotMoving=0;
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            SensorEvent sensorEvent = event;

            float[] values = sensorEvent.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            currentMeasurement= new SensorMeasurement(x,y,z,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
            currentMeasurement.setSensor("ACCELEROMETER");
            currentMeasurement.setTag("");
            timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);

            // Formula to see when device enters restingstate
            float accelationSquareRoot =(float) (Math.sqrt(x * x + y * y + z * z)
                    / (SensorManager.GRAVITY_EARTH));


            long actualTime = event.timestamp;

            if (accelationSquareRoot >= 1.25)
            {
                deviceIsResting=false;
                if(deviceEnteredRestingState){
                    System.out.println("Device Left Resting State");
                    System.out.println("Accleration value: "+accelationSquareRoot);
                    deviceEnteredRestingState=false;
                    asyncResponse.leftRestingState("");

                }
                startNotMoving=0;
                lastUpdate = actualTime;

            }
            else{
                if(deviceIsResting==false){
                    startNotMoving=event.timestamp;
                }

                if(event.timestamp - startNotMoving >= RESTING_STATE_TIME){
                    if(deviceEnteredRestingState==false) {
                        asyncResponse.enteredRestingState("Device entered resting state");
                        deviceEnteredRestingState=true;
                    }
                    //We entered a Resting state
                }
                deviceIsResting=true;

            }
            return ;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public interface AsyncResponse {
        void enteredRestingState(String output);
        void leftRestingState(String Tag);
    }
    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        timedQueue.filterSensorMeasurements(System.currentTimeMillis());
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }

}

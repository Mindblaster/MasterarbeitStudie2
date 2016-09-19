package ma.mimuc.com.masterarbeitstudie;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;

/**
 * Created by Raphael on 10.11.2015.
 */
public class TimedQueue {
    private ArrayList<SensorMeasurement> sensorMeasurements;
    private long savedIntervall;
    private long lastmeasurementAdded;

    public TimedQueue(){
        sensorMeasurements= new ArrayList<SensorMeasurement>();
        lastmeasurementAdded=System.currentTimeMillis();
    }

    public void addToSensorMeasurements(SensorMeasurement sensorMeasurement, long savedIntervall){
        sensorMeasurements.add(sensorMeasurement);
        this.savedIntervall =savedIntervall;
        if(savedIntervall!=0) {
            if((System.currentTimeMillis()- lastmeasurementAdded)>5000) {
                filterSensorMeasurements(System.currentTimeMillis());
                lastmeasurementAdded=System.currentTimeMillis();
            }
        }
        //if savedIntervall is 0 all entries should be saved
    }

    public void filterSensorMeasurements(long currentTimeInMs){
        for(int i=0; i< sensorMeasurements.size();i++){
            if(currentTimeInMs - sensorMeasurements.get(i).getTimestampSysTime()>=savedIntervall){
                sensorMeasurements.remove(i);
            }
        }
    }
    /*
    public ArrayList<SensorMeasurement> getSensorMeasurements(){
        return this.sensorMeasurements;
    }*/


    public ArrayList<SensorMeasurement> getSensorMeasurements(){
        ArrayList<SensorMeasurement> sensorMeasurementsCopy= new ArrayList<SensorMeasurement>();
        for(int i= 0;i<this.sensorMeasurements.size();i++){
            sensorMeasurementsCopy.add(sensorMeasurements.get(i));
        }
        return  sensorMeasurementsCopy;
    }

    public void emptyTimedQueue(){
        sensorMeasurements.clear();
    }
}

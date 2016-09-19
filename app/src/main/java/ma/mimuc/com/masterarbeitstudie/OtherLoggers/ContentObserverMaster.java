package ma.mimuc.com.masterarbeitstudie.OtherLoggers;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.SystemValues;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 26.11.2015.
 */
public class ContentObserverMaster extends ContentObserver {
    private AudioManager audioManager;
    private int currentMusicVolume=-1;
    private int currentRingtoneVolume=-1;

    private TimedQueue timedQueueMedia;
    private TimedQueue timedQueueRingtone;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurementMedia;
    private SensorMeasurement currentMeasurementRingtone;

    private SystemValues systemValues;

    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

    public ContentObserverMaster(Context context, Handler handler) {
        super(handler);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        timedQueueMedia= new TimedQueue();
        timedQueueRingtone= new TimedQueue();
        timestamp=new Timestamp();
        systemValues=new SystemValues(context);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return false;
    }

    @Override
    public void onChange(boolean selfChange) {
        //MusicVolume Change
        if(currentMusicVolume==-1){
            currentMusicVolume=systemValues.getMediaVolume();
        }
        //if volume changed
        if(currentMusicVolume!=systemValues.getMediaVolume()){
            currentMeasurementMedia = new SensorMeasurement(systemValues.getMediaVolume(), 0, 0, System.currentTimeMillis(), timestamp.getCurrentTimeStamp());
            currentMeasurementMedia.setSensor("VOLUME_MEDIA");
            if(currentMusicVolume<systemValues.getMediaVolume()){
                currentMeasurementMedia.setTag("VOLUME__RAISED");
            }
            else if(currentMusicVolume<systemValues.getMediaVolume()){
                currentMeasurementMedia.setTag("VOLUME__LOWERED");
            }
            else{
                currentMeasurementMedia.setTag("VOLUME_OTHER");
            }
            timedQueueMedia.addToSensorMeasurements(currentMeasurementMedia, MEASUREMENT_INTERVALL);
            System.out.println("VolumeME: " + systemValues.getMediaVolume());
            currentMusicVolume=systemValues.getMediaVolume();
        }

        //MusicVolume Change
        if(currentRingtoneVolume==-1){
            currentMusicVolume=systemValues.getRingToneVolume();
        }
        //if volume changed
        if(currentRingtoneVolume!=systemValues.getRingToneVolume()){
            currentMeasurementRingtone = new SensorMeasurement(systemValues.getRingToneVolume(), 0, 0, System.currentTimeMillis(), timestamp.getCurrentTimeStamp());
            currentMeasurementRingtone.setSensor("VOLUME");
            if(currentMusicVolume<systemValues.getRingToneVolume()){
                currentMeasurementRingtone.setTag("VOLUME__RAISED");
            }
            else if(currentMusicVolume>systemValues.getRingToneVolume()){
                currentMeasurementRingtone.setTag("VOLUME__LOWERED");
            }
            else{
                currentMeasurementRingtone.setTag("VOLUME_OTHER");
            }
            timedQueueRingtone.addToSensorMeasurements(currentMeasurementRingtone, MEASUREMENT_INTERVALL);
            System.out.println("VolumeRT: " + systemValues.getRingToneVolume());
            currentRingtoneVolume=systemValues.getRingToneVolume();
        }




    }
    public ArrayList<SensorMeasurement> getCurrentMediaMeasurements(){
        return timedQueueMedia.getSensorMeasurements();
    }
    public void deleteMediaMeasurements(){
        timedQueueMedia.emptyTimedQueue();
    }


    public ArrayList<SensorMeasurement> getCurrentRingtoneMeasurements(){
        return timedQueueRingtone.getSensorMeasurements();
    }
    public void deleteRingtoneMeasurements(){
        timedQueueRingtone.emptyTimedQueue();
    }
}

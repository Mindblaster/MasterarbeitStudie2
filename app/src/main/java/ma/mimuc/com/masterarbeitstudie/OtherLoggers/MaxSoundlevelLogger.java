package ma.mimuc.com.masterarbeitstudie.OtherLoggers;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 19.11.2015.
 */
public class  MaxSoundlevelLogger {




        private MediaRecorder mRecorder = null;
        Timer timer;
        private boolean started=false;
        private TimedQueue timedQueue;
        private Timestamp timestamp;
        private SensorMeasurement currentMeasurement;
        private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms

        private String tag; //tells if sound was measured in or out a resting state

        public MaxSoundlevelLogger(){
            timedQueue= new TimedQueue();
            timestamp=new Timestamp();
        }



        public void start() {
            if(!started){
                started=true;
                timer = new Timer();
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                timer.scheduleAtFixedRate(new RecorderTask(mRecorder), 0, 500);
                mRecorder.setOutputFile("/dev/null");

                try {
                    mRecorder.prepare();
                    mRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            if (mRecorder != null) {
                timer.cancel();
                timer.purge();
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
                started=false;
            }
        }

    public boolean isStarted(){
        return  this.started;
    }
    public ArrayList<SensorMeasurement> getCurrentMeasurements(){
        return timedQueue.getSensorMeasurements();
    }
    public void deleteMeasurements(){
        timedQueue.emptyTimedQueue();
    }

    public void setTag(String tag){
        this.tag=tag;
    }

private class RecorderTask extends TimerTask {
    private MediaRecorder recorder;

    public RecorderTask(MediaRecorder recorder) {
        this.recorder = recorder;
    }
    @Override
    public void run() {
        int amplitude = recorder.getMaxAmplitude();
        double amplitudeDb = 20 * Math.log10((double)Math.abs(amplitude));
        if(amplitudeDb<0){

        }
        else{
            currentMeasurement= new SensorMeasurement((float)amplitudeDb,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
            currentMeasurement.setSensor("SOUND_LEVEL");
            currentMeasurement.setTag(tag);
            timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);
        }


    }
}




}

package ma.mimuc.com.masterarbeitstudie;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.deprecated.ApplicationLogger;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.ApplicationLoggerLollipop;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.AudioDevicePlugLogger;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.BluetoothOnOffLogger;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.ChargingStatusLogger;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.CheckforWiFiConnection;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.DeviceOnOffLogger;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.PhoneCallLogger;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.PluggedInOutLogger;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.ScreenTimeLogger;
import ma.mimuc.com.masterarbeitstudie.BroadcastReceivers.WiFiOnOffLogger;
import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.OtherLoggers.ContentObserverMaster;
import ma.mimuc.com.masterarbeitstudie.OtherLoggers.MaxSoundlevelLogger;
import ma.mimuc.com.masterarbeitstudie.SensorListeners.AccelerometerWOGravityLogger;
import ma.mimuc.com.masterarbeitstudie.SensorListeners.MovementLogger;
import ma.mimuc.com.masterarbeitstudie.SensorListeners.LightSensorLogger;
import ma.mimuc.com.masterarbeitstudie.SensorListeners.OrientationLogger;
import ma.mimuc.com.masterarbeitstudie.SensorListeners.SensorFusionLoggerEfficient;

//TODO: Check if creating objects in logger classes slows Application down

public class LoggingService extends IntentService implements MovementLogger.AsyncResponse, DeviceOnOffLogger.OnDeviceOff{


    private AudioDevicePlugLogger audioDevicePlugLogger;
    private BluetoothOnOffLogger bluetoothOnOffLogger;
    private ChargingStatusLogger chargingStatusLogger;
    private PhoneCallLogger phoneCallLogger;
    private PluggedInOutLogger pluggedInOutLogger;
    private ScreenTimeLogger screenTimeLogger;
    private WiFiOnOffLogger wiFiOnOffLogger;
    private MovementLogger accGravity;
    private AccelerometerWOGravityLogger accelerometerWOGravityLogger;
    private OrientationLogger orientationLogger;
    private SensorFusionLoggerEfficient sensorFusionLoggerEfficient;
    private MaxSoundlevelLogger maxSoundlevelLogger;
    private ContentObserverMaster contentObserverMaster;
    private LightSensorLogger lightSensorLogger;
    private DeviceOnOffLogger deviceOnOffLogger;

    private ApplicationLogger applicationLogger;
    private ApplicationLoggerLollipop applicationLoggerLollipop;

    private CheckforWiFiConnection checkforWiFiConnection;

    private ArrayList<SensorMeasurement> audioDevicePlugged;
    private ArrayList<SensorMeasurement> bluetoothOnOff;
    private ArrayList<SensorMeasurement> chargingStatus;
    private ArrayList<SensorMeasurement> phoneCalls;
    private ArrayList<SensorMeasurement> pluggedInOut;
    private ArrayList<SensorMeasurement> screenInOut;
    private ArrayList<SensorMeasurement> wifiOnOff;
    private ArrayList<SensorMeasurement> volumeRingtone;
    private ArrayList<SensorMeasurement> volumeMedia;
    private ArrayList<SensorMeasurement> soundlevel;
    private ArrayList<SensorMeasurement> accelerometerWOgravity;
    private ArrayList<SensorMeasurement> accelerometer;
    private ArrayList<SensorMeasurement> brightness;
    private ArrayList<SensorMeasurement> orientation;
    private ArrayList<SensorMeasurement> sensorFusion;
    private ArrayList<SensorMeasurement> gyroscope;


    private long restingStateStartTime;
    private long restingStateEndTime;

    private boolean lollipop;

    private DatabaseHandler databaseHandler;
    private SharedPreferencesManager sharedPreferencesManager;
    private Timestamp timestamp;


    private final long MIN_RESTING_STATE_DURATION=20000;//20 Seconds

    private int temporaryRestingStateID;

    private long currentRestingStateStartTimestamp;

    private String currentRestingStateStartFormattedTimestamp;




    private final long CUTOFF_TIME_ENTER_RESTING_STATE=7000;// the last 7 Seconds because Resting state should have 10 seconds of no movement

    private final long CUTOFF_TIME_LEFT_RESTING_STATE=10000;// the first 10 seconds because buffer saves last 20 seconds and at least 10 are no movement


    public LoggingService() {
        super("loggingService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LoggingService(String name) {
        super(name);
    }

    @Override
    public void onCreate (){
        super.onCreate();
        Log.d("Logging Service", "Service started");
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        if(!sharedPreferencesManager.keyExists()){
            sharedPreferencesManager.saveRestingStateID(0);
            System.out.println("Key does not exist");
        }
        if(!sharedPreferencesManager.systemVersionExists()){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sharedPreferencesManager.saveSystemVersion(1); //Means we use built in tracker instead of polling
            }
            else{
                sharedPreferencesManager.saveSystemVersion(0);
            }
        }
        databaseHandler= new DatabaseHandler(getApplicationContext());

        if(sharedPreferencesManager.getSystemVersion()==1){
            lollipop=true;
            applicationLoggerLollipop = new ApplicationLoggerLollipop(getApplicationContext(),databaseHandler);
        }
        else if(sharedPreferencesManager.getSystemVersion()==0){
            lollipop=false;
            applicationLogger= new ApplicationLogger(getApplicationContext());
        }

        timestamp=new Timestamp();

        //Create Instances of Loggers
        audioDevicePlugLogger = new AudioDevicePlugLogger(getApplicationContext());

        bluetoothOnOffLogger= new BluetoothOnOffLogger(getApplicationContext());

        chargingStatusLogger=new ChargingStatusLogger(getApplicationContext());

        phoneCallLogger=new PhoneCallLogger(getApplicationContext());

        pluggedInOutLogger = new PluggedInOutLogger(getApplicationContext());

        screenTimeLogger= new ScreenTimeLogger(getApplicationContext());

        wiFiOnOffLogger = new WiFiOnOffLogger(getApplicationContext());

        accGravity= new MovementLogger(getApplicationContext(),this); //triggers resting state

        accelerometerWOGravityLogger= new AccelerometerWOGravityLogger(getApplicationContext(),this);

        orientationLogger = new OrientationLogger(getApplicationContext(), SensorManager.SENSOR_DELAY_NORMAL);
        orientationLogger.enable();

        sensorFusionLoggerEfficient=new SensorFusionLoggerEfficient(getApplicationContext()); //Also contains Data for only gyroscope

        maxSoundlevelLogger= new MaxSoundlevelLogger();

        //Media and Ringtone Volume Levels
        contentObserverMaster = new ContentObserverMaster(getApplicationContext(),new Handler());
        this.getApplicationContext().getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                contentObserverMaster );

        lightSensorLogger= new LightSensorLogger(getApplicationContext(),SensorManager.SENSOR_DELAY_NORMAL);

        deviceOnOffLogger= new DeviceOnOffLogger(getApplicationContext(),this);

        checkforWiFiConnection=new CheckforWiFiConnection(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    //cutoff Method for cutting off last seconds of enter RestingState Data
    private void cutOffEnd(){
        for(int i=0;i<accelerometer.size();i++){
            //System.out.println("Size: "+ accelerometer.size());
            //System.out.println("wert: "+(restingStateStartTime-accelerometer.get(i).getTimestampSysTime()));
            if((restingStateStartTime-accelerometer.get(i).getTimestampSysTime())<CUTOFF_TIME_ENTER_RESTING_STATE){
                accelerometer.remove(i);
            }
        }
        for(int i=0;i<accelerometerWOgravity.size();i++){
            if((restingStateStartTime-accelerometerWOgravity.get(i).getTimestampSysTime())<CUTOFF_TIME_ENTER_RESTING_STATE){
                accelerometerWOgravity.remove(i);
            }
        }
        for(int i=0;i<gyroscope.size();i++){
            if((restingStateStartTime-gyroscope.get(i).getTimestampSysTime())<CUTOFF_TIME_ENTER_RESTING_STATE){
                gyroscope.remove(i);
            }
        }
        for(int i=0;i<sensorFusion.size();i++){
            if((restingStateStartTime-sensorFusion.get(i).getTimestampSysTime())<CUTOFF_TIME_ENTER_RESTING_STATE){
                sensorFusion.remove(i);
            }
        }
        for(int i=0;i<orientation.size();i++){
            if((restingStateStartTime-orientation.get(i).getTimestampSysTime())<CUTOFF_TIME_ENTER_RESTING_STATE){
                orientation.remove(i);
            }
        }
        for(int i=0;i<sensorFusion.size();i++){
            if((restingStateStartTime-sensorFusion.get(i).getTimestampSysTime())<CUTOFF_TIME_ENTER_RESTING_STATE){
                sensorFusion.remove(i);
            }
        }

    }


    //
    //Enter Resting State calls Write to Database Class no more
    //instead we buffer ther Data to check if resting state is long enough to qualify before we write to db
    private void writeToBuffer(){

        currentRestingStateStartTimestamp=System.currentTimeMillis();
        currentRestingStateStartFormattedTimestamp=timestamp.getCurrentTimeStamp();

        //Copy separate Buffers to Main Buffer
        audioDevicePlugged=audioDevicePlugLogger.getCurrentMeasurements();
        bluetoothOnOff=bluetoothOnOffLogger.getCurrentMeasurements();
        chargingStatus=chargingStatusLogger.getCurrentMeasurements();
        phoneCalls=phoneCallLogger.getCurrentMeasurements();
        pluggedInOut=pluggedInOutLogger.getCurrentMeasurements();
        screenInOut=screenTimeLogger.getCurrentMeasurements();
        wifiOnOff=wiFiOnOffLogger.getCurrentMeasurements();
        volumeRingtone=contentObserverMaster.getCurrentRingtoneMeasurements();
        volumeMedia=contentObserverMaster.getCurrentMediaMeasurements();
        accelerometerWOgravity=accelerometerWOGravityLogger.getCurrentMeasurements();
        accelerometer=accGravity.getCurrentMeasurements();
        brightness=lightSensorLogger.getCurrentMeasurements();
        orientation=orientationLogger.getCurrentMeasurements();
        sensorFusion=sensorFusionLoggerEfficient.getCurrentSensorFusionMeasurements();
        gyroscope=sensorFusionLoggerEfficient.getCurrentGyroMeasurements();
        //cutOffEnd();
        //Clear local Buffers
        audioDevicePlugLogger.deleteMeasurements();
        bluetoothOnOffLogger.deleteMeasurements();
        chargingStatusLogger.deleteMeasurements();
        phoneCallLogger.deleteMeasurements();
        pluggedInOutLogger.deleteMeasurements();
        screenTimeLogger.deleteMeasurements();
        wiFiOnOffLogger.deleteMeasurements();
        accelerometerWOGravityLogger.deleteMeasurements();
        lightSensorLogger.deleteMeasurements();
        accGravity.deleteMeasurements();
        orientationLogger.deleteMeasurements();
        sensorFusionLoggerEfficient.deleteSensorFusionMeasurements();
        sensorFusionLoggerEfficient.deleteGyroMeasurements();
        contentObserverMaster.deleteMediaMeasurements();
        contentObserverMaster.deleteRingtoneMeasurements();
    }
    @Override
    public void enteredRestingState(String output) {
        temporaryRestingStateID=sharedPreferencesManager.getKey();
        System.out.println(output);
        //set starttime
        restingStateStartTime=System.currentTimeMillis();

        //Write Data in temporary Buffers to do Checks for shorter Resting states
        writeToBuffer();


        //new WriteToDatabase().execute(true);
        if (!maxSoundlevelLogger.isStarted()){
            maxSoundlevelLogger.setTag("ENTERED_RESTING_STATE");
            maxSoundlevelLogger.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    maxSoundlevelLogger.stop();
                    soundlevel=maxSoundlevelLogger.getCurrentMeasurements();
                   //
                    maxSoundlevelLogger.deleteMeasurements();
                }
            }, 5000);
         }
    }

    //LEFT RESTING STATE

    @Override
    public void leftRestingState(String Tag) {

        restingStateEndTime=System.currentTimeMillis();
        if((restingStateEndTime-restingStateStartTime)>MIN_RESTING_STATE_DURATION) {
            System.out.println("RESTING_STATE_REGISTERED");
            new WriteToDatabase().execute(true);
            maxSoundlevelLogger.setTag("LEFT_RESTING_STATE");
            maxSoundlevelLogger.start();
            sharedPreferencesManager.saveLastRestingStateTime(System.currentTimeMillis());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    maxSoundlevelLogger.stop();
                    databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(maxSoundlevelLogger.getCurrentMeasurements()), "soundlevel");
                    databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(accGravity.getCurrentMeasurements()), "accelerometer");
                    databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(accelerometerWOGravityLogger.getCurrentMeasurements()), "accelerometer_wo_gravity");
                    databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(orientationLogger.getCurrentMeasurements()), "orientation");
                    databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(sensorFusionLoggerEfficient.getCurrentSensorFusionMeasurements()), "sensor_fusion");
                    databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(lightSensorLogger.getCurrentMeasurements()), "brightness");
                    databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(sensorFusionLoggerEfficient.getCurrentGyroMeasurements()), "gyroscope");
                    databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(getRestingState(false)), "resting_states");
                    maxSoundlevelLogger.deleteMeasurements();
                    accelerometerWOGravityLogger.deleteMeasurements();
                    lightSensorLogger.deleteMeasurements();
                    accGravity.deleteMeasurements();
                    orientationLogger.deleteMeasurements();
                    sensorFusionLoggerEfficient.deleteSensorFusionMeasurements();
                    sensorFusionLoggerEfficient.deleteGyroMeasurements();
                    temporaryRestingStateID = sharedPreferencesManager.getKey();
                }
            }, 5000);
        }
    }


    private ArrayList<SensorMeasurement> addRestingStateStatusToMeasurements(ArrayList<SensorMeasurement> sensorMeasurements){
        ArrayList<SensorMeasurement> currentSensorMeasurements = new ArrayList<SensorMeasurement>(sensorMeasurements);

        for(int i=0; i<currentSensorMeasurements.size();i++){
            currentSensorMeasurements.get(i).setTag("LEFT_RESTING_STATE");
        }

        return currentSensorMeasurements;
    }



    private ArrayList<SensorMeasurement> addRestingStateIDToMeasurements(ArrayList<SensorMeasurement> sensorMeasurements){
        ArrayList<SensorMeasurement> currentSensorMeasurements= new ArrayList<SensorMeasurement>(sensorMeasurements);

        for(int i=0; i<currentSensorMeasurements.size();i++){
            currentSensorMeasurements.get(i).setRestingStateID(temporaryRestingStateID);
        }

        return addRestingStateStatusToMeasurements(currentSensorMeasurements);
    }
    public ArrayList<SensorMeasurement> getRestingState(boolean intoRestingState) {

        SensorMeasurement sensorMeasurement= new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
        sensorMeasurement.setPackageName("");
        sensorMeasurement.setSensor("RESTING_STATE");
        if(intoRestingState){
            sensorMeasurement.setTag("ENTERED_RESTING_STATE");
        }
        else{
            sensorMeasurement.setTag("LEFT_RESTING_STATE");
        }

        ArrayList<SensorMeasurement> sensorMeasurements=new ArrayList<>();
        sensorMeasurements.add(sensorMeasurement);
        return sensorMeasurements;
    }

    @Override
    public void saveDataOnDeviceOff() {
        new WriteToDatabase().execute(false);
    }


    private class WriteToDatabase extends AsyncTask<Boolean, Void, String> {

        private int restingStateID;
        private boolean restingStateStatus;

        @Override
        protected String doInBackground(Boolean... params) {
            restingStateStatus=params[0];
            writeDataToDatabase();
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {
            audioDevicePlugged.clear();
            bluetoothOnOff.clear();
            chargingStatus.clear();
            phoneCalls.clear();
            pluggedInOut.clear();
            screenInOut.clear();
            wifiOnOff.clear();
            volumeRingtone.clear();
            volumeMedia.clear();
            accelerometerWOgravity.clear();
            accelerometer.clear();
            brightness.clear();
            orientation.clear();
            sensorFusion.clear();
            gyroscope.clear();
        }

        private void writeDataToDatabase(){
            restingStateID = sharedPreferencesManager.getKey();
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(audioDevicePlugged), "audio_device_plugged");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(bluetoothOnOff), "bluetooth");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(chargingStatus), "charging_status");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(phoneCalls), "phone_calls");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(pluggedInOut), "plugged_power");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(screenInOut), "screentime");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(wifiOnOff), "wifi");
            databaseHandler.addMeasurementsToDB(addRestingStateStatusToMeasurements(accelerometer), "accelerometer");
            databaseHandler.addMeasurementsToDB(addRestingStateStatusToMeasurements(accelerometerWOgravity), "accelerometer_wo_gravity");
            databaseHandler.addMeasurementsToDB(addRestingStateStatusToMeasurements(orientation), "orientation");
            databaseHandler.addMeasurementsToDB(addRestingStateStatusToMeasurements(sensorFusion), "sensor_fusion");
            //soundlevel is added directly to DB
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(volumeMedia), "volume_media");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(volumeRingtone), "volume_ringtone");
            databaseHandler.addMeasurementsToDB(addRestingStateStatusToMeasurements(brightness), "brightness");
            databaseHandler.addMeasurementsToDB(addRestingStateStatusToMeasurements(gyroscope), "gyroscope");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(getRestingState(restingStateStatus)), "resting_states");
            databaseHandler.addMeasurementsToDB(addRestingStateIDToMeasurements(soundlevel), "soundlevel");


            ++restingStateID;
            sharedPreferencesManager.saveRestingStateID(restingStateID);
            System.out.println("Inserting Done");
        }

        private ArrayList<SensorMeasurement> addRestingStateIDToMeasurements(ArrayList<SensorMeasurement> sensorMeasurements){
            ArrayList<SensorMeasurement> currentSensorMeasurements= new ArrayList<SensorMeasurement>(sensorMeasurements);

            for(int i=0; i<currentSensorMeasurements.size();i++){
                currentSensorMeasurements.get(i).setRestingStateID(restingStateID);
            }

            return currentSensorMeasurements;
        }

        private ArrayList<SensorMeasurement> addRestingStateStatusToMeasurements(ArrayList<SensorMeasurement> sensorMeasurements){
            ArrayList<SensorMeasurement> currentSensorMeasurements= new ArrayList<SensorMeasurement>(sensorMeasurements);

            for(int i=0; i<currentSensorMeasurements.size();i++){
                currentSensorMeasurements.get(i).setTag("ENTERED_RESTING_STATE");
            }

            return addRestingStateIDToMeasurements(currentSensorMeasurements);
        }

        public ArrayList<SensorMeasurement> getRestingState(boolean intoRestingState) {

            SensorMeasurement sensorMeasurement= new SensorMeasurement(0,0,0,currentRestingStateStartTimestamp,currentRestingStateStartFormattedTimestamp);
            sensorMeasurement.setPackageName("");
            sensorMeasurement.setSensor("RESTING_STATE");
            if(intoRestingState){
                sensorMeasurement.setTag("ENTERED_RESTING_STATE");
            }
            else{
                sensorMeasurement.setTag("DEVICE_TURNED_OFF");
            }

            ArrayList<SensorMeasurement> sensorMeasurements=new ArrayList<>();
            sensorMeasurements.add(sensorMeasurement);
            return sensorMeasurements;
        }


    }

}

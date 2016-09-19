package ma.mimuc.com.masterarbeitstudie.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;

/**
 * Created by Raphael on 10.11.2015.
 */
public class DatabaseHandler {
    private Context context;

    private static DatabaseHandler databaseHandler;
    public static DatabaseHandler getInstance(Context context){
        if (databaseHandler == null){
            databaseHandler = new DatabaseHandler(context);
        }

        return databaseHandler;
    }
    private Object lock;
    private SQLiteHelper helper;
    private SQLiteDatabase database;
    public DatabaseHandler(Context context){
        helper = new SQLiteHelper(context);
        database = helper.getWritableDatabase();
        lock = new Object();
        this.context=context;
    }

    public void addMeasurementsToDB(ArrayList<SensorMeasurement> sensorMeasurements, String table) {
        if (table.equals("audio_device_plugged")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_AUDIO_DEVICE_PLUGGED,sensorMeasurements);
        }
        else if (table.equals("bluetooth")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_BLUETOOTH,sensorMeasurements);
        }
        else if (table.equals("charging_status")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_CHARGING_STATUS,sensorMeasurements);
        }
        else if (table.equals("phone_calls")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_PHONE_CALLS,sensorMeasurements);
        }
        else if (table.equals("plugged_power")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_PLUGGED_POWER,sensorMeasurements);
        }
        else if (table.equals("screentime")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_SCREENTIME,sensorMeasurements);
        }
        else if (table.equals("wifi")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_WIFI,sensorMeasurements);
        }
        else if (table.equals("accelerometer")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_ACCELEROMETER,sensorMeasurements);
        }
        else if (table.equals("accelerometer_wo_gravity")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_ACCELEROMETER_WO_GRAVITY,sensorMeasurements);
        }
        else if (table.equals("orientation")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_ORIENTATION,sensorMeasurements);
        }
        else if (table.equals("sensor_fusion")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_SENSOR_FUSION,sensorMeasurements);
        }
        else if (table.equals("soundlevel")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_SOUNDLEVEL,sensorMeasurements);
        }
        else if (table.equals("volume_media")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_VOLUME_MEDIA,sensorMeasurements);
        }
        else if (table.equals("volume_ringtone")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_VOLUME_RINGTONE,sensorMeasurements);
        }
        else  if (table.equals("brightness")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_BRIGHTNESS,sensorMeasurements);
        }
        else if (table.equals("gyroscope")) {
            batchInsertintoDatabase(SQLiteHelper.TABLE_GYROSCOPE,sensorMeasurements);
        }
//needs to be done in seperate function
        else if (table.equals("app_usage")) {
            batchInsertintoAppUsageDatabase(SQLiteHelper.TABLE_APP_USAGE, sensorMeasurements);
        }
        else if(table.equals("notifications")){
            batchInsertintoNotificationDatabase(SQLiteHelper.TABLE_NOTIFICATIONS, sensorMeasurements);
        }
        else if(table.equals("resting_states")){
            batchInsertintoAppUsageDatabase(SQLiteHelper.TABLE_RESTING_STATE, sensorMeasurements);
        }
        else if(table.equals("device_on_off")){
            batchInsertintoDatabase(SQLiteHelper.TABLE_DEVICE_ON_OFF, sensorMeasurements);
        }

    }

    public void insertUserAnswerIntoDatabase(String Location,String privacyLevel,String feedback,long userID, long timestamp, String formattedTimeStamp, String Tag, int restingStateID){
        //insert them
        synchronized (lock) {
            String sql = "INSERT INTO " + "user_location" + " VALUES (?,?,?,?,?,?,?,?,?);";
            SQLiteStatement statement = database.compileStatement(sql);
            database.beginTransaction();
            statement.clearBindings();
            statement.bindNull(1);
            statement.bindLong(2, restingStateID);
            statement.bindString(3, Location);
            statement.bindString(4,privacyLevel);
            statement.bindString(5,feedback);
            statement.bindLong(6,userID);
            statement.bindLong(7, timestamp);
            statement.bindString(8, formattedTimeStamp);
            statement.bindString(9,Tag);
            statement.execute();
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }
    public void insertUserStudyPerformanceIntoDatabase(SensorMeasurement sensorMeasurement){
        //insert them
        synchronized (lock) {
            String sql = "INSERT INTO " + "user_study_performance" + "  VALUES (?,?,?,?,?,?,?,?,?);";
            SQLiteStatement statement = database.compileStatement(sql);
            database.beginTransaction();
            statement.clearBindings();
            statement.bindNull(1);
            statement.bindLong(2, sensorMeasurement.getRestingStateID());
            statement.bindLong(3, sensorMeasurement.getTimestampSysTime());
            statement.bindString(4, sensorMeasurement.getTimeStampFormatted());
            statement.bindDouble(5, sensorMeasurement.getxValue());
            statement.bindDouble(6, sensorMeasurement.getyValue());
            statement.bindDouble(7, sensorMeasurement.getzValue());
            statement.bindString(8, sensorMeasurement.getTag());
            statement.bindString(9, sensorMeasurement.getSensor());
            statement.execute();
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }



    private void batchInsertintoDatabase(String table,ArrayList<SensorMeasurement> sensorMeasurements ){
        //insert them
        synchronized (lock) {
            String sql = "INSERT INTO " + table + " VALUES (?,?,?,?,?,?,?,?,?);";
            SQLiteStatement statement = database.compileStatement(sql);
            database.beginTransaction();
            for (int i = 0; i < sensorMeasurements.size(); i++) {
                statement.clearBindings();
                statement.bindNull(1);
                statement.bindLong(2, sensorMeasurements.get(i).getRestingStateID());
                statement.bindLong(3, sensorMeasurements.get(i).getTimestampSysTime());
                statement.bindString(4, sensorMeasurements.get(i).getTimeStampFormatted());
                statement.bindDouble(5, sensorMeasurements.get(i).getxValue());
                statement.bindDouble(6, sensorMeasurements.get(i).getyValue());
                statement.bindDouble(7, sensorMeasurements.get(i).getzValue());
                statement.bindString(8, sensorMeasurements.get(i).getTag());
                statement.bindString(9, sensorMeasurements.get(i).getSensor());
                statement.execute();
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }

    private void batchInsertintoNotificationDatabase(String table,ArrayList<SensorMeasurement> sensorMeasurements ){
        //insert them
        synchronized (lock) {
            String sql = "INSERT INTO " + table + " VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            SQLiteStatement statement = database.compileStatement(sql);
            database.beginTransaction();
            for (int i = 0; i < sensorMeasurements.size(); i++) {
                statement.clearBindings();
                statement.bindNull(1);
                statement.bindLong(2, sensorMeasurements.get(i).getRestingStateID());
                statement.bindLong(3, sensorMeasurements.get(i).getTimestampSysTime());
                statement.bindString(4, sensorMeasurements.get(i).getTimeStampFormatted());
                statement.bindDouble(5, sensorMeasurements.get(i).getxValue());
                statement.bindDouble(6, sensorMeasurements.get(i).getyValue());
                statement.bindDouble(7, sensorMeasurements.get(i).getzValue());
                statement.bindString(8, sensorMeasurements.get(i).getPackageName());
                statement.bindString(9, sensorMeasurements.get(i).getNotificationID());
                statement.bindString(10, sensorMeasurements.get(i).getTag());
                statement.bindString(11, sensorMeasurements.get(i).getSensor());
                statement.execute();
            }

            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }


    private void batchInsertintoAppUsageDatabase(String table,ArrayList<SensorMeasurement> sensorMeasurements ){
        //insert them
        synchronized (lock) {
            String sql = "INSERT INTO " + table + " VALUES (?,?,?,?,?,?,?,?,?,?);";
            SQLiteStatement statement = database.compileStatement(sql);
            database.beginTransaction();
            for (int i = 0; i < sensorMeasurements.size(); i++) {
                statement.clearBindings();
                statement.bindNull(1);
                statement.bindLong(2, sensorMeasurements.get(i).getRestingStateID());
                statement.bindLong(3, sensorMeasurements.get(i).getTimestampSysTime());
                statement.bindString(4, sensorMeasurements.get(i).getTimeStampFormatted());
                statement.bindDouble(5, sensorMeasurements.get(i).getxValue());
                statement.bindDouble(6, sensorMeasurements.get(i).getyValue());
                statement.bindDouble(7, sensorMeasurements.get(i).getzValue());
                statement.bindString(8, sensorMeasurements.get(i).getPackageName());
                statement.bindString(9, sensorMeasurements.get(i).getTag());
                statement.bindString(10, sensorMeasurements.get(i).getSensor());
                statement.execute();
            }

            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }


    public void deleteDatabases(long lastUpload) {
        System.out.println("deleteDb");
        database.execSQL("delete from " + SQLiteHelper.TABLE_RESTING_STATE);
        database.execSQL("delete from " + SQLiteHelper.TABLE_ACCELEROMETER_WO_GRAVITY);
        database.execSQL("delete from " + SQLiteHelper.TABLE_ACCELEROMETER );
        database.execSQL("delete from " + SQLiteHelper.TABLE_WIFI );
        database.execSQL("delete from " + SQLiteHelper.TABLE_NOTIFICATIONS);
        database.execSQL("delete from " + SQLiteHelper.TABLE_BLUETOOTH );
        database.execSQL("delete from " + SQLiteHelper.TABLE_APP_USAGE );
        database.execSQL("delete from " + SQLiteHelper.TABLE_AUDIO_DEVICE_PLUGGED);
        database.execSQL("delete from " + SQLiteHelper.TABLE_BRIGHTNESS);
        database.execSQL("delete from " + SQLiteHelper.TABLE_CHARGING_STATUS );
        database.execSQL("delete from " + SQLiteHelper.TABLE_SENSOR_FUSION);
        database.execSQL("delete from " + SQLiteHelper.TABLE_PLUGGED_POWER );
        database.execSQL("delete from " + SQLiteHelper.TABLE_GYROSCOPE );
        database.execSQL("delete from " + SQLiteHelper.TABLE_ORIENTATION );
        database.execSQL("delete from " + SQLiteHelper.TABLE_PHONE_CALLS);
        database.execSQL("delete from " + SQLiteHelper.TABLE_SCREENTIME);
        database.execSQL("delete from " + SQLiteHelper.TABLE_SOUNDLEVEL );
        database.execSQL("delete from " + SQLiteHelper.TABLE_VOLUME_MEDIA);
        database.execSQL("delete from " + SQLiteHelper.TABLE_VOLUME_RINGTONE );
        database.execSQL("delete from " + SQLiteHelper.TABLE_USER_LOCATION);
        database.execSQL("delete from " + SQLiteHelper.TABLE_RESTING_STATE);
    }


/*
    public void deleteDatabases(long lastUpload) {
        System.out.println("deleteDb");
        database.execSQL("delete from " + SQLiteHelper.TABLE_RESTING_STATE + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " < " + lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_ACCELEROMETER_WO_GRAVITY + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_ACCELEROMETER + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_WIFI + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_NOTIFICATIONS + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_BLUETOOTH + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_APP_USAGE + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_AUDIO_DEVICE_PLUGGED + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_BRIGHTNESS + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_CHARGING_STATUS + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_SENSOR_FUSION + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_PLUGGED_POWER + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_GYROSCOPE + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_ORIENTATION + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_PHONE_CALLS + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_SCREENTIME + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_SOUNDLEVEL + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_VOLUME_MEDIA + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
        database.execSQL("delete from " + SQLiteHelper.TABLE_VOLUME_RINGTONE + " where " + SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME +" < "+ lastUpload);
    }
*/
        /**
         * Export database to external storage, so it can be accessed from / copied
     * to a computer.
     *
     * @return
     */
    public boolean exportDB() {

        File sd = Environment.getExternalStorageDirectory();

        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "ma.mimuc.com.masterarbeitstudie" + "/databases/"
                + SQLiteHelper.DATABASE_NAME;
        String backupDBPath = SQLiteHelper.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        Log.d("RESTORE", backupDB.toString());
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            MediaScannerConnection.scanFile(this.context, new String[]{backupDB.getAbsolutePath()}, null, null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }





}

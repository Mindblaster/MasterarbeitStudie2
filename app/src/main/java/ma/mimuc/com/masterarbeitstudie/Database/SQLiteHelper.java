package ma.mimuc.com.masterarbeitstudie.Database;

/**
 * Created by Raphael on 10.11.2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Measurements.sqlite";

    // table names
    public static final String TABLE_AUDIO_DEVICE_PLUGGED="audio_device_plugged";
    public static final String TABLE_BLUETOOTH="bluetooth";
    public static final String TABLE_CHARGING_STATUS="charging_status";
    public static final String TABLE_PHONE_CALLS="phone_calls";
    public static final String TABLE_PLUGGED_POWER="plugged_power";
    public static final String TABLE_SCREENTIME="screen_time";
    public static final String TABLE_WIFI="wifi";
    public static final String TABLE_ACCELEROMETER="acclerometer";
    public static final String TABLE_ACCELEROMETER_WO_GRAVITY="acclerometer_wo_gravity";
    public static final String TABLE_ORIENTATION="orientation";
    public static final String TABLE_SENSOR_FUSION="sensor_fusion";
    public static final String TABLE_SOUNDLEVEL="soundlevel";
    public static final String TABLE_VOLUME_MEDIA="volume_media";
    public static final String TABLE_VOLUME_RINGTONE="volume_ringtone";
    public static final String TABLE_BRIGHTNESS="brightness";
    public static final String TABLE_GYROSCOPE="gyroscope";

    public static final String TABLE_APP_USAGE="app_usage";
    public static final String TABLE_NOTIFICATIONS="notifications";

    public static final String TABLE_RESTING_STATE="resting_states";

    public static final String TABLE_DEVICE_ON_OFF="device_on_off";


    //Table for User Answers
    public static final String TABLE_USER_LOCATION="user_location";

    //Table for StudyPerformance
    public static final String TABLE_USER_STUDY_PERFORMANCE="user_study_performance";





    //  column names all tables
    public static final String KEY_X_VALUE = "x_value";
    public static final String KEY_Y_VALUE = "y_value";
    public static final String KEY_Z_VALUE = "z_value";
    public static final String KEY_TIMESTAMP_SYSTEM_TIME = "timestamp_systime";
    public static final String KEY_TIMESTAMP_FORMATTED = "timestamp_formatted";
    public static final String KEY_TAG = "tag";
    public static final String KEY_SENSOR="sensor";
    public static final String KEY_RESTINGSTATE_ID = "restingstate_id";

    //Added Columns for User Answers
    public static final String KEY_USER_LOCATION="location";
    public static final String KEY_USER_PRIVACY_LEVEL="privacy_level";
    public static final String KEY_USER_FEEDBACK="feedback";
    public static final String Key_USER_ID="userid";


    //extra column for notificationtracker id string

    public static final String KEY_NOTIFICATION_ID="notification_id";

    //extra column for app logger
    public static final String KEY_PACKAGE_NAME="package_name";

    //AUDIO DEVICE PLUGGED TABLE
    public static final String SQL_CREATE_AUDIO_DEVICE_PLUGGED_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_AUDIO_DEVICE_PLUGGED + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," +
                    SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_AUDIO_DEVICE_PLUGGED_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_AUDIO_DEVICE_PLUGGED;



    //BLUETOOTH TABLE

    public static final String SQL_CREATE_BLUETOOTH_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_BLUETOOTH + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," +
                    SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_BLUETOOTH_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_BLUETOOTH;

    //CHARGING STATUS
    public static final String SQL_CREATE_CHARGING_STATUS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_CHARGING_STATUS + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," +SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_CHARGING_STATUS_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_CHARGING_STATUS;


    //PHONE CALL TABLE
    public static final String SQL_CREATE_PHONE_CALL_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_PHONE_CALLS + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_PHONE_CALL_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_PHONE_CALLS;

    //PLUGGED POWER TABLE
    public static final String SQL_CREATE_PLUGGED_POWER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_PLUGGED_POWER + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_PLUGGED_POWER_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_PLUGGED_POWER;


    //SCREENTIME TABLE
    public static final String SQL_CREATE_SCREENTIME_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_SCREENTIME + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_SCREENTIME_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_SCREENTIME;


    //SCREENTIME WIFI
    public static final String SQL_CREATE_WIFI_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_WIFI + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_WIFI_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_WIFI;


    //ACCELEROMETER TABLE
    public static final String SQL_CREATE_ACCELEROMETER_TABLE=
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_ACCELEROMETER + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_ACCELEROMETER_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_ACCELEROMETER;


    //ACCELEROMETER WITHOUT GRAVITY TABLE
    public static final String SQL_CREATE_ACCELEROMETER_WO_GRAVITY_TABLE=
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_ACCELEROMETER_WO_GRAVITY + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_ACCELEROMETER_WO_GRAVITY_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_ACCELEROMETER_WO_GRAVITY;

    //GYROSCOPE_TABLE
    public static final String SQL_CREATE_GYROSCOPE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_GYROSCOPE + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" +")";


    public static final String SQL_DELETE_GYROSCOPE_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_GYROSCOPE;

    //BRIGHTNESS TABLE
    public static final String SQL_CREATE_BRIGHTNESS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_BRIGHTNESS + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_BRIGHTNESS_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_BRIGHTNESS;


    //ORIENTATION TABLE
    public static final String SQL_CREATE_ORIENTATION_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_ORIENTATION + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," +
                    SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_ORIENTATION_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_ORIENTATION;


    //SENSOR FUSION TABLE
    public static final String SQL_CREATE_SENSOR_FUSION_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_SENSOR_FUSION + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_SENSOR_FUSION_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_SENSOR_FUSION;

    //SOUND LEVEL TABLE
    public static final String SQL_CREATE_SOUND_LEVEL_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_SOUNDLEVEL + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_SOUND_LEVEL_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_SOUNDLEVEL;


    //VOLUME MEDIA TABLE
    public static final String SQL_CREATE_VOLUME_MEDIA_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_VOLUME_MEDIA + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_VOLUME_MEDIA_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_VOLUME_MEDIA;


    //VOLUME RINGTONE TABLE
    public static final String SQL_CREATE_VOLUME_RINGTONE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_VOLUME_RINGTONE + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_VOLUME_RINGTONE_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_VOLUME_RINGTONE;

    //APP USAGE TABLE
    public static final String SQL_CREATE_APP_USAGE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_APP_USAGE + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_PACKAGE_NAME + " TEXT," +
                    SQLiteHelper.KEY_TAG + " TEXT,"
                    + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_APP_USAGE_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_APP_USAGE;

    //NOTIFICATION TABLE

    public static final String SQL_CREATE_NOTIFICATION_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NOTIFICATIONS + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_PACKAGE_NAME + " TEXT," +
                    SQLiteHelper.KEY_NOTIFICATION_ID+ "TEXT," +
                    SQLiteHelper.KEY_TAG + " TEXT,"
                    + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_NOTIFICATION_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_NOTIFICATIONS;


//RESTING STATE TABLE
    public static final String SQL_CREATE_RESTING_STATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_RESTING_STATE + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_PACKAGE_NAME + " TEXT," +
                    SQLiteHelper.KEY_TAG + " TEXT,"
                    + SQLiteHelper.KEY_SENSOR + " TEXT" +")";

    public static final String SQL_DELETE_RESTING_STATE_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_RESTING_STATE;


    //DEVICE ON OFF TABLE
    public static final String SQL_CREATE_DEVICE_ON_OFF_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_DEVICE_ON_OFF + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," +
                    SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_DEVICE_ON_OFF_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_DEVICE_ON_OFF;


    //USER LOCATION TABLE
    public static final String SQL_CREATE_USER_LOCATION_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_USER_LOCATION + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_USER_LOCATION + " TEXT," +
                    SQLiteHelper.KEY_USER_PRIVACY_LEVEL + " TEXT," +
                    SQLiteHelper.KEY_USER_FEEDBACK + " TEXT," +
                    SQLiteHelper.Key_USER_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_TAG + " TEXT" + ")";

    public static final String SQL_DELETE_USER_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_USER_LOCATION;


    //DEVICE ON OFF TABLE
    public static final String SQL_CREATE_USER_STUDY_PERFORMANCE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_USER_STUDY_PERFORMANCE + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SQLiteHelper.KEY_RESTINGSTATE_ID + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_SYSTEM_TIME + " LONG," +
                    SQLiteHelper.KEY_TIMESTAMP_FORMATTED + " TEXT," +
                    SQLiteHelper.KEY_X_VALUE + " LONG," +
                    SQLiteHelper.KEY_Y_VALUE + " LONG," +
                    SQLiteHelper.KEY_Z_VALUE + " LONG," +
                    SQLiteHelper.KEY_TAG + " TEXT," +
                    SQLiteHelper.KEY_SENSOR + " TEXT" + ")";

    public static final String SQL_DELETE_USER_STUDY_PERFORMANCE_TABLE =
            "DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_USER_STUDY_PERFORMANCE;



    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ORIENTATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ACCELEROMETER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CHARGING_STATUS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SCREENTIME_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ACCELEROMETER_WO_GRAVITY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_BLUETOOTH_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PHONE_CALL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PLUGGED_POWER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SENSOR_FUSION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SOUND_LEVEL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VOLUME_MEDIA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VOLUME_RINGTONE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WIFI_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_AUDIO_DEVICE_PLUGGED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GYROSCOPE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_BRIGHTNESS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_APP_USAGE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTIFICATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RESTING_STATE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DEVICE_ON_OFF_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER_STUDY_PERFORMANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(SQL_DELETE_ORIENTATION_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_ACCELEROMETER_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_CHARGING_STATUS_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_SCREENTIME_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_ACCELEROMETER_WO_GRAVITY_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_BLUETOOTH_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_PHONE_CALL_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_PLUGGED_POWER_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_SENSOR_FUSION_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_SOUND_LEVEL_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_VOLUME_MEDIA_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_VOLUME_RINGTONE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_WIFI_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_AUDIO_DEVICE_PLUGGED_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_GYROSCOPE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_BRIGHTNESS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_APP_USAGE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_NOTIFICATION_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_RESTING_STATE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_DEVICE_ON_OFF_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    public void deleteDB(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(SQL_DELETE_ORIENTATION_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_ACCELEROMETER_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_CHARGING_STATUS_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_SCREENTIME_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_ACCELEROMETER_WO_GRAVITY_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_BLUETOOTH_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_PHONE_CALL_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_PLUGGED_POWER_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_SENSOR_FUSION_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_SOUND_LEVEL_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_VOLUME_MEDIA_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_VOLUME_RINGTONE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_WIFI_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_AUDIO_DEVICE_PLUGGED_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_GYROSCOPE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_BRIGHTNESS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_APP_USAGE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_RESTING_STATE_TABLE);
    }
}

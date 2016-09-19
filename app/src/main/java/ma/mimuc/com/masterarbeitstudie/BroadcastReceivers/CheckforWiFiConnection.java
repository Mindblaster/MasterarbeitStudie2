package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.SharedPreferencesManager;
import ma.mimuc.com.masterarbeitstudie.UploadDB;

/**
 * Created by Raphael on 02.12.2015.
 */
public class CheckforWiFiConnection {

    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;
    private DatabaseHandler databaseHandler;
    private Timer fuseTimer = new Timer();
    private final long ONE_DAY_IN_MILLISECONDS=86400000;
    private final long UPLOAD_INTERVALL=600000;
    private boolean isConnectedViaWifi=false;
    private boolean isUploading=false;


    public CheckforWiFiConnection(Context context){
        sharedPreferencesManager= new SharedPreferencesManager(context);
        if(!sharedPreferencesManager.lastUploadExists()){
            sharedPreferencesManager.saveLastUpload(0);
        }
        this.context=context;
        databaseHandler= new DatabaseHandler(context);
        fuseTimer.scheduleAtFixedRate(new UploadDataTimer(),
                15000, ONE_DAY_IN_MILLISECONDS/12);


        BroadcastReceiver wifiConnectedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isConnectedViaWifi()) {
                            System.out.println("works");
                            if (System.currentTimeMillis() - sharedPreferencesManager.getLastUpload() > UPLOAD_INTERVALL) {
                                if (!isUploading)
                                    new Upload().execute();
                                    System.out.println("upload execute");
                                }
                            }
                        else {
                            isConnectedViaWifi = false;
                            // wifi connection was lost
                        }
                    }
                }, 5000);


            }
        };

        /*
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.EXTRA_SUPPLICANT_CONNECTED);
        context.registerReceiver(wifiConnectedReceiver, filter);
*/

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION );
        context.registerReceiver(wifiConnectedReceiver, filter);


    }
    private boolean isConnectedViaWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }


    private class Upload extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            if(!isUploading) {
                isUploading = true;
                UploadDB uploadDB = new UploadDB(context);
                System.out.println("wifi receiver upload");
                if (uploadDB.uploadData() == 0) {
                    return "SUCCESS";
                } else {
                    return "FAILURE";
                }
            }
            else{
                return "CURRENTLY UPLOADING";
            }

        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {
            if(result.equals("SUCCESS")) {
                databaseHandler.deleteDatabases(sharedPreferencesManager.getLastUpload());
                sharedPreferencesManager.saveLastUpload(System.currentTimeMillis());
                isUploading=false;
                System.out.println("SUCCESS");
            }
            else if (result.equals("FAILURE")){
                System.out.println("FAIL");
                isUploading=false;
            }
            else if(result.equals("CURRENTLY UPLOADING")){

            }
        }


    }

    class UploadDataTimer extends TimerTask {
        public void run() {
            System.out.println("regular repetition upload");
            if(isConnectedViaWifi()) {
                if (System.currentTimeMillis() - sharedPreferencesManager.getLastUpload() > UPLOAD_INTERVALL) {
                    isUploading=true;
                    UploadDB uploadDB = new UploadDB(context);
                    isUploading=false;
                    if (uploadDB.uploadData() == 0) {
                        databaseHandler.deleteDatabases(sharedPreferencesManager.getLastUpload());
                        sharedPreferencesManager.saveLastUpload(System.currentTimeMillis());
                        System.out.println("SUCCESS");
                    } else {
                        System.out.println("FAIL");
                    }
                }
            }
        }
    }
}

package ma.mimuc.com.masterarbeitstudie.BroadcastReceivers;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/**
 * Created by Raphael on 11.11.2015.
 */
public class ApplicationLoggerLollipop {

    private TimedQueue timedQueue;
    private Timestamp timestamp;
    private SensorMeasurement currentMeasurement;
    private Context context;
    private UsageStatsManager usageStatsManager;
    private final long ONE_DAY_IN_MILLISECONDS=86400000;
    private final long MEASUREMENT_INTERVALL=0; //time that values are saved in ms
    private DatabaseHandler databaseHandler;

    public ApplicationLoggerLollipop(Context context, DatabaseHandler databaseHandler){
        this.context=context;
        usageStatsManager=(UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        timedQueue= new TimedQueue();
        timestamp=new Timestamp();
        this.databaseHandler=databaseHandler;


        BroadcastReceiver applicationLoggerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar calendar=Calendar.getInstance();
                long endTime=calendar.getTimeInMillis();
                calendar.add(Calendar.YEAR, -1);
                long startTime=calendar.getTimeInMillis();
                Log.d("TAG", "Range start:" + startTime);
                Log.d("TAG", "Range end:" + endTime);
                List<UsageStats> usageStatsList=usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
                Log.d("TAG", "results: " + usageStatsList.size());
                new WriteApplicationDataToDatabase().execute(usageStatsList);
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        context.registerReceiver(applicationLoggerReceiver, filter);

    }

    private class WriteApplicationDataToDatabase extends AsyncTask<List<UsageStats>, Void, String> {
        @Override
        protected String doInBackground(List <UsageStats> ... params) {
            List<UsageStats> usageStats = params[0];
            System.out.println(usageStats.get(0).getPackageName());
            for(int i=0;i<usageStats.size();i++){
                currentMeasurement= new SensorMeasurement(usageStats.get(i).getTotalTimeInForeground(),usageStats.get(i).getFirstTimeStamp(),usageStats.get(i).getLastTimeStamp(),System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
                currentMeasurement.setPackageName(usageStats.get(i).getPackageName());
                currentMeasurement.setSensor("APPLICATION_LOGGER");
                currentMeasurement.setTag("LOLLIPOP");
                timedQueue.addToSensorMeasurements(currentMeasurement,MEASUREMENT_INTERVALL);
            }
            databaseHandler.addMeasurementsToDB(timedQueue.getSensorMeasurements(),"app_usage");
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {
            timedQueue.emptyTimedQueue();
        }


    }
}

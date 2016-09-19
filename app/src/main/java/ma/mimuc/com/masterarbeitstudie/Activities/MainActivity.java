package ma.mimuc.com.masterarbeitstudie.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import ma.mimuc.com.masterarbeitstudie.LoggingService;
import ma.mimuc.com.masterarbeitstudie.NotificationHandler;
import ma.mimuc.com.masterarbeitstudie.OtherLoggers.NotificationLoggerService;
import ma.mimuc.com.masterarbeitstudie.R;
import ma.mimuc.com.masterarbeitstudie.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {


    private Button button, button2, button3, button4,button6;
    private SharedPreferencesManager sharedPreferencesManager;
    private EditText editText;
    private NotificationHandler notificationHandler;

//TODO start application on device launch

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

        //SETUP SHARED PREFERNCES
        if(!sharedPreferencesManager.keyExists()){
            Intent usageIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(usageIntent);
        }
        if(!sharedPreferencesManager.keyExists()){
            Intent notifiactionsIntent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(notifiactionsIntent);
        }

        if(!sharedPreferencesManager.uploadTimesExists()){
            sharedPreferencesManager.saveUploadTimes(0);
        }

        if(!sharedPreferencesManager.lastRestingStateTimeExists()){
            sharedPreferencesManager.saveLastRestingStateTime(0);
        }

        if(!sharedPreferencesManager.lastAnsweredPollExists()){
            sharedPreferencesManager.saveLastAnsweredPollTime(0);
        }
        if(!sharedPreferencesManager.lastDeletedNotificationTimeExists()){
            sharedPreferencesManager.saveLastDeletedNotificationTime(0);
        }
        if(!sharedPreferencesManager.lastUnansweredPollTimeExists()){
            sharedPreferencesManager.saveLastUnansweredPollTime(0);
        }
        if(!sharedPreferencesManager.lastNotificationPostedTimeExists()){
            sharedPreferencesManager.saveLastNotificationPostedTime(0);
        }






        AlarmManager scheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), LoggingService.class);
        PendingIntent scheduledIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, scheduledIntent);


        Intent intent2 = new Intent(getApplicationContext(), NotificationLoggerService.class);
        PendingIntent scheduledIntent2 = PendingIntent.getService(getApplicationContext(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, scheduledIntent2);


/*
        button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                databaseHandler.exportDB();
            }
        });

        button2= (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Upload().execute();
            }
        });

        editText=(EditText) findViewById(R.id.editTextSubjectID);

        button3= (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesManager.saveSubjectID(Long.parseLong(editText.getText().toString()));
            }
        });

        notificationHandler= new NotificationHandler(getApplicationContext());

        button4= (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button works");
                notificationHandler.askUserToAnswerQuestion();
            }
        });

        button6= (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesManager.deleteCustomSlots();
            }
        });

*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_debugging:
                Intent intent = new Intent(this,DebuggingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
/*
    public void changeActivity(View view){
        Intent intent=new Intent(this,PollActivity.class);
        startActivity(intent);


    }*/
/*

    private class Upload extends AsyncTask<Void, Void, String> {

        private int restingStateID;
        private boolean restingStateStatus;

        @Override
        protected String doInBackground(Void... params) {
            UploadDB uploadDB= new UploadDB(getApplicationContext());
            uploadDB.uploadData();
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
    /*
        protected void onPostExecute(String result) {
            System.out.println("SUCCESS");
        }


    }*/

}


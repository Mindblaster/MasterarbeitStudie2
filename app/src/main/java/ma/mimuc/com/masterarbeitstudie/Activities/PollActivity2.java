package ma.mimuc.com.masterarbeitstudie.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.R;
import ma.mimuc.com.masterarbeitstudie.SharedPreferencesManager;

public class PollActivity2 extends AppCompatActivity {

    private DatabaseHandler databaseHandler;
    private Timestamp timestamp;
    private SharedPreferencesManager sharedPreferencesManager;

    private RadioGroup likertScale;

    private RadioButton unsafeRadioButton,probablyUnsafeRadioButton,neutralRadioButton, probablySafeRadioButton,safeRadioButton;

    private EditText feedback;

    private Button backButton, cancelButton, applyButton;

    private String Location;

    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll2);

        thisActivity=this;
        databaseHandler = new DatabaseHandler(getApplicationContext());
        timestamp = new Timestamp();
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

        Location=getIntent().getStringExtra("Location");
        likertScale=(RadioGroup)findViewById(R.id.likertScale);

        unsafeRadioButton=(RadioButton)findViewById(R.id.unsafe);
        probablyUnsafeRadioButton=(RadioButton)findViewById(R.id.probablyUnsafe);
        neutralRadioButton=(RadioButton)findViewById(R.id.neutral);
        probablySafeRadioButton=(RadioButton)findViewById(R.id.probablySafe);
        safeRadioButton=(RadioButton)findViewById(R.id.safe);

        feedback=(EditText)findViewById(R.id.feedback);


        backButton=(Button) findViewById(R.id.backButton);
        cancelButton=(Button) findViewById(R.id.cancelButton2);
        applyButton=(Button) findViewById(R.id.applyButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                builder.setMessage("Wollen Sie diese Fragen nicht beantworten?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create().show();

            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likertScale.getCheckedRadioButtonId()==-1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                    builder.setMessage("Bitte bewerten Sie die Sicherheit des Ortes!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .create().show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                    builder.setMessage("Sind folgende Daten richtig? \n Ort: " + Location + " Sicherheit: " + getChosenRadioButton())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //write data to DB
                                    String feedbackString=feedback.getText().toString();
                                    databaseHandler.insertUserAnswerIntoDatabase(Location,getChosenRadioButton(),feedbackString,sharedPreferencesManager.getSubjectID(),System.currentTimeMillis(),timestamp.getCurrentTimeStamp(),"USER_ANSWER",sharedPreferencesManager.getKey()-1);
                                    sharedPreferencesManager.saveLastAnsweredPollTime(System.currentTimeMillis());
                                    new WriteUserStudyPerformanceToDatabase().execute();
                                    finishAffinity();
                                }
                            })
                            .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create().show();
                }
            }
        });


    }
    private String getChosenRadioButton(){
        int selectedId = likertScale.getCheckedRadioButtonId();
        //Should never happen
        if(selectedId==-1){
            return("Something went wrong");
        }
        else if(selectedId==unsafeRadioButton.getId()){
            return("Unsicher");
        }
        else if(selectedId==probablyUnsafeRadioButton.getId()){
            return("Eher Unsicher");
        }
        else if(selectedId==neutralRadioButton.getId()){
            return("Neutral");
        }
        else if(selectedId==probablySafeRadioButton.getId()){
            return("Eher Sicher");
        }
        else if(selectedId==safeRadioButton.getId()){
            return("Sicher");
        }
        //should never happen
        return("");
    }


    private class WriteUserStudyPerformanceToDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void ... params) {
            SensorMeasurement sensorMeasurement=new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
            sensorMeasurement.setSensor("USER_STUDY_PERFORMANCE");
            sensorMeasurement.setTag("ALL QUESTIONS ANSWERED");
            databaseHandler.insertUserStudyPerformanceIntoDatabase(sensorMeasurement);
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {

        }
    }
}

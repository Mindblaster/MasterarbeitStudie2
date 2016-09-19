package ma.mimuc.com.masterarbeitstudie.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.NotificationHandler;
import ma.mimuc.com.masterarbeitstudie.R;
import ma.mimuc.com.masterarbeitstudie.SharedPreferencesManager;
import ma.mimuc.com.masterarbeitstudie.UploadDB;

public class DebuggingActivity extends AppCompatActivity {


    private Button button, button2, button3, button4,button6;
    private SharedPreferencesManager sharedPreferencesManager;
    private EditText editText;
    private NotificationHandler notificationHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugging);
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_debugging, menu);
        return true;
    }

    public void changeActivity(View view){
        Intent intent=new Intent(this,PollActivity.class);
        startActivity(intent);
    }

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
        protected void onPostExecute(String result) {
            System.out.println("SUCCESS");
        }


    }
}

package ma.mimuc.com.masterarbeitstudie.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ma.mimuc.com.masterarbeitstudie.R;
import ma.mimuc.com.masterarbeitstudie.SharedPreferencesManager;

public class CreateCustomLocationActivity extends AppCompatActivity {

    private Button previewButton;
    private EditText locationName;
    private Button acceptLocationButton;
    private Button addLocationToListButton;
    private Activity thisActivity;

    private boolean locationSet=false;

    private final int MAX_CHARS_IN_PREVIEW_BUTTON=10;
    private String fullLocationName;

    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_location);
        fullLocationName="";

        previewButton=(Button)findViewById(R.id.previewButton);
        locationName=(EditText)findViewById(R.id.locationName);
        acceptLocationButton=(Button)findViewById(R.id.locationAddButton);
        addLocationToListButton=(Button)findViewById(R.id.addLocationToListButton);
        sharedPreferencesManager=new SharedPreferencesManager(getApplicationContext());

        thisActivity=this;


        acceptLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locationName.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                    builder.setMessage("Bitte geben Sie einen Ortsnamen ein!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }
                else{
                    fullLocationName=locationName.getText().toString();
                    if(fullLocationName.length()>12) {
                        previewButton.setText(locationName.getText().toString().substring(0, MAX_CHARS_IN_PREVIEW_BUTTON));
                    }
                    else{
                        previewButton.setText(locationName.getText().toString());
                    }
                    locationSet=true;
                }

            }
        });


        addLocationToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!locationSet){
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                    builder.setMessage("Bitte fügen Sie einen Ortsnamen wie beschrieben hinzu!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            }).create().show();
                }
                else{
                    //Add in shared preferences by checking which slot is used

                    //This should never happen as adding new Locations should not be possible
                    if(sharedPreferencesManager.checkSlots()>=7){
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setMessage("Sie haben bereits 7 eigene Orte erstellt mehr sind leider nicht möglich, gehen Sie über den Zurück Button oben links oder mit der Zurück Taste zurück zum Fragebogen")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                }).create().show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                            builder.setMessage("Sind Sie sicher, dass Sie diesen Ort hinzufügen wollen? Dies kann nicht rückgängig gemacht werden.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            switch(sharedPreferencesManager.checkSlots()) {
                                                case (0):
                                                    sharedPreferencesManager.saveCustomSlot1(fullLocationName);
                                                    break;
                                                case (1):
                                                    sharedPreferencesManager.saveCustomSlot2(fullLocationName);
                                                    break;
                                                case (2):
                                                    sharedPreferencesManager.saveCustomSlot3(fullLocationName);
                                                    break;
                                                case (3):
                                                    sharedPreferencesManager.saveCustomSlot4(fullLocationName);
                                                    break;
                                                case (4):
                                                    sharedPreferencesManager.saveCustomSlot5(fullLocationName);
                                                    break;
                                                case (5):
                                                    sharedPreferencesManager.saveCustomSlot6(fullLocationName);
                                                    break;
                                                case (6):
                                                    sharedPreferencesManager.saveCustomSlot7(fullLocationName);
                                                    break;
                                                default:
                                                    //should never happen
                                                    break;
                                            }
                                            finish();
                                            finish();
                                        }
                                    }).setNegativeButton("Abbrechen",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                }).create().show();
                        }
                }

            }
        });


    }



}

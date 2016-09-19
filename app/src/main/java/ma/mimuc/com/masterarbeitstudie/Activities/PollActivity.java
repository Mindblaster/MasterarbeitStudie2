package ma.mimuc.com.masterarbeitstudie.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ma.mimuc.com.masterarbeitstudie.Database.DatabaseHandler;
import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.R;
import ma.mimuc.com.masterarbeitstudie.SharedPreferencesManager;

public class PollActivity extends Activity {


    private String Answer="";
    private DatabaseHandler databaseHandler;
    private Timestamp timestamp;
    private SharedPreferencesManager sharedPreferencesManager;
    private Activity thisActivity;
    private int highlightedButton=-1;
    private String LocationName="";
    private final int MAX_CHARS_IN_BUTTON=10;


    private Button homeButton,workbutton,schoolButton,visitingButton,barButton,restaurantButton,hotelButton,clubButton,carButton,busButton,
            outsideButton,enrouteButton,customButton1,customButton2, customButton3,customButton4,customButton5, customButton6,customButton7, addButton;

    private Button continueButton,cancelButton;

    private TextView question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);


        databaseHandler = new DatabaseHandler(getApplicationContext());
        timestamp = new Timestamp();
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        thisActivity = this;

        question= (TextView) findViewById(R.id.question);

        long lastRSTime = getIntent().getLongExtra("RestingStateTime", 0);
        Date date = new Date();
        date.setTime(lastRSTime);
        String formattedDate=new SimpleDateFormat("HH:mm").format(date);


        Date date2 = new Date();
        date2.setTime(lastRSTime);
        String formattedDate2=new SimpleDateFormat("dd.MM.yyyy").format(date);



        question.setText( "Wo waren Sie um 19:25 am 22.04.2016 ?");//+formattedDate+ " am "+  formattedDate2 + " ?");


        homeButton = (Button) findViewById(R.id.homeButton);
        workbutton = (Button) findViewById(R.id.workButton);
        schoolButton = (Button) findViewById(R.id.schoolButton);
        visitingButton = (Button) findViewById(R.id.visitingButton);
        barButton = (Button) findViewById(R.id.barButton);
        restaurantButton = (Button) findViewById(R.id.restaurantButton);
        hotelButton = (Button) findViewById(R.id.hotelButton);
        clubButton = (Button) findViewById(R.id.clubButton);
        carButton = (Button) findViewById(R.id.carButton);
        busButton = (Button) findViewById(R.id.busButton);
        outsideButton = (Button) findViewById(R.id.outsideButton);
        enrouteButton = (Button) findViewById(R.id.enrouteButton);
        customButton1 = (Button) findViewById(R.id.customButton1);
        customButton2 = (Button) findViewById(R.id.customButton2);
        customButton3 = (Button) findViewById(R.id.customButton3);
        customButton4 = (Button) findViewById(R.id.customButton4);
        customButton5 = (Button) findViewById(R.id.customButton5);
        customButton6 = (Button) findViewById(R.id.customButton6);
        customButton7 = (Button) findViewById(R.id.customButton7);
        addButton = (Button) findViewById(R.id.addButton);


        cancelButton = (Button) findViewById(R.id.cancelButton);
        continueButton = (Button) findViewById(R.id.continueButton);

        checkCustomLocations();

        setButtonListeners();


        new WriteUserStudyPerformanceToDatabase().execute();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sharedPreferencesManager.checkSlots()) {
                    case (7):
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setMessage("Sie haben bereits 7 eigene Orte erstellt mehr sind leider nicht möglich.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                }).create().show();
                        break;
                    default:
                        Intent intent = new Intent(getApplicationContext(), CreateCustomLocationActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });

    }

    private void setButtonListeners(){

        //LOcation Highlight Buttons
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highlightedButton!=0){
                    homeButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=0;
                    LocationName=homeButton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        workbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highlightedButton!=1){
                    workbutton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=1;
                    LocationName=workbutton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        schoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highlightedButton!=2){
                    schoolButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=2;
                    LocationName=schoolButton.getText().toString();
                }
                else{
                    uncheckButton();
                }

            }
        });

        visitingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highlightedButton!=3){
                    visitingButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=3;
                    LocationName=visitingButton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        barButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highlightedButton!=4){
                    barButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=4;
                    LocationName=barButton.getText().toString();
                }
                else{
                    uncheckButton();
                }

            }
        });
        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highlightedButton!=5){
                    restaurantButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=5;
                    LocationName=restaurantButton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        hotelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=6){
                    hotelButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=6;
                    LocationName=hotelButton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        clubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=7){
                    clubButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=7;
                    LocationName=homeButton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        carButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=8){
                    carButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=8;
                    LocationName=carButton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        busButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=9){
                    busButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=9;
                    LocationName=busButton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        outsideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=10){
                    outsideButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=10;
                    LocationName=outsideButton.getText().toString();
                }
                else{
                    uncheckButton();
                }

            }
        });
        enrouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highlightedButton!=11){
                    enrouteButton.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=11;
                    LocationName=enrouteButton.getText().toString();
                }
                else{
                    uncheckButton();
                }
            }
        });
        customButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=12){
                    customButton1.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=12;
                    LocationName=sharedPreferencesManager.getCustomLocationNameSlot1();
                }
                else{
                    uncheckButton();
                }

            }
        });
        customButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=13){
                    customButton2.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=13;
                    LocationName=sharedPreferencesManager.getCustomLocationNameSlot2();
                }
                else{
                    uncheckButton();
                }
            }
        });
        customButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highlightedButton!=14){
                    customButton3.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=14;
                    LocationName=sharedPreferencesManager.getCustomLocationNameSlot3();
                }
                else{
                    uncheckButton();
                }

            }
        });

        customButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=15){
                    customButton4.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=15;
                    LocationName=sharedPreferencesManager.getCustomLocationNameSlot4();
                }
                else{
                    uncheckButton();
                }
            }
        });
        customButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=16){
                    customButton5.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=16;
                    LocationName=sharedPreferencesManager.getCustomLocationNameSlot5();
                }
                else{
                    uncheckButton();
                }
            }
        });
        customButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=17){
                    customButton6.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=17;
                    LocationName=sharedPreferencesManager.getCustomLocationNameSlot6();
                }
                else{
                    uncheckButton();
                }
            }
        });
        customButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(highlightedButton!=18){
                    customButton7.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
                    uncheckButton();
                    highlightedButton=18;
                    LocationName =sharedPreferencesManager.getCustomLocationNameSlot7();
                }
                else{
                    uncheckButton();
                }
            }
        });

        //Continue and Cancel Button

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
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LocationName.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                    builder.setMessage("Wollen Sie keinen Ort angeben?")
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent= new Intent(thisActivity, PollActivity2.class);
                                    intent.putExtra("Location",LocationName);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create().show();

                }
                else{
                    Intent intent= new Intent(thisActivity, PollActivity2.class);
                    intent.putExtra("Location",LocationName);
                    startActivity(intent);
                }
            }
        });





    }

    public void uncheckButton(){
        switch(highlightedButton){
            case(-1):
                break;
            case(0):
                homeButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(1):
                workbutton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(2):
                schoolButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(3):
                visitingButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(4):
                barButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(5):
                restaurantButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(6):
                hotelButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(7):
                clubButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(8):
                carButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(9):
                busButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(10):
                outsideButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(11):
                enrouteButton.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(12):
                customButton1.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(13):
                customButton2.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(14):
                customButton3.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(15):
                customButton4.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(16):
                customButton5.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(17):
                customButton6.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case(18):
                customButton7.setBackgroundResource(android.R.drawable.btn_default);
                break;
            default:
                break;
        }
        LocationName="";
        highlightedButton=-1;
    }

    public void checkCustomLocations(){
        switch (sharedPreferencesManager.checkSlots()) {
            case (0):
                break;
            case (1):
                customButton1.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot1().length()>MAX_CHARS_IN_BUTTON) {
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1());
                }
                break;
            case (2):
                customButton1.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot1().length()>MAX_CHARS_IN_BUTTON) {
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1());
                }
                customButton2.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot2().length()>MAX_CHARS_IN_BUTTON) {
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2());
                }
                break;
            case (3):
                customButton1.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot1().length()>MAX_CHARS_IN_BUTTON) {
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1());
                }
                customButton2.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot2().length()>MAX_CHARS_IN_BUTTON) {
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2());
                }
                customButton3.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot3().length()>MAX_CHARS_IN_BUTTON) {
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3());
                }
                break;
            case (4):
                customButton1.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot1().length()>MAX_CHARS_IN_BUTTON) {
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1());
                }
                customButton2.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot2().length()>MAX_CHARS_IN_BUTTON) {
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2());
                }
                customButton3.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot3().length()>MAX_CHARS_IN_BUTTON) {
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3());
                }
                customButton4.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot4().length()>MAX_CHARS_IN_BUTTON) {
                    customButton4.setText(sharedPreferencesManager.getCustomLocationNameSlot4().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton4.setText(sharedPreferencesManager.getCustomLocationNameSlot4());
                }
                break;
            case (5):
                customButton1.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot1().length()>MAX_CHARS_IN_BUTTON) {
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1());
                }
                customButton2.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot2().length()>MAX_CHARS_IN_BUTTON) {
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2());
                }
                customButton3.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot3().length()>MAX_CHARS_IN_BUTTON) {
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3());
                }
                customButton4.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot4().length()>MAX_CHARS_IN_BUTTON) {
                    customButton4.setText(sharedPreferencesManager.getCustomLocationNameSlot4().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton4.setText(sharedPreferencesManager.getCustomLocationNameSlot4());
                }
                customButton5.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot5().length()>MAX_CHARS_IN_BUTTON) {
                    customButton5.setText(sharedPreferencesManager.getCustomLocationNameSlot5().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton5.setText(sharedPreferencesManager.getCustomLocationNameSlot5());
                }
                break;
            case (6):
                customButton1.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot1().length()>MAX_CHARS_IN_BUTTON) {
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1());
                }
                customButton2.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot2().length()>MAX_CHARS_IN_BUTTON) {
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2());
                }
                customButton3.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot3().length()>MAX_CHARS_IN_BUTTON) {
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3());
                }
                customButton4.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot4().length()>MAX_CHARS_IN_BUTTON) {
                    customButton4.setText(sharedPreferencesManager.getCustomLocationNameSlot4().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton4.setText(sharedPreferencesManager.getCustomLocationNameSlot4());
                }
                customButton5.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot5().length()>MAX_CHARS_IN_BUTTON) {
                    customButton5.setText(sharedPreferencesManager.getCustomLocationNameSlot5().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton5.setText(sharedPreferencesManager.getCustomLocationNameSlot5());
                }
                customButton6.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot6().length()>MAX_CHARS_IN_BUTTON) {
                    customButton6.setText(sharedPreferencesManager.getCustomLocationNameSlot6().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton6.setText(sharedPreferencesManager.getCustomLocationNameSlot6());
                }
                break;
            case (7):
                customButton1.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot1().length()>MAX_CHARS_IN_BUTTON) {
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton1.setText(sharedPreferencesManager.getCustomLocationNameSlot1());
                }
                customButton2.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot2().length()>MAX_CHARS_IN_BUTTON) {
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton2.setText(sharedPreferencesManager.getCustomLocationNameSlot2());
                }
                customButton3.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot3().length()>MAX_CHARS_IN_BUTTON) {
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton3.setText(sharedPreferencesManager.getCustomLocationNameSlot3());
                }
                customButton4.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot4().length()>MAX_CHARS_IN_BUTTON) {
                    customButton4.setText(sharedPreferencesManager.getCustomLocationNameSlot4().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton4.setText(sharedPreferencesManager.getCustomLocationNameSlot4());
                }
                customButton5.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot5().length()>MAX_CHARS_IN_BUTTON) {
                    customButton5.setText(sharedPreferencesManager.getCustomLocationNameSlot5().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton5.setText(sharedPreferencesManager.getCustomLocationNameSlot5());
                }
                customButton6.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot6().length()>MAX_CHARS_IN_BUTTON) {
                    customButton6.setText(sharedPreferencesManager.getCustomLocationNameSlot6().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton6.setText(sharedPreferencesManager.getCustomLocationNameSlot6());
                }
                customButton7.setVisibility(View.VISIBLE);
                if(sharedPreferencesManager.getCustomLocationNameSlot7().length()>MAX_CHARS_IN_BUTTON) {
                    customButton7.setText(sharedPreferencesManager.getCustomLocationNameSlot7().substring(0, MAX_CHARS_IN_BUTTON));
                }
                else{
                    customButton7.setText(sharedPreferencesManager.getCustomLocationNameSlot7());
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkCustomLocations();
    }


    private class WriteUserStudyPerformanceToDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void ... params) {
            SensorMeasurement sensorMeasurement=new SensorMeasurement(0,0,0,System.currentTimeMillis(),timestamp.getCurrentTimeStamp());
            sensorMeasurement.setSensor("USER_STUDY_PERFORMANCE");
            sensorMeasurement.setTag("NOTIFICATION CLICKED");
            databaseHandler.insertUserStudyPerformanceIntoDatabase(sensorMeasurement);
            return "";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(String result) {

        }
    }
}

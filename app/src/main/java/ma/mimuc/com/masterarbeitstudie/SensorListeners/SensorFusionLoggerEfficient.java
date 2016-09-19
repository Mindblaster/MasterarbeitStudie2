package ma.mimuc.com.masterarbeitstudie.SensorListeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ma.mimuc.com.masterarbeitstudie.Objects.SensorMeasurement;
import ma.mimuc.com.masterarbeitstudie.Objects.Timestamp;
import ma.mimuc.com.masterarbeitstudie.TimedQueue;

/************************************************************************************
 * Copyright (c) 2012 Paul Lawitzki
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 ************************************************************************************/

public class SensorFusionLoggerEfficient implements SensorEventListener {

    //FOR SENSOR FUSION
    private TimedQueue timedQueue;
    private Timestamp timestampforData;
    private SensorMeasurement currentMeasurement;

    private final long MEASUREMENT_INTERVALL=20000; //time that values are saved in ms

    //FOR GYRO ONLY
    private TimedQueue timedQueueGyro;
    private Timestamp timestampforGyro;
    private SensorMeasurement currentGyroMeasurement;

    private final long MEASUREMENT_INTERVALL_GYRO=600000; //time that values are saved in ms
    private final int CUSTOM_SENSOR_DELAY=500000;//0.1 seconds in microseconds



    private SensorManager mSensorManager = null;

    // angular speeds from gyro
    private float[] gyro = new float[3];

    // rotation matrix from gyro data
    private float[] gyroMatrix = new float[9];

    // orientation angles from gyro matrix
    private float[] gyroOrientation = new float[3];

    // magnetic field vector
    private float[] magnet = new float[3];

    // accelerometer vector
    private float[] accel = new float[3];

    // orientation angles from accel and magnet
    private float[] accMagOrientation = new float[3];

    // final orientation angles from sensor fusion
    private float[] fusedOrientation = new float[3];

    // accelerometer and magnetometer based rotation matrix
    private float[] rotationMatrix = new float[9];

    public static final float EPSILON = 0.000000001f;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private boolean initState = true;

    public static final int TIME_CONSTANT = 60;
    public static final float FILTER_COEFFICIENT = 0.98f;
    private Timer fuseTimer = new Timer();
    public Handler mHandler;


    //external objects
    private float[] normValues;

    private float[] initMatrix;

    private float[] test;

    private float[] deltaVector;

    private float[] deltaMatrix;

    private float[] xM;
    private float[] yM;
    private float[] zM;

    private float[] resultMatrix;

    private float[] result;

    private float[] resultGyroInitMatrxMult= new float[9];
    private float[] resultGyroDeltaMatrxMult= new float[9];
    private float[] resultxMyMMult= new float[9];
    private float[] resultzMResultMult= new float[9];

    private ArrayList<SensorMeasurement> sensorMeasurements;

    private long lastmeasurementAdded;



    DecimalFormat d = new DecimalFormat("#.##");


    public SensorFusionLoggerEfficient(Context context){

        timedQueue= new TimedQueue();
        timestampforData=new Timestamp();

        timedQueueGyro= new TimedQueue();
        timestampforGyro=new Timestamp();

        sensorMeasurements= new ArrayList<SensorMeasurement>();

        gyroOrientation[0] = 0.0f;
        gyroOrientation[1] = 0.0f;
        gyroOrientation[2] = 0.0f;

        // initialise gyroMatrix with identity matrix
        gyroMatrix[0] = 1.0f; gyroMatrix[1] = 0.0f; gyroMatrix[2] = 0.0f;
        gyroMatrix[3] = 0.0f; gyroMatrix[4] = 1.0f; gyroMatrix[5] = 0.0f;
        gyroMatrix[6] = 0.0f; gyroMatrix[7] = 0.0f; gyroMatrix[8] = 1.0f;

        // get sensorManager and initialise sensor listeners
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        initListeners();

        // wait for one second until gyroscope and magnetometer/accelerometer
        // data is initialised then scedule the complementary filter task
        fuseTimer.scheduleAtFixedRate(new calculateFusedOrientationTask(),
                1000, TIME_CONSTANT);

        lastmeasurementAdded=System.currentTimeMillis();


    }
    // This function registers sensor listeners for the accelerometer, magnetometer and gyroscope.
    public void initListeners(){
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                CUSTOM_SENSOR_DELAY);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                CUSTOM_SENSOR_DELAY);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                CUSTOM_SENSOR_DELAY);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                // copy new accelerometer data into accel array and calculate orientation
                System.arraycopy(event.values, 0, accel, 0, 3);
                calculateAccMagOrientation();
                break;

            case Sensor.TYPE_GYROSCOPE:
                //Save pure Gyro Data
                currentGyroMeasurement= new SensorMeasurement(event.values[0],event.values[1],event.values[2],System.currentTimeMillis(),timestampforData.getCurrentTimeStamp());
                currentGyroMeasurement.setSensor("GYROSCOPE");
                currentGyroMeasurement.setTag("");
                timedQueueGyro.addToSensorMeasurements(currentGyroMeasurement,MEASUREMENT_INTERVALL);
                // process gyro data
                gyroFunction(event);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                // copy new magnetometer data into magnet array
                System.arraycopy(event.values, 0, magnet, 0, 3);
                break;
        }
    }
    // calculates orientation angles from accelerometer and magnetometer output
    public void calculateAccMagOrientation() {
        if(SensorManager.getRotationMatrix(rotationMatrix, null, accel, magnet)) {
            SensorManager.getOrientation(rotationMatrix, accMagOrientation);
        }
    }

    // This function is borrowed from the Android reference
    // at http://developer.android.com/reference/android/hardware/SensorEvent.html#values
    // It calculates a rotation vector from the gyroscope angular speed values.

    //getRotationVectorFromGyro(gyro, deltaVector, dT / 2.0f);
    private void getRotationVectorFromGyro(float timeFactor)
    {

        if(normValues==null){
            normValues=new float[3];
        }
        else{
            for(int i=0;i<normValues.length;i++){
                normValues[i]=0;
            }
        }


        // Calculate the angular speed of the sample
        float omegaMagnitude =
                (float)Math.sqrt(gyro[0] * gyro[0] +
                        gyro[1] * gyro[1] +
                        gyro[2] * gyro[2]);

        // Normalize the rotation vector if it's big enough to get the axis
        if(omegaMagnitude > EPSILON) {
            normValues[0] = gyro[0] / omegaMagnitude;
            normValues[1] = gyro[1] / omegaMagnitude;
            normValues[2] = gyro[2] / omegaMagnitude;
        }

        // Integrate around this axis with the angular speed by the timestep
        // in order to get a delta rotation from this sample over the timestep
        // We will convert this axis-angle representation of the delta rotation
        // into a quaternion before turning it into the rotation matrix.
        float thetaOverTwo = omegaMagnitude * timeFactor;
        float sinThetaOverTwo = (float)Math.sin(thetaOverTwo);
        float cosThetaOverTwo = (float)Math.cos(thetaOverTwo);
        deltaVector[0] = sinThetaOverTwo * normValues[0];
        deltaVector[1] = sinThetaOverTwo * normValues[1];
        deltaVector[2] = sinThetaOverTwo * normValues[2];
        deltaVector[3] = cosThetaOverTwo;
    }

    // This function performs the integration of the gyroscope data.
    // It writes the gyroscope based orientation into gyroOrientation.
    public void gyroFunction(SensorEvent event) {
        // don't start until first accelerometer/magnetometer orientation has been acquired
        if (accMagOrientation == null)
            return;

        // initialisation of the gyroscope based rotation matrix
        if(initState) {

            if(initMatrix==null) {
                initMatrix = new float[9];
            }
            else{
                for(int i=0;i<initMatrix.length;i++){
                    initMatrix[i]=0;
                }
            }
            getRotationMatrixFromOrientationAccMag();

            if(test==null){
                test = new float[3];
            }
            else{
                for(int i=0;i<test.length;i++){
                    test[i]=0;
                }
            }
            SensorManager.getOrientation(initMatrix, test);
            matrixMultiplicationGyroInit();
            initState = false;
        }

        // copy the new gyro values into the gyro array
        // convert the raw gyro data into a rotation vector
        if(deltaVector==null){
            deltaVector = new float[4];

        }
        else{
            for(int i=0;i<deltaVector.length;i++){
                deltaVector[i]=0;
            }
        }
        if(timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            System.arraycopy(event.values, 0, gyro, 0, 3);
            getRotationVectorFromGyro(dT / 2.0f);
        }

        // measurement done, save current time for next interval
        timestamp = event.timestamp;

        // convert rotation vector into rotation matrix
        if(deltaMatrix==null){
            deltaMatrix = new float[9];
        }
        else{
            for(int i=0;i<deltaMatrix.length;i++){
                deltaMatrix[i]=0;
            }
        }

        SensorManager.getRotationMatrixFromVector(deltaMatrix, deltaVector);

        // apply the new rotation interval on the gyroscope based rotation matrix
        matrixMultiplicationGyroDelta();

        // get the gyroscope based orientation from the rotation matrix
        SensorManager.getOrientation(gyroMatrix, gyroOrientation);
    }


    private void getRotationMatrixFromOrientationAccMag() {
        if(xM==null){
            xM = new float[9];
        }
        else{
            for(int i=0;i<xM.length;i++){
                xM[i]=0;
            }
        }

        if(yM==null){
            yM = new float[9];
        }
        else{
            for(int i=0;i<yM.length;i++){
                yM[i]=0;
            }
        }

        if(zM==null){
            zM = new float[9];
        }
        else{
            for(int i=0;i<zM.length;i++){
                zM[i]=0;
            }
        }


        float sinX = (float)Math.sin(accMagOrientation[1]);
        float cosX = (float)Math.cos(accMagOrientation[1]);
        float sinY = (float)Math.sin(accMagOrientation[2]);
        float cosY = (float)Math.cos(accMagOrientation[2]);
        float sinZ = (float)Math.sin(accMagOrientation[0]);
        float cosZ = (float)Math.cos(accMagOrientation[0]);

        // rotation about x-axis (pitch)
        xM[0] = 1.0f; xM[1] = 0.0f; xM[2] = 0.0f;
        xM[3] = 0.0f; xM[4] = cosX; xM[5] = sinX;
        xM[6] = 0.0f; xM[7] = -sinX; xM[8] = cosX;

        // rotation about y-axis (roll)
        yM[0] = cosY; yM[1] = 0.0f; yM[2] = sinY;
        yM[3] = 0.0f; yM[4] = 1.0f; yM[5] = 0.0f;
        yM[6] = -sinY; yM[7] = 0.0f; yM[8] = cosY;

        // rotation about z-axis (azimuth)
        zM[0] = cosZ; zM[1] = sinZ; zM[2] = 0.0f;
        zM[3] = -sinZ; zM[4] = cosZ; zM[5] = 0.0f;
        zM[6] = 0.0f; zM[7] = 0.0f; zM[8] = 1.0f;

        // rotation order is y, x, z (roll, pitch, azimuth)

        if(resultMatrix==null){
            resultMatrix=new float[9];
        }
        else{
            for(int i=0;i<resultMatrix.length;i++){
                resultMatrix[i]=0;
            }
        }

        matrixMultiplicationXmYm();
        matrixMultiplicationzMResult();
        initMatrix=resultMatrix;
    }

    private void getRotationMatrixFromOrientationFused() {
        if(xM==null){
            xM = new float[9];
        }
        else{
            for(int i=0;i<xM.length;i++){
                xM[i]=0;
            }
        }

        if(yM==null){
            yM = new float[9];
        }
        else{
            for(int i=0;i<yM.length;i++){
                yM[i]=0;
            }
        }

        if(zM==null){
            zM = new float[9];
        }
        else{
            for(int i=0;i<zM.length;i++){
                zM[i]=0;
            }
        }


        float sinX = (float)Math.sin(fusedOrientation[1]);
        float cosX = (float)Math.cos(fusedOrientation[1]);
        float sinY = (float)Math.sin(fusedOrientation[2]);
        float cosY = (float)Math.cos(fusedOrientation[2]);
        float sinZ = (float)Math.sin(fusedOrientation[0]);
        float cosZ = (float)Math.cos(fusedOrientation[0]);

        // rotation about x-axis (pitch)
        xM[0] = 1.0f; xM[1] = 0.0f; xM[2] = 0.0f;
        xM[3] = 0.0f; xM[4] = cosX; xM[5] = sinX;
        xM[6] = 0.0f; xM[7] = -sinX; xM[8] = cosX;

        // rotation about y-axis (roll)
        yM[0] = cosY; yM[1] = 0.0f; yM[2] = sinY;
        yM[3] = 0.0f; yM[4] = 1.0f; yM[5] = 0.0f;
        yM[6] = -sinY; yM[7] = 0.0f; yM[8] = cosY;

        // rotation about z-axis (azimuth)
        zM[0] = cosZ; zM[1] = sinZ; zM[2] = 0.0f;
        zM[3] = -sinZ; zM[4] = cosZ; zM[5] = 0.0f;
        zM[6] = 0.0f; zM[7] = 0.0f; zM[8] = 1.0f;

        // rotation order is y, x, z (roll, pitch, azimuth)

        if(resultMatrix==null){
            resultMatrix=new float[9];
        }
        else{
            for(int i=0;i<resultMatrix.length;i++){
                resultMatrix[i]=0;
            }
        }

        matrixMultiplicationXmYm();
        matrixMultiplicationzMResult();
        gyroMatrix=resultMatrix;
    }

    private void matrixMultiplicationGyroInit() {
        resultGyroInitMatrxMult[0] = gyroMatrix[0] * initMatrix[0] + gyroMatrix[1] * initMatrix[3] + gyroMatrix[2] * initMatrix[6];
        resultGyroInitMatrxMult[1] = gyroMatrix[0] * initMatrix[1] + gyroMatrix[1] * initMatrix[4] + gyroMatrix[2] * initMatrix[7];
        resultGyroInitMatrxMult[2] = gyroMatrix[0] * initMatrix[2] + gyroMatrix[1] * initMatrix[5] + gyroMatrix[2] * initMatrix[8];

        resultGyroInitMatrxMult[3] = gyroMatrix[3] * initMatrix[0] + gyroMatrix[4] * initMatrix[3] + gyroMatrix[5] * initMatrix[6];
        resultGyroInitMatrxMult[4] = gyroMatrix[3] * initMatrix[1] + gyroMatrix[4] * initMatrix[4] + gyroMatrix[5] * initMatrix[7];
        resultGyroInitMatrxMult[5] = gyroMatrix[3] * initMatrix[2] + gyroMatrix[4] * initMatrix[5] + gyroMatrix[5] * initMatrix[8];

        resultGyroInitMatrxMult[6] = gyroMatrix[6] * initMatrix[0] + gyroMatrix[7] * initMatrix[3] + gyroMatrix[8] * initMatrix[6];
        resultGyroInitMatrxMult[7] = gyroMatrix[6] * initMatrix[1] + gyroMatrix[7] * initMatrix[4] + gyroMatrix[8] * initMatrix[7];
        resultGyroInitMatrxMult[8] = gyroMatrix[6] * initMatrix[2] + gyroMatrix[7] * initMatrix[5] + gyroMatrix[8] * initMatrix[8];
        gyroMatrix=resultGyroInitMatrxMult;
    }

    private void matrixMultiplicationGyroDelta() {
        resultGyroDeltaMatrxMult[0] = gyroMatrix[0] * deltaMatrix[0] + gyroMatrix[1] * deltaMatrix[3] + gyroMatrix[2] * deltaMatrix[6];
        resultGyroDeltaMatrxMult[1] = gyroMatrix[0] * deltaMatrix[1] + gyroMatrix[1] * deltaMatrix[4] + gyroMatrix[2] * deltaMatrix[7];
        resultGyroDeltaMatrxMult[2] = gyroMatrix[0] * deltaMatrix[2] + gyroMatrix[1] * deltaMatrix[5] + gyroMatrix[2] * deltaMatrix[8];

        resultGyroDeltaMatrxMult[3] = gyroMatrix[3] * deltaMatrix[0] + gyroMatrix[4] * deltaMatrix[3] + gyroMatrix[5] * deltaMatrix[6];
        resultGyroDeltaMatrxMult[4] = gyroMatrix[3] * deltaMatrix[1] + gyroMatrix[4] * deltaMatrix[4] + gyroMatrix[5] * deltaMatrix[7];
        resultGyroDeltaMatrxMult[5] = gyroMatrix[3] * deltaMatrix[2] + gyroMatrix[4] * deltaMatrix[5] + gyroMatrix[5] * deltaMatrix[8];

        resultGyroDeltaMatrxMult[6] = gyroMatrix[6] * deltaMatrix[0] + gyroMatrix[7] * deltaMatrix[3] + gyroMatrix[8] * deltaMatrix[6];
        resultGyroDeltaMatrxMult[7] = gyroMatrix[6] * deltaMatrix[1] + gyroMatrix[7] * deltaMatrix[4] + gyroMatrix[8] * deltaMatrix[7];
        resultGyroDeltaMatrxMult[8] = gyroMatrix[6] * deltaMatrix[2] + gyroMatrix[7] * deltaMatrix[5] + gyroMatrix[8] * deltaMatrix[8];
        gyroMatrix=resultGyroDeltaMatrxMult;
    }


    private void matrixMultiplicationXmYm() {
        resultxMyMMult[0] = xM[0] * yM[0] + xM[1] * yM[3] + xM[2] * yM[6];
        resultxMyMMult[1] = xM[0] * yM[1] + xM[1] * yM[4] + xM[2] * yM[7];
        resultxMyMMult[2] = xM[0] * yM[2] + xM[1] * yM[5] + xM[2] * yM[8];

        resultxMyMMult[3] = xM[3] * yM[0] + xM[4] * yM[3] + xM[5] * yM[6];
        resultxMyMMult[4] = xM[3] * yM[1] + xM[4] * yM[4] + xM[5] * yM[7];
        resultxMyMMult[5] = xM[3] * yM[2] + xM[4] * yM[5] + xM[5] * yM[8];

        resultxMyMMult[6] = xM[6] * yM[0] + xM[7] * yM[3] + xM[8] * yM[6];
        resultxMyMMult[7] = xM[6] * yM[1] + xM[7] * yM[4] + xM[8] * yM[7];
        resultxMyMMult[8] = xM[6] * yM[2] + xM[7] * yM[5] + xM[8] * yM[8];
        resultMatrix= resultxMyMMult;
    }

    private void matrixMultiplicationzMResult() {

        resultzMResultMult[0] = zM[0] * resultMatrix[0] + zM[1] * resultMatrix[3] + zM[2] * resultMatrix[6];
        resultzMResultMult[1] = zM[0] * resultMatrix[1] + zM[1] * resultMatrix[4] + zM[2] * resultMatrix[7];
        resultzMResultMult[2] = zM[0] * resultMatrix[2] + zM[1] * resultMatrix[5] + zM[2] * resultMatrix[8];

        resultzMResultMult[3] = zM[3] * resultMatrix[0] + zM[4] * resultMatrix[3] + zM[5] * resultMatrix[6];
        resultzMResultMult[4] = zM[3] * resultMatrix[1] + zM[4] * resultMatrix[4] + zM[5] * resultMatrix[7];
        resultzMResultMult[5] = zM[3] * resultMatrix[2] + zM[4] * resultMatrix[5] + zM[5] * resultMatrix[8];

        resultzMResultMult[6] = zM[6] * resultMatrix[0] + zM[7] * resultMatrix[3] + zM[8] * resultMatrix[6];
        resultzMResultMult[7] = zM[6] * resultMatrix[1] + zM[7] * resultMatrix[4] + zM[8] * resultMatrix[7];
        resultzMResultMult[8] = zM[6] * resultMatrix[2] + zM[7] * resultMatrix[5] + zM[8] * resultMatrix[8];
        resultMatrix=resultzMResultMult;
    }






    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    class calculateFusedOrientationTask extends TimerTask {
        public void run() {
            float oneMinusCoeff = 1.0f - FILTER_COEFFICIENT;

            /*
             * Fix for 179° <--> -179° transition problem:
             * Check whether one of the two orientation angles (gyro or accMag) is negative while the other one is positive.
             * If so, add 360° (2 * math.PI) to the negative value, perform the sensor fusion, and remove the 360° from the result
             * if it is greater than 180°. This stabilizes the output in positive-to-negative-transition cases.
             */

            // azimuth
            if (gyroOrientation[0] < -0.5 * Math.PI && accMagOrientation[0] > 0.0) {
                fusedOrientation[0] = (float) (FILTER_COEFFICIENT * (gyroOrientation[0] + 2.0 * Math.PI) + oneMinusCoeff * accMagOrientation[0]);
                fusedOrientation[0] -= (fusedOrientation[0] > Math.PI) ? 2.0 * Math.PI : 0;
            }
            else if (accMagOrientation[0] < -0.5 * Math.PI && gyroOrientation[0] > 0.0) {
                fusedOrientation[0] = (float) (FILTER_COEFFICIENT * gyroOrientation[0] + oneMinusCoeff * (accMagOrientation[0] + 2.0 * Math.PI));
                fusedOrientation[0] -= (fusedOrientation[0] > Math.PI)? 2.0 * Math.PI : 0;
            }
            else {
                fusedOrientation[0] = FILTER_COEFFICIENT * gyroOrientation[0] + oneMinusCoeff * accMagOrientation[0];
            }

            // pitch
            if (gyroOrientation[1] < -0.5 * Math.PI && accMagOrientation[1] > 0.0) {
                fusedOrientation[1] = (float) (FILTER_COEFFICIENT * (gyroOrientation[1] + 2.0 * Math.PI) + oneMinusCoeff * accMagOrientation[1]);
                fusedOrientation[1] -= (fusedOrientation[1] > Math.PI) ? 2.0 * Math.PI : 0;
            }
            else if (accMagOrientation[1] < -0.5 * Math.PI && gyroOrientation[1] > 0.0) {
                fusedOrientation[1] = (float) (FILTER_COEFFICIENT * gyroOrientation[1] + oneMinusCoeff * (accMagOrientation[1] + 2.0 * Math.PI));
                fusedOrientation[1] -= (fusedOrientation[1] > Math.PI)? 2.0 * Math.PI : 0;
            }
            else {
                fusedOrientation[1] = FILTER_COEFFICIENT * gyroOrientation[1] + oneMinusCoeff * accMagOrientation[1];
            }

            // roll
            if (gyroOrientation[2] < -0.5 * Math.PI && accMagOrientation[2] > 0.0) {
                fusedOrientation[2] = (float) (FILTER_COEFFICIENT * (gyroOrientation[2] + 2.0 * Math.PI) + oneMinusCoeff * accMagOrientation[2]);
                fusedOrientation[2] -= (fusedOrientation[2] > Math.PI) ? 2.0 * Math.PI : 0;
            }
            else if (accMagOrientation[2] < -0.5 * Math.PI && gyroOrientation[2] > 0.0) {
                fusedOrientation[2] = (float) (FILTER_COEFFICIENT * gyroOrientation[2] + oneMinusCoeff * (accMagOrientation[2] + 2.0 * Math.PI));
                fusedOrientation[2] -= (fusedOrientation[2] > Math.PI)? 2.0 * Math.PI : 0;
            }
            else {
                fusedOrientation[2] = FILTER_COEFFICIENT * gyroOrientation[2] + oneMinusCoeff * accMagOrientation[2];
            }

            // overwrite gyro matrix and orientation with fused orientation
            // to comensate gyro drift
            getRotationMatrixFromOrientationFused();
            System.arraycopy(fusedOrientation, 0, gyroOrientation, 0, 3);
                currentMeasurement= new SensorMeasurement((float)(accMagOrientation[0] * 180 / Math.PI),(float)(accMagOrientation[1] * 180 / Math.PI),(float)(accMagOrientation[2] * 180 / Math.PI),System.currentTimeMillis(),timestampforData.getCurrentTimeStamp());
                currentMeasurement.setSensor("SENSORFUSION");
                currentMeasurement.setTag("");

            addToSensorMeasurements();

            // write data in Buffer
            //mHandler.post(updateOreintationDisplayTask);
        }
    }
    public ArrayList<SensorMeasurement> getCurrentSensorFusionMeasurements(){
        System.out.println("Measurement requested");
        return this.getSensorMeasurements();
    }
    public void deleteSensorFusionMeasurements(){
        this.emptyTimedQueue();
    }

    public ArrayList<SensorMeasurement> getCurrentGyroMeasurements(){
        return timedQueueGyro.getSensorMeasurements();
    }
    public void deleteGyroMeasurements(){
        timedQueueGyro.emptyTimedQueue();
    }

    public void addToSensorMeasurements(){
        sensorMeasurements.add(currentMeasurement);
        if(MEASUREMENT_INTERVALL!=0) {

            //throttle filter to once per second
            if((System.currentTimeMillis()- lastmeasurementAdded)>5000) {
                filterSensorMeasurements(System.currentTimeMillis());
                lastmeasurementAdded=System.currentTimeMillis();
            }
        }
        //if savedIntervall is 0 all entries should be saved
    }

    public void filterSensorMeasurements(long currentTimeInMs){
        for(int i=0; i< sensorMeasurements.size();i++){
            if(currentTimeInMs - sensorMeasurements.get(i).getTimestampSysTime()>= MEASUREMENT_INTERVALL){
                sensorMeasurements.remove(i);
            }
        }
    }

    public ArrayList<SensorMeasurement> getSensorMeasurements(){
        return this.sensorMeasurements;
    }

    public void emptyTimedQueue(){
        sensorMeasurements.clear();
    }

}

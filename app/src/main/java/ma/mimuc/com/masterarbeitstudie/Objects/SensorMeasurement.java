package ma.mimuc.com.masterarbeitstudie.Objects;

/**
 * Created by Raphael on 10.11.2015.
 */
public class SensorMeasurement {
    private float xValue;  //REQUIRED
    private float yValue;
    private float zValue;
    private long timestampSysTime;
    private String timeStampFormatted;
    private long restingStateID;
    private String tag;
    private String sensor;
    private String packageName;
    private String notificationID;

    public SensorMeasurement(float xValue,float yValue, float zValue, long timestampSysTime, String timeStampFormatted){
        this.xValue=xValue;
        this.yValue=yValue;
        this.zValue=zValue;
        this.timestampSysTime=timestampSysTime;
        this.timeStampFormatted=timeStampFormatted;
    }


    public float getxValue(){
        return this.xValue;
    }
    public float getyValue(){
        return this.yValue;
    }
    public float getzValue(){
        return this.zValue;
    }

    public void setxValue(float xValue){
        this.xValue=xValue;
    }
    public void setyValue(float yValue){
        this.yValue=yValue;
    }
    public void setzValue(float zValue) {
        this.zValue=zValue;
    }

    public void setTimestampSysTime(long timestampSysTime){
        this.timestampSysTime=timestampSysTime;
    }

    public long getTimestampSysTime(){
        return this.timestampSysTime;
    }
    public void setTimestampFormatted(String timeStampFormatted){
        this.timeStampFormatted=timeStampFormatted;
    }
    public String getTimeStampFormatted(){
        return this.timeStampFormatted;
    }

    public void setRestingStateID(long restingStateID){
        this.restingStateID=restingStateID;
    }
    public long getRestingStateID(){
        return this.restingStateID;
    }
    public void setTag(String tag){
        this.tag=tag;
    }
    public String getTag(){
        return this.tag;
    }

    public void setSensor(String sensor){
        this.sensor=sensor;
    }

    public String getSensor(){
        return this.sensor;
    }
    public void setPackageName(String packageName){
        this.packageName=packageName;
    }
    public String getPackageName(){
        return this.packageName;
    }

    public void setNotificationID(String notificationID){
        this.notificationID=notificationID;
    }
    public String getNotificationID(){
        return this.notificationID;
    }

}

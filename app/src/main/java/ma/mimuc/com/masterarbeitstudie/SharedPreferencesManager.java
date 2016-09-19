package ma.mimuc.com.masterarbeitstudie;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;




public class SharedPreferencesManager {

    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }


    public int getKey() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int name = sharedPreferences.getInt("restingStateID", -1);
        return name;
    }

    public boolean keyExists() {
        int restingStateID = getKey();
        if (restingStateID==-1) {
            return false;
        } else return true;
    }

    public void saveRestingStateID(int id) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("restingStateID", id);
        editor.commit();
    }



    public int getSystemVersion(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int systemVersion = sharedPreferences.getInt("systemVersion", -1);
        return systemVersion;
    }

    public boolean systemVersionExists() {
        int systemVersion = getSystemVersion();
        if (systemVersion==-1) {
            return false;
        } else return true;
    }

    public void saveSystemVersion(int id) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("systemVersion", id);
        editor.commit();
    }



    public long getLastUpload(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        long lastUpload = sharedPreferences.getLong("lastUpload", -1);
        return lastUpload;
    }

    public boolean lastUploadExists() {
        long lastUpload = getLastUpload();
        if (lastUpload==-1) {
            return false;
        } else return true;
    }

    public void saveLastUpload(long lastUpload) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastUpload", lastUpload);
        editor.commit();
    }

    public long getSubjectID(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        long subjectID = sharedPreferences.getLong("subjectID", -1);
        return subjectID;
    }

    public boolean subjectIDExists() {
        long subjectID = getSubjectID();
        if (getSubjectID()==-1) {
            return false;
        } else return true;
    }

    public void saveSubjectID(long subjectID) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("subjectID", subjectID);
        editor.commit();
    }

    public long getLastRestingStateTime(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        long lastRestingStateTime = sharedPreferences.getLong("lastRestingStateTime", -1);
        return lastRestingStateTime;
    }

    public boolean lastRestingStateTimeExists() {
        long lastRestingStateTime = getLastRestingStateTime();
        if (lastRestingStateTime==-1) {
            return false;
        } else return true;
    }

    public void saveLastRestingStateTime(long lastRestingStateTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastRestingStateTime", lastRestingStateTime);
        editor.commit();
    }


    //LAST NOTIFIACTION POSTED TIME
    public long getLastNotificationPostedTime(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        long lastNotificationPostedTime = sharedPreferences.getLong("lastNotificationPostedTime", -1);
        return lastNotificationPostedTime;
    }

    public boolean lastNotificationPostedTimeExists() {
        long lastNotificationPostedTime = getLastNotificationPostedTime();
        if (lastNotificationPostedTime==-1) {
            return false;
        } else return true;
    }

    public void saveLastNotificationPostedTime(long lastNotificationPostedTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastNotificationPostedTime", lastNotificationPostedTime);
        editor.commit();
    }

    //LAST ANSWERED POLL TIME
    public long getLastAnsweredPollTime(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        long lastAnsweredPollTime = sharedPreferences.getLong("lastAnsweredPollTime", -1);
        return lastAnsweredPollTime;
    }

    public boolean lastAnsweredPollExists() {
        long lastAnsweredPollTime = getLastAnsweredPollTime();
        if (lastAnsweredPollTime==-1) {
            return false;
        } else return true;
    }

    public void saveLastAnsweredPollTime(long lastAnsweredPollTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastAnsweredPollTime", lastAnsweredPollTime);
        editor.commit();
    }

    //DELETED NOTIFICATION TIME
    public long getLastDeletedNotificationTime(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        long lastDeletedNotificationTime = sharedPreferences.getLong("lastDeletedNotificationTime", -1);
        return lastDeletedNotificationTime;
    }

    public boolean lastDeletedNotificationTimeExists() {
        long lastDeletedNotificationTime = getLastDeletedNotificationTime();
        if (lastDeletedNotificationTime==-1) {
            return false;
        } else return true;
    }

    public void saveLastDeletedNotificationTime(long lastDeletedNotificationTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastDeletedNotificationTime", lastDeletedNotificationTime);
        editor.commit();
    }

    //UNANSWERED POLL TIME
    public long getLastUnansweredPollTime(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        long lastUnansweredPollTime = sharedPreferences.getLong("lastUnansweredPollTime", -1);
        return lastUnansweredPollTime;
    }

    public boolean lastUnansweredPollTimeExists() {
        long lastUnansweredPollTime = getLastUnansweredPollTime();
        if (lastUnansweredPollTime==-1) {
            return false;
        } else return true;
    }

    public void saveLastUnansweredPollTime(long lastUnansweredPollTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastUnansweredPollTime", lastUnansweredPollTime);
        editor.commit();
    }

    //UploadTimes
    public int getUploadTimes(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int uploadTimes = sharedPreferences.getInt("uploadTimes", -1);
        return uploadTimes;
    }

    public boolean uploadTimesExists() {
        int uploadTimes = getUploadTimes();
        if (uploadTimes==-1) {
            return false;
        } else return true;
    }

    public void saveUploadTimes(int uploadTimes) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("uploadTimes", uploadTimes);
        editor.commit();
    }

    //Slots for Custom Locations


    //SLOT1

    public String getCustomLocationNameSlot1(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String customSlot1 = sharedPreferences.getString("customSlot1", "-1");
        return customSlot1;
    }

    public boolean customSlot1IsUsed() {
        String customSlot1=getCustomLocationNameSlot1();
        if (customSlot1.equals("-1")) {
            return false;
        } else return true;
    }

    public void saveCustomSlot1(String customLocationName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customSlot1", customLocationName);
        editor.commit();
    }


    //SLOT2

    public String getCustomLocationNameSlot2(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String customSlot2 = sharedPreferences.getString("customSlot2", "-1");
        return customSlot2;
    }

    public boolean customSlot2IsUsed() {
        String customSlot2=getCustomLocationNameSlot2();
        if (customSlot2.equals("-1")) {
            return false;
        } else return true;
    }

    public void saveCustomSlot2(String customLocationName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customSlot2", customLocationName);
        editor.commit();
    }


    //SLOT3
    public String getCustomLocationNameSlot3(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String customSlot3 = sharedPreferences.getString("customSlot3", "-1");
        return customSlot3;
    }

    public boolean customSlot3IsUsed() {
        String customSlot3=getCustomLocationNameSlot3();
        if (customSlot3.equals("-1")) {
            return false;
        } else return true;
    }

    public void saveCustomSlot3(String customLocationName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customSlot3", customLocationName);
        editor.commit();
    }
    //SLOT4
    public String getCustomLocationNameSlot4(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String customSlot4 = sharedPreferences.getString("customSlot4", "-1");
        return customSlot4;
    }

    public boolean customSlot4IsUsed() {
        String customSlot4=getCustomLocationNameSlot4();
        if (customSlot4.equals("-1")) {
            return false;
        } else return true;
    }

    public void saveCustomSlot4(String customLocationName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customSlot4", customLocationName);
        editor.commit();
    }
    
    //SLOT5
    public String getCustomLocationNameSlot5(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String customSlot5 = sharedPreferences.getString("customSlot5", "-1");
        return customSlot5;
    }

    public boolean customSlot5IsUsed() {
        String customSlot5=getCustomLocationNameSlot5();
        if (customSlot5.equals("-1")) {
            return false;
        } else return true;
    }

    public void saveCustomSlot5(String customLocationName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customSlot5", customLocationName);
        editor.commit();
    }
    //SLOT6
    public String getCustomLocationNameSlot6(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String customSlot6 = sharedPreferences.getString("customSlot6", "-1");
        return customSlot6;
    }

    public boolean customSlot6IsUsed() {
        String customSlot6=getCustomLocationNameSlot6();
        if (customSlot6.equals("-1")) {
            return false;
        } else return true;
    }

    public void saveCustomSlot6(String customLocationName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customSlot6", customLocationName);
        editor.commit();
    }
    //SLOT7
    public String getCustomLocationNameSlot7(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String customSlot7 = sharedPreferences.getString("customSlot7", "-1");
        return customSlot7;
    }

    public boolean customSlot7IsUsed() {
        String customSlot7=getCustomLocationNameSlot7();
        if (customSlot7.equals("-1")) {
            return false;
        } else return true;
    }

    public void saveCustomSlot7(String customLocationName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customSlot7", customLocationName);
        editor.commit();
    }

    public int checkSlots(){
        int usedslot=0;
        if(customSlot1IsUsed()){
            usedslot=1;
        }
        if(customSlot2IsUsed()){
            usedslot=2;
        }
        if(customSlot3IsUsed()){
            usedslot=3;
        }
        if(customSlot4IsUsed()){
            usedslot=4;
        }
        if(customSlot5IsUsed()){
            usedslot=5;
        }
        if(customSlot6IsUsed()){
            usedslot=6;
        }
        if(customSlot7IsUsed()){
            usedslot=7;
        }
        return usedslot;
    }


    public void deleteCustomSlots(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("customSlot1").commit();
        editor.remove("customSlot2").commit();
        editor.remove("customSlot3").commit();
        editor.remove("customSlot4").commit();
        editor.remove("customSlot5").commit();
        editor.remove("customSlot6").commit();
        editor.remove("customSlot7").commit();
    }



}

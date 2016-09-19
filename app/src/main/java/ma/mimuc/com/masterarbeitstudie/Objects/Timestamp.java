package ma.mimuc.com.masterarbeitstudie.Objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Raphael on 29.10.2015.
 */
public class Timestamp {

    public Timestamp(){

    }

    public String getTimeStampDate(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);
        int month = c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        return ("" + hour + ":" +minute+ ":" + seconds + " " + day + "." + month + " "+ year);

    }

    public String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTimeStampMs(){
        return "";
    }
}

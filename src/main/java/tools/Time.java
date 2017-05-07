package tools;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by lucas on 07/05/17.
 */
public class Time {
    public static String timeActivity(String act, String startact) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH':'mm");
        Date jd = sdf.parse(startact);
        switch (act){
            case "restaurant" : jd.getTime(); break;
        }
        return jd.toString();
    }
}

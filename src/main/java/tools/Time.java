package tools;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by lucas on 07/05/17.
 */
public class Time {

    //On donne une activité ainsi que son heure de début et retourne la date de fin
    public static String timeEndActivity(String act, String startAct) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH':'mm");
        Date jd = sdf.parse(startAct);
        //Le temps est en millisecondes
        switch (act){
            case "restaurant" : jd=new Date(jd.getTime()+3600000); break;
            case "bar" : jd=new Date(jd.getTime()+3600000); break;
            case "cafe" : jd=new Date(jd.getTime()+1800000); break;
            case "night_club": jd=new Date(jd.getTime()+10800000); break;
            case "museum": jd=new Date(jd.getTime()+7200000); break;
            case "park": jd=new Date(jd.getTime()+3600000); break;
            case "amusement_park": jd=new Date(jd.getTime()+7200000); break;
            case "library": jd=new Date(jd.getTime()+7200000); break;
            case "casino": jd=new Date(jd.getTime()+10800000); break;
            case "movie_theater": jd=new Date(jd.getTime()+7200000); break;
            case "shopping_mall": jd=new Date(jd.getTime()+3600000); break;
            case "aquarium": jd=new Date(jd.getTime()+3600000); break;
            case "bowling_alley": jd=new Date(jd.getTime()+5400000); break;
            case "cinema": jd=new Date(jd.getTime()+9000000); break;
            default:break;
        }
        return sdf.format(jd);
    }

    //Donne le temps de début d'une activité(flottement entre chaque activité)en prenant le temps de fin de l'activité précédente
    public static String timeStartActivity(String endAct) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH':'mm");
        Date jd = sdf.parse(endAct);
        //30 minutes de flottement entre chaque activité
        return sdf.format(new Date(jd.getTime()+1800000));
    }
}

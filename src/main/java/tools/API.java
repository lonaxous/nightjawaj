package tools;

import modele.Activity;
import modele.Event;
import modele.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by dnguye2 on 02/05/17.
 */
public class API {
    private final String APIKEY;

    API(String api){
        APIKEY = api;
    }

    public JSONObject getJ(String type, String param) throws IOException, JSONException {
        URL url = new URL("https://maps.googleapis.com/maps/api/place/"+type+"/json?"+
                param+"&key=" + APIKEY); // Search for the address via API
        System.out.println("tools.API issued request : "+url.toString());
        Scanner scan = new Scanner(url.openStream());
        String html_output = new String();
        while (scan.hasNext())
            html_output += scan.nextLine();
        scan.close();
        JSONObject j = new JSONObject(html_output);
        return j;
    }
    public JSONObject textSearch(String query) throws IOException, JSONException {
        String formattedQuery = URLEncoder.encode(query,"UTF-8"); // Make the parameter URL compliant
        return getJ("textsearch","query="+formattedQuery);
    }

    public JSONObject placedetails(String placeid) throws IOException, JSONException {
        String formattedPlaceid = URLEncoder.encode(placeid,"UTF-8"); // Make the parameter URL compliant
        return getJ("details","placeid="+formattedPlaceid);
    }

    public JSONObject nearBy(String placeid, String typeActivity, Event e) throws IOException, JSONException{
        String formattedPlaceid = URLEncoder.encode(placeid,"UTF-8");
        String formattedType = URLEncoder.encode(typeActivity,"UTF-8");
        List<User> ambiances = e.getHisAmbiances();
        String foodpref="";
        for(int i = 0;i<ambiances.size();i++){
            if (ambiances.get(i).getFoodpref() != null){
                foodpref=foodpref.concat(" "+ambiances.get(i).getFoodpref());//Getting all preferences from all users
            }
        }
        if (e.getHisOrganiser().getFoodpref() != null) foodpref=foodpref.concat(" "+e.getHisOrganiser().getFoodpref());//Adding organiser preferences

        String[] tokens = foodpref.split(" ");//Split each keywords of the foodpref
        String minusKeyword = "";
        for (String t : tokens){
            minusKeyword = minusKeyword.concat(t+" -");//Put a minus before every words to exclude them from research
        }

        String urlFood = URLEncoder.encode(minusKeyword,"UTF-8");//make the food prefs url complient


        JSONObject jsonObject = placedetails(formattedPlaceid);
        Double latitude = jsonObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        Double longitude = jsonObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        return getJ("nearbysearch","location="+latitude+","+longitude+"&radius=1000&type="+formattedType+"&keyword="+urlFood);//all words  in foodprefs are excluded from research
    }

    public String localisation(Activity a) throws IOException, JSONException {
        JSONObject jsonObject = placedetails(a.getPlaceid());
        Double latitude = jsonObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        Double longitude = jsonObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        return latitude+","+longitude;
    }

    public String localisation(User u) throws IOException, JSONException {
        JSONObject jsonObject = placedetails(u.getPlaceid());
        Double latitude = jsonObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        Double longitude = jsonObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        return latitude+","+longitude;
    }

    public String staticMap(Event e) throws IOException, JSONException {

        String color[] = {"blue", "yellow", "red", "green", "purple"};
        String url = "https://maps.googleapis.com/maps/api/staticmap?center="+localisation(e.getHisOrganiser())+"&size=500x500";
        for (int i = 0; i < e.getHisActivities().size();i++){
            url=url+"&markers=color:"+color[i]+"%7Clabel:"+i+1+"%7C"+localisation(e.getHisActivities().get(i));
        }
        return url+"&key="+APIKEY;
    }
}
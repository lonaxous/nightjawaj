package tools;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by dnguye2 on 02/05/17.
 */
public class API {
    private final String APIKEY;

    API(String api){
        APIKEY = api;
    }

    public JSONObject getJ(String type, String param) throws IOException {
        URL url = new URL("https://maps.googleapis.com/maps/api/place/"+type+"/json?"+
                param+"&key=" + APIKEY); // Search for the address via API
        System.out.println(url.toString());
        Scanner scan = new Scanner(url.openStream());
        String html_output = new String();
        while (scan.hasNext())
            html_output += scan.nextLine();
        scan.close();
        JSONObject j = new JSONObject(html_output);
        return j;
    }
    public JSONObject textSearch(String query) throws IOException {
        String formattedQuery = URLEncoder.encode(query,"UTF-8"); // Make the parameter URL compliant
        return getJ("textsearch","query="+formattedQuery);
    }

    public JSONObject placedetails(String placeid) throws IOException {
        String formattedPlaceid = URLEncoder.encode(placeid,"UTF-8"); // Make the parameter URL compliant
        return getJ("details","placeid="+formattedPlaceid);
    }
}
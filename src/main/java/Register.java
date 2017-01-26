import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import static spark.Spark.*;

/**
 * Created by nomeji on 1/26/17.
 */

public class Register {
    private String last_name;
    private String first_name;
    private String address;

    private static String GooglePlacesKey = "AIzaSyBPLlRzEty62nUM8JIArfmRv8YLFMaY5u4";

    Register(){
        post("/registering", (request,response)-> { //Getting information from the registering form (HTML)
            // register informations
           last_name = request.queryParams("last_name");
           first_name = request.queryParams("first_name");
           address = request.queryParams("address");
            System.out.println(last_name+first_name+address);

            //Verify address
            if (isAddressValid(address)){
                registerAddress(address);
                return "It does exist!";
                // Create session from now on
            }
            else return "This address doen't exist";
            // Return to previous page
        });
    }

    public boolean isAddressValid(String address) throws IOException, JSONException {
        String addressEncoded = URLEncoder.encode(address,"UTF-8"); // Make the address URL compliant
        URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+addressEncoded+"&key=" + GooglePlacesKey); // Search for the address via API
        Scanner scan = new Scanner(url.openStream());
        String html_output = new String();
        while (scan.hasNext())
            html_output += scan.nextLine();
        scan.close();
        JSONObject j = new JSONObject(html_output);

        System.out.println(j.getString("status"));
        return (j.getString("status").equals("OK")); // if status is OK then address is valid
    }

    public void registerAddress(String address) throws IOException, JSONException {
        String addressEncoded = URLEncoder.encode(address, "UTF-8"); // Make the address URL compliant
        URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + addressEncoded + "&key=" + GooglePlacesKey); // Search for the address via API
        Scanner scan = new Scanner(url.openStream());
        String html_output = new String();
        while (scan.hasNext())
            html_output += scan.nextLine();
        scan.close();
        JSONObject j = new JSONObject(html_output);

        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < j.length()-1; i++) { // Getting results in an Array
            JSONObject addr = j.getJSONArray("results").getJSONObject(i);
            results.add(addr.getString("formatted_address"));
            System.out.println(results.get(i));
        }
    }

    public static void main(String args[]) throws IOException, JSONException {
        staticFiles.location("/"); // Initialize static files folder
        Register user1 = new Register();
    }
}

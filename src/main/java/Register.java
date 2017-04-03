import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.json.JSONException;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
                ArrayList<String> list_address = registerAddress(address);
                // Create session from now on
                request.session(true);
                request.session().attribute("lname",last_name);
                request.session().attribute("fname",first_name);

                chooseAddress(list_address);
                response.redirect("/choose_address");
                return "<p>It does exist!</p>";
            }
            else return "<p>This address doesn't exist</p>";
            // Return to previous page
        });
    }

    public boolean isAddressValid(String address) throws IOException, JSONException {
        String addressEncoded = URLEncoder.encode(address,"UTF-8"); // Make the address URL compliant
        URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+
                addressEncoded+"&key=" + GooglePlacesKey); // Search for the address via API
        Scanner scan = new Scanner(url.openStream());
        String html_output = new String();
        while (scan.hasNext())
            html_output += scan.nextLine();
        scan.close();
        JSONObject j = new JSONObject(html_output);

        System.out.println(j.getString("status"));
        return (j.getString("status").equals("OK")); // if status is OK then address is valid
    }

    public ArrayList<String> registerAddress(String address) throws IOException, JSONException {
        String addressEncoded = URLEncoder.encode(address, "UTF-8"); // Make the address URL compliant
        URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" +
                addressEncoded + "&key=" + GooglePlacesKey); // Search for the address via API
        Scanner scan = new Scanner(url.openStream());
        String html_output = new String();
        while (scan.hasNext())
            html_output += scan.nextLine();
        scan.close();
        JSONObject j = new JSONObject(html_output);

        ArrayList<String> results = new ArrayList<>();
        System.out.println("nb address : "+j.getJSONArray("results").length());
        for (int i = 0; i < j.getJSONArray("results").length(); i++) { // Getting results in an Array
            JSONObject addr = j.getJSONArray("results").getJSONObject(i);
            results.add(addr.getString("formatted_address"));
            System.out.println(results.get(i));
        }

        return results;
    }

    public void chooseAddress(ArrayList<String> list_address){
        String tmp = "";

        for (int i = 0; i < list_address.size();i++){
            tmp = tmp.concat("value="+i+">"+list_address.get(i)+"</a><br>");
        }

        String page = tmp;

        Map map = new HashMap();
        map.put("items", list_address);

        get("/choose_address", (request, response) -> new ModelAndView(map,"chooseAddress_test.hbs"), new HandlebarsTemplateEngine()); // Create template
        // get("/choose_address", (request, response) -> "sava");
    }

    public static void main(String args[]) throws IOException, JSONException {
        System.setProperty("https.proxyHost", "cache.u-psud.fr");
        System.setProperty("https.proxyPort", "8080");
        staticFiles.location("/"); // Initialize static files folder
        Register user1 = new Register();
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
    }
}

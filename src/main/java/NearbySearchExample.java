/*
  Exemple de requête de recherche de lieu
  * de type 'restaurant'
  * dans un rayon de 500 m
  * autour de la Place de la Bastille (coordonnées GPS décimales: 48.855218,2.368622)
  * contenant 'burger' dans le nom de l'enseigne
 */

/*
  Pour compiler et exécuter ce programme:
  > make
  > make run
 */

import java.net.*;
import java.util.*;
import org.json.*;

import static spark.Spark.get;

class NearbySearchExample{



    // METTEZ ICI VOTRE CLE D'API DE GOOGLE PLACES
    private static String GooglePlacesKey = "AIzaSyBPLlRzEty62nUM8JIArfmRv8YLFMaY5u4";

    public static void main(String[] args) throws Exception{
        System.setProperty("https.proxyHost", "cache.u-psud.fr");
        System.setProperty("https.proxyPort", "8080");

        // Lire une URL
        URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.855218,2.368622&radius=500&type=restaurant&name=burger&key=" + GooglePlacesKey);
        Scanner scan = new Scanner(url.openStream());
        String html_output = new String();
        while (scan.hasNext())
            html_output += scan.nextLine();
        scan.close();

        // Construire l'objet JSON
        // Toute la documentation de la bibliothèque org.json est disponible sur https://stleary.github.io/JSON-java/index.html
        JSONObject j = new JSONObject(html_output);

        // Afficher
        String burger = "J'ai envie d'un bon burger autour de Bastille !<br>";
        System.out.println ("J'ai envie d'un bon burger autour de Bastille !");
        for (int i = 0 ; i < j.length() ; i++){
            JSONObject lieu = (j.getJSONArray("results")).getJSONObject (i);
            burger = burger.concat("  -> " + lieu.getString ("name") + ", " + lieu.getString ("vicinity")+"<br>");
            System.out.println ("  -> " + lieu.getString ("name") + ", " + lieu.getString ("vicinity"));
        }


        URL urlDist = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Paris,FR&destinations=Orsay,FR&key="+ GooglePlacesKey);
        scan = new Scanner(urlDist.openStream());
        html_output = "";
        while(scan.hasNext())
            html_output += scan.nextLine();
        scan.close();

        JSONObject dist = new JSONObject(html_output);

        JSONArray origin = dist.getJSONArray("origin_addresses");
        JSONArray arri = dist.getJSONArray("destination_addresses");

        JSONObject distance = (dist.getJSONArray("rows")).getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance");

        JSONObject duree = (dist.getJSONArray("rows")).getJSONObject (0).getJSONArray("elements").getJSONObject (0).getJSONObject("duration");

        String affDistance = "La distance entre "+origin.get(0)+" et "+arri.get(0)+" est de "+distance.getString("text")+" pour une durée de "+duree.getString("text");
        System.out.println("La distance entre "+origin.get(0)+" et "+arri.get(0)+" est de "+distance.getString("text")+" pour une durée de "+duree.getString("text"));

        String finalBurger = burger;
        get("/hello", (req, res) -> finalBurger + "<br>" + affDistance);

    }
}
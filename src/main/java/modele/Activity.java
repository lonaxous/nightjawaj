package modele;

import org.json.JSONException;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import tools.Server;

import java.io.IOException;
import java.util.*;

import static spark.Spark.post;
import static spark.Spark.get;

/**
 * Created by dnguye2 on 27/03/17.
 */
public class Activity {
    private int id;
    private String name;
    private String placeid;
    private String type;
    private String startDate;
    private String endDate;
    private Event hisEvent;

    //Constructeur


    public String getPlaceid() {
        return placeid;
    }

    public Activity(int anInt, String string, String name, String placeid, Event hisEvent) {
        this.name = name;
        this.placeid = placeid;
        this.hisEvent = hisEvent;
    }

    public Activity(String type) {
        this.type = type;
    }

    public Activity(String name, String placeid, String startDate, String endDate) {
        this.name = name;
        this.placeid = placeid;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Activity(int id,String name, String placeid, String startDate, String endDate, Event hisEvent) {
        this.id =id;
        this.name = name;
        this.placeid = placeid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hisEvent = hisEvent;
    }

    //Fonction
    public static void start(){
        get("/modifactivity", (request,response) -> {//Show the form to specify types of activities
            User u = request.session().attribute("user");
            //Vérification si l'user est bien connecté
            if(u == null){
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            //Obtention de l'identifiant de l'event
            try{
                int ide = Integer.parseInt(request.queryParams("idevent"));
                //Vérification si l'user est bien l'organsier de l'event
                if(!Server.getDatabase().isOragniserEvent(u.getId(),ide)){
                    //Receive


                    Map map = new HashMap();
                    map.put("message","Redirection error");
                    return new ModelAndView(map,"error.hbs");
                }
                Map map = new HashMap();
                map.put("ide",ide);
                return new ModelAndView(map,"choixactivite.hbs");
            }
            catch (NumberFormatException e){
                response.redirect("/error?msg=parse error");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
        },new HandlebarsTemplateEngine());

        post("/ajoutactivite",(request, response) -> {//Search for random activities and display result
            User u = request.session().attribute("user");
            //Vérification si l'user est bien connecté
            if(u == null){
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            try{
                int ide = Integer.parseInt(request.queryParams("ide"));

                if(Server.getDatabase().isOragniserEvent(u.getId(),ide)) {
                    // Receive parameters
                    ArrayList<String> typeList = new ArrayList<>();
                    typeList.add(request.queryParams("type1"));
                    typeList.add(request.queryParams("type2"));
                    typeList.add(request.queryParams("type3"));
                    typeList.add(request.queryParams("type4"));
                    typeList.add(request.queryParams("type5"));

                    // Create the event
                    Event e = Server.getDatabase().selectEvent(ide);

                    // Search for a random activities for each type
                    ArrayList<JSONObject> jsList = new ArrayList<>();
                    for(int i=0;i<typeList.size();i++){
                        if(!typeList.get(i).equals("empty")){
                            jsList.add(Server.getAPI().nearBy(u.getPlaceid(),typeList.get(i),e));
                            e.getHisActivities().add(new Activity(typeList.get(i)));
                        }
                    }
                    request.session().attribute("event",e);
                    request.session().attribute("json",jsList);// Temporary store the event into the session before sending to database
                    response.redirect("/showactivities");// Show activities and wait confirmation from user before sending to database
                    Map map = new HashMap();
                    map.put("message","Redirection error");
                    return new ModelAndView(map,"error.hbs");
                }
                else{
                    response.redirect("/error?msg=not your event!");
                    Map map = new HashMap();
                    map.put("message","Redirection error");
                    return new ModelAndView(map,"error.hbs");
                }
            }
            catch(NumberFormatException e){
                response.redirect("/error?msg=parse error");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            catch(Exception e){
                response.redirect("/error?msg=no result for those criteria");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
        });

        get("/showactivities",(request, response) -> {
            ArrayList<JSONObject> jsList = request.session().attribute("json");
            Event e = request.session().attribute("event");
            if (jsList != null && e != null){
                // Search for a random activity for each type
                for (int i=0;i<e.getHisActivities().size();i++){
                    e.getHisActivities().get(i).getRandom(jsList.get(i),e);//For each activity in event get a random one matching
                }
                String msg = "";

                ArrayList<Map> activities = new ArrayList<>();
                for (int i=0;i<e.getHisActivities().size();i++){
                    HashMap<String,String> info = new HashMap<>();
                    Activity a = e.getHisActivities().get(i);
                    info.put("type",a.type);//Set all info
                    info.put("name",a.name);
                    info.put("adresse",Address.getAddressFromId(a.placeid).formattedAddress);
                    activities.add(info);
                }
                HashMap map = new HashMap();
                map.put("map",Server.getAPI().staticMap(e));
                map.put("items",activities);
                return new ModelAndView(map,"afficheactivite.hbs");
            }
            else{
                response.redirect("/error?msg=your didn't create an event");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
        },new HandlebarsTemplateEngine());
    }

    public void getRandom(JSONObject j,Event e) throws Exception {//This constructor search for a random activity matching parameters
        Random rand = new Random();

        if(!j.getString("status").equals("OK")) throw new Exception("No result");

        int nbresult = j.getJSONArray("results").length();
        JSONObject addr = j.getJSONArray("results").getJSONObject(rand.nextInt(nbresult));

        this.name = addr.getString("name");
        this.placeid = addr.getString("place_id");
        this.hisEvent = e;
    }
}

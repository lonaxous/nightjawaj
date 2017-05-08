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
    private String name;
    private String placeid;
    private String startDate;
    private String endDate;
    private Event hisEvent;

    //Constructeur


    public Activity(String name, String placeid, Event hisEvent) {
        this.name = name;
        this.placeid = placeid;
        this.hisEvent = hisEvent;
    }

    public Activity(String name, String placeid, String startDate, String endDate) {
        this.name = name;
        this.placeid = placeid;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Activity(String name, String placeid, String startDate, String endDate, Event hisEvent) {
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

                    // Search for a random activity for each type
                    for(int i=0;i<typeList.size();i++){
                        if(!typeList.get(i).equals("empty")){
                            Activity a = new Activity(u,typeList.get(i),e);//Search for a random activity
                            e.addHisActivities(a);
                            //Server.getDatabase().createActivity(e.getIde(),a.name,a.placeid);
                        }
                    }
                    request.session().attribute("event",e);// Temporary store the event into the session before sending to database
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
        });

        get("/showactivities",(request, response) -> {
            Event e = request.session().attribute("event");
            if (e != null){
                String msg = "";
                for (int i=0;i<e.getHisActivities().size();i++){
                    Activity a = e.getHisActivities().get(i);
                    msg = msg.concat("Activity name : "+a.name+"<br>Activity address : "+Address.getAddressFromId(a.placeid).formattedAddress);
                }
                return msg;
            }
            else{
                response.redirect("/error?msg=your didn't create an event");
                return "error";
            }
        });
    }

    public Activity (User u,String type,Event e) throws IOException, JSONException {//This constructor search for a random activity matching parameters
        Random rand = new Random();

        JSONObject j = Server.getAPI().nearBy(u.getPlaceid(),type,e);

        int nbresult = j.getJSONArray("results").length();
        JSONObject addr = j.getJSONArray("results").getJSONObject(rand.nextInt(nbresult));

        this.name = addr.getString("name");
        this.placeid = addr.getString("place_id");
        this.hisEvent = e;
    }
}

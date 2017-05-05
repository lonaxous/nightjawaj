package modele;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import tools.Server;

import java.text.SimpleDateFormat;
import java.util.*;

import static spark.Spark.post;
import static spark.Spark.get;

/**
 * Created by dnguye2 on 27/03/17.
 */
public class Event {
    private int ide;
    private String name;
    private String dateDeb;
    private String dateFin;
    private User hisOrganiser;

    private List<User> hisAmbiances;
    private List<Activity> hisActivities;

    public Event(String name, String dateDeb, String dateFin) {
        this.name = name;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    public Event(int ide,String name, String dateDeb, String dateFin) {
        this.ide = ide;
        this.name = name;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    public Event(int ide, String name, String dateDeb, String dateFin, User hisOrganiser) {
        this.ide = ide;
        this.name = name;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.hisOrganiser = hisOrganiser;
    }

    public List<User> getHisAmbiances() {
        return hisAmbiances;
    }

    public static void start(){
        get("/event", (request, response) -> {
            User u = request.session().attribute("user");
            if (u == null){
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            else{
                // Print all events that user created
                ArrayList<Event> listeE = Server.getDatabase().selectUserEvent(u.getId());
                ArrayList<Map> events = new ArrayList<>();

                // Date management
                SimpleDateFormat dateFormatInput = new SimpleDateFormat("YYYY-mm-dd'T'HH':'MM");
                SimpleDateFormat dateFormatOuput = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                for(int i=0;i<listeE.size();i++){
                    Map<String,Object> info = new HashMap<>();

                    info.put("eventname",listeE.get(i).name);
                    info.put("nom",listeE.get(i).hisOrganiser.getName());
                    info.put("prenom",listeE.get(i).hisOrganiser.getFirstname());

                    Date datedeb = dateFormatInput.parse(listeE.get(i).dateDeb);
                    info.put("hdeb",dateFormatOuput.format(datedeb));

                    Date datefin = dateFormatInput.parse(listeE.get(i).dateFin);
                    info.put("hfin",dateFormatOuput.format(datedeb));

                    info.put("adresse",Address.getAddressFromId(listeE.get(i).hisOrganiser.getPlaceid()).formattedAddress);
                    info.put("own",true);

                    events.add(info);
                }

                // Print all event that user paticipate in
                listeE = Server.getDatabase().selectAmbianceEvent(u.getId());
                for(int i=0;i<listeE.size();i++){
                    Map<String,Object> info = new HashMap<>();

                    info.put("eventname",listeE.get(i).name);
                    info.put("nom",listeE.get(i).hisOrganiser.getName());
                    info.put("prenom",listeE.get(i).hisOrganiser.getFirstname());

                    Date datedeb = dateFormatInput.parse(listeE.get(i).dateDeb);
                    info.put("hdeb",dateFormatOuput.format(datedeb));

                    Date datefin = dateFormatInput.parse(listeE.get(i).dateFin);
                    info.put("hfin",dateFormatOuput.format(datedeb));

                    info.put("adresse",Address.getAddressFromId(listeE.get(i).hisOrganiser.getPlaceid()).formattedAddress);
                    info.put("own",false);

                    events.add(info);
                }

                HashMap map = new HashMap();
                map.put("items",events);

                return new ModelAndView(map,"listeevenement.hbs");
            }
        }, new HandlebarsTemplateEngine());

        get("/ajoutAmbiance", (request,response) -> {
            User u = request.session().attribute("user");
            //Vérification si l'user est bien connecté
            if(u == null){
                response.redirect("/error?msg=session not present");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }

            //Obtention de l'identifiant de l'event
            int ide = Integer.parseInt(request.queryParams("idevent"));

            //Vérification si l'user est bien l'organsier de l'event
            if(Server.getDatabase().verifOragniserEvent(u.getId(),ide)){
                response.redirect("/error?msg=try again");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }

            ArrayList<User> listeU = Server.getDatabase().selectAmbiance(ide);
            ArrayList<Map> ambiances = new ArrayList<>();
            //On attrappe l'ensembre des utilisateurs
            for(int i=0;i<listeU.size();i++){
                Map<String,Object> info = new HashMap<>();
                info.put("nom",listeU.get(i).getName());
                info.put("prenom",listeU.get(i).getFirstname());
                info.put("mail",listeU.get(i).getMail());
                info.put("adresse",Address.getAddressFromId(listeU.get(i).getPlaceid()).formattedAddress);

                ambiances.add(info);
            }

            HashMap map = new HashMap();
            map.put("items",ambiances);

            return new ModelAndView(map,"ajoutAmbiance.hbs");
        },new HandlebarsTemplateEngine());

        post("/event", (request, response) -> {
            Event e = new Event(request.queryParams("nomevenement"),
                    request.queryParams("heuredebut"),
                    request.queryParams("heurefin"));

            if (!e.dateDeb.matches("([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9])T([0-5][0-9]):([0-5][0-9])")) {
                response.redirect("/error?msg=Error, start hour doesn't match format. Hint : ([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9])T([0-5][0-9]):([0-5][0-9])");
            } else if (!e.dateDeb.matches("([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9])T([0-5][0-9]):([0-5][0-9])")) {
                response.redirect("/error?msg=Error, end hour doesn't match format. Hint : ([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9])T([0-5][0-9]):([0-5][0-9])");
            }
            User u = request.session().attribute("user");
            if (u == null) response.redirect("/");
            Server.getDatabase().createEvent(u.getId(), e.name, e.dateDeb, e.dateFin); // Adding event to database
            response.redirect("/event" );
            Map map = new HashMap();
            map.put("message","Redirection error");
            return new ModelAndView(map,"error.hbs");
        });
    }
}

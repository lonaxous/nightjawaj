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
        hisActivities = new ArrayList<>();
    }

    public Event(int ide,String name, String dateDeb, String dateFin) {
        this.ide = ide;
        this.name = name;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        hisActivities = new ArrayList<>();
    }

    public Event(int ide, String name, String dateDeb, String dateFin, User hisOrganiser) {
        this.ide = ide;
        this.name = name;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.hisOrganiser = hisOrganiser;
        hisActivities = new ArrayList<>();
    }

    public Event(int ide, String name, String dateDeb, String dateFin, User hisOrganiser, List<User> hisAmbiances,List<Activity> hisActivities) {
        this.ide = ide;
        this.name = name;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.hisOrganiser = hisOrganiser;
        this.hisAmbiances = hisAmbiances;
        this.hisActivities=hisActivities;
    }

    public int getIde() {
        return ide;
    }

    public List<Activity> getHisActivities() {
        return hisActivities;
    }

    public User getHisOrganiser() {
        return hisOrganiser;
    }

    public void addHisActivities(Activity activity) {
        hisActivities.add(activity);
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
                SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH':'mm");
                SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                for(int i=0;i<listeE.size();i++){
                    Map<String,Object> info = new HashMap<>();

                    info.put("eventname",listeE.get(i).name);
                    info.put("nom",listeE.get(i).hisOrganiser.getName());
                    info.put("prenom",listeE.get(i).hisOrganiser.getFirstname());

                    Date datedeb = dateFormatInput.parse(listeE.get(i).dateDeb);
                    info.put("hdeb",dateFormatOutput.format(datedeb));

                    Date datefin = dateFormatInput.parse(listeE.get(i).dateFin);
                    info.put("hfin",dateFormatOutput.format(datefin));

                    info.put("adresse",Address.getAddressFromId(listeE.get(i).hisOrganiser.getPlaceid()).formattedAddress);
                    info.put("own",true);
                    info.put("ide",listeE.get(i).ide);

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
                    info.put("hdeb",dateFormatOutput.format(datedeb));

                    Date datefin = dateFormatInput.parse(listeE.get(i).dateFin);
                    info.put("hfin",dateFormatOutput.format(datefin));

                    info.put("adresse",Address.getAddressFromId(listeE.get(i).hisOrganiser.getPlaceid()).formattedAddress);
                    info.put("own",false);
                    info.put("ide",listeE.get(i).ide);

                    events.add(info);
                }

                HashMap map = new HashMap();
                map.put("items",events);

                return new ModelAndView(map,"listeevenement.hbs");
            }
        }, new HandlebarsTemplateEngine());

        //Aller sur la page pour ajouter des ambiancés
        get("/ajoutambiance", (request,response) -> {
            User u = request.session().attribute("user");
            //Vérification si l'user est bien connecté
            if(u == null){
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            else {
                try {
                    try{
                        //Obtention de l'identifiant de l'event
                        int ide = Integer.parseInt(request.queryParams("idevent"));
                        //On attrappe l'ensembre des utilisateurs
                        ArrayList<User> listeU = Server.getDatabase().selectAmbiance(ide);
                        ArrayList<Map> ambiances = new ArrayList<>();
                        //On entre l'ensemble des données sur la page web
                        for (int i = 0; i < listeU.size(); i++) {
                            Map<String, Object> info = new HashMap<>();
                            info.put("nom", listeU.get(i).getName());
                            info.put("prenom", listeU.get(i).getFirstname());
                            info.put("mail", listeU.get(i).getMail());
                            info.put("adresse", Address.getAddressFromId(listeU.get(i).getPlaceid()).formattedAddress);
                            info.put("idu", listeU.get(i).getId());
                            info.put("ide", ide);

                            ambiances.add(info);
                        }
                        HashMap map = new HashMap();
                        map.put("items", ambiances);
                        map.put("own",Server.getDatabase().isOragniserEvent(u.getId(), ide));

                        return new ModelAndView(map, "ajoutambiance.hbs");
                    }
                    catch (NumberFormatException e){
                        response.redirect("/error?msg=parse error");
                        Map map = new HashMap();
                        map.put("message","Redirection error");
                        return new ModelAndView(map,"error.hbs");
                    }
                } catch (NumberFormatException e) {
                    response.redirect("/error?msg=parse error");
                    Map map = new HashMap();
                    map.put("message", "Redirection error");
                    return new ModelAndView(map, "error.hbs");
                }
            }

        },new HandlebarsTemplateEngine());

        //Ajouter un ambiancé
        post("/ajoutambiance", (request, response) -> {
            //On recupère les attributs que nous avons besoin
            String mail = request.queryParams("mail");
            int ide = Integer.parseInt(request.queryParams("idevent"));
            User u = request.session().attribute("user");

            int idambiance=-1;
            idambiance=Server.getDatabase().selectUserMail(mail);
            //Vérification que l'utilisateur est bien connecté
            if (u == null) response.redirect("/error?msg=Votre Session n'existe plus");
            //Vérification si l'user est bien l'organsier de l'event
            else if(!Server.getDatabase().isOragniserEvent(u.getId(),ide)){
                response.redirect("/error?msg=Vous n'etes pas l'organisateur");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            //Vérification que l'utilisateur à bien saisie une adresse mail valide
            else if(idambiance==-1) response.redirect("/error?msg=mail invalide");
            //Vérification si l'invité fais déjà partit des ambiancé
            else if(Server.getDatabase().isAmbiance(idambiance,ide))response.redirect("/error?msg=Cette ambiance est deja invite");
            //Vérification si l'invité n'est pas l'organisateur
            else if(Server.getDatabase().isOragniserEvent(idambiance,ide))response.redirect("/error?msg=vous ne pouvez pas vous inviter vous meme !");
            //On ajoute l'ambiancé à la base de données
            else {
                Server.getDatabase().insertAmbiance(idambiance, ide);
                response.redirect("/ajoutambiance?idevent=" + ide);
            }
            Map map = new HashMap();
            map.put("message","Redirection error");
            return new ModelAndView(map,"error.hbs");
        });

        //Supprimer un Ambiancé
        get("/supprambiance", (request,response) -> {
            User u = request.session().attribute("user");
            //On recupère les attributs que nous avons besoin
            int idambiance = Integer.parseInt(request.queryParams("idu"));
            int ide = Integer.parseInt(request.queryParams("ide"));
            //Vérification si l'user est bien connecté
            if(u == null){
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            //Vérification si l'user est bien l'organsier de l'event
            else if(!Server.getDatabase().isOragniserEvent(u.getId(),ide)){
                response.redirect("/error?msg=Vous n'etes pas l'organisateur");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            //Vérification si l'ambiancé existe bien dans la base de données
            else if(!Server.getDatabase().isAmbiance(idambiance,ide)){
                response.redirect("/error?msg=Cette ambiance est deja invite");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            else {
                Server.getDatabase().deleteAmbiance(idambiance, ide);
                response.redirect("/ajoutambiance?idevent=" + ide);
            }
            Map map = new HashMap();
            map.put("message", "Redirection error");
            return new ModelAndView(map, "error.hbs");
        },new HandlebarsTemplateEngine());

        post("/event", (request, response) -> {
            Event e = new Event(request.queryParams("nomevenement"),
                    request.queryParams("heuredebut"),
                    request.queryParams("heurefin"));

            if (!e.dateDeb.matches("([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9])T([0-5][0-9]):([0-5][0-9])")) {
                response.redirect("/error?msg=Error, start hour doesn't match format. Hint : ([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9])T([0-5][0-9]):([0-5][0-9])");
            } else if (!e.dateFin.matches("([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9])T([0-5][0-9]):([0-5][0-9])")) {
                response.redirect("/error?msg=Error, end hour doesn't match format. Hint : ([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9])T([0-5][0-9]):([0-5][0-9])");
            }
            User u = request.session().attribute("user");
            if (u == null) response.redirect("/");
            Server.getDatabase().createEvent(u.getId(), e.name, e.dateDeb, e.dateFin); // Adding event to database
            response.redirect("/modifactivity" );
            Map map = new HashMap();
            map.put("message","Redirection error");
            return new ModelAndView(map,"error.hbs");
        });

        //Suppression d'un event (il faut un ide)
        get("/supprevent",(request, response) -> {
            User u = request.session().attribute("user");
            //On récupère les informations que nous avons besoin
            int ide = Integer.parseInt(request.queryParams("ide"));
            if(u == null){
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            //Véification si l'event existe
            else if(!Server.getDatabase().isEvent(ide)){
                response.redirect("/error?msg=L'event n'existe pas");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            //Vérification si l'user est bien l'organsier de l'event
            else if(!Server.getDatabase().isOragniserEvent(u.getId(),ide)){
                response.redirect("/error?msg=Vous n'etes pas l'organisateur");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            else{
                Server.getDatabase().deleteEvent(ide);
                response.redirect("/event" );
            }
            Map map = new HashMap();
            map.put("message", "Redirection error");
            return new ModelAndView(map, "error.hbs");
        },new HandlebarsTemplateEngine());
    }
}

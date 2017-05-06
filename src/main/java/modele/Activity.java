package modele;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import tools.Server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        get("/modifactivity", (request,response) -> {
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
            if(!Server.getDatabase().isOragniserEvent(u.getId(),ide)){
                response.redirect("/error?msg=try again");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }

            return null;
        },new HandlebarsTemplateEngine());
    }
}

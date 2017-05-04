package modele;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import tools.Server;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by dnguye2 on 27/03/17.
 */
public class User {
    private String name;
    private String lastName;
    private String placeid;
    private String mail;
    private String mdp;


    public User(String n, String ln) {
        name = n;
        lastName = ln;
    }

    public User(String name, String lastName, String mail, String mdp) {
        this.name = name;
        this.lastName = lastName;
        this.mail = mail;
        this.mdp = mdp;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public static void start() {
        post("/user", (request, response) -> { //Getting information from the registering form (HTML)
            // register informations
            User u = new User(request.queryParams("nom"),
                    request.queryParams("prenom"),
                    request.queryParams("mail"),
                    request.queryParams("mpd"));

            if (!Server.getDatabase().verifmail(u.mail)){//Mail verification
                Map map = new HashMap();
                map.put("message", "Mail already present in database");
                return new ModelAndView(map, "error.hbs");
            }

            String unformattedAddress = request.queryParams("adresse");

            // Create session from now on
            request.session(true);
            request.session().attribute("user", u);

            response.redirect("/choose_address?unformatted_address=" + unformattedAddress);
            Map map = new HashMap();
            map.put("message", "Redirection error");
            return new ModelAndView(map, "error.hbs");
        }, new HandlebarsTemplateEngine());

        get("/register", (request, response) -> {//Register the user in database
            if (request.session().attribute("user") != null) {
                User u = request.session().attribute("user");
                Server.getDatabase().register(u.name,u.lastName,u.mail,u.mdp,u.placeid);

                Map map = new HashMap();
                map.put("message", "Successfully created user");
                return new ModelAndView(map, "error.hbs");
            } else {
                Map map = new HashMap();
                map.put("message", "Error, session doesn't exist");
                return new ModelAndView(map, "error.hbs");
            }
        }, new HandlebarsTemplateEngine());

//        post("/home", ((request, response) -> {
//            User u = Server.getDatabase().selectUser(request.queryParams("mail"),request.queryParams("mdp"));
//            if (u  != null){
//              request.session().attribute("user",u);
//              Map map = new HashMap();
//              map.put("name", u.name);
//              return new ModelAndView(map, "userpage.hbs");
//            }
//        });
    }
}

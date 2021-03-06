package modele;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import tools.Server;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.redirect;

/**
 * Created by dnguye2 on 27/03/17.
 */
public class User {
    private int id;
    private String name;
    private String firstname;
    private String placeid;
    private String mail;
    private String mdp;
    private String foodpref;


    public User(String n, String ln) {
        name = n;
        firstname = ln;
    }

    public User(String name, String firstname, String mail, String mdp) {
        this.name = name;
        this.firstname = firstname;
        this.mail = mail;
        this.mdp = mdp;
    }

    public User(int id, String name, String firstname, String placeid, String mail) {
        this.id = id;
        this.name = name;
        this.firstname = firstname;
        this.placeid = placeid;
        this.mail = mail;
    }

    public User(int id, String name, String firstname, String placeid, String mail, String foodpref) {
        this.id = id;
        this.name = name;
        this.firstname = firstname;
        this.placeid = placeid;
        this.mail = mail;
        this.foodpref = foodpref;
    }

    public String getFoodpref() {
        return foodpref;
    }

    public String getMail() {
        return mail;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPlaceid() {
        return placeid;
    }

    public static void start() {
        post("/user", (request, response) -> { //Getting information from the registering form (HTML)
            // register informations
            User u = new User(request.queryParams("nom"),
                    request.queryParams("prenom"),
                    request.queryParams("mail"),
                    request.queryParams("mdp"));

            if (Server.getDatabase().verifmail(u.mail)){//Mail verification
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
                Server.getDatabase().register(u.firstname,u.name,u.placeid,u.mail,u.mdp);

                request.session().attribute("user",null); // Closing session to disconnect user
                request.session(false);

                response.redirect("/felicitation.html");
                Map map = new HashMap();
                map.put("message", "Redirection error");
                return new ModelAndView(map, "error.hbs");
            } else {
                Map map = new HashMap();
                map.put("message", "Error, session doesn't exist");
                return new ModelAndView(map, "error.hbs");
            }
        }, new HandlebarsTemplateEngine());

        get("/", (request, response) -> { //Redirect user to login page or to homepage if session is present or not
            User u = request.session().attribute("user");
            if (u == null){
                response.redirect("/utilisateur.html");
                Map map = new HashMap();
                map.put("message", "Redirection error");
                return new ModelAndView(map, "error.hbs");
            }
            else{
                Map map = new HashMap();
                map.put("name", u.name);
                map.put("lname",u.firstname);
                return new ModelAndView(map, "userpage.hbs");
            }
        }, new HandlebarsTemplateEngine());

        post("/connect", (request,response) -> {//Connect user and create session
            try{
                User u = Server.getDatabase().connect(request.queryParams("mail"),request.queryParams("mdp"));
                request.session().attribute("user",u);
                Map map = new HashMap();
                map.put("name", u.name);
                map.put("lname",u.firstname);
                return new ModelAndView(map, "userpage.hbs");
            }
            catch(Exception e){
                Map map = new HashMap();
                map.put("message", "Unknown user/password");//Show an error if password and mail aren't correct
                return new ModelAndView(map, "error.hbs");
            }
        }, new HandlebarsTemplateEngine());

        get("/disconnect", (request, response) -> {//Disconnect user
            User u = request.session().attribute("user");
            if (u != null){
                request.session(false);
                request.session().attribute("user",null);
                response.redirect("/utilisateur.html");
                Map map = new HashMap();
                map.put("message", "Redirection error");
                return new ModelAndView(map, "error.hbs");
            }
            else {
                Map map = new HashMap();
                map.put("message", "You are not connected");
                return new ModelAndView(map, "error.hbs");
            }
        }, new HandlebarsTemplateEngine());
    }
}

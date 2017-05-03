package modele;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

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
    private int age;
    private Address address;
    private String placeid;
    private String mail;
    private String mdp;


    public User(String n,String ln) {
        name = n;
        lastName = ln;
    }

    public User(String name, String lastName, int age, String mail, String mdp) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.mail = mail;
        this.mdp = mdp;
    }

    public String getMail() {
        return mail;
    }

    public void setPlaceid(String placeid){
        this.placeid = placeid;
    }

    public static void start() {
        post("/user", (request, response) -> { //Getting information from the registering form (HTML)
            // register informations
            User u = new User(request.queryParams("nom"),
                    request.queryParams("prenom"),
                    Integer.parseInt(request.queryParams("age")),
                    request.queryParams("mail"),
                    request.queryParams("mpd"));

            String unformattedAddress = request.queryParams("adresse");

            // Create session from now on
            request.session(true);
            request.session().attribute("user",u);


//            request.session().attribute("lname",u.name);
//            request.session().attribute("fname",u.lastName);

            response.redirect("/choose_address?unformatted_address="+unformattedAddress);
            Map map = new HashMap();
            map.put("message","Redirection error");
            return new ModelAndView(map,"error.hbs");
        },new HandlebarsTemplateEngine());

        get("/register",(request, response) -> {//Register the user in database
            if(request.session().attribute("user") != null){
                //Server.getDatabase().register();

                Map map = new HashMap();
                map.put("message","Successfully created user");
                return new ModelAndView(map,"error.hbs");
            }
            else{
                Map map = new HashMap();
                map.put("message","Error, session doesn't exist");
                return new ModelAndView(map,"error.hbs");
            }
            },new HandlebarsTemplateEngine());
    }

//    public static void registerUser() {
//        post("/register",((request, response) -> {
//           request.queryParams();
//           retrun null;
//        });
//    }
}

package modele;

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
            return "<h1>Redirection error</h1>";
        });

        get("/register",(request, response) -> {//Register the user in database
            if(request.session().attribute("user") != null){
                //Server.getDatabase().register();

                User u = request.session().attribute("user");
                return "<h1>Successfully created user</h1>" +
                        "<p>Name : "+u.mail+"</p>"+
                        "<p>placeid : "+u.placeid+"</p>";
            }
            else return "<h1>Error, session doesn't exist";
            });
    }

//    public static void registerUser() {
//        post("/register",((request, response) -> {
//           request.queryParams();
//           retrun null;
//        });
//    }
}

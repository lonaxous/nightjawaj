package modele;

import java.util.ArrayList;

import static spark.Spark.post;
import tools.Database;

/**
 * Created by dnguye2 on 27/03/17.
 */
public class User {
    private String name;
    private String lastName;
    private Address address;

    public User() {
        name = "";
        lastName = "";
        address = new Address();
    }

    public static void start() {
        post("/user", (request, response) -> { //Getting information from the registering form (HTML)
            // register informations
            String name = request.queryParams("prenom");
            String lastName = request.queryParams("nom");
            String unformattedAdress = request.queryParams("adresse");
            response.redirect("/choose_address?unformatted_address="+unformattedAdress);
            return null;
        });
    }

//    public static void registerUser() {
//        post("/register",((request, response) -> {
//           request.queryParams();
//           retrun null;
//        });
//    }
}

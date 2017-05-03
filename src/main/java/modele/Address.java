package modele;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.context.FieldValueResolver;
import controleur.ControlerAddress;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import tools.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by dnguye2 on 27/03/17.
 */
public class Address {
    public String formattedAddress;
    public String placeid;

    public Address(){}

    public Address(String formattedAddress, String placeid) {
        this.formattedAddress = formattedAddress;
        this.placeid = placeid;
    }

    public static void start(){
        get("/choose_address", (request, response) -> { //User choose the valid address in the liste
            String unformattedAddress = request.queryParams("unformatted_address");
            ControlerAddress control = new ControlerAddress();
            //Map map = control.getValidAddress(unformattedAddress);
            ArrayList<Map> liste = control.getValidAddress(unformattedAddress);

            Map map = new HashMap();
            map.put("items", liste);

            return new ModelAndView(map,"tabaddress.hbs");
        }, new HandlebarsTemplateEngine());

        post("/validAddress",(request, response) -> {//Getting the placid from user
            User u = request.session().attribute("user");

            if(u != null){
                u.setPlaceid(request.queryParams("listeadresse"));//Set the address
                request.session().attribute("user",u);//Update the session with placeid
                response.redirect("/register");
                return "<h1>Redirection error</h1>";
            }
            else return "<h1>Error, session doesn't exist";
        });
    }


    public boolean isValid(String unformattedAddress) throws IOException {
        JSONObject j = Server.getAPI().textSearch(unformattedAddress);

        System.out.println(j.getString("status"));
        return (j.getString("status").equals("OK")); // if status is OK then address is valid
    }


}

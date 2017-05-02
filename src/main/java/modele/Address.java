package modele;

import controleur.ControlerAddress;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import tools.Server;

import java.io.IOException;
import java.util.Map;

import static spark.Spark.get;

/**
 * Created by dnguye2 on 27/03/17.
 */
public class Address {
    public String formatedAddress;
    public String placeid;

    public Address(){}

    public Address(String placeid, String validAddress){
        this.placeid = placeid;
        formatedAddress = validAddress;
    }

    public static void start(){
        get("/choose_address", (request, response) -> { //User choose the valid address in the liste
            String unformattedAddress = request.queryParams("unformatted_address");
            ControlerAddress control = new ControlerAddress();
            Map validList = control.getValidAddress(unformattedAddress);

            return new ModelAndView(validList,"tabaddress.hbs");
        }, new HandlebarsTemplateEngine());

//        post("/validAddress",((request, response) -> {
//            String request.queryParams();
//
//            return null
//        });
    }

    public String getPlaceid() {
        return placeid;
    }

    public String getFormatedAddress() {
        return formatedAddress;
    }

    public boolean isValid(String unformattedAddress) throws IOException {
        JSONObject j = Server.getAPI().textSearch(unformattedAddress);

        System.out.println(j.getString("status"));
        return (j.getString("status").equals("OK")); // if status is OK then address is valid
    }


}

package modele;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.context.FieldValueResolver;
import controleur.ControlerAddress;
import org.json.JSONException;
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
            try{
                String unformattedAddress = request.queryParams("unformatted_address");
                ControlerAddress control = new ControlerAddress();
                //Map map = control.getValidAddress(unformattedAddress);
                ArrayList<Map> liste = control.getValidAddress(unformattedAddress);

                Map map = new HashMap();
                map.put("items", liste);

                return new ModelAndView(map,"tabaddress.hbs");
            }
            catch(Exception e){
                response.redirect("/error?msg="+e.toString());

                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }

        }, new HandlebarsTemplateEngine());

        post("/validAddress",(request, response) -> {//Getting the placid from user
            User u = request.session().attribute("user");

            if(u != null){
                if (u.getPlaceid() != null){//Assuming user already register so modify the address instead of register
                    Server.getDatabase().modifyUserPlaceId(u.getId(),request.queryParams("listeadresse"));
                    response.redirect("/");
                    Map map = new HashMap();
                    map.put("message","Redirection error");
                    return new ModelAndView(map,"error.hbs");
                }
                else{
                    u.setPlaceid(request.queryParams("listeadresse"));//Set the address
                    request.session().attribute("user",u);//Update the session with placeid
                    response.redirect("/register");
                    Map map = new HashMap();
                    map.put("message","Redirection error");
                    return new ModelAndView(map,"error.hbs");
                }
            }
            else{
                Map map = new HashMap();
                map.put("message","Error, session doesn't exist");
                return new ModelAndView(map,"error.hbs");
            }
        },new HandlebarsTemplateEngine());
    }


    public boolean isValid(String unformattedAddress) throws IOException, JSONException {
        JSONObject j = Server.getAPI().textSearch(unformattedAddress);

        return (j.getString("status").equals("OK")); // if status is OK then address is valid
    }

    public static Address getAddressFromId(String placeid) throws IOException, JSONException {
        JSONObject j = Server.getAPI().placedetails(placeid);
        JSONObject addr = j.getJSONObject("result");

        Address a = new Address(addr.getString("formatted_address"),placeid);

        return a;
    }
}

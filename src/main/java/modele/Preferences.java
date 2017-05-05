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
public class Preferences {
    public static void start(){
        get("/profil",(request, response)->{
            User u = request.session().attribute("user");
            if (u == null) {
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            else{
                HashMap map = new HashMap();
                map.put("nom",u.getName());
                map.put("prenom",u.getFirstname());
                map.put("adresse",Address.getAddressFromId(u.getPlaceid()).formattedAddress);

                map.put("preferences",Server.getDatabase().selectFoodPref(u.getId()));

                return new ModelAndView(map,"modifprofil.hbs");
            }
        }, new HandlebarsTemplateEngine());

        post("/profil",(request, response) -> {
            User u = request.session().attribute("user");
            if (u == null) {
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
            else{
                String foodpref = request.queryParams("preferencealimentaire");
                Server.getDatabase().modifyFoodPref(u.getId(),foodpref);
                response.redirect("/");
                Map map = new HashMap();
                map.put("message","Redirection error");
                return new ModelAndView(map,"error.hbs");
            }
        }, new HandlebarsTemplateEngine());
    }
}

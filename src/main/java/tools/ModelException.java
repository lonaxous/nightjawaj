package tools;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;

/**
 * Created by dnguye2 on 04/05/17.
 */
public class ModelException extends Exception{

    public ModelException(String message){
        super(message);
    }

    public static void start() {
        get("/error", (request, response) -> {
            String message = request.queryParams("msg");
            Map map = new HashMap();
            map.put("message", message);
            return new ModelAndView(map, "error.hbs");
        }, new HandlebarsTemplateEngine());
    }
}

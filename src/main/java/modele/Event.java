package modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static spark.Spark.post;

/**
 * Created by dnguye2 on 27/03/17.
 */
public class Event {
    private String name;
    private String dateDeb;
    private String dateFin;

    private List<User> hisAmbiances;
    private List<Activity> hisActivities;

    public Event(String name, String dateDeb, String dateFin) {
        this.name = name;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    public static void start(){
        post("/event", (request, response) -> {
            Event e = new Event(request.queryParams("nomevenement"),
                    request.queryParams("heuredebut"),
                    request.queryParams("heurefin"));
            return "Name : "+e.name+"<br>"+"hdeb : "+e.dateDeb+"<br>hfin : "+e.dateFin;
        });
    }
}

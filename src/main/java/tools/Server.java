package tools;

import modele.Address;
import modele.Event;
import modele.Preferences;
import modele.User;
import spark.Spark;

import java.sql.SQLException;
import java.util.Scanner;

import static spark.Spark.staticFiles;

/**
 * Created by dnguye2 on 02/05/17.
 */
public class Server {
    private static API api;
    private static Database db;

    public void initiate() throws SQLException {
        db = new Database(); //Initiate connection to database
        System.out.println("tools.tools.Database creation sucess!");
        staticFiles.location("/"); // Initialize static files folder

        Scanner sc = new Scanner(System.in);
        System.out.print("API Key : ");
        String apikey = sc.nextLine();
        db.insertApiG(apikey);
    }

    public void start() throws SQLException {
        db = new tools.Database();
        System.out.println("tools.Database connection success, starting server");

        String apikey = db.selectApiG();
        api = new API(apikey);

        staticFiles.location("/"); // Initialize static files folder
        User.start();
        Address.start();
        Event.start();
        ModelException.start();
        Preferences.start();
    }

    public static API getAPI(){
        return api;
    }

    public static Database getDatabase(){
        return db;
    }

    public static void main(String args[]) throws SQLException {
        /*System.setProperty("https.proxyHost", "cache.u-psud.fr");
        System.setProperty("https.proxyPort", "8080");*/

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        Server s = new Server();
        if (args.length > 0){
            if (args[0].equals("start")) s.start();
            else if (args[0].equals("initiate")) s.initiate();
            else System.out.println("Invalid argument");
        }
        else System.out.println("Arguments :\n<start> start the server\n<initiate> create the initial config and database");
    }
}

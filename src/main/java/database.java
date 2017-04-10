/**
 * Created by lucas on 09/04/17.
 */
import java.sql.*;

public class database {

    //Attribut
    Connection co;

    //Constructeur
    database() throws SQLException {
        co = DriverManager.getConnection("à mettre");
    }

    //Fonctions

    //Inscription
    public void register(String fname, String lname){
        String text =  "insert into user(id,fname,lname)" +
                " values(seq_user.nextval,'"+fname+"','"+lname+"')";

        executeSQL(text);
    }

    //Modifier firstName d'un utilisateur
    public void modifyUserFName(int idu,String fname){
        String text = "update user" +
                "set fname = '"+fname+"' "+
                "where id = "+idu;

        executeSQL(text);
    }

    //Modifier lastName d'un utilisateur
    public void modifyUserLName(int idu,String lname){
        String text = "update user" +
                "set lname = '"+lname+"' "+
                "where id = "+idu;

        executeSQL(text);
    }

    //Modifier adresse d'un utilisateur
    public void modifyUserAdrs(int idu,String adrs){
        String text = "update user" +
                "set adrs = '"+adrs+"' "+
                "where id = "+idu;

        executeSQL(text);
    }

    //Modifier le moyen de transport d'un utilisateur
    public void modifyTransport(int idu, String transport){
        String text = "update user" +
                "set transport = '"+transport+"' "+
                "where id = "+idu;

        executeSQL(text);
    }

    //Modifier les préférences allimentaire
    public void modifyFoodPref(int idu, String foodPref){
        String text = "update user" +
                "set foodpref = '"+foodPref+"' "+
                "where id = "+idu;

        executeSQL(text);
    }

    //Créer un évènement
    public void createEvent(int idu, String name, Date date){
        int ide = seqNumber("event");
        //Insertion d'un nouvelle event
        String text = "insert into event(id,name,date)+" +
                " values("+ide+",'"+name+"',"+date+")";
        executeSQL(text);
        //Liaison entre un event et l'organisateur
        String text2 = "insert into organiser " +
                "values(seq_organiser.nextval,"+idu+","+ide+")";
        executeSQL(text2);
    }

    //Evenement modifier heure de début
    public void modifyEventStartHour(int ide, String startHour){
        String text = "update event " +
                "set starthour = "+startHour+" " +
                "where id ="+ide;
        executeSQL(text);
    }

    //Evenement modifier heure de fin
    public void modifyEventEndHour(int ide, String endHour){
        String text = "update event " +
                "set endhour ="+endHour+" " +
                "where id ="+ide;
        executeSQL(text);
    }

    //Evenement modifier la date
    public void modifyEventDate(int ide, Date date){
        String text = "update event " +
                "set date ="+date+" " +
                "where id ="+ide;
        executeSQL(text);
    }

    //Evenement modifier le nom
    public void modifyEventName(int ide, String name){
        String text = "update event " +
                "set name ="+name+" " +
                "where id ="+ide;
        executeSQL(text);
    }

    //Annuler un événement
    public  void deleteEvent(int ide, int idu){
        String text = "delete * " +
                "from organiser " +
                "where ide = "+ide+" " +
                "and idu = "+idu;
        executeSQL(text);

        String text2 = "delete * " +
                "from event " +
                "where id ="+ide;
        executeSQL(text2);
    }



    //Ajouter une activité à un événement
    public void addActivity(int ide, String name, String adrs){
        int ida = seqNumber("activity");
        //Insertion d'une nouvelle activity
        String text = "insert into activity(id,name,date)+" +
                " values("+ida+",'"+name+"',"+adrs+")";
        executeSQL(text);
        //Liaison entre un event et l'organisateur
        String text2 = "insert into planing " +
                "values(seq_planing.nextval,"+ide+","+ida+")";
        executeSQL(text2);
    }

    //Activity modifier le nom
    public void modifyActivityName(int ida, String name){
        String text = "update activity " +
                "set name ="+name+" " +
                "where id ="+ida;
        executeSQL(text);
    }

    //Activity modifier l'adresse
    public void modifyActivityAdrs(int ida, String adrs){
        String text = "update activity " +
                "set adrs ="+adrs+" " +
                "where id ="+ida;
        executeSQL(text);
    }

    //Activity modifier la date
    public void modifyActivityDate(int ida, Date date){
        String text = "update activity " +
                "set date ="+date+" " +
                "where id ="+ida;
        executeSQL(text);
    }

    //Activity modifier l'heure de début
    public void modifyActivityStartHour(int ida, String startHour){
        String text = "update activity " +
                "set starthour ="+startHour+" " +
                "where id ="+ida;
        executeSQL(text);
    }

    //Activity modifier l'heure de fin
    public void modifyActivityEndHour(int ida, String endHour){
        String text = "update activity " +
                "set endhour ="+endHour+" " +
                "where id ="+ida;
        executeSQL(text);
    }

    //Supprimer une activité
    public void deletePlaning(int ida, int ide){
        String text = "delete * " +
                "from planing " +
                "where ide = "+ide+" " +
                "and ida ="+ida;
        executeSQL(text);

        String text2 = "delete * " +
                "from activity " +
                "where id ="+ida;
        executeSQL(text2);
    }


    //Ajouter un ambiancé
    public void addAmbiance(int ide,int idu){
        int idamb = seqNumber("ambiance");
        String text = "insert into ambiance " +
                "values("+idamb+","+idu+","+ide+")";
        executeSQL(text);
    }

    //Enlever un ambiancé
    public void deleteAmbiance(int ide, int idu){
        String text = "delete * " +
                "from ambiance " +
                "where ide = "+ide+" " +
                "and idu ="+idu;
        executeSQL(text);
    }

    //Select

    //Selection de la liste des ambiancés d'un événement
    public ResultSet selectListAmbiance(int ide){
        String text = "select fname, lname " +
                "from user u, ambiance a, organiser o " +
                "where u.id = a.idu " +
                "and u.id = o.idu " +
                "and a.ide = "+ide+" " +
                "and o.ide = "+ide;
        return selectSQL(text);
    }

    //Selection des informations d'un utilisateur
    public  ResultSet selectUser(int idu){
        String text = "select * " +
                "from user " +
                "where id ="+idu;
        return selectSQL(text);
    }

    //Selection des informations d'un evenement
    public ResultSet selectEvent(int ide){
        String text = "select * " +
                "from event " +
                "where id = "+ide;
        return selectSQL(text);
    }

    //Selection des information d'une activité
    public ResultSet selectActivity(int ida){
        String text = "select * " +
                "from activity " +
                "where id = "+ida;
        return selectSQL(text);
    }

    //Selection du planing d'un événement
    public ResultSet selectPlaning(int ide){
        String text = "select name, starthour, endhour, adrs " +
                "from activity a, planing p " +
                "where a.id = p.ida " +
                "and p.ide ="+ide;
        return  selectSQL(text);
    }




    //Fonction executant du SQL
    public void executeSQL(String text){
        try {
            Statement s = co.createStatement();
            s.executeUpdate(text);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Fonction Pour executer les select en sql
    public ResultSet selectSQL(String text){
        try{
            Statement s = co.createStatement();
            return s.executeQuery(text);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //Select de sequence - prend le nom d'une table et retourne le nextval d'une sequence
    //Tables : (user,event,activity,organiser,ambiance,planing)
    public int seqNumber(String table){
        String text = "select seq_"+table+".nextval from dual";
        try{
            Statement s = co.createStatement();
            ResultSet r = s.executeQuery(text);
            if(r.next()) return r.getInt(1);
            else{
                System.out.println("Erreur table incorrecte dans la demande de numéro de séquence");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
 }
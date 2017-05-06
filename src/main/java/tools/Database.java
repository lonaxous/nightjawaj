package tools; /**
 * Created by lucas on 09/04/17.
 */
//         ()
// ________/__
// \ _____/_ /
//  \\     //
//   \\   //
//    \\ //
//     | |
//     | |
//    /   \
//   /_____\
//  NightJawaj
import modele.Event;
import modele.User;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    //Attribut
    Connection co;

    //Constructeur
    Database() throws SQLException {
        co = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    //Fonctions

    //Inscription
    public void register(String fname, String lname, String placeid, String mail,String psw) throws Exception {
        String text =  "insert into user(fname,lname,placeid,mail,psw) " +
                "values(?,?,?,?,?)";
        if(!verifmail(mail)) {
            PreparedStatement ps = co.prepareStatement(text);
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, placeid);
            ps.setString(4, mail);
            ps.setString(5, psw);
            ps.executeUpdate();
            ps.close();
        }
        else
            throw new Exception("Mail already present");

    }

    //Modifier firstName d'un user
    public void modifyUserFName(int idu,String fname)throws SQLException{
        String text = "update user " +
                "set fname = ? "+
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1,fname);
        ps.setInt(2,idu);
        ps.executeUpdate();
        ps.close();
    }

    //Modifier lastName d'un user
    public void modifyUserLName(int idu,String lname)throws SQLException{
        String text = "update user " +
                "set lname = ? "+
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1,lname);
        ps.setInt(2,idu);
        ps.executeUpdate();
        ps.close();
    }

    //Modifier adresse d'un user
    public void modifyUserPlaceId(int idu,String placeid)throws SQLException{
        String text = "update user " +
                "set placeid = ? "+
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, placeid);
        ps.setInt(2,idu);
        ps.executeUpdate();
        ps.close();
    }

    //Modifier le moyen de transport d'un user
    public void modifyTransport(int idu, String transport)throws SQLException{
        String text = "update user " +
                "set transport = ? "+
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, transport);
        ps.setInt(2,idu);
        ps.executeUpdate();
        ps.close();
    }

    //Modifier les préférences allimentaire
    public void modifyFoodPref(int idu, String foodPref)throws SQLException{
        String text = "update user " +
                "set foodpref = ? "+
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, foodPref);
        ps.setInt(2,idu);
        ps.executeUpdate();
        ps.close();
    }

    //Modifier l'adresse mail
    public void modifyMail(int idu, String mail)throws SQLException{
        String text = "update user set mail = ? where idu = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, mail);
        ps.setInt(2, idu);
        ps.executeUpdate();
        ps.close();
    }

    //Modifier les evenements
    public void modifyPsw(int idu, String psw)throws SQLException{
        String text = "update user set psw = ? where idu = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, psw);
        ps.setInt(2, idu);
        ps.executeUpdate();
        ps.close();
    }

    //Créer un évènement
    public void createEvent(int idu, String name,String startDate,String endDate)throws SQLException{
        //Insertion d'un nouvelle event
        String text = "insert into event(name,startdate,enddate) " +
                "values(?,?,?)";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1,name);
        System.out.println(startDate);
        ps.setString(2, startDate);
        ps.setString(3, endDate);
        ps.executeUpdate();
        ps.close();
        //On prend le dernier id
        int ide = -1;
        String lastId = "select max(id) from event where name ='"+name+"' and startdate ='"+startDate+"' and enddate ='"+endDate+"'";
        Statement s = co.createStatement();
        ResultSet rs = s.executeQuery(lastId);
        if(rs.next())ide = rs.getInt(1);
        //Liaison entre un event et l'organisateur
        String text2 = "insert into organiser(idu,ide) " +
                "values(?,?)";
        PreparedStatement ps2 = co.prepareStatement(text2);
        ps2.setInt(1,idu);
        ps2.setInt(2,ide);
        ps2.executeUpdate();
        ps.close();
    }

    //Evenement modifier date de début
    public void modifyEventStartDate(int ide, String startDate)throws SQLException{
        String text = "update event " +
                "set startdate = ? " +
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, startDate);
        ps.setInt(2,ide);
        ps.executeUpdate();
        ps.close();
    }

    //Evenement modifier date de fin
    public void modifyEventEndDate(int ide, String endDate)throws SQLException{
        String text = "update event " +
                "set enddate = ? " +
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, endDate);
        ps.setInt(2,ide);
        ps.executeUpdate();
        ps.close();
    }

    //Evenement modifier le nom
    public void modifyEventName(int ide, String name)throws SQLException{
        String text = "update event " +
                "set name = ? " +
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, name);
        ps.setInt(2,ide);
        ps.executeUpdate();
        ps.close();
    }

    //Annuler un événement
    public  void deleteEvent(int ide)throws SQLException{
        String text = "delete " +
                "from organiser " +
                "where ide = ? ";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setInt(1, ide);
        ps.executeUpdate();
        ps.close();

        String text2 = "delete " +
                "from ambiance " +
                "where ide = ?";
        PreparedStatement ps2 = co.prepareStatement(text2);
        ps2.setInt(1,ide);
        ps2.executeUpdate();
        ps2.close();

        String text3 = "delete " +
                "from event " +
                "where id = ?";
        PreparedStatement ps3 = co.prepareStatement(text3);
        ps3.setInt(1, ide);
        ps3.executeUpdate();
        ps3.close();
    }

    //Ajouter une activité à un événement
    public void createActivity(int ide, String name, String placeid)throws SQLException{
        //Insertion d'une nouvelle activity
        String text = "insert into activity(name,placeid) " +
                "values(?,?)";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, name);
        ps.setString(2, placeid);
        ps.executeUpdate();
        ps.close();
        //On prend le dernier id
        int ida = -1;
        String lastId = "select max(id) from activity where name ='"+name+"' and placeid ='"+placeid+"'";
        Statement s = co.createStatement();
        ResultSet rs = s.executeQuery(lastId);
        if(rs.next())ida = rs.getInt(1);
        //Liaison entre un event et l'organisateur
        String text2 = "insert into planing " +
                "values(?,?)";
        PreparedStatement ps2 = co.prepareStatement(text2);
        ps2.setInt(1, ide);
        ps2.setInt(2, ida);
        ps2.executeUpdate();
        ps2.close();
    }

    //Activity modifier le nom
    public void modifyActivityName(int ida, String name)throws SQLException{
        String text = "update activity " +
                "set name = ? " +
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, name);
        ps.setInt(2,ida);
        ps.executeUpdate();
        ps.close();
    }

    //Activity modifier l'adresse
    public void modifyActivityPlaceid(int ida, String placeid)throws SQLException{
        String text = "update activity " +
                "set placeid = ? " +
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, placeid);
        ps.setInt(2,ida);
        ps.executeUpdate();
        ps.close();
    }

    //Activity modifier la date de début
    public void modifyActivityStartDate(int ida, String startdate)throws SQLException{
        String text = "update activity " +
                "set startdate = ? " +
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, startdate);
        ps.setInt(2,ida);
        ps.executeUpdate();
        ps.close();
    }

    //Activity modifier la date de fin
    public void modifyActivityEndDate(int ida, String enddate)throws SQLException{
        String text = "update activity " +
                "set enddate = ? " +
                "where id = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1, enddate);
        ps.setInt(2,ida);
        ps.executeUpdate();
        ps.close();
    }

    //Supprimer une activité
    public void deletePlaning(int ida)throws SQLException{
        String text = "delete " +
                "from planing " +
                "where ida = ? ";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setInt(1,ida);
        ps.executeUpdate();
        ps.close();

        String text2 = "delete " +
                "from activity " +
                "where id = ?";
        PreparedStatement ps2 = co.prepareStatement(text2);
        ps.setInt(1,ida);
        ps.executeUpdate();
        ps.close();
    }


    //Ajouter un ambiancé
    public void insertAmbiance(int idu,int ide) throws SQLException {
        String text = "insert into ambiance(idu,ide) values(?,?)";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setInt(1, idu);
        ps.setInt(2, ide);
        ps.executeUpdate();
        ps.close();
    }

    //Enlever un ambiancé
    public void deleteAmbiance(int idu, int ide) throws SQLException {
        String text = "delete from ambiance where idu = ? and ide = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setInt(1, idu);
        ps.setInt(2, ide);
        ps.executeUpdate();
        ps.close();
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

    //Connexion
    public User connect(String mail,String password) throws Exception {
        String text = "SELECT id FROM user WHERE mail = ? AND psw = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1,mail);
        ps.setString(2,password);

        ResultSet rs = ps.executeQuery();

        if(!rs.next()){
            throw new Exception("Unkown user");
        }
        int idu = rs.getInt("id");

        User u = selectUser(idu);
        return u;
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

    //Donne l'id un utilisateur à partir du mail
    public int selectUserMail(String mail)throws SQLException{
        String requete = "select id from user where mail='"+mail+"'";
        Statement s = co.createStatement();
        ResultSet rs = s.executeQuery(requete);
        if(rs.next())return rs.getInt(1);
        return -1;
    }

    //insertion d'une clé apiGoogle(à utiliser une unique fois pour entrer la clé)
    public void insertApiG(String clef)throws SQLException{
        String text = "insert into APIgoogle values(1,?)";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1,clef);
        ps.executeUpdate();
        ps.close();
    }

    //Modification d'une clé apiGoogle
    public void updateApiG(String clef)throws SQLException{
        String text = "update APIgoogle set clef = ? where id = 1";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1,clef);
        ps.executeUpdate();
        ps.close();
    }

    //Obtenir la clé d'api google
    public String selectApiG()throws SQLException{
        String text = "select clef from APIgoogle where id = 1";
        ResultSet rs = selectSQL(text);
        return rs.getString(1);
    }

    //Selection des event que l'utilisateur à organiser
    public ArrayList<Event> selectUserEvent(int idu) throws Exception {
        ArrayList<Event> listEvent = new ArrayList<>();
        String requete = "select e.id,e.name,e.startdate,e.enddate " +
                "from event e, organiser o " +
                "where e.id = o.ide " +
                "and o.idu = "+idu;
        Statement s= co.createStatement();
        ResultSet rs = s.executeQuery(requete);
        while(rs.next()){
            listEvent.add(new Event(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),selectUser(idu)));
        }
        return listEvent;
    }

    //Selection des event auquels l'utilisateur participe
    public ArrayList<Event> selectAmbianceEvent(int idu) throws Exception {
        ArrayList<Event> listEvent = new ArrayList<>();
        String requete = "select e.id,e.name,e.startdate,e.enddate " +
                "from event e, ambiance a " +
                "where e.id = a.ide " +
                "and a.idu = "+idu;
        Statement s= co.createStatement();
        ResultSet rs = s.executeQuery(requete);
        while(rs.next()){
            listEvent.add(new Event(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),selectOrganiser(rs.getInt(1))));
        }
        return listEvent;
    }

    //Donne l'organisateur d'un event à partir de l'identifiant de celui-ci
    public User selectOrganiser(int ide)throws Exception{
        String requete = "select idu from organiser where ide="+ide;
        Statement s = co.createStatement();
        ResultSet rs = s.executeQuery(requete);
        if(rs.next()){
            return selectUser(rs.getInt(1));
        }
        throw new Exception("Event does not exist");
    }

    //Selection d'un user à partir de son id
    public User selectUser(int idu)throws Exception{
        String requete = "select * from user where id = "+idu;
        Statement s = co.createStatement();
        ResultSet rs = s.executeQuery(requete);
        if(rs.next()){
            return new User(rs.getInt(1),rs.getString(3),rs.getString(2),rs.getString(4),rs.getString(5));
        }
        throw new Exception("User does not exist");
    }

    //Donne les préférence allimentaire d'un utilisateur à partir de son id
    public String selectFoodPref(int idu) throws Exception {
        String requete = "select foodpref from user where id ="+idu;
        Statement s = co.createStatement();
        ResultSet rs = s.executeQuery(requete);
        if(rs.next()) return rs.getString(1);
        throw new Exception("User does not exist");
    }

    //Donne tous les ambiances d'un event en fonction de l'ide
    public ArrayList<User> selectAmbiance(int ide)throws SQLException{
        ArrayList<User> listUser = new ArrayList<>();
        String requete = "select distinct u.id,u.fname,u.lname,u.placeid,u.mail " +
                "from user u,ambiance a " +
                "where u.id = a.idu " +
                "and a.ide ="+ide;
        Statement s = co.createStatement();
        ResultSet rs = s.executeQuery(requete);
        while (rs.next()){
            listUser.add(new User(rs.getInt(1),rs.getString(3),rs.getString(2),rs.getString(4),rs.getString(5)));
        }
        return listUser;
    }

    //Fonction pour vérifier si une adressemail existe déjà
    //Retourne vrai si elle existe, faux sinon
    public boolean verifmail(String mail)throws SQLException{
        String text = "select id from user where mail = ?";
        PreparedStatement ps = co.prepareStatement(text);
        ps.setString(1,mail);
        ResultSet rs = ps.executeQuery();
        boolean isPresent = rs.next();
        return isPresent;
    }

    //Fonction vérifiant si l'user est bien l'organiser d'un event
    public boolean isOragniserEvent(int idu, int ide)throws SQLException{
        String text = "select * from organiser where idu ="+idu+" and ide = "+ide;
        Statement s = co.createStatement();
        ResultSet rs = s.executeQuery(text);
        return rs.next();
    }

    //Fonction vérifie si l'id est un ambiancé de l'event
    public boolean isAmbiance(int idu, int ide)throws SQLException{
        String text = "select * from ambiance where idu ="+idu+" and ide = "+ide;
        Statement s = co.createStatement();
        ResultSet rs =s.executeQuery(text);
        return rs.next();
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

 }
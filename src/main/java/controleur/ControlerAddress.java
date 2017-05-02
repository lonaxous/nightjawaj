package controleur;

import modele.Address;
import org.json.JSONObject;
import tools.Database;
import tools.Server;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by dnguye2 on 02/05/17.
 */
public class ControlerAddress {
    Address hisAddress;


    public Map getValidAddress(String unformattedAddress) throws Exception {
        Address a = new Address();
        if(a.isValid(unformattedAddress)){
            JSONObject j = Server.getAPI().textSearch(unformattedAddress);

            ArrayList<Address> listAddress = new ArrayList<>();
            System.out.println("nb address : "+j.getJSONArray("results").length());
            for (int i = 0; i < j.getJSONArray("results").length(); i++) { // Getting results in an Array
                JSONObject addr = j.getJSONArray("results").getJSONObject(i);
                listAddress.add(new Address(addr.getString("place_id"),addr.getString("formatted_address"))); //getting the valid address
            }

            Map map = new HashMap();
            map.put("items", listAddress);

            return map;

        }
        else throw(new Exception("Unvalid address"));
    }
}

package controleur;

import modele.Address;
import org.json.JSONObject;
import tools.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                System.out.println("Place id : "+ addr.getString("place_id"));
                listAddress.add(new Address(addr.getString("formatted_address"),addr.getString("place_id"))); //getting the valid address
            }

            Map map = new HashMap();
            map.put("items", listAddress);

            return map;

        }
        else throw(new Exception("Unvalid address"));
    }
}

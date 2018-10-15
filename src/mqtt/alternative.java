/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;



/**
 *
 * @author Loukas
 */
public class alternative {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        JSONObject json1 = new JSONObject(IOUtils.toString(new URL("http://api.thingspeak.com/channels/438939/feed.json"), Charset.forName("UTF-8")));//for current date
        	
	System.out.print("For one day: ");
        //System.out.println(json1);
        JSONArray jsonarr= json1.getJSONArray("feeds");
        System.out.println(jsonarr);
        String address = jsonarr.getJSONObject(1).getString("field1");
	System.out.println(address);
               
    }
   
       
}
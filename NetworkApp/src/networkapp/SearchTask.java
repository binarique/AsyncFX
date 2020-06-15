/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkapp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.visionwireless.asyncfx.AsyncFX;
import com.visionwireless.asyncfx.Header;
import com.visionwireless.asyncfx.Pair;
import java.util.Iterator;

/**
 *
 * @author ICY media
 */
public class SearchTask extends AsyncFX<String>{
    private final MainController controller;
    private final String URL;
    private final String SearchKey;
    
    public SearchTask(MainController controller, String URL, String SearchKey){
    this.controller = controller;
    this.URL = URL;
    this.SearchKey = SearchKey;
    }
    @Override
    protected String doInBackground() {
 
    Pair pair = new Pair(null);
    pair.add("q", SearchKey);
    pair.add("client", "webapp");
    pair.add("category", "2");
    Header header = new Header();
    header.add("lang", "lug");
    final String response = Get(URL, header, pair);
    return response;
    }

    @Override
    protected void onError(String error) {
    System.out.println(error);
    }

    @Override
    protected void onBeforeSend() {
    System.out.println("seaching...");
    controller.showProgress(true);
    }

    @Override
    protected void onSuccess(String response) {
      controller.showProgress(false);
     System.out.println("finished......");
      System.out.println("Response Code:" + getResponseCode());
      
     JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
     Iterator<JsonElement>itr = jsonObject.getAsJsonArray("products").iterator();
     while(itr.hasNext()){
      JsonElement ele =  itr.next();
      String productname = ele.getAsJsonObject().get("product_name").getAsString();
     controller.UpdateList(productname);
     }
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**emController
 * FXML Controller class
 *
 * @author ICY media
 */
public class ListItemController{

    @FXML
    private Text item_text;
    @FXML
    private HBox list_item;

    public  ListItemController(){
     FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("list_item.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    public void setResultName(String resultValue){
    item_text.setText(resultValue);    
    }
    
    
    public HBox getView(){
    return list_item;   
    }
}

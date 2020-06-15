/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkapp;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class SearchListCell extends ListCell<ResultModel>{
    
private ListItemController controller;

public SearchListCell(){
super();
}    

@Override
public void updateItem(ResultModel helper, boolean empty){
super.updateItem(helper, empty);
setText(null);
setGraphic(null); 
if(helper != null && !empty){
controller = new ListItemController();
controller.setResultName(helper.getResultName());
HBox hbox = controller.getView();
setGraphic(hbox);
}
}
}

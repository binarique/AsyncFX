/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkapp;

import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author ICY media
 */
public class MainController implements Initializable {
    
    private Label label;
    @FXML
    private TextField search_bar;
    @FXML
    private ListView<ResultModel> search_list;
    
    public final ObservableList<ResultModel> search_items = FXCollections.observableArrayList();
    @FXML
    private JFXProgressBar progress_bar;
    private final String URL = "http://localhost/shopper/api/v1/search/search.php";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    progress_bar.setVisible(false);
    search_items.clear();
  initFileList();
    }
    
     private void initFileList(){
    search_list.setItems(search_items);
    search_list.setCellFactory(param-> new SearchListCell());
    conductSearch("");
    }
     
     public void UpdateList(String productname){
     search_items.add(new ResultModel(productname));    
     }
     
     public void conductSearch(String searchKey){
     search_items.clear();
     SearchTask task = new SearchTask(this, URL, searchKey);
    task.setConcurrent(true);
    task.execute();    
     }

    @FXML
    private void searchFiles(KeyEvent event) {
    String searchKey = search_bar.getText();    
    conductSearch(searchKey);
    }
    
    public void showProgress(boolean status){
    progress_bar.setVisible(status);
    }
    
}

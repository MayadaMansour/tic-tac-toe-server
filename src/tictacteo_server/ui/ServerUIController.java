/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictacteo_server.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import tictactoe.resources.styles.Styles;
import tictactoe.router.RouteViewController;

/**
 * FXML Controller class
 *
 * @author ITI
 */
public class ServerUIController extends RouteViewController{

    @FXML
    private Button StartButton;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          scene().getStylesheets().add(resourcesLoader().getCss(Styles.GAME_STYLE_STRING).toString());
            CategoryAxis categoryAxis = new CategoryAxis();
            NumberAxis NumberAxis = new NumberAxis();
             BarChart barChart =  new BarChart<>(categoryAxis,NumberAxis);  
             
             categoryAxis.setLabel("bars");       
             NumberAxis.setLabel("Value");
             
            XYChart.Series <String,Number>series1 = new XYChart.Series<>(); 
            series1.setName("totalUsers");
            series1.getData().add(new XYChart.Data("totalUsers",totalUsers.getValue())); 
            
            XYChart.Series <String,Number>series2 = new XYChart.Series<>(); 
            series2.setName("activeClients");
            series2.getData().add(new XYChart.Data("activeClients",activeGames.values()));
            
            barChart.getData().addAll(series1,series2);
            
            barChart.setMaxHeight(400);
            barChart.setMaxWidth(400);
            barChart.setLayoutX(140);
            barChart.setLayoutY(0);
            
            anchorPane.getChildren().add(barChart);
            
            StartButton.setOnAction((event) -> {
                
             StartButton.setText("Stop");
           
         });
       

    }    

    @Override
    public URL getViewUri() {
     return getClass().getResource("ServerUI.fxml");
    }
    
}

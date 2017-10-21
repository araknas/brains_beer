package com.araknas.brains_beer.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Giedrius on 2017.10.21.
 */
@Component
public class GamesWindowController {

    @Autowired
    ConfigurableApplicationContext springContext;

    @FXML
    Button gameWindowBackButton;

    Stage gamesWindowStage;

    public void handleGameWindowBackButtonClick(){
        hideGameWindow();
    }

    public void displayGameWindow(){
        try {
            if(gamesWindowStage != null && !gamesWindowStage.isShowing()){
                gamesWindowStage.show();
            }
            else{
                createNewGamesWindowAndDisplay();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            // TODO: add logging
        }
    }

    public void hideGameWindow(){
        try {
            if(gamesWindowStage != null && gamesWindowStage.isShowing()){
                gamesWindowStage.hide();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            // TODO: add logging
        }
    }

    private void createNewGamesWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/GamesWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent gamesWindowParent = fxmlLoader.load();

        gamesWindowStage = new Stage();
        gamesWindowStage.setTitle("Games Window");
        gamesWindowStage.setScene(new Scene(gamesWindowParent));
        gamesWindowStage.show();
    }
}

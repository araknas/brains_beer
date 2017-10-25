package com.araknas.brains_beer.controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Giedrius on 2017.10.21.
 */
@Component
public class GamesWindowController {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    ConfigurableApplicationContext springContext;

    @Autowired
    TeamsWindowController teamsWindowController;

    @FXML
    Button gameWindowBackButton;

    private Stage gamesWindowStage;

    public void handleGameWindowBackButtonClick(){
        hideGamesWindow();
        teamsWindowController.displayTeamsWindow();
    }

    public void displayGamesWindow(){
        try {
            if(gamesWindowStage != null && !gamesWindowStage.isShowing()){
                gamesWindowStage.show();
            }
            else{
                createNewGamesWindowAndDisplay();
            }
        }
        catch (Exception e){
            logger.error("Exception while displaying Games Window, e = " + e.getMessage(), e);
        }
    }

    public void hideGamesWindow(){
        try {
            if(gamesWindowStage != null && gamesWindowStage.isShowing()){
                gamesWindowStage.hide();
            }
        }
        catch (Exception e){
            logger.error("Exception while hiding Games Window, e = " + e.getMessage(), e);
        }
    }

    private void createNewGamesWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/GamesWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent gamesWindowParent = fxmlLoader.load();

        gamesWindowStage = new Stage();
        gamesWindowStage.setTitle("Games Window");

        gamesWindowStage.setScene(new Scene(gamesWindowParent));

        gamesWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        gamesWindowStage.show();
    }
}

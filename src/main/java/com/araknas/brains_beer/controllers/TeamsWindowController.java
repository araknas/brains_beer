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
public class TeamsWindowController {

    @Autowired
    ConfigurableApplicationContext springContext;

    @Autowired
    GamesWindowController gamesWindowController;

    @FXML
    Button teamWindowNextButton;

    private Stage teamsWindowStage;

    public void handleTeamWindowNextButtonClick(){
        this.hideTeamsWindow();
        gamesWindowController.displayGamesWindow();
    }

    public void displayTeamsWindow(){
        try {
            if(teamsWindowStage != null && !teamsWindowStage.isShowing()){
                teamsWindowStage.show();
            }
            else{
                createNewTeamsWindowAndDisplay();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            // TODO: add logging
        }
    }

    public void hideTeamsWindow(){
        try {
            if(teamsWindowStage != null && teamsWindowStage.isShowing()){
                teamsWindowStage.hide();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            // TODO: add logging
        }
    }

    private void createNewTeamsWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/TeamsWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent gamesWindowParent = fxmlLoader.load();

        teamsWindowStage = new Stage();
        teamsWindowStage.setTitle("Teams Window");
        teamsWindowStage.setScene(new Scene(gamesWindowParent));
        teamsWindowStage.show();
    }
}

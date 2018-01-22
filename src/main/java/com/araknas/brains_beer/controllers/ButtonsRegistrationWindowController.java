package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.Team;
import com.araknas.brains_beer.services.KeyboardService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Giedrius on 2017-11-17.
 */

@Component
public class ButtonsRegistrationWindowController {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    GameStartWindowController gameStartWindowController;

    @Autowired
    MessageWindowController messageWindowController;

    @Autowired
    ConfigurableApplicationContext springContext;

    @Autowired
    KeyboardService keyboardService;

    @Autowired
    BlitzRoundWindowController blitzRoundWindowController;

    private Stage buttonsRegistrationWindowStage;

    private HashMap<Integer, String> teamButtonMap = new HashMap<>();
    private int teamCounter = 0;

    private Game game;
    private Round round;
    private ObservableList<Team> teamsObservableList;

    @FXML
    Button backButton;
    @FXML
    Button nextButton;

    @FXML
    Label teamLabel;
    @FXML
    Label registeredButtonLabel;

    public void displayButtonsRegistrationWindow(
            Game game,
            Round round,
            ObservableList<Team> teamsObservableList){

        try {
            this.game = game;
            this.round = round;
            this.teamsObservableList = teamsObservableList;

            if(buttonsRegistrationWindowStage != null && !buttonsRegistrationWindowStage.isShowing()){
                buttonsRegistrationWindowStage.show();
            }
            else{
                createButtonsRegistrationWindowAndDisplay();
            }

            nextButton.setDisable(true);

        }
        catch (Exception e){
            logger.error("Exception while displaying Blitz Window, e = " + e.getMessage(), e);
        }
    }

    private void createButtonsRegistrationWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ButtonsRegistrationWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent parent = fxmlLoader.load();

        teamButtonMap.clear();
        teamCounter = 0;

        Team currentTeam = teamsObservableList.get(teamCounter);
        if(currentTeam != null){
            teamLabel.setText(currentTeam.getName());
            registeredButtonLabel.setText("...");
            nextButton.setDisable(true);
        }

        parent.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

              logger.info("Pressed:"  + event.getCode());
                registerTeamButton(event);
            }
        });

        buttonsRegistrationWindowStage = new Stage();
        buttonsRegistrationWindowStage.setTitle("Start Buttons registration Window");

        buttonsRegistrationWindowStage.setScene(new Scene(parent));

        buttonsRegistrationWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.out.println("Buttons Registration Stage is closing");
                buttonsRegistrationWindowStage = null;
            }
        });

        buttonsRegistrationWindowStage.show();
    }

    private void registerTeamButton(KeyEvent event) {
        try{
            Team currentTeam = teamsObservableList.get(teamCounter);
            if(currentTeam != null){
                String buttonCode = event.getText();

                boolean isButtonAvailable = checkIfButtonAvailable(buttonCode);
                if(isButtonAvailable){
                    teamButtonMap.put(currentTeam.getId(), buttonCode);

                    teamLabel.setText(currentTeam.getName());
                    registeredButtonLabel.setText(buttonCode);
                    nextButton.setDisable(false);
                }
                else {
                    messageWindowController.displayMessageWindow("Button " + buttonCode + " is already taken.");
                }
            }

        }catch (Exception e){
            logger.error("Exception while registering team button, e = "  + e.getMessage());
        }
    }

    private boolean checkIfButtonAvailable(String buttonCode) throws Exception{

        for(Map.Entry<Integer, String> entry : teamButtonMap.entrySet()){
            if(entry.getValue().equals(buttonCode)){
                return false;
            }
        }
        return true;
    }

    public void handleNextButtonClick(){
        try{
            if(teamCounter < teamsObservableList.size() - 1){

                teamCounter++;
                Team currentTeam = teamsObservableList.get(teamCounter);
                if(currentTeam != null){

                    teamLabel.setText(currentTeam.getName());
                    String registeredButton = retrieveTeamButton(currentTeam.getId());
                    registeredButtonLabel.setText(registeredButton);

                    if(registeredButton.equals("...")){
                        nextButton.setDisable(true);
                    }
                    else {
                        nextButton.setDisable(false);
                    }
                }
            }
            else{
                blitzRoundWindowController.displayBlitzRoundWindow(
                        game, round, teamsObservableList, teamButtonMap);
                buttonsRegistrationWindowStage.close();
                buttonsRegistrationWindowStage = null;
            }
        }
        catch (Exception e){
            logger.error("Exception while handling next button, e = " + e.getMessage());
        }
    }

    public void handleBackButtonClick(){
        try{
            if(teamCounter > 0){

                teamCounter--;
                Team currentTeam = teamsObservableList.get(teamCounter);
                if(currentTeam != null){

                    teamLabel.setText(currentTeam.getName());
                    String registeredButton = retrieveTeamButton(currentTeam.getId());
                    registeredButtonLabel.setText(registeredButton);

                    if(registeredButton.equals("...")){
                        nextButton.setDisable(true);
                    }
                    else {
                        nextButton.setDisable(false);
                    }
                }
            }
        }
        catch (Exception e){
            logger.error("Exception while handling next button, e = " + e.getMessage());
        }
    }

    private String retrieveTeamButton(Integer id) throws Exception{
        String teamButton = "...";
        if(teamButtonMap.get(id) != null){
            teamButton = teamButtonMap.get(id);
        }
        return teamButton;
    }


}

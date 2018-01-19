package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.RoundType;
import com.araknas.brains_beer.models.Team;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Giedrius on 2017-10-30.
 */
@Component
public class GameStartWindowController implements Initializable, ViewController{

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    ConfigurableApplicationContext springContext;

    @Autowired
    GamePrepareWindowController gamePrepareWindowController;

    @Autowired
    ButtonsRegistrationWindowController buttonsRegistrationWindowController;

    @Autowired
    BlitzRoundWindowController blitzRoundWindowController;

    @Autowired
    MessageWindowController messageWindowController;

    @FXML
    Label gameTitle;

    @FXML
    private ListView<Round> roundsListView;

    private Stage startGameWindowStage;

    private Game selectedGame;
    private Round selectedRound;
    private ObservableList<Round> selectedRoundsObservableList;
    private ObservableList<Team> selectedTeamsObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleStartGameNextButtonClick(){
        try{
            if(selectedRound != null){
                loadSelectedRound();
            }
            else{
                messageWindowController.displayMessageWindow("Psirinkite turą!");
            }
        }
        catch (Exception e){
            String error = "Exception while loading selected round data, e = " + e.getMessage();
            logger.error(error + e.getMessage(), e);
            messageWindowController.displayMessageWindow(error);
        }
    }

    private void loadSelectedRound() throws Exception{
        RoundType roundType = selectedRound.getRoundType();
        switch (roundType.getTitle()){
            case "Blitz":
/*                blitzRoundWindowController.displayBlitzRoundWindow(
                        selectedGame, selectedRound, selectedTeamsObservableList);*/

                buttonsRegistrationWindowController.displayButtonsRegistrationWindow(
                        selectedGame, selectedRound, selectedTeamsObservableList);
                break;
            default: messageWindowController.displayMessageWindow("Round type is not implemented");
        }
    }

    public void handleStartGameBackButtonClick(){
        hideStartGameWindow();
        gamePrepareWindowController.displayGamePrepareWindow(this.selectedGame, false, false);
    }

    public void displayStartGamesWindow(
            Game selectedGame,
            ObservableList<Team> selectedTeamsObservableList,
            ObservableList<Round> selectedRoundsObservableList){

        try {
            if(startGameWindowStage != null && !startGameWindowStage.isShowing()){
                startGameWindowStage.show();
            }
            else{
                createNewStartGameWindowAndDisplay();
            }
            this.selectedGame = selectedGame;
            this.selectedTeamsObservableList = selectedTeamsObservableList;
            this.selectedRoundsObservableList = selectedRoundsObservableList;
            reloadSelectedGameData();
        }
        catch (Exception e){
            logger.error("Exception while displaying Games Window, e = " + e.getMessage(), e);
        }
    }

    public void hideStartGameWindow(){
        try {
            if(startGameWindowStage != null && startGameWindowStage.isShowing()){
                startGameWindowStage.hide();
            }
        }
        catch (Exception e){
            logger.error("Exception while hiding Games Window, e = " + e.getMessage(), e);
        }
    }

    private void createNewStartGameWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/StartGameWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent gamesWindowParent = fxmlLoader.load();

        startGameWindowStage = new Stage();
        startGameWindowStage.setTitle("Start Game Window");

        startGameWindowStage.setScene(new Scene(gamesWindowParent));

        startGameWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        startGameWindowStage.show();
    }

    private void reloadSelectedGameData() {
        try{
            if(selectedGame != null && selectedRoundsObservableList != null){

                gameTitle.setText(selectedGame.getGameTitle());
                roundsListView.setItems(selectedRoundsObservableList);
                roundsListView.setCellFactory(gamesListView -> new RoundListViewCellController(this, false));
            }
        }
        catch (Exception e){
            String error = "Exception while reloading selected game data, e = " + e.getMessage();
            logger.error(error + e.getMessage(), e);
            messageWindowController.displayMessageWindow(error);
        }
    }

    public boolean checkIfGameStartWindowIsShowing(){

        boolean isShowing = false;
        if(startGameWindowStage != null && startGameWindowStage.isShowing()){
            isShowing = true;
        }

        return isShowing;
    }

    public Round getSelectedRound() {
        return selectedRound;
    }

    public void setSelectedRound(Round selectedRound) {
        this.selectedRound = selectedRound;
    }
}

package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.Team;
import com.araknas.brains_beer.repositories.RoundRepository;
import com.araknas.brains_beer.repositories.TeamRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Giedrius on 2017.10.23.
 */

@Component
public class GamePrepareWindowController implements Initializable, ViewController {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    GamesWindowController gamesWindowController;

    @Autowired
    GameStartWindowController gameStartWindowController;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    ConfigurableApplicationContext springContext;

    @Autowired
    MessageWindowController messageWindowController;

    @FXML
    Label gameTitle;

    @FXML
    private ListView<Round> allRoundsListView;

    @FXML
    private ListView<Round> selectedRoundsListView;

    @FXML
    private ListView<Team> allTeamsListView;

    @FXML
    private ListView<Team> selectedTeamsListView;

    @FXML
    Button gamePrepareBackButton;

    @FXML
    Button gamePrepareNextButton;

    private ObservableList<Round> allRoundsObservableList;
    private ObservableList<Round> selectedRoundsObservableList;
    private ObservableList<Team> allTeamsObservableList;
    private ObservableList<Team> selectedTeamsObservableList;

    private Game selectedGame;

    private Stage gamePrepareWindowStage;

    public void handleGamePrepareNextButtonClick(){
        if(checkIfGamePrepared()){
            gameStartWindowController.displayStartGamesWindow(
                    this.selectedGame, this.selectedTeamsObservableList, this.selectedRoundsObservableList);
            hideGamePrepareWindow();
        }
        else {
            messageWindowController.displayMessageWindow("Pasirinkite bent dvi komandas ir bent vieną žaidimą.");
        }
    }

    private boolean checkIfGamePrepared(){
        boolean isPrepared = false;
        try{
            if(this.selectedTeamsObservableList.size() > 1 && this.selectedRoundsObservableList.size() > 0){
                isPrepared = true;
            }
        }
        catch (Exception e){
            String error = "Exception while checking game preparation, e = " + e.getMessage();
            messageWindowController.displayMessageWindow(error);
        }
        return isPrepared;
    }

    public void handleGamePrepareBackButtonClick(){
        this.hideGamePrepareWindow();
        gamesWindowController.displayGamesWindow();
    }

    public boolean checkIfGamePrepareWindowIsShowing(){

        boolean isShowing = false;
        if(gamePrepareWindowStage != null && gamePrepareWindowStage.isShowing()){
            isShowing = true;
        }

        return isShowing;
    }

    public void displayGamePrepareWindow(Game selectedGame, boolean isNeedToFetchFromDb, boolean isNeedToClear){
        try {
            if(gamePrepareWindowStage != null && !gamePrepareWindowStage.isShowing()){
                gamePrepareWindowStage.show();
            }
            else{
                createNewGamePrepareWindowAndDisplay();
            }
            this.selectedGame = selectedGame;

            reloadSelectedGameData();
            reloadAllTeamsListView(isNeedToFetchFromDb, isNeedToClear);
            reloadAllRoundsListView(isNeedToFetchFromDb, isNeedToClear);
            reloadSelectedTeamsListView(isNeedToFetchFromDb, isNeedToClear);
            reloadSelectedRoundsListView(isNeedToFetchFromDb, isNeedToClear);
        }
        catch (Exception e){
            logger.error("Exception while displaying Game Prepare Window, e = " + e.getMessage(), e);
            messageWindowController.displayMessageWindow(e.getMessage());
        }
    }

    private void reloadSelectedGameData() {
        try{
            if(selectedGame != null){
                gameTitle.setText(selectedGame.getGameTitle());
            }
        }
        catch (Exception e){
            String error = "Exception while reloading selected game data, e = " + e.getMessage();
            logger.error(error + e.getMessage(), e);
            messageWindowController.displayMessageWindow(error);
        }
    }

    public void hideGamePrepareWindow(){
        try {
            if(gamePrepareWindowStage != null && gamePrepareWindowStage.isShowing()){
                gamePrepareWindowStage.hide();
            }
        }
        catch (Exception e){
            logger.error("Exception while hiding Game Prepare Window, e = " + e.getMessage(), e);
            messageWindowController.displayMessageWindow(e.getMessage());
        }
    }

    private void createNewGamePrepareWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/GamePrepareWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent gamePrepareWindowParent = fxmlLoader.load();

        gamePrepareWindowStage = new Stage();
        gamePrepareWindowStage.setTitle("Game Prepare Window");
        gamePrepareWindowStage.setScene(new Scene(gamePrepareWindowParent));

        gamePrepareWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        gamePrepareWindowStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allTeamsObservableList = FXCollections.observableArrayList();
        allRoundsObservableList = FXCollections.observableArrayList();
        selectedTeamsObservableList = FXCollections.observableArrayList();
        selectedRoundsObservableList = FXCollections.observableArrayList();
        reloadAllTeamsListView(true, true);
        reloadAllRoundsListView(true, true);
        reloadSelectedTeamsListView(true, true);
        reloadSelectedRoundsListView(true, true);
    }

    private void reloadAllTeamsListView(boolean isNeedToFetchFromDb, boolean isNeedToClear) {
        try{
            if(isNeedToClear){
                allTeamsObservableList.clear();
            }
            if(isNeedToFetchFromDb){
                List<Team> allTeams = teamRepository.findAll();

                for(Team team : allTeams){
                    allTeamsObservableList.add(team);
                }
            }

            allTeamsListView.setItems(allTeamsObservableList);
            allTeamsListView.setCellFactory(allTeamsListView -> new TeamListViewCellController(this, false));

        }catch (Exception e){
            String message = "Exception while reloading all teams list view, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }

    private void reloadSelectedTeamsListView(boolean isNeedToFetchFromDb, boolean isNeedToClear) {
        try{
            //TODO: retrieve form saved games (future feature)
            if(isNeedToClear){
                selectedTeamsObservableList.clear();
            }
            selectedTeamsListView.setItems(selectedTeamsObservableList);
            selectedTeamsListView.setCellFactory(allTeamsListView -> new TeamListViewCellController(this, true));

        }catch (Exception e){
            String message = "Exception while reloading all teams list view, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }

    private void reloadAllRoundsListView(boolean isNeedToFetchFromDb, boolean isNeedToClear) {
        try{
            if(isNeedToClear){
                allRoundsObservableList.clear();
            }

            if(isNeedToFetchFromDb){
                List<Round> allRounds = roundRepository.findAll();

                for(Round round : allRounds){
                    allRoundsObservableList.add(round);
                }
            }

            allRoundsListView.setItems(allRoundsObservableList);
            allRoundsListView.setCellFactory(allTeamsListView -> new RoundListViewCellController(this, false));

        }catch (Exception e){
            String message = "Exception while reloading all teams list view, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }

    private void reloadSelectedRoundsListView(boolean isNeedToFetchFromDb, boolean isNeedToClear) {
        try{
            //TODO: retrieve form saved rounds (future feature)
            if(isNeedToClear){
                selectedRoundsObservableList.clear();
            }
            selectedRoundsListView.setItems(selectedRoundsObservableList);
            selectedRoundsListView.setCellFactory(allRoundsListView -> new RoundListViewCellController(this, true));

        }catch (Exception e){
            String message = "Exception while reloading all teams list view, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }

    public void transferTeamToSelectedOnes(Team team) throws Exception{
        logger.info("Transferring team " + team.getName() + " to selected ones.");

        if(allTeamsObservableList.contains(team)){
            allTeamsObservableList.remove(team);
        }

        if(!selectedTeamsObservableList.contains(team)){
            selectedTeamsObservableList.add(team);
        }

        reloadAllTeamsListView(false, false);
        reloadSelectedTeamsListView(false, false);
    }

    public void transferTeamToAllTeams(Team team) throws Exception{

        logger.info("Transferring team " + team.getName() + " to all teams.");

        if(selectedTeamsObservableList.contains(team)){
            selectedTeamsObservableList.remove(team);
        }

        if(!allTeamsObservableList.contains(team)){
            allTeamsObservableList.add(team);
        }

        reloadAllTeamsListView(false, false);
        reloadSelectedTeamsListView(false, false);
    }

    public void transferRoundToSelectedOnes(Round round) throws Exception{
        logger.info("Transferring round " + round.getTitle() + " to selected ones.");

        if(allRoundsObservableList.contains(round)){
            allRoundsObservableList.remove(round);
        }

        if(!selectedRoundsObservableList.contains(round)){
            selectedRoundsObservableList.add(round);
        }

        reloadAllRoundsListView(false, false);
        reloadSelectedRoundsListView(false, false);
    }

    public void transferRoundToAllRounds(Round round) throws Exception{

        logger.info("Transferring round " + round.getTitle() + " to all rounds.");

        if(selectedRoundsObservableList.contains(round)){
            selectedRoundsObservableList.remove(round);
        }

        if(!allRoundsObservableList.contains(round)){
            allRoundsObservableList.add(round);
        }

        reloadAllRoundsListView(false, false);
        reloadSelectedRoundsListView(false, false);
    }
}

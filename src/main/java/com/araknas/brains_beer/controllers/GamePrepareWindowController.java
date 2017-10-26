package com.araknas.brains_beer.controllers;

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
public class GamePrepareWindowController implements Initializable {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    GamesWindowController gamesWindowController;

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

    private Stage gamePrepareWindowStage;

    public void handleGamePrepareNextButtonClick(){
        // TODO: implement
    }

    public void handleGamePrepareBackButtonClick(){
        this.hideGamePrepareWindow();
        gamesWindowController.displayGamesWindow();
    }

    public void displayGamePrepareWindow(){
        try {
            if(gamePrepareWindowStage != null && !gamePrepareWindowStage.isShowing()){
                reloadAllTeamsListView();
                reloadAllRoundsListView();
                gamePrepareWindowStage.show();
            }
            else{
                createNewGamePrepareWindowAndDisplay();
            }
        }
        catch (Exception e){
            logger.error("Exception while displaying Game Prepare Window, e = " + e.getMessage(), e);
            messageWindowController.displayMessageWindow(e.getMessage());
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
        reloadAllTeamsListView();
        reloadAllRoundsListView();
    }

    private void reloadAllTeamsListView() {
        try{
            List<Team> allTeams = teamRepository.findAll();
            allTeamsObservableList.clear();

            for(Team team : allTeams){
                allTeamsObservableList.add(team);
            }
            allTeamsListView.setItems(allTeamsObservableList);
            allTeamsListView.setCellFactory(allTeamsListView -> new TeamListViewCellController());

        }catch (Exception e){
            String message = "Exception while reloading all teams list view, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }

    private void reloadAllRoundsListView() {
        try{
            List<Round> allRounds = roundRepository.findAll();
            allRoundsObservableList.clear();

            for(Round round : allRounds){
                allRoundsObservableList.add(round);
            }
            allRoundsListView.setItems(allRoundsObservableList);
            allRoundsListView.setCellFactory(allTeamsListView -> new RoundListViewCellController());

        }catch (Exception e){
            String message = "Exception while reloading all teams list view, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }
}

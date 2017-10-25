package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Team;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Giedrius on 2017.10.21.
 */
@Component
public class TeamsWindowController implements Initializable {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ConfigurableApplicationContext springContext;

    @Autowired
    GamesWindowController gamesWindowController;

    @Autowired
    MessageWindowController messageWindowController;

    private Stage teamsWindowStage;

    @FXML
    Button addTeamButton;
    @FXML
    TextField teamNameField;
    @FXML

    TextField membersCountField;
    @FXML
    private ListView teamsListView;


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
            logger.error("Exception while displaying Teams Window, e = " + e.getMessage(), e);
            messageWindowController.displayMessageWindow(e.getMessage());
        }
    }

    public void hideTeamsWindow(){
        try {
            if(teamsWindowStage != null && teamsWindowStage.isShowing()){
                teamsWindowStage.hide();
            }
        }
        catch (Exception e){
            logger.error("Exception while hiding Teams Window, e = " + e.getMessage(), e);
            messageWindowController.displayMessageWindow(e.getMessage());
        }
    }

    private void createNewTeamsWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/TeamsWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent gamesWindowParent = fxmlLoader.load();

        teamsWindowStage = new Stage();
        teamsWindowStage.setTitle("Teams Window");
        teamsWindowStage.setScene(new Scene(gamesWindowParent));

        teamsWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        teamsWindowStage.show();
    }

    public void handleAddTeamButtonClick(){
        try{
            logger.info("Adding new team.");
            addTeamToDatabase();
        }
        catch (Exception e){
            logger.error("Exception while adding a team, e = " + e.getMessage());
            messageWindowController.displayMessageWindow("Exception while adding a team, e = " + e.getMessage());
        }
    }

    private void addTeamToDatabase() throws Exception{

        String teamName = teamNameField.getText();
        int membersCount = Integer.parseInt(membersCountField.getText());

        validateTeam(teamName, membersCount);

        Team newTeam = new Team();
        newTeam.setName(teamName);
        newTeam.setMembersCount(membersCount);

        teamRepository.save(newTeam);
    }

    private void validateTeam(String teamName, int membersCount) throws Exception {

        String errorMsg = "";
        if ((teamName != null && teamName.equals(""))) {
            errorMsg = "Invalid team name";
        }

        if (membersCount <= 0) {
            errorMsg = "Please add few members.";
        }

        Team team = teamRepository.findByName(teamName);
        if(team != null){
            errorMsg = "Team " + teamName + " already exists";
        }

        if(!errorMsg.equals("")){
            throw (new Exception(errorMsg));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> values = Arrays.asList("one", "two", "three", "three","three","three","three","three","three","three");
        teamsListView.setItems(FXCollections.observableList(values));
    }
}

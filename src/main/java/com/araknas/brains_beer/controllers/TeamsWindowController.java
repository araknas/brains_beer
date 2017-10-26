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
    Button deleteTeamButton;

    @FXML
    TextField teamNameField;

    @FXML
    TextField membersCountField;

    @FXML
    private ListView<Team> teamsListView;

    private ObservableList<Team> teamObservableList;


    public void handleTeamWindowNextButtonClick(){
        this.hideTeamsWindow();
        gamesWindowController.displayGamesWindow();
    }

    public void handleAddTeamButtonClick(){
        try{
            logger.info("Adding new team.");
            addTeamToDatabase();
            reloadTeamsListView();
        }
        catch (Exception e){
            logger.error("Exception while adding a team, e = " + e.getMessage());
            messageWindowController.displayMessageWindow("Exception while adding a team, e = " + e.getMessage());
        }
    }

    public void handleDeleteTeamButtonClick(){
        try{
            logger.info("Deleting team.");
            deleteTeamFromDatabase();
            reloadTeamsListView();
        }
        catch (Exception e){
            String message = "Exception while deleting a team, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
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

    private void addTeamToDatabase() throws Exception{

        String teamName = teamNameField.getText();
        int membersCount = Integer.parseInt(membersCountField.getText());

        validateTeam(teamName, membersCount);

        Team newTeam = new Team();
        newTeam.setName(teamName);
        newTeam.setMembersCount(membersCount);

        teamRepository.save(newTeam);
    }

    private void deleteTeamFromDatabase() throws Exception{

        String teamName = teamNameField.getText();
        Team team = teamRepository.findByName(teamName);

        if(team == null){
            throw (new Exception("Cannot find this team in the database."));
        }

        teamRepository.delete(team);
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
        teamObservableList = FXCollections.observableArrayList();
        reloadTeamsListView();
    }

    private void reloadTeamsListView() {
        try{
            List<Team> allTeams = teamRepository.findAll();
            teamObservableList.clear();

            for(Team team : allTeams){
                teamObservableList.add(team);
            }
            teamsListView.setItems(teamObservableList);
            teamsListView.setCellFactory(teamsListView -> new TeamListViewCellController());

        }catch (Exception e){
            String message = "Exception while reloading teams list view, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }
}

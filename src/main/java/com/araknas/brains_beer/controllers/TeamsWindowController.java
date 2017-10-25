package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Team;
import com.araknas.brains_beer.repositories.TeamRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Giedrius on 2017-10-20.
 */
@Component
public class TeamsWindowController {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    TeamRepository teamRepository;

    @FXML
    Button addTeamButton;
    @FXML
    TextField teamNameField;
    @FXML
    TextField membersCountField;

    public void handleAddTeamButtonClick(){
        try{
            logger.info("Adding new team.");
            addTeamToDatabase();
        }
        catch (Exception e){
            logger.error("Exception while handling team add button, e = " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addTeamToDatabase() throws Exception{

        String teamName = teamNameField.getText();
        int membersCount = Integer.parseInt(membersCountField.getText());

        String errorMsg = checkIfTeamValid(teamName, membersCount);

        if(errorMsg.equals("") ){
            Team newTeam = new Team();
            newTeam.setName(teamName);
            newTeam.setMembersCount(membersCount);

            teamRepository.save(newTeam);
        }
        else{
            logger.info(errorMsg);
        }

    }

    private String checkIfTeamValid(String teamName, int membersCount) throws Exception{

        String errorMsg = "";
        if((teamName != null && !teamName.equals(""))){

            errorMsg = "Invalid team name";
            return errorMsg;
        }

        if(membersCount <= 0){
            errorMsg = "Please add few members.";
        }
        return errorMsg;
    }
}

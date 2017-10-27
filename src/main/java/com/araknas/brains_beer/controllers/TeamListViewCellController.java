package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Created by Giedrius on 2017-10-26.
 */
@Component
public class TeamListViewCellController extends ListCell<Team> {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    private ViewController parentController;
    private boolean isSelectedList;

    @FXML
    Label teamNameLabel;

    @FXML
    GridPane teamCellGridPane;

    private FXMLLoader fxmlLoader;

    public TeamListViewCellController(ViewController parentController, boolean isSelectedList) {
            this.parentController = parentController;
            this.isSelectedList = isSelectedList;
    }

    public TeamListViewCellController() {
    }

    @Override
    protected void updateItem(Team team, boolean empty) {

        try{
            super.updateItem(team, empty);
            if(empty || team == null) {
                setText(null);
                setGraphic(null);
            }
            else {

                if (fxmlLoader == null) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("/views/TeamListCell.fxml"));
                    fxmlLoader.setController(this);
                    fxmlLoader.load();
                }
                teamNameLabel.setText(team.getName());
                setOnMouseClicked(event -> handleMouseClick(event, team));

                setText(null);
                setGraphic(teamCellGridPane);
            }
        }
        catch (Exception e){
            logger.error("Exception while updating Team list cells, e = " + e.getMessage());
        }
    }

    private void handleMouseClick(MouseEvent event, Team team) {
        if(parentController instanceof GamePrepareWindowController){
            boolean isGamePrepareWindowShowing =
                    ((GamePrepareWindowController) parentController).checkIfGamePrepareWindowIsShowing();
            if(isGamePrepareWindowShowing){
                handleMouseClickFromGamePrepareWindow(event, team);
                logger.info("Mouse click on " + team.getName());
            }
        }
    }

    private void handleMouseClickFromGamePrepareWindow(MouseEvent event, Team team) {

        try{
            if(event.getClickCount() == 2){
                if(!isSelectedList){

                    ((GamePrepareWindowController) parentController).transferTeamToSelectedOnes(team);
                }
                else{
                    ((GamePrepareWindowController) parentController).transferTeamToAllTeams(team);
                }
            }
        }
        catch (Exception e){
            String error = "Exception while handling mouse click e = " + e.getMessage();
            logger.error(error);
        }

    }
}

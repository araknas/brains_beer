package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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

    @FXML
    Label teamNameLabel;

    @FXML
    GridPane teamCellGridPane;

    private FXMLLoader fxmlLoader;

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

                setText(null);
                setGraphic(teamCellGridPane);
            }
        }
        catch (Exception e){
            logger.error("Exception while updating Team list cells, e = " + e.getMessage());
        }
    }
}

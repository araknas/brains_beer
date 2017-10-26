package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Round;
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
public class RoundListViewCellController extends ListCell<Round> {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @FXML
    Label roundNameLabel;

    @FXML
    GridPane roundCellGridPane;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(Round round, boolean empty) {

        try{
            super.updateItem(round, empty);
            if(empty || round == null) {
                setText(null);
                setGraphic(null);
            }
            else {
                if (fxmlLoader == null) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("/views/RoundListCell.fxml"));
                    fxmlLoader.setController(this);
                    fxmlLoader.load();
                }
                roundNameLabel.setText(round.getTitle());

                setText(null);
                setGraphic(roundCellGridPane);
            }
        }
        catch (Exception e){
            logger.error("Exception while updating Round list cells, e = " + e.getMessage());
        }
    }
}

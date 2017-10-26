package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Game;
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
public class GameListViewCellController extends ListCell<Game> {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @FXML
    Label gameNameLabel;

    @FXML
    GridPane gameCellGridPane;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(Game game, boolean empty) {

        try{
            super.updateItem(game, empty);
            if(empty || game == null) {
                setText(null);
                setGraphic(null);
            }
            else {
                if (fxmlLoader == null) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("/views/GameListCell.fxml"));
                    fxmlLoader.setController(this);
                    fxmlLoader.load();
                }
                gameNameLabel.setText(game.getGameTitle());

                setText(null);
                setGraphic(gameCellGridPane);
            }
        }
        catch (Exception e){
            logger.error("Exception while updating Game list cells, e = " + e.getMessage());
        }
    }
}

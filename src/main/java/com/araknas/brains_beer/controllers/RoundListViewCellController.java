package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Round;
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
public class RoundListViewCellController extends ListCell<Round> {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    private ViewController parentController;
    private boolean isSelectedList;

    @FXML
    Label roundNameLabel;
    @FXML
    Label roundTypeLabel;

    @FXML
    GridPane roundCellGridPane;

    private FXMLLoader fxmlLoader;

    public RoundListViewCellController(ViewController parentController, boolean isSelectedList) {
        this.parentController = parentController;
        this.isSelectedList = isSelectedList;
    }

    public RoundListViewCellController() {
    }

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
                roundTypeLabel.setText(round.getRoundType().getTitle());
                setOnMouseClicked(event -> handleMouseClick(event, round));

                setText(null);
                setGraphic(roundCellGridPane);
            }
        }
        catch (Exception e){
            logger.error("Exception while updating Round list cells, e = " + e.getMessage());
        }
    }

    private void handleMouseClick(MouseEvent event, Round round) {
        if(parentController instanceof GamePrepareWindowController){
            boolean isGamePrepareWindowShowing =
                    ((GamePrepareWindowController) parentController).checkIfGamePrepareWindowIsShowing();
            if(isGamePrepareWindowShowing){
                handleMouseClickFromGamePrepareWindow(event, round);
                logger.info("Mouse click on " + round.getTitle());
            }
        }
    }

    private void handleMouseClickFromGamePrepareWindow(MouseEvent event, Round round) {

        try{
            if(event.getClickCount() == 2){
                if(!isSelectedList){

                    ((GamePrepareWindowController) parentController).transferRoundToSelectedOnes(round);
                }
                else{
                    ((GamePrepareWindowController) parentController).transferRoundToAllRounds(round);
                }
            }
        }
        catch (Exception e){
            String error = "Exception while handling mouse click e = " + e.getMessage();
            logger.error(error);
        }

    }
}

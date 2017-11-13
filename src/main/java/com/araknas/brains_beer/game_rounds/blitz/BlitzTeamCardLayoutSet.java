package com.araknas.brains_beer.game_rounds.blitz;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by Giedrius on 2017-11-13.
 */
public class BlitzTeamCardLayoutSet {

    private GridPane cardGridPane;
    private Label teamNameLabel;
    private Label teamPlaceInQueueLabel;

    public BlitzTeamCardLayoutSet(GridPane cardGridPane, Label teamNameLabel, Label teamPlaceInQueueLabel) {
        this.cardGridPane = cardGridPane;
        this.teamNameLabel = teamNameLabel;
        this.teamPlaceInQueueLabel = teamPlaceInQueueLabel;
    }

    public Label getTeamPlaceInQueueLabel() {
        return teamPlaceInQueueLabel;
    }

    public void setTeamPlaceInQueueLabel(Label teamPlaceInQueueLabel) {
        this.teamPlaceInQueueLabel = teamPlaceInQueueLabel;
    }

    public GridPane getCardGridPane() {
        return cardGridPane;
    }

    public void setCardGridPane(GridPane cardGridPane) {
        this.cardGridPane = cardGridPane;
    }

    public Label getTeamNameLabel() {
        return teamNameLabel;
    }

    public void setTeamNameLabel(Label teamNameLabel) {
        this.teamNameLabel = teamNameLabel;
    }
}

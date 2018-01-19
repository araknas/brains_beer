package com.araknas.brains_beer.game_rounds.blitz;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by Giedrius on 2017-11-13.
 */
public class BlitzTeamCardLayoutSet {

    private GridPane cardGridPane;
    private Label teamNameLabel;
    private Label teamPlaceInQueueLabel;
    private Label teamPointsLabel;
    private Button teamPointsUpButton;
    private Button teamPointsDownButton;


    public BlitzTeamCardLayoutSet(
            GridPane cardGridPane,
            Label teamNameLabel,
            Label teamPlaceInQueueLabel,
            Label teamPointsLabel,
            Button teamPointsUpButton,
            Button teamPointsDownButton)
    {
        this.cardGridPane = cardGridPane;
        this.teamNameLabel = teamNameLabel;
        this.teamPlaceInQueueLabel = teamPlaceInQueueLabel;
        this.teamPointsLabel = teamPointsLabel;

        this.teamPointsUpButton = teamPointsUpButton;
        this.teamPointsDownButton = teamPointsDownButton;
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

    public Label getTeamPointsLabel() {
        return teamPointsLabel;
    }

    public void setTeamPointsLabel(Label teamPointsLabel) {
        this.teamPointsLabel = teamPointsLabel;
    }

    public Button getTeamPointsUpButton() {
        return teamPointsUpButton;
    }

    public void setTeamPointsUpButton(Button teamPointsUpButton) {
        this.teamPointsUpButton = teamPointsUpButton;
    }

    public Button getTeamPointsDownButton() {
        return teamPointsDownButton;
    }

    public void setTeamPointsDownButton(Button teamPointsDownButton) {
        this.teamPointsDownButton = teamPointsDownButton;
    }
}

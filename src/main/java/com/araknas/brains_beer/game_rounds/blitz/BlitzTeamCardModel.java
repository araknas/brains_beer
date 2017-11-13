package com.araknas.brains_beer.game_rounds.blitz;

import com.araknas.brains_beer.models.Team;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by Giedrius on 2017-11-13.
 */
public class BlitzTeamCardModel {
    private Team team;
    private BlitzTeamCardLayoutSet blitzTeamCardLayoutSet;

    public BlitzTeamCardLayoutSet getBlitzTeamCardLayoutSet() {
        return blitzTeamCardLayoutSet;
    }

    public void setBlitzTeamCardLayoutSet(BlitzTeamCardLayoutSet blitzTeamCardLayoutSet) {
        this.blitzTeamCardLayoutSet = blitzTeamCardLayoutSet;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}

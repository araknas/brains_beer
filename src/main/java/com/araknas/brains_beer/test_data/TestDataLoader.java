package com.araknas.brains_beer.test_data;

import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.Team;
import com.araknas.brains_beer.repositories.GameRepository;
import com.araknas.brains_beer.repositories.RoundRepository;
import com.araknas.brains_beer.repositories.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giedrius on 2017-10-26.
 */
@Component
public class TestDataLoader {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    RoundRepository roundRepository;

    public void loadTestData(){
        try{
            logger.info("Loading test data...");

            loadTeams();
            loadGames();
            loadRounds();
        }
        catch (Exception e){
            logger.error("Exception while loading test data, e = " + e.getMessage(), e);
        }

    }

    public void loadTeams() throws Exception{
        logger.info("Loading test teams data...");

        List<Team> teams = new ArrayList<>();
        teams.add(new Team("Žagarėliai", 3));
        teams.add(new Team("Kukurūzai", 6));
        teams.add(new Team("Mėlynieji kebabai", 3));
        teams.add(new Team("Imbieriniai Tritaškių princai", 4));
        teams.add(new Team("Simonas & Garfunkelis", 3));
        teams.add(new Team("Begėdžiai", 5));
        teams.add(new Team("Rūgštieji agrastai", 5));
        teams.add(new Team("Parduodu malkas", 9));
        teams.add(new Team("GudrOliai", 10));
        teams.add(new Team("Tamsos Greitis", 8));
        teams.add(new Team("Barsuko Nagas", 4));
        teams.add(new Team("Gal tuo ir baikim", 2));
        teamRepository.save(teams);
    }

    public void loadGames() throws Exception{
        logger.info("Loading test games...");

        List<Game> games = new ArrayList<>();
        games.add(new Game("Saldus žaidimas", "Su cukrumi"));
        games.add(new Game("Žaidžiam iš kukurūzų", "Aha kukurūzai"));
        games.add(new Game("Food fight", "Nebevalgomi"));
        games.add(new Game("Einikio karuselė", "Sabai ar išbėgsi?"));
        games.add(new Game("SEL'o albumo minėjimas", "Ooo, ponia Robinson..."));
        games.add(new Game("X-files teminis", "Čia tipo serialas "));
        gameRepository.save(games);
    }

    public void loadRounds() throws Exception{
        logger.info("Loading test rounds...");

        List<Round> rounds = new ArrayList<>();
        rounds.add(new Round("Filmų turas (siaubo)", null, null));
        rounds.add(new Round("Geografija", null, null));
        rounds.add(new Round("Muzikinis (Eminem)", null, null));
        rounds.add(new Round("Vaizdų (tvirtovės)", null, null));
        roundRepository.save(rounds);
    }
}

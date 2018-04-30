package com.araknas.brains_beer.test_data;

import com.araknas.brains_beer.models.*;
import com.araknas.brains_beer.repositories.*;
import com.araknas.brains_beer.services.ImageService;
import com.araknas.brains_beer.services.ImportService;
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
    RoundTypeRepository roundTypeRepository;
    @Autowired
    RoundRepository roundRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ImageService imageService;
    @Autowired
    ImportService importService;

    public void loadTestData(){
        try{
            logger.info("Loading test data...");

            loadTeams();
            loadGames();
            loadRoundTypes();
            loadRounds();
            loadQuestions();
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


    private void loadRoundTypes() throws Exception{
        logger.info("Loading test round types...");

        List<RoundType> roundTypes = new ArrayList<>();
        roundTypes.add(new RoundType("Blitz", null));
        roundTypes.add(new RoundType("Regular", null));

        roundTypeRepository.save(roundTypes);
    }

    public void loadRounds() throws Exception{
        logger.info("Loading test rounds...");

        RoundType blitzRound = roundTypeRepository.findByTitle("Blitz");
        RoundType regularRound = roundTypeRepository.findByTitle("Regular");

        List<Round> rounds = new ArrayList<>();
        rounds.add(new Round("Filmų turas (siaubo)", blitzRound, null));
        //rounds.add(new Round("Geografija", blitzRound, null));
        //rounds.add(new Round("Muzikinis (Eminem)", blitzRound, null));
        //rounds.add(new Round("Vaizdų (tvirtovės)", regularRound, null));
        //rounds.add(new Round("Apie Čaką Norris", regularRound, null));
        roundRepository.save(rounds);
    }

    private void loadQuestions() throws Exception{
        logger.info("Loading test questions...");

        //Round chuckNorrisRound = roundRepository.findByTitle("Apie Čaką Norris");
        List<Question> questions = new ArrayList<>();
        /*questions.add(new Question(1, "Kas yra vienitnelis žmogus, peršokęs Everestą?", "", "Čakas Norrisas", 60, chuckNorrisRound ));
        questions.add(new Question(2, "Žmogus, radęs kampų apvaliame kambaryje?", "", "Čakas Norrisas", 60, chuckNorrisRound ));
        questions.add(new Question(3, "Žmogus nunuodijęs gyvatę?", "", "Čakas Norrisas", 60, chuckNorrisRound ));
        questions.add(new Question(4, "Chuck Norris turi dvi pavaras. Kokias?", "", "Eiti ir žudyti.", 60, chuckNorrisRound ));
        questions.add(new Question(5, "kas dalijasi iš nulio?", "", "Čakas Norrisas", 60, chuckNorrisRound ));
        questionRepository.save(questions);
*/
        Round moviesRound = roundRepository.findByTitle("Filmų turas (siaubo)");
        questions = new ArrayList<>();
        questions.add(new Question(1, "Filmo Švytėjimas autorius?", "", "Stanley Bubrick", 30, moviesRound, "http://www.freepngimg.com/download/avengers/5-2-avengers-png-picture.png"));
        questions.add(new Question(2, "Psycho autorius?", "", "Alfred Hitchcock", 30, moviesRound, "https://i.pinimg.com/originals/47/ca/55/47ca552897e1904ab0125445dede933a.png" ));
        questions.add(new Question(3, "Filmas It pastatytas pagal knygą, kurios autorius yra?", "", "Styvenas Kingas", 30, moviesRound,"https://img00.deviantart.net/f487/i/2017/287/f/f/hela_thor_ragnarok_png_by_gasa979-dblax42.png" ));
        questions.add(new Question(4, "Kiek yra pjūklo dalių?", "", "Per daug", 30, moviesRound, "https://orig00.deviantart.net/4d03/f/2016/162/5/7/superman_w_thor_s_hammer_png_render_by_mrvideo_vidman-da5teet.png" ));
        questions.add(new Question(5, "Kiek yra skambučio dalių (remake)?", "", "Dvi", 30, moviesRound, "https://vignette.wikia.nocookie.net/marvelmovies/images/8/84/Destroyer8-Thor.png/revision/latest?cb=20140208042847" ));
       // imageService.downloadQuestionsImages(questions);

        questionRepository.save(questions);

        importService.importRounds();

    }
}

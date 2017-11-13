package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.game_rounds.blitz.BlitzTeamCardLayoutSet;
import com.araknas.brains_beer.game_rounds.blitz.BlitzTeamCardModel;
import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Question;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.Team;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.net.URL;
import java.util.*;

/**
 * Created by Giedrius on 2017.11.04.
 */
@Component
public class BlitzRoundWindowController implements ViewController, Initializable {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    GameStartWindowController gameStartWindowController;

    @Autowired
    MessageWindowController messageWindowController;

    @Autowired
    ConfigurableApplicationContext springContext;

    private Stage blitzRoundWindowStage;

    @FXML
    Button blitzBackButton;
    @FXML
    Button blitzNextButton;
    @FXML
    Button blitzEndButton;
    @FXML
    ImageView blitzQuestionImageView;
    @FXML
    Label blitzRoundNameLabel;
    @FXML
    TextArea blitzQuestionTextArea;

    @FXML
    GridPane team1GridPane;
    @FXML
    GridPane team2GridPane;
    @FXML
    GridPane team3GridPane;
    @FXML
    GridPane team4GridPane;
    @FXML
    GridPane team5GridPane;

    @FXML
    Label team1CardTeamLabel;
    @FXML
    Label team2CardTeamLabel;
    @FXML
    Label team3CardTeamLabel;
    @FXML
    Label team4CardTeamLabel;
    @FXML
    Label team5CardTeamLabel;

    @FXML
    Label team1PlaceInQueueLabel;
    @FXML
    Label team2PlaceInQueueLabel;
    @FXML
    Label team3PlaceInQueueLabel;
    @FXML
    Label team4PlaceInQueueLabel;
    @FXML
    Label team5PlaceInQueueLabel;

    HashMap<Integer, BlitzTeamCardModel> teamCardMap = new HashMap<>();

    private Game game;
    private Round round;
    private List<Question> questions;
    int questionIndex = -1;
    private ObservableList<Team> teamsObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleBlitzNextQuestion(){
        try {
            if(questionIndex >= -1 && questionIndex < questions.size() - 1){
                questionIndex++;
                Question currentQuestion = questions.get(questionIndex);
                blitzQuestionTextArea.setText(currentQuestion.getQuestionText());
                Image image = receiveQuestionImage(currentQuestion);
                blitzQuestionImageView.setImage(image);
            }
        }
        catch (Exception e){
            logger.error("Exception while handling Blitz next question, e = " + e.getMessage(), e);
        }
    }

    private Image receiveQuestionImage(Question currentQuestion) {

        Image image = null;
        try{
            image = new Image(currentQuestion.getImageUrl());
        }
        catch (Exception e){
            logger.error("exception while loading an image, e = " + e.getMessage());
            //TODO: create default image
            image = new Image("https://cdn.pixabay.com/photo/2016/10/18/18/19/question-mark-1750942_960_720.png");
        }

        return image;
    }

    public void handleBlitzPreviousQuestion(){
        try {
            if(questionIndex >= 1 && questionIndex < questions.size()){
                questionIndex--;
                Question currentQuestion = questions.get(questionIndex);
                blitzQuestionTextArea.setText(currentQuestion.getQuestionText());
                //TODO: set image
            }
        }
        catch (Exception e){
            logger.error("Exception while displaying Blitz previous question, e = " + e.getMessage(), e);
        }
    }

    public void displayBlitzRoundWindow(
            Game game,
            Round round,
            ObservableList<Team> teamsObservableList){

        try {
            if(blitzRoundWindowStage != null && !blitzRoundWindowStage.isShowing()){
                blitzRoundWindowStage.show();
            }
            else{
                createNewBlitzRoundWindowAndDisplay();
            }
            this.game = game;
            this.round = round;
            this.teamsObservableList = teamsObservableList;

            reloadRoundData();
            prepareTeamCards();
        }
        catch (Exception e){
            logger.error("Exception while displaying Blitz Window, e = " + e.getMessage(), e);
        }
    }

    private void prepareTeamCards() throws Exception{
        logger.info("Preparing team cards");

        team1GridPane.setVisible(false);
        team2GridPane.setVisible(false);
        team3GridPane.setVisible(false);
        team4GridPane.setVisible(false);
        team5GridPane.setVisible(false);

        for(Integer teamId : teamCardMap.keySet()) {
            BlitzTeamCardModel blitzTeamCardModel = teamCardMap.get(teamId);
            Team team = blitzTeamCardModel.getTeam();
            blitzTeamCardModel.getBlitzTeamCardLayoutSet().getCardGridPane().setVisible(true);
            blitzTeamCardModel.getBlitzTeamCardLayoutSet().getCardGridPane().setStyle("-fx-background-color: #FFFFFF;");
            blitzTeamCardModel.getBlitzTeamCardLayoutSet().getTeamPlaceInQueueLabel().setText("?");
            blitzTeamCardModel.getBlitzTeamCardLayoutSet().getTeamNameLabel().setText(team.getName());
        }

    }

    private void reloadRoundData() {
        try{
            if(round != null && game != null && teamsObservableList != null){
                blitzRoundNameLabel.setText(round.getTitle());
                questions = round.getQuestions();
                questionIndex = -1;
                constructTeamCardMap(teamsObservableList);
            }
        }
        catch (Exception e){
            String error = "Exception while reloading round data, e = " + e.getMessage();
            logger.error(error + e.getMessage(), e);
            messageWindowController.displayMessageWindow(error);
        }
    }

    private void constructTeamCardMap(ObservableList<Team> teamsObservableList) throws Exception{
        logger.info("Constructing team card map");

        teamCardMap = new HashMap<>();
        int teamListSize = teamsObservableList.size();
        ArrayList<BlitzTeamCardLayoutSet> teamCardGridPaneList = constructTeamCardLayoutSetList(teamListSize);
        int cardIndex = 0;

        for(Team team : teamsObservableList){
            BlitzTeamCardModel blitzTeamCardModel = new BlitzTeamCardModel();

            blitzTeamCardModel.setBlitzTeamCardLayoutSet(teamCardGridPaneList.get(cardIndex));
            blitzTeamCardModel.setTeam(team);
            teamCardMap.put(team.getId(), blitzTeamCardModel);
            cardIndex++;
        }
    }

    private ArrayList<BlitzTeamCardLayoutSet> constructTeamCardLayoutSetList(int teamListSize) throws Exception{

        ArrayList<BlitzTeamCardLayoutSet> list = new ArrayList<>();
        if(teamListSize == 1){
            list.add(new BlitzTeamCardLayoutSet(team1GridPane,team1CardTeamLabel,team1PlaceInQueueLabel));
        }
        else if(teamListSize == 2){
            list.add(new BlitzTeamCardLayoutSet(team2GridPane,team2CardTeamLabel, team2PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team4GridPane,team4CardTeamLabel, team4PlaceInQueueLabel));
        }
        else if(teamListSize == 3){
            list.add(new BlitzTeamCardLayoutSet(team1GridPane,team1CardTeamLabel, team1PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team3GridPane,team3CardTeamLabel, team3PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team5GridPane,team5CardTeamLabel, team5PlaceInQueueLabel));
        }
        else if(teamListSize == 4){
            list.add(new BlitzTeamCardLayoutSet(team1GridPane,team1CardTeamLabel,team1PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team2GridPane,team2CardTeamLabel, team2PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team4GridPane,team4CardTeamLabel, team4PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team5GridPane,team5CardTeamLabel, team5PlaceInQueueLabel));
        }
        else if (teamListSize == 5){
            list.add(new BlitzTeamCardLayoutSet(team1GridPane,team1CardTeamLabel, team1PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team2GridPane,team2CardTeamLabel, team2PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team3GridPane,team3CardTeamLabel, team3PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team4GridPane,team4CardTeamLabel, team4PlaceInQueueLabel));
            list.add(new BlitzTeamCardLayoutSet(team5GridPane,team5CardTeamLabel, team5PlaceInQueueLabel));
        }
        else{
            throw (new Exception("Incorrect number of teams (" + teamListSize + ")"));
        }
        return list;
    }

    public void hideBlitzRoundWindow(){
        try {
            if(blitzRoundWindowStage != null && blitzRoundWindowStage.isShowing()){
                blitzRoundWindowStage.hide();
            }
        }
        catch (Exception e){
            logger.error("Exception while hiding Blitz Window, e = " + e.getMessage(), e);
        }
    }

    private void createNewBlitzRoundWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/BlitzRoundWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent blitzWindowParent = fxmlLoader.load();

        blitzRoundWindowStage = new Stage();
        blitzRoundWindowStage.setTitle("Start Blitz round Window");

        blitzRoundWindowStage.setScene(new Scene(blitzWindowParent));

        blitzRoundWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        blitzRoundWindowStage.show();
    }
}

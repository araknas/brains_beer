package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.game_rounds.blitz.BlitzTeamCardLayoutSet;
import com.araknas.brains_beer.game_rounds.blitz.BlitzTeamCardModel;
import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Question;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.Team;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

    private boolean isListeningForClicks = false;
    private Stack<Integer> answeringStack = new Stack<>();
    private HashMap<Integer, Integer> answeringTeamMap = new HashMap<>();

    @FXML
    Button blitzBackButton;
    @FXML
    Button blitzNextButton;
    @FXML
    Button blitzEndButton;
    @FXML
    Button answerButton;
    @FXML
    Label answerLabel;
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

    @FXML
    Label team1PointsLabel;
    @FXML
    Label team2PointsLabel;
    @FXML
    Label team3PointsLabel;
    @FXML
    Label team4PointsLabel;
    @FXML
    Label team5PointsLabel;

    @FXML
    Button team1PointsUpButton;
    @FXML
    Button team2PointsUpButton;
    @FXML
    Button team3PointsUpButton;
    @FXML
    Button team4PointsUpButton;
    @FXML
    Button team5PointsUpButton;

    @FXML
    Button team1PointsDownButton;
    @FXML
    Button team2PointsDownButton;
    @FXML
    Button team3PointsDownButton;
    @FXML
    Button team4PointsDownButton;
    @FXML
    Button team5PointsDownButton;


    HashMap<Integer, BlitzTeamCardModel> teamCardMap = new HashMap<>();

    private Game game;
    private Round round;
    private List<Question> questions;
    int questionIndex = -1;
    private ObservableList<Team> teamsObservableList;
    private HashMap<Integer, String> teamButtonMap = new HashMap<>();
    private HashMap<Integer, Integer> teamPointsMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleBlitzNextQuestion(){
        try {
            if(questionIndex >= -1 && questionIndex < questions.size() - 1){
                questionIndex++;
                Question currentQuestion = questions.get(questionIndex);
                blitzQuestionTextArea.setText(currentQuestion.getQuestionText());

                resetAnsweringStack();
                resetTeamCards();

                isListeningForClicks = true;
                answerLabel.setText(currentQuestion.getRealAnswerText());
                showAnswer(false);

                Image image = receiveQuestionImage(currentQuestion);
                blitzQuestionImageView.setImage(image);
            }
        }
        catch (Exception e){
            logger.error("Exception while handling Blitz next question, e = " + e.getMessage(), e);
        }
    }

    private void resetTeamCards() throws Exception{
        for(Map.Entry<Integer, BlitzTeamCardModel> entry : teamCardMap.entrySet()){
            BlitzTeamCardModel blitzTeamCardModel = entry.getValue();
            blitzTeamCardModel.getBlitzTeamCardLayoutSet().getTeamPlaceInQueueLabel().setText("?");
            blitzTeamCardModel.getBlitzTeamCardLayoutSet().getCardGridPane().setStyle("-fx-background-color: #FFFFFF;");
        }
    }

    private void resetAnsweringStack() throws Exception{

        answeringTeamMap.clear();
        int teamCount = teamsObservableList.size();
        answeringStack.empty();

        for(int i = teamCount; i > 0; i--){
            answeringStack.push(i);
        }
    }

    private Image receiveQuestionImage(Question currentQuestion) {

        Image image = null;
        try{
            //File imageFile = new File("extra_data/default_question.png");
            File imageFile = new File(currentQuestion.getImageUrl());
            InputStream stream = new FileInputStream(imageFile);
            image = new Image(stream);
        }
        catch (Exception e){
            logger.error("exception while loading an image, e = " + e.getMessage());
            ClassLoader classLoader = getClass().getClassLoader();
            //TODO: save string to constants
            InputStream defaultQuestionIconUrl = classLoader.getResourceAsStream("images/question/default_question_icon.png");
            image = new Image(defaultQuestionIconUrl);
        }

        return image;
    }

    public void handleBlitzPreviousQuestion(){
        try {
            if(questionIndex >= 1 && questionIndex < questions.size()){
                questionIndex--;
                Question currentQuestion = questions.get(questionIndex);

                resetAnsweringStack();
                resetTeamCards();

                isListeningForClicks = true;
                blitzQuestionTextArea.setText(currentQuestion.getQuestionText());
                answerLabel.setText(currentQuestion.getRealAnswerText());
                showAnswer(false);

                Image image = receiveQuestionImage(currentQuestion);
                blitzQuestionImageView.setImage(image);
            }
        }
        catch (Exception e){
            logger.error("Exception while displaying Blitz previous question, e = " + e.getMessage(), e);
        }
    }

    public void displayBlitzRoundWindow(
            Game game,
            Round round,
            ObservableList<Team> teamsObservableList,
            HashMap<Integer, String> teamButtonMap){

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
            this.teamButtonMap = teamButtonMap;

            reloadRoundData();
            prepareTeamCards();
            showAnswer(false);
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

            int points = teamPointsMap.get(teamId);
            blitzTeamCardModel.getBlitzTeamCardLayoutSet().getTeamPointsLabel().setText(String.valueOf(points));
        }
    }

    private void reloadRoundData() {
        try{
            if(round != null && game != null && teamsObservableList != null){
                blitzRoundNameLabel.setText(round.getTitle());
                questions = round.getQuestions();
                questionIndex = -1;
                constructTeamCardMap(teamsObservableList);
                constructTeamPointsMap(teamsObservableList);
            }
        }
        catch (Exception e){
            String error = "Exception while reloading round data, e = " + e.getMessage();
            logger.error(error + e.getMessage(), e);
            messageWindowController.displayMessageWindow(error);
        }
    }

    private void constructTeamPointsMap(ObservableList<Team> teamsObservableList) throws Exception{
        logger.info("Constructing team points map");
        for(Team team : teamsObservableList){
           teamPointsMap.put(team.getId(), 0);
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
            list.add(new BlitzTeamCardLayoutSet(team1GridPane,team1CardTeamLabel,team1PlaceInQueueLabel, team1PointsLabel, team1PointsUpButton, team1PointsDownButton));
        }
        else if(teamListSize == 2){
            list.add(new BlitzTeamCardLayoutSet(team2GridPane,team2CardTeamLabel, team2PlaceInQueueLabel, team2PointsLabel, team2PointsUpButton, team2PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team4GridPane,team4CardTeamLabel, team4PlaceInQueueLabel, team4PointsLabel, team4PointsUpButton, team4PointsDownButton));
        }
        else if(teamListSize == 3){
            list.add(new BlitzTeamCardLayoutSet(team1GridPane,team1CardTeamLabel, team1PlaceInQueueLabel, team1PointsLabel, team1PointsUpButton, team1PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team3GridPane,team3CardTeamLabel, team3PlaceInQueueLabel, team3PointsLabel, team3PointsUpButton, team3PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team5GridPane,team5CardTeamLabel, team5PlaceInQueueLabel, team5PointsLabel, team5PointsUpButton, team5PointsDownButton));
        }
        else if(teamListSize == 4){
            list.add(new BlitzTeamCardLayoutSet(team1GridPane,team1CardTeamLabel,team1PlaceInQueueLabel, team1PointsLabel, team1PointsUpButton, team1PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team2GridPane,team2CardTeamLabel, team2PlaceInQueueLabel, team2PointsLabel, team2PointsUpButton, team2PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team4GridPane,team4CardTeamLabel, team4PlaceInQueueLabel, team4PointsLabel, team4PointsUpButton, team4PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team5GridPane,team5CardTeamLabel, team5PlaceInQueueLabel, team5PointsLabel, team5PointsUpButton, team5PointsDownButton));
        }
        else if (teamListSize == 5){
            list.add(new BlitzTeamCardLayoutSet(team1GridPane,team1CardTeamLabel, team1PlaceInQueueLabel, team1PointsLabel, team1PointsUpButton, team1PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team2GridPane,team2CardTeamLabel, team2PlaceInQueueLabel, team2PointsLabel, team2PointsUpButton, team2PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team3GridPane,team3CardTeamLabel, team3PlaceInQueueLabel, team3PointsLabel, team3PointsUpButton, team3PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team4GridPane,team4CardTeamLabel, team4PlaceInQueueLabel, team4PointsLabel, team4PointsUpButton, team4PointsDownButton));
            list.add(new BlitzTeamCardLayoutSet(team5GridPane,team5CardTeamLabel, team5PlaceInQueueLabel, team5PointsLabel, team5PointsUpButton, team5PointsDownButton));
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
        blitzWindowParent.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                logger.info("Pressed:"  + event.getCode());
                handlePlayerPressEvent(event);
            }
        });


        blitzRoundWindowStage = new Stage();
        blitzRoundWindowStage.setTitle("Start Blitz round Window");

        blitzRoundWindowStage.setScene(new Scene(blitzWindowParent));

        blitzRoundWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                logger.info("Closing Blitz window");
                blitzRoundWindowStage = null;
            }
        });
        blitzRoundWindowStage.show();
    }

    public void handlePointsUpButtonClick(ActionEvent event){
        try{
            Button source = (Button) event.getSource();
            String buttonId = source.getId();
            Label pointsLabel = getPointsLabelByButtonId(buttonId);
            Integer points = Integer.valueOf(pointsLabel.getText());
            points++;
            pointsLabel.setText(String.valueOf(points));

        }catch (Exception e){
            logger.error("Exception while handling points up button, e = " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handlePointsDownButtonClick(ActionEvent event){
        try{
            Button source = (Button) event.getSource();
            String buttonId = source.getId();
            Label pointsLabel = getPointsLabelByButtonId(buttonId);
            Integer points = Integer.valueOf(pointsLabel.getText());
            if(points > 0){
                points--;
            }
            pointsLabel.setText(String.valueOf(points));

        }catch (Exception e){
            logger.error("Exception while handling points down button, e = " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Label getPointsLabelByButtonId(String buttonId) {

        Label pointsLabel = null;

        if(buttonId.contains("1")){
            pointsLabel = team1PointsLabel;
        }
        else if (buttonId.contains("2")){
            pointsLabel = team2PointsLabel;
        }
        else if (buttonId.contains("3")){
            pointsLabel = team3PointsLabel;
        }
        else if (buttonId.contains("4")){
            pointsLabel = team4PointsLabel;
        }
        else if (buttonId.contains("5")){
            pointsLabel = team5PointsLabel;
        }
        return pointsLabel;
    }

    private void handlePlayerPressEvent(KeyEvent event){

        try{
            Date clickTime = new Date();

            if(isListeningForClicks){
                String buttonCode = event.getText();
                for(Map.Entry<Integer, String> entry : teamButtonMap.entrySet()){
                    if(entry.getValue().equals(buttonCode)){
                        int teamId = entry.getKey();
                        putTeamIntoTheAnsweringQueue(teamId);
                    }
                }
            }
        }
        catch (Exception e){
            String error = "Exception while handling player press event, e = " + e.getMessage();
            logger.error(error);
            e.printStackTrace();
        }
    }

    private void putTeamIntoTheAnsweringQueue(int teamId) throws Exception{

        // Checks if team is not already put into the queue
        if(!answeringTeamMap.containsKey(teamId)){

            int answeringNumber = answeringStack.pop();
            BlitzTeamCardModel blitzTeamCardModel = teamCardMap.get(teamId);
            answeringTeamMap.put(teamId, answeringNumber);

            blitzTeamCardModel.
                    getBlitzTeamCardLayoutSet().
                    getTeamPlaceInQueueLabel().
                    setText(String.valueOf(answeringNumber));

            if(answeringNumber == 1){
                blitzTeamCardModel.getBlitzTeamCardLayoutSet().getCardGridPane().setStyle("-fx-background-color: #E94815;");
            }

            if(answeringStack.isEmpty()){
                isListeningForClicks = false;
            }
        }
    }

    public void handleBlitzEndButtonClick(){
        logger.info("Closing Blitz Game...");
        blitzRoundWindowStage.close();
    }

    public void handleAnswerButtonClick(){
        try{
            if(answerLabel.isVisible()){
                showAnswer(false);
            }
            else{
                showAnswer(true);
            }
        }
        catch (Exception e){
            String msg = "Exception showing the answer, e = " + e.getMessage();
            messageWindowController.displayMessageWindow(msg);
            logger.error(msg);
        }
    }

    public void showAnswer(boolean isNeedToShowAnswer)
            throws Exception {

        answerLabel.setVisible(isNeedToShowAnswer);
        if (isNeedToShowAnswer) {
            answerButton.setText("Ats. (Slėpti)");
        }
        else{
            answerButton.setText("Ats. (Rodyti)");
        }
    }

}

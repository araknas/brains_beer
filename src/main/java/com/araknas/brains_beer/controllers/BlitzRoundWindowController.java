package com.araknas.brains_beer.controllers;

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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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

    private Game game;
    private Round round;
    private List<Question> questions;
    int questionIndex = -1;
    private  ObservableList<Team> teamsObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleBlitzNextQuestion(){
        try {
            if(questionIndex >= -1 && questionIndex < questions.size() - 1){
                questionIndex++;
                Question currentQuestion = questions.get(questionIndex);
                blitzQuestionTextArea.setText(currentQuestion.getQuestionText());
                //TODO: set image
            }
        }
        catch (Exception e){
            logger.error("Exception while handling Blitz next question, e = " + e.getMessage(), e);
        }
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
        }
        catch (Exception e){
            logger.error("Exception while displaying Blitz Window, e = " + e.getMessage(), e);
        }
    }

    private void reloadRoundData() {
        try{
            if(round != null && game != null && teamsObservableList != null){
                blitzRoundNameLabel.setText(round.getTitle());
                questions = round.getQuestions();
                questionIndex = -1;
            }
        }
        catch (Exception e){
            String error = "Exception while reloading round data, e = " + e.getMessage();
            logger.error(error + e.getMessage(), e);
            messageWindowController.displayMessageWindow(error);
        }
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

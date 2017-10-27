package com.araknas.brains_beer.controllers;

import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.repositories.GameRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

/**
 * Created by Giedrius on 2017.10.21.
 */
@Component
public class GamesWindowController implements Initializable, ViewController {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @Autowired
    ConfigurableApplicationContext springContext;

    @Autowired
    TeamsWindowController teamsWindowController;

    @Autowired
    GamePrepareWindowController gamePrepareWindowController;

    @Autowired
    MessageWindowController messageWindowController;

    @Autowired
    GameRepository gameRepository;

    @FXML
    Button gameWindowBackButton;

    @FXML
    Button addGameButton;

    @FXML
    Button deleteGameButton;

    @FXML
    TextField gameNameField;

    @FXML
    TextField gameDescriptionField;


    @FXML
    private ListView<Game> gamesListView;

    private ObservableList<Game> gameObservableList;

    private Stage gamesWindowStage;
    private Game selectedGame;

    public void handleGameWindowNextButtonClick(){

        if(selectedGame != null){
            hideGamesWindow();
            gamePrepareWindowController.displayGamePrepareWindow(selectedGame);
        }
        else{
            messageWindowController.displayMessageWindow("Pasirinkite komandą!");
        }
    }

    public void handleGameWindowBackButtonClick(){
        hideGamesWindow();
        teamsWindowController.displayTeamsWindow();
    }

    public void handleAddGameButtonClick(){
        try{
            logger.info("Adding new game.");
            addGameToDatabase();
            reloadGamesListView();
        }
        catch (Exception e){
            String message = "Exception while adding a game, e =" + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }

    public void handleDeleteGameButtonClick(){
        try{
            logger.info("Deleting team.");
            deleteGameFromDatabase();
            reloadGamesListView();
        }
        catch (Exception e){
            String message = "Exception while deleting a game, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }

    public void displayGamesWindow(){
        try {
            if(gamesWindowStage != null && !gamesWindowStage.isShowing()){
                gamesWindowStage.show();
            }
            else{
                createNewGamesWindowAndDisplay();
            }
        }
        catch (Exception e){
            logger.error("Exception while displaying Games Window, e = " + e.getMessage(), e);
        }
    }

    public void hideGamesWindow(){
        try {
            if(gamesWindowStage != null && gamesWindowStage.isShowing()){
                gamesWindowStage.hide();
            }
        }
        catch (Exception e){
            logger.error("Exception while hiding Games Window, e = " + e.getMessage(), e);
        }
    }

    private void createNewGamesWindowAndDisplay() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/GamesWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent gamesWindowParent = fxmlLoader.load();

        gamesWindowStage = new Stage();
        gamesWindowStage.setTitle("Games Window");

        gamesWindowStage.setScene(new Scene(gamesWindowParent));

        gamesWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        gamesWindowStage.show();
    }

    private void addGameToDatabase() throws Exception{

        String gameName = gameNameField.getText();
        String gameDesc = gameDescriptionField.getText();

        validateGame(gameName, gameDesc);

        Game newGame = new Game();
        newGame.setGameTitle(gameName);
        newGame.setGameDesc(gameDesc);

        gameRepository.save(newGame);
    }

    private void deleteGameFromDatabase() throws Exception{

        String gameName = gameNameField.getText();
        Game game = gameRepository.findByGameTitle(gameName);

        if(game == null){
            throw (new Exception("Cannot find this game in the database."));
        }

        gameRepository.delete(game);
    }

    private void validateGame(String gameName, String gameDesc) throws Exception {

        String errorMsg = "";
        if ((gameName != null && gameName.equals(""))) {
            errorMsg = "Invalid game name";
        }

        Game game = gameRepository.findByGameTitle(gameName);
        if(game != null){
            errorMsg = "Game " + gameName + " already exists";
        }

        if(!errorMsg.equals("")){
            throw (new Exception(errorMsg));
        }
    }

    private void reloadGamesListView() {
        try{
            List<Game> allGames = gameRepository.findAll();
            gameObservableList.clear();

            for(Game game : allGames){
                gameObservableList.add(game);
            }
            gamesListView.setItems(gameObservableList);
            gamesListView.setCellFactory(gamesListView -> new GameListViewCellController(this));

        }catch (Exception e){
            String message = "Exception while reloading games list view, e = " + e.getMessage();
            logger.error(message);
            messageWindowController.displayMessageWindow(message);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameObservableList = FXCollections.observableArrayList();
        reloadGamesListView();
    }

    public void setSelectedGame(Game selectedGame) {
        this.selectedGame = selectedGame;
    }
}

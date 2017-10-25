package com.araknas.brains_beer.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Giedrius on 2017-10-25.
 */
@Component
public class MessageWindowController {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    @FXML
    Button messageWindowOkButton;
    @FXML
    Label messageWindowLabel;

    @Autowired
    ConfigurableApplicationContext springContext;

    private Stage messageWindowStage;

    public void handleMessageWindowOkButton(){
        hideMessageWindow();
    }

    public void displayMessageWindow(String message){
        try {
            if(messageWindowStage != null && !messageWindowStage.isShowing()){
                messageWindowLabel.setText(message);
                messageWindowStage.show();
            }
            else{
                createNewMessageWindowAndDisplay(message);
            }
        }
        catch (Exception e){
            logger.error("Exception while displaying Message Window, e = " + e.getMessage(), e);
        }
    }

    public void hideMessageWindow(){
        try {
            if(messageWindowStage != null && messageWindowStage.isShowing()){
                messageWindowStage.hide();
            }
        }
        catch (Exception e){
            logger.error("Exception while hiding Message Window, e = " + e.getMessage(), e);
        }
    }

    private void createNewMessageWindowAndDisplay(String message) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MessageWindow.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent gamesWindowParent = fxmlLoader.load();

        messageWindowStage = new Stage();
        messageWindowStage.initStyle(StageStyle.UNDECORATED);
        messageWindowStage.initModality(Modality.APPLICATION_MODAL);
        messageWindowStage.setAlwaysOnTop(true);

        messageWindowStage.setTitle("Message Window");
        messageWindowStage.setScene(new Scene(gamesWindowParent));

        messageWindowLabel.setText(message);
        messageWindowStage.show();
    }

}

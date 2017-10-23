package com.araknas.brains_beer;
import com.araknas.brains_beer.controllers.GamesWindowController;
import com.araknas.brains_beer.controllers.TeamsWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BrainsBeerApplication extends Application {

	private ConfigurableApplicationContext springContext;
	private TeamsWindowController teamsWindowController;

	public static void main(String[] args) {
		launch(BrainsBeerApplication.class, args);
	}

	@Override
	public void init() throws Exception {
		springContext = SpringApplication.run(BrainsBeerApplication.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		teamsWindowController = (TeamsWindowController) springContext.getBean("teamsWindowController");
		teamsWindowController.displayTeamsWindow();
	}

	@Override
	public void stop() throws Exception {
		springContext.stop();
	}

}

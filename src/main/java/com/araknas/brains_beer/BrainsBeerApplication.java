package com.araknas.brains_beer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BrainsBeerApplication extends Application {

	private ConfigurableApplicationContext springContext;
	private Parent root;

	public static void main(String[] args) {
		launch(BrainsBeerApplication.class, args);
	}

	@Override
	public void init() throws Exception {
		// Test changes
		springContext = SpringApplication.run(BrainsBeerApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
		root = fxmlLoader.load();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World");
		Scene scene = new Scene(root, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		springContext.stop();
	}

}

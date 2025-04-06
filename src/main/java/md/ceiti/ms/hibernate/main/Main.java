package md.ceiti.ms.hibernate.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import md.ceiti.ms.hibernate.controller.MainController;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/md/ceiti/ms/hibernate/view/main-page.fxml"));
        Parent root = (Parent)loader.load();
        MainController controller = (MainController)loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("JavaFX Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

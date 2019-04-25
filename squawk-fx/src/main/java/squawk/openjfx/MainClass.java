package squawk.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainClass extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        RequestHelper rqst = new RequestHelper();
        if (rqst.tryAuth()) {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("SourceView.fxml"));
            primaryStage.setScene(new Scene(root,1500, 1300));
        } else {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Auth.fxml"));
            primaryStage.setScene(new Scene(root, 500, 300));
        }
        primaryStage.setTitle("Squawk");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
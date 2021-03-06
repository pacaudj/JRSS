package squawk.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.json.Json;
import java.io.IOException;

public class RegisterController {

        @FXML
        private TextField userField;

        @FXML
        private TextField emailField;

        @FXML
        private PasswordField passwordField;

        @FXML
        private Button cancelButton;

        @FXML
        protected void handleCancelButtonAction(ActionEvent event) {
            try {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Auth.fxml"));
                stage.setScene(new Scene(root, 500, 300));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        protected void handleSubmitButton(ActionEvent event) {
            String postData = Json.createObjectBuilder()
                    .add("username", userField.getText())
                    .add("email", emailField.getText())
                    .add("password", passwordField.getText())
                    .build()
                    .toString();
            RequestHelper rqst = new RequestHelper();
            try {
                String result = rqst.doPost("https://squawkapi.chaz.pro/register", postData);
                System.out.println(result);
            } catch (Exception ex) {
            }
        }
}

package squawk.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class AuthController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton, registerButton;

    @FXML
    private Label authStatus;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();
        String postData = Json.createObjectBuilder()
                .add("email", emailField.getText())
                .add("password", passwordField.getText())
                .build()
                .toString();
        RequestHelper rqst = new RequestHelper();
        String result = "";
        try {
            result = rqst.doPost("https://squawkapi.chaz.pro/auth", postData);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            authStatus.setText("Wrong password or email");
            authStatus.setTextFill(Color.web("#ff0000"));
            passwordField.clear();
        }
        if (!result.equals("")) {
            System.out.println("result = " + result);
            JsonReader jsonReader = Json.createReader(new StringReader(result));
            JsonObject object = jsonReader.readObject();
            authStatus.setText(object.getString("status:") + '\n' + "Your token is : " + object.getString("token"));
            authStatus.setTextFill(Color.web("#32CD32"));
        }
    }

    @FXML
    protected void handleRegisterButtonAction(ActionEvent event) throws Exception{
        Parent root;
        Stage stage;

        stage = (Stage) registerButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getClassLoader().getResource("Register.fxml"));
        Scene scene = new Scene(root,500, 300);
        stage.setScene(scene);
        stage.show();
    }
}
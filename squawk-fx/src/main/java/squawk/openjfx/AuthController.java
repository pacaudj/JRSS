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

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    protected void handleSubmitButtonAction() {
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
            writeTokenToFile(object.getString("token"));
        }
        if (rqst.tryAuth()) {
            try {
                Stage stage = (Stage) submitButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("SourceView.fxml"));
                stage.setScene(new Scene(root,1500, 1300));
                stage.show();
            } catch(Exception err) {
                System.out.println(err.getMessage());
            }
        }
    }

    public void writeTokenToFile(String token) {
        try (BufferedWriter bw = new BufferedWriter(new PrintWriter("token.txt"))) {
            bw.write(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleRegisterButtonAction(ActionEvent event) throws Exception{
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Register.fxml"));
        stage.setScene(new Scene(root,500, 300));
        stage.show();
    }
}
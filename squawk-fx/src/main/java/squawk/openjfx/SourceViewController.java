package squawk.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SourceViewController {

    private String token;

    @FXML
    private VBox sourceList;

    @FXML
    private Text sourceDetail;

    @FXML
    private TextField name, link;

    @FXML
    private ImageView pic;

    public void initialize() {
        try {
            token = new String(Files.readAllBytes(Paths.get("token.txt")));
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        try {
            Image img = new Image(new FileInputStream("squawk.png"));
            pic.setImage(img);
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        updateSources();
    }

    public void updateSources() {
        RequestHelper rqst = new RequestHelper();
        HttpResponse response = rqst.getSources(token);
        String json;
        try {
            json = EntityUtils.toString(response.getEntity());
        } catch (Exception err) {
            return;
        }
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject object = jsonReader.readObject();
        JsonArray arr = object.getJsonArray("sources");
        sourceList.getChildren().clear();
        for (int i = 0; i < arr.size(); i++) {
            JsonObject obj = arr.getJsonObject(i);
            Text name = new Text(obj.getString("host") + "     " + obj.getString("link"));
            name.setOnMouseClicked(e -> {
                RequestHelper rqs = new RequestHelper();
                HttpResponse resp = rqs.getSourceContent(token, obj.getString("source_id"));
                String body;
                try {
                    body = EntityUtils.toString(resp.getEntity());
                } catch (Exception err) {
                    return;
                }
                JsonReader jReader = Json.createReader(new StringReader(body));
                JsonObject jobject = jReader.readObject();
                JsonArray array = jobject.getJsonArray("content");
                for (int j = 0; j< array.size(); j++) {
                    JsonObject content = array.getJsonObject(j);
                    sourceDetail.setText(content.getString("description"));
                }
            });

            sourceList.getChildren().add(name);
        }
    }

    @FXML
    protected void logout() {
        File file = new File("token.txt");
        file.delete();
        Stage stage = (Stage) name.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Auth.fxml"));
            stage.setScene(new Scene(root,500, 300));
            stage.show();
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }

    @FXML
    protected void reload() {
        updateSources();
        sourceDetail.setText("");
        link.clear();
        name.clear();
    }

    @FXML
    protected void handleSubmitButton() {
        RequestHelper rqst = new RequestHelper();
        rqst.postSource(token, link.getText(), name.getText());
        updateSources();
    }
}

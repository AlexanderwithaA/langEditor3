import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

//can listen for keystrokes with the stage

public class FXwindow extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("BTA Language Pack Editor");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/langEditorPanel2.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void buttonClicked(Event e) {
        System.out.println("input");
    }
}


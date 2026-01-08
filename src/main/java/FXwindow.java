import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

//can listen for keystrokes with the stage

public class FXwindow extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("BTA Language Pack Editor");

        Label label = new Label("hi");
        Scene scene = new Scene(label, 400,300);
        stage.setScene(scene);

        stage.show();
    }
}
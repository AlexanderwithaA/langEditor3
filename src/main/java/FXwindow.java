import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FXwindow extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("BTA Language Pack Editor");

        Label label = new Label("hi");
        Scene scene = new Scene(label, 20,300);
        stage.setScene(scene);

        stage.show();
    }
}

import javafx.application.Application;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    static FXwindow programWindow;
    static FileCollection fileCollection;

    public static void main(String[] args) {

        fileCollection = new FileCollection();
        programWindow = new FXwindow();

    }

    public static FileCollection getFileCollection() {
        return fileCollection;
    }
}

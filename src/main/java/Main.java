import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    static LangFileCollection lfc;

    public static void main(String[] args) throws IOException {

        lfc = new LangFileCollection();

        lfc.populateCollection("C:\\Users\\N112Student\\Documents\\AsCS\\AlexCode\\langEditor\\en_US\\en_US");
        Editor terminal  = new Editor("strings.lang");
        terminal.initializeEditor();


//        Object handle = Signals.register("INT", () -> {
//            System.out.println("Caught SIGINT");
//            // Perform cleanup
//        });



    }

}

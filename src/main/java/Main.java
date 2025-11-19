import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        LangFileCollection temp = new LangFileCollection();

        temp.populateCollection("C:\\Users\\N112Student\\Documents\\AsCS\\AlexCode\\langEditor\\en_US\\en_US");
        Editor terminal  = new Editor(temp.loadFile("strings.lang"));
        terminal.initializeEditor();


//        Object handle = Signals.register("INT", () -> {
//            System.out.println("Caught SIGINT");
//            // Perform cleanup
//        });



    }
}

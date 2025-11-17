import java.io.IOException;

public class Main {

    static LangFileCollection langFileAccessor = new LangFileCollection();

    public static void main(String[] args) throws IOException {
        Editor terminal  = new Editor();
        terminal.initializeEditor();
        terminal.inputPrompt();
        terminal.fileSelect();
        terminal.initializeScreenBuffer();
        terminal.editor();

//        Object handle = Signals.register("INT", () -> {
//            System.out.println("Caught SIGINT");
//            // Perform cleanup
//        });



    }
}

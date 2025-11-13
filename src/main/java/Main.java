import org.jline.terminal.Terminal;
import org.jline.utils.Signals;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    static LangFileCollection langFileAccessor = new LangFileCollection();

    public static void main(String[] args) throws IOException {
        TerminalHandler terminal  = new TerminalHandler();
        terminal.initialize();
        terminal.inputPrompt();
        terminal.clearScreen();
        terminal.fileSelect();


//        Object handle = Signals.register("INT", () -> {
//            System.out.println("Caught SIGINT");
//            // Perform cleanup
//        });



    }
}

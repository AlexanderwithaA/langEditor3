import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal.Signal;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class TerminalHandler {

    static Terminal terminal;
    static PrintWriter writer;
    static Reader reader;
    static int terminalRows;
    static int terminalColumns;

    public static void createOutPutWindow() throws IOException {
        terminal = TerminalBuilder.builder().name("BTA Language Pack Editor").system(true).type("ansi").build();
        writer = terminal.writer();
        reader = terminal.reader();
        terminalRows = terminal.getSize().getColumns();
        terminalColumns = terminal.getSize().getRows();

        terminal.handle(Signal.INT, signal -> {
            // Handle Ctrl+C
            System.out.println("success");
        });
        terminal.handle(Signal.WINCH, signal -> {
            terminalRows = terminal.getSize().getRows();
            terminalColumns = terminal.getSize().getColumns();
            //System.out.println("terminal rows is " + terminal.getSize().getRows() + " and " + terminal.getSize().getColumns() + " columns");
            reDraw();
        });

        terminal.handle(Signal.TSTP, signal -> {
            // Handle Ctrl+Z (suspend)
            terminal.pause();
        });

    }

    public static void reDraw() {
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < terminalColumns; i++) {
            line.append("-");
        }
        terminal.puts(InfoCmp.Capability.clear_screen);
        writer.flush();
        writer.println(line);
        writer.flush();
    }

    public void testOutPut() {
        writer.println("waoh");
        writer.println("woah!");
        writer.println("man wth!!");
        writer.flush();
    }

    public void inputPrompt() {
        writer.println("Give location to a .LANG type file or a folder containing such files:");
        writer.flush();
        while(reader != )
    }



//    Object handle = Signals.register("INT", () -> {
//        System.out.println("Caught SIGINT");
//        // Perform cleanup
//    });
}

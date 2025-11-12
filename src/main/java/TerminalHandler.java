import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal.Signal;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.PrintWriter;

public class TerminalHandler {

    LangFileCollection langFileAccessor = Main.langFileAccessor;

    static Terminal terminal;
    static PrintWriter writer;
    static int terminalRows;
    static int terminalColumns;
    static LineReader reader;
    static DefaultParser parser;

    public static void initialize() throws IOException {
        parser = new DefaultParser();
        parser.setEscapeChars(null);

        terminal = TerminalBuilder.builder().name("BTA Language Pack Editor").system(true).type("ansi").build();
        writer = terminal.writer();
        reader = LineReaderBuilder.builder().terminal(terminal).parser(parser).build();
        terminalRows = terminal.getSize().getColumns();
        terminalColumns = terminal.getSize().getRows();

        terminal.handle(Signal.INT, signal -> {
            // Handle Ctrl+C
            System.out.println("success");
        });
        terminal.handle(Signal.WINCH, signal -> {
            terminalRows = terminal.getSize().getRows();
            terminalColumns = terminal.getSize().getColumns();
            draw();
        });

        terminal.handle(Signal.TSTP, signal -> {
            // Handle Ctrl+Z (suspend)
            terminal.pause();
        });

    }

    public static void draw() {
//        StringBuilder line = new StringBuilder();
//        for(int i = 0; i < terminalColumns; i++) {
//            line.append("-");
//        }
//        terminal.puts(InfoCmp.Capability.clear_screen);
//        writer.flush();
//        writer.println(line);
//        writer.flush();
    }

    public void clearScreen() {
        terminal.puts(InfoCmp.Capability.clear_screen);
        writer.flush();
    }

    public void inputPrompt() throws IOException {
        String prompt = "Path or directory: ";
        writer.println("Input filepath of a .LANG formatted file or a folder containing such files. Type \"Done\" to complete file input.");
        writer.flush();
        String input = reader.readLine(prompt);

        while(!(input.equalsIgnoreCase("done") || input.equalsIgnoreCase("y"))) {
            langFileAccessor.populateCollection(input);
            writer.println();
            writer.println("Input filepath of a .LANG formatted file or a folder containing such files. Type \"Done\" to complete file input.");
            writer.flush();
            input = reader.readLine(prompt);
        }
    }

    public void fileSelect() {
        int i = 1;
        for(; i <= langFileAccessor.getFileNames().length; i++) {
            writer.println(i + ") " + langFileAccessor.getFileNames()[i]);
        }
        writer.println();
        writer.println("Enter Between " + 1 + " and " + i + " to load a file.");
        writer.flush();
    }

    public void editor() {

    }
}

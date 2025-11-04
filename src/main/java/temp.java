import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class temp {
    public static void main(String[] args) {
        LangFileCollection temp = new LangFileCollection();
        temp.populateCollection("C:/Users/N112Student/Documents/AsCS/AlexCode/langEditor/en_US/en_US");
        //String[] temp1 = {"C:/Users/N112Student/Documents/AsCS/AlexCode/langEditor/en_US/en_US/options.lang","a","","C:/Users/N112Student/Documents/AsCS/AlexCode/langEditor/en_US/en_US/stats.lang"};
        //temp.populateCollection(temp1);

//        LangFile temp3 = temp.loadFile("options.lang");
//        System.out.println(temp3.getSubKey(""));
//        temp3 = temp.loadFile("stats.lang");
//        System.out.println(temp3.getSubKey("stat"));

        //System.out.println(temp.getSubKey(""));

        try {
            // Create a terminal
            Terminal terminal = TerminalBuilder.builder().system(true).build();

            // Create a line reader
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            // Read lines from the user
            while (true) {
                String line = reader.readLine("prompt> ");

                // Exit if requested
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                // Echo the line back to the user
                terminal.writer().println("You entered: " + line);
                terminal.flush();
            }

            terminal.writer().println("Goodbye!");
            terminal.close();

        } catch (IOException e) {
            System.err.println("Error creating terminal: " + e.getMessage());
        }
    }
}

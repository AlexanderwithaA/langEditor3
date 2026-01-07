import org.apache.commons.lang3.EnumUtils;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal.Signal;
import org.jline.utils.*;
import org.jline.utils.InfoCmp.Capability;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.jline.keymap.KeyMap.key;

public class Editor {

    Terminal terminal;
    PrintWriter writer;
    NonBlockingReader reader;
    Size terminalSize;
    LangFile file;
    LangFile newFile;
    Display keyValueBox;

    int KVlistOffset = 0;
    int KVlistOffsetPre = 0;
    int KVseparatorPos = 0;
    int cursorLR;
    int cursorMoveLimit = 0;
    int cursorUD;
    AttributedStringBuilder lineText;


    enum Operation {LEFT, RIGHT, UP, DOWN, Z}
    KeyMap<Operation> keyMap = new KeyMap<>();

    List<AttributedString> parsedTreeMapData;

    Editor(String fileString) {
        file = Main.lfc.loadFile(fileString);
        newFile = Main.lfc.loadFile("__" + fileString);
    }

    public void initializeEditor() throws IOException {
        lineText = new AttributedStringBuilder();
        terminal = TerminalBuilder.builder().name("BTA Language Pack Editor").system(true).type("ansi").build();
        //terminal.puts(Capability.enter_ca_mode);
        terminal.flush();
        keyValueBox = new Display(terminal, false);
        writer = terminal.writer();
        reader = terminal.reader();
        terminalSize = terminal.getSize();

        screenDisplay(true);

        terminal.handle(Signal.INT, signal -> {
            // Handle Ctrl+C
            System.out.println("nuh-uh");
        });

        // There is no "AtomicTrinary" so I'm using an integer. 0 tells the thread to run,
        // 1 tells the thread to stop, 2 indicates that the thread has received the stop
//        AtomicInteger submitInput = new AtomicInteger(0);
//        AtomicReference<AttributedStringBuilder> inputString = new AtomicReference<>();
//        inputString.set(new AttributedStringBuilder());
//
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.submit(() -> {
//            while(true) {
//                if(submitInput.get() >= 1) {
//                    submitInput.set(2);
//                    while(submitInput.get() == 2);
//                } else {
//                    Editor.Operation op;
//                    BindingReader bindingReader = new BindingReader(terminal.reader());
//                    op = bindingReader.readBinding(keyMap);
//                    if(EnumUtils.isValidEnum(Operation, op))
//                        inputString.updateAndGet(v -> v.append((char) c));
//                        terminal.puts(Capability.cursor_address, cursorUD, cursorLR);
//                        terminal.puts(Capability.clr_eol);
//                        writer.print(inputString.get());
//                        writer.flush();
//                    //}
//                }
//            }
//        });

        terminal.handle(Signal.WINCH, signal -> {
            terminalSize = terminal.getSize();

                KVseparatorPos = terminalSize.getColumns()/4;
                keyValueBox.resize(terminalSize.getRows(),terminalSize.getColumns());
                cursorMoveLimit = terminalSize.getRows() - 4;
                screenDisplay(false);
            //}
        });

//        terminal.handle(Signal.TSTP, signal -> {
//            // Handle Ctrl+Z (suspend)
//            terminal.pause();
//        });

        BindingReader bindingReader = new BindingReader(terminal.reader());
        keyMap.bind(Operation.LEFT, key(terminal, Capability.key_left));
        keyMap.bind(Operation.RIGHT, key(terminal, Capability.key_right));
        keyMap.bind(Operation.UP, key(terminal, Capability.key_up));
        keyMap.bind(Operation.DOWN, key(terminal, Capability.key_down));
        keyMap.bind(Operation.Z, "z");

        Operation op;

        while (true) {

            op = bindingReader.readBinding(keyMap);
            switch(op) {
                case LEFT:
                    if(!(cursorLR < KVseparatorPos + 2)) {
                        cursorLR--;
                    }
                    break;
                case RIGHT:
                    if(!(cursorLR > terminalSize.getColumns())) {
                        cursorLR++;
                    }
                    break;
                case UP:
                    KVlistOffsetPre--;
                    break;
                case DOWN:
                    KVlistOffsetPre++;
                    break;
            }


            if(cursorUD < terminalSize.getRows() - 1 && KVlistOffsetPre > KVlistOffset) {
                KVlistOffsetPre--;
                cursorUD++;
            }   if(cursorUD > 0 && KVlistOffsetPre < KVlistOffset) {
                KVlistOffsetPre++;
                cursorUD--;
            }

            if(KVlistOffsetPre < 0) {
                KVlistOffsetPre = 0;
            } if(KVlistOffsetPre > parsedTreeMapData.size() - terminalSize.getRows()) {
                KVlistOffsetPre = parsedTreeMapData.size() - terminalSize.getRows();
            }

            KVlistOffset = KVlistOffsetPre;

            AttributedStyle style = AttributedStyle.DEFAULT;
            lineText.setLength(0);
            lineText.styled(style.foreground(AttributedStyle.YELLOW), "-<");
            lineText.styled(style.foreground(AttributedStyle.RED), file.getValue(cursorUD + KVlistOffset));
            lineText.styled(style.foreground(AttributedStyle.YELLOW), ">-");

            screenDisplay(false);
            terminal.puts(Capability.cursor_address, 0,0);
            writer.println(parsedTreeMapData.get(KVlistOffset)); // this fixes the display box not printing this line... for some reason
            //terminal.puts(Capability.cursor_address, 0,0);
            //writer.print("Up/Down " + cursorUD + ", Left/Right " + cursorLR + ", PreOffset " + KVlistOffsetPre + ", Offset " + KVlistOffset + " ");
            terminal.puts(Capability.cursor_address, cursorUD, cursorLR);

            terminal.flush();
        }
    }

//    public void inputPrompt() {
//        String prompt = "Path or directory: ";
//
//        writer.println("Input filepath of a .LANG formatted file or a folder containing such files. Type \"Done\" to complete file input.");
//        writer.flush();
//        String input = reader.readLine(prompt);
//
//        while(!(input.equalsIgnoreCase("done") || input.equalsIgnoreCase("y"))) {
//            langFileAccessor.populateCollection(input.replaceAll("\"",""));
//            writer.println();
//            writer.println("Input filepath of a .LANG formatted file or a folder containing such files. Type \"Done\" to complete file input.");
//            writer.flush();
//            input = reader.readLine(prompt);
//        }
//    }

//    public void fileSelect() {
//        int i = 0;
//        int input;
//        for(; i < langFileAccessor.getFileNames().length; i++) {
//            writer.println(i + ") " + langFileAccessor.getFileNames()[i]);
//        }
//        writer.println();
//        writer.flush();
//        try {
//            input = Integer.parseInt(reader.readLine("Enter a number between " + 0 + " and " + (i - 1) + " to load a file: "));
//        } catch(NumberFormatException e) {
//            clearScreen();
//            terminal.puts(Capability.cursor_address, 8, 0);
//            writer.println("Must be an integer!");
//            terminal.puts(Capability.cursor_address, 0, 0);
//            writer.flush();
//            fileSelect();
//            return;
//        }
//        if(input < 0 || input > i) {
//            clearScreen();
//            terminal.puts(Capability.cursor_address, 8, 0);
//            writer.println("Must be within range!");
//            terminal.puts(Capability.cursor_address, 0, 0);
//            writer.flush();
//            fileSelect();
//            return;
//        }
//        file = langFileAccessor.loadFile(langFileAccessor.getFileNames()[input]);
//    }

    private List<AttributedString> reparser(int delimiterPos, List<String> keys, List<String> values) {
        List<AttributedString> reparseOutput = new ArrayList<>();
        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < keys.size(); i++) {
            //test code
            //temp.append(i);
            if(delimiterPos > 0) {
                temp.append(keys.get(i).substring(0, Math.min(keys.get(i).length(), delimiterPos - 1)));
            }
            temp.append(" ");

                if (temp.length() % 2 == 1) {
                    temp.append(" ");
                }
                temp.repeat("- ", Math.max((delimiterPos - temp.length() + 1) / 2, 0));

            temp.append(values.get(i));

            reparseOutput.add(new AttributedString(temp));

            temp.setLength(0);
        }
        return reparseOutput;
    }

    private void screenDisplay(boolean setup) {
        if(setup) {
            KVseparatorPos = terminalSize.getColumns()/4;
            KVlistOffset = 0;
        }

        keyValueBox.resize(terminalSize.getRows(),terminalSize.getColumns() + 1); //resizing fixes the god awful Display class issue.
        keyValueBox.resize(terminalSize.getRows(),terminalSize.getColumns());

        parsedTreeMapData = reparser(KVseparatorPos, (List<String>) file.getTreeMap().getFirst(), (List<String>) file.getTreeMap().getLast());
        terminal.puts(InfoCmp.Capability.clear_screen);

        keyValueBox.update(parsedTreeMapData.subList(Math.max(0, KVlistOffset),Math.min(KVlistOffset + terminalSize.getRows(), parsedTreeMapData.size())),0);

        if(setup) {
            terminal.puts(Capability.cursor_address, 0, KVseparatorPos + 1);
            cursorLR = KVseparatorPos + 1;
        }

        terminal.puts(Capability.save_cursor);
        if(cursorUD > 1) {
            terminal.puts(Capability.cursor_address, cursorUD - 1, KVseparatorPos - 1);
        } else {
            terminal.puts(Capability.cursor_address, cursorUD + 1, KVseparatorPos - 1);
        }
        terminal.flush();
        //lineText = AttributedString.fromAnsi(file.getValue(KVlistOffset + cursorUD));
        //writer.println(" " + lineText + " ");
        lineText.println(terminal);

        terminal.puts(Capability.restore_cursor);

        writer.flush();
    }
}

package org.wildedit;

public class Main {
    public static void main(String[] args) {
        LangEditor.main(args);
    }
}

// todo... improve window layout
// it looks ass
// RELATED: improve how I create the items (textfields, labels, etc) populating the editing window

// todo... save inputs in many input fields
// export window, jar paths, etc

// todo... implement menu bar
// -file, help, edit tabs
// RELATED: implement re-importing language packs and merging with jar to allow updating to newer versions
// (basically update existing language packs)

// todo, misc...
// does it work on other platforms? (macos)
// x any memory leaks? (what happens when I "open" and "close" files?) Seemingly no....
// x make FXwindow main branch
// cleanup repo
// hotkeys to format text or a formatting bar
// preview window (would be neat if I could hook into the provided game jar to use the native thing for this!)
// organize source code better? Is it sensible?
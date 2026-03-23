package org.wildedit;

public class Main {
    public static void main(String[] args) {
        LangEditor.main(args);
    }
}

// todo... reinforce jar picker, ie:
// x prevent picking of non jar files
// x improve filtering for bta jars
// x determine what proper action is when previously a jar was selected and a new jar is selected (prompt to save and clear?
// prevent such action? do older jars have translation keys that may want to be used to make multi-version language packs?)
// x fix export being allowed after selecting non-bta jar (warning occurs, yet exporting becomes enabled)
// x make invalid jar warning not have a cancel option (why does it even have that?)

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

// todo... fix locales
// -use intermediate file path replacing any locale wherever a pack is still in development
// in the fileCollection mostly?
// make the region picker use locales.

// todo... optional id in export
// -generate from title if left blank and indicate leaving blank is an option

// todo... editable list of ppl for credits
// -change text field into expandable number of items... list ig
// -filter out commas or see if \ acts as an escape there so that no filtering is needed.

// todo... don't close on invalid manifest for export
// -as it sounds, which is sounding less and less clear...
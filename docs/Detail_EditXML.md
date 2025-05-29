## File: `EditXML.java`

**General Purpose:** This file defines the `EditXML` class, which extends `TextEditor`. It provides a GUI window allowing the user to directly view and edit the XML file that defines the processor's architecture. It includes functionality to parse the edited XML and apply valid changes to the simulator.

**Class: `EditXML` (extends `TextEditor`, implements `ActionListener`)**

*   **Key Fields:**
    *   `Button butParse`: GUI button to trigger parsing of the edited XML.
    *   `Button butCancel`: GUI button to discard changes and close.
    *   `String oldDoc`: Stores the original text of the document when the editor was opened, used to check if actual changes were made.

*   **Constructor:**
    *   `public EditXML(ProcSim procSim)`: Initializes the editor (calls `super` with title "Edit Processor Architecture"), adds "Parse" and "Cancel" buttons to the `buttonPanel`. Sets up frame size and action listeners for buttons and menu items. Overrides default close operation to handle saving/parsing logic.

*   **GUI and Actions (`actionPerformed`):**
    *   `butCancel`: Calls `this.source.loadSim.newArch(true)` (which likely reloads the last valid architecture or a default) and then disposes the editor window.
    *   `butParse`:
        1.  Disables the `butCancel` button.
        2.  Saves the current editor content to the simulator's current XML path (`this.source.sim.path`).
        3.  Calls `doParse()`.
        4.  If parsing succeeds: displays a success message, then calls `this.source.loadSim.resetSim()` and `this.source.loadSim.updateSim()` to reinitialize and update the main simulator with the new architecture.
        5.  If parsing fails: displays an error message.
    *   `butDone` (from `TextEditor`):
        1.  Disables `butCancel`.
        2.  Saves the file to `this.source.sim.path`.
        3.  Calls `doParse()`.
        4.  If parsing succeeds: checks if `this.txtDoc.getText()` is different from `this.oldDoc`. Calls `this.source.loadSim.newArch(true)` if no changes, `this.source.loadSim.newArch(false)` if changes were made. Then disposes the editor.
        5.  If parsing fails: displays an error message prompting the user to fix issues.
    *   `fmLoad`: Calls `openXML()` to load a different XML architecture file into the editor.
    *   `fmSaveAs`: Calls `saveAs(false)` to save the current content to a new XML file.
    *   `fmSave`: Disables `butCancel` and saves the current content to `this.source.sim.path`.
    *   `fmNew`: Prompts to save existing changes. Then, sets the editor text to a default XML template for a new architecture and calls `saveAs(true)` to save it under a new name (defaulting to "newSim.xml").
    *   **Window Closing Event**: A `WindowAdapter` handles this. If `butCancel` is enabled (meaning no successful parse attempt has locked in changes), it calls `this.source.loadSim.newArch(true)` and disposes. Otherwise (if changes were made), it saves, calls `doParse()`, and if successful, calls `this.source.loadSim.newArch(false)` and disposes. If parsing fails on close, an error is shown.

*   **Parsing Logic:**
    *   `public boolean doParse()`: Delegates the actual parsing to the `ParseSimXML` instance associated with `LoadSim` by calling `this.source.loadSim.parser.startParse(this.source.sim)`.

*   **File Operations:**
    *   `public boolean saveAs(boolean z)`: Handles saving the editor content to a new file. If `z` is true (from `fmNew`), suggests "newSim.xml". Updates `this.source.sim.path`.
    *   `public void openXML()`: Opens a file dialog for XML files, loads the selected file's content into the editor using `openFile()`, and updates `this.source.sim.path`.

*   **Initialization:**
    *   `public void starting()`: Called to set up the editor when it's first displayed. It loads the content of the current simulator's XML file (`this.source.sim.path`) using `openFile()`, enables the `butCancel` button, and stores the initial document text in `this.oldDoc`.

**Relationship to other files:**

*   `TextEditor.java`: Base class, provides core text editing features, undo/redo, and probably basic file I/O helpers like `openFile`, `saveFile`, `fileDialog`.
*   `ProcSim.java`: The main application class, `EditXML` accesses it via `this.source` (which is a `ProcSim` instance) to get to `LoadSim`, `Simulator`, and `Console`.
*   `LoadSim.java`: Interacts with `LoadSim` to trigger re-parsing (`parser.startParse`), simulator reset/update (`resetSim`, `updateSim`), and reloading of architecture (`newArch`).
*   `Simulator.java`: The `sim.path` is used as the source/target for XML loading/saving.
*   `ParseSimXML.java`: `doParse()` delegates to this class to perform the actual XML parsing and validation.
*   `MsgBox.java`: Used for displaying dialog messages.

**Summary:** `EditXML.java` provides a dedicated text editor for the simulator's XML architecture files. It allows users to modify the XML directly and then attempt to parse and apply these changes to the live simulator instance. It manages the workflow of editing, saving, parsing, and updating the simulator or reverting changes if parsing fails or is cancelled. It relies on `TextEditor` for basic editing and `ParseSimXML` for the complex parsing logic. 
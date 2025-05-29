## File: `TextEditor.java`

**General Purpose:** This abstract class serves as a base for creating text editor windows within the ProcSim application. It extends `JFrame` and provides common text editing functionalities like file operations (new, open, save, save as), edit operations (undo, copy, paste, find), a menu bar, and a basic button panel.

**Class: `TextEditor` (abstract, extends `JFrame`, implements `ActionListener`)**

*   **Key GUI Components:**
    *   `JTextArea txtDoc`: The main text editing area.
    *   `JScrollPane jpan`: Wraps `txtDoc` to provide scrolling.
    *   `Panel buttonPanel`: A panel at the bottom, typically for buttons like "Done".
    *   `Button butDone`: A standard "Done" button.
    *   `MenuBar menuBar`: Contains File, Edit, and Help menus.
    *   `MenuItem`s: For New, Open, Save, Save As, Close (File menu); Undo, Copy, Paste, Find, Find Next (Edit menu); Help (Help menu).

*   **Core Functionality:**
    *   **Constructors:** `TextEditor(ProcSim procSim, String str)` and `TextEditor(ProcSim procSim, String str, boolean z)`. The boolean `z` in `setupEditor` controls whether the full menu bar is shown or a simpler setup (likely just the `butDone` button for dialog-like editors).
    *   **Undo/Redo:** Implemented using `javax.swing.undo.UndoManager`. An `UndoableEditListener` on `txtDoc.getDocument()` populates the manager. Ctrl+Z and Ctrl+Y are mapped for undo/redo.
    *   **Find/Find Next:** Provides functionality to search for text within `txtDoc`. `findText()` opens a dialog for the search string, `findTextNext()` (and F3) finds subsequent occurrences.
    *   **Action Handling (`actionPerformed`):** Manages events from menu items and buttons, delegating to specific methods like `txtDoc.copy()`, `txtDoc.paste()`, `findText()`, `source.showHelp()`, or `dispose()`.

*   **File Operations:**
    *   `openFile(String path)`: Reads the content of the specified file into `txtDoc` using `BufferedReader` and `FileReader`. Updates the window title.
    *   `saveFile(String path)`: Saves the content of `txtDoc` to the specified file using `BufferedWriter` and `FileWriter`. Updates the window title.
    *   `fileDialog(boolean saveMode, Frame parent, String title, String defaultDir, String defaultFile)`: A utility method to display a `java.awt.FileDialog` for opening or saving files. Returns the selected file path.

*   **Setup Methods:**
    *   `setupEditor(...)`: Private method called by constructors to initialize GUI components, menus, action listeners, and undo/redo functionality.
    *   `setupFrame(double widthScale, double heightScale)`: Configures the main panel, text area properties (font, tabs), scroll pane, packs the frame, and then resizes and centers it on the screen based on the provided scales.

**Relationship to other files:**

*   `ProcSim.java`: `TextEditor` holds a reference to the main `ProcSim` instance (`this.source`) for actions like `showHelp()`.
*   `Assembly.java`: Extends `TextEditor` to create the assembly code editor.
*   `EditXML.java`: Extends `TextEditor` to create the XML architecture editor.
*   `ProcFunc.java`: `fileDialog` uses `ProcFunc.fileSep()` to correctly construct file paths.
*   Swing/AWT: Heavily uses Swing (`JFrame`, `JTextArea`, `JScrollPane`, `UndoManager`, `AbstractAction`) and AWT (`Button`, `Panel`, `MenuBar`, `MenuItem`, `FileDialog`) for its GUI.

**Summary:** `TextEditor.java` is an abstract base class that encapsulates the common features of a text editor window, including a text area, menu bar with standard file and edit operations, undo/redo support, and find functionality. Subclasses like `Assembly` and `EditXML` inherit these features and add their specific behaviors and buttons. 
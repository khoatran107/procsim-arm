## File: `ProcSim.java`

**General Purpose:** This is the main class for the MIPS processor simulator application. It extends `java.awt.Frame` and serves as the entry point and central coordinator for all other major components of the simulator, including the assembly editor, architecture loader, simulation view, and console output.

**Class: `ProcSim` (extends `Frame`, implements `ActionListener`)**

*   **Key Static Fields:**
    *   `boolean CONSOLE = true`: If true, the custom console window is enabled.
    *   `boolean SHOWOUT = false`: If true, regular output messages (`System.out`) are shown in the console (and potentially printed to the actual standard out if `HOLDOUT` is false).
    *   `boolean SHOWERR = false`: If true, error messages (`System.err`) are shown in the console.
    *   `boolean HOLDOUT = false`: If true, it seems to prevent `System.out` messages from being printed even if `SHOWOUT` is true (used by `ViewSim`'s "Instant" speed).

*   **Key Instance Fields:**
    *   `Simulator sim`: Instance of the core simulator logic and data.
    *   `Assembly assembly`: Instance of the MIPS assembly code editor.
    *   `LoadSim loadSim`: Instance of the processor architecture editor/loader.
    *   `DiagCanvas diagCanvas`: The shared canvas for drawing diagrams, used by `LoadSim` and `ViewSim`.
    *   `ViewSim viewSim`: Instance of the simulation visualization window.
    *   `Console console`: Instance of the console window for `System.out`/`err`.
    *   `static HelpViewer helpViewer`: A static instance of the help window.
    *   `Button butAddCode`, `butLoadSim`, `butViewSim`, `butConsole`, `butExit`: Main GUI buttons for core application functions.
    *   `Panel mainPanel`, `buttonPanel`: Panels for organizing the main window GUI.
    *   `Label lblAssem`, `lblSim`, `lblSimName`: Labels, likely for status information (though not explicitly shown to be updated in the provided code).

*   **Inner Class: `HelpViewer` (extends `TextEditor`)**
    *   A specialized `TextEditor` used to display help information.
    *   Constructor: `HelpViewer(ProcSim procSim)`
        *   Calls `super(procSim, "Help - Readme", true)`.
        *   Enables line wrap and word wrap for its `txtDoc`.
        *   Calls `setupFrame(0.5d, 0.6d)` to set its size.
        *   Calls `openFile("README.txt")` to load the content of `README.txt`.
        *   Makes its `txtDoc` non-editable.

*   **Constructors:**
    *   `public ProcSim()`: Calls `setupFrame(false)`.
    *   `public ProcSim(boolean z)`: Calls `setupFrame(z)`. The `boolean z` is typically derived from the "-noout" command-line argument.

*   **Initialization (`private void setupFrame(boolean z)`):**
    1.  Sets the application icon and attempts to set the Windows Look and Feel.
    2.  Initializes the `console` (if `CONSOLE` flag is true and not disabled by command line).
    3.  The `z` parameter (from "-noout" arg) influences the initial state of `SHOWOUT`.
    4.  Displays a modal "Loading Simulator... Please Wait..." `Dialog`.
    5.  Sets up the main `ProcSim` frame (size, title "ProcSim v2.0 - by James Garton", location, window closing behavior to exit the application).
    6.  Creates and arranges GUI elements (buttons, labels, panels) with specific fonts and colors.
    7.  **Instantiates Core Components:**
        *   `this.assembly = new Assembly(this);` (then opens "sample just R-Format.asm" and parses it).
        *   `this.diagCanvas = new DiagCanvas(this);`
        *   `this.sim = new Simulator(this);`
        *   `this.loadSim = new LoadSim(this);` (Its constructor calls `parser.startParse()` which loads the default XML).
        *   `this.viewSim = new ViewSim(this.sim);`
    8.  Makes the main `ProcSim` frame visible.
    9.  Disposes of the loading dialog.
    10. Sets `this.loadSim.parser.starting = false;` (likely to enable full functionality of the parser after initial load).

*   **Event Handling (`actionPerformed(ActionEvent actionEvent)`):**
    *   `butExit`: Calls `System.exit(0)`.
    *   `butAddCode`: If `viewSim` is not visible, shows the `assembly` editor window.
    *   `butLoadSim`: If `viewSim` is not visible, shows the `loadSim` architecture editor window. Calls `loadSim.setupLoadSim()` on first view.
    *   `butViewSim`: If `loadSim` and `assembly` editors are not visible, shows the `viewSim` simulation window. Calls `viewSim.setupSim()`.
    *   `butConsole`: Toggles the visibility of the `console.frame`. Creates the console if it's null.

*   **Main Method (`public static void main(String[] strArr)`):**
    1.  Prints information about optional command-line parameters (`-noconsole`, `-noout`).
    2.  Prints the OS name and recommends Windows.
    3.  Parses command-line arguments:
        *   If "-noconsole", sets `CONSOLE = false`.
        *   If "-noout", sets a local `boolean z = true`.
    4.  Creates a new `ProcSim(z)` instance, starting the application.

*   **Static Output Methods (Logging Wrappers):**
    *   `public static void out(String str)`: Prints `str` to `System.out` if `SHOWOUT` is true and `HOLDOUT` is false.
    *   `public static void outErr(String str)`: Prints `str` to `System.err` if `SHOWERR` is true.
    *   `public static void outLine(String str)`: Prints `str` to `System.out` (without newline) under same conditions as `out()`.
    *   `public static void outErrLine(String str)`: Prints `str` to `System.err` (without newline) under same conditions as `outErr()`, though `HOLDOUT` check is missing here compared to `outErr` in typical use.

*   **Other Methods:**
    *   `getCenterX()`, `getCenterY()`: Return center coordinates of the `ProcSim` frame.
    *   `showHelp()`: Creates (if null) and shows the `helpViewer` window (which loads `README.txt`).

**Relationship to other files:**

*   Acts as the central hub, creating and holding instances of almost all other major GUI and simulation logic classes (`Simulator`, `Assembly`, `LoadSim`, `DiagCanvas`, `ViewSim`, `Console`).
*   Manages the primary application window and the launching of other tool windows.
*   Provides static flags (`CONSOLE`, `SHOWOUT`, `SHOWERR`, `HOLDOUT`) and logging methods (`out`, `outErr`) used by many other classes for console output control.
*   `TextEditor.java`: The `HelpViewer` inner class extends `TextEditor`.
*   `MsgBox.java`: Used to display simple dialog messages to the user.

**Summary:** `ProcSim.java` is the entry point and main container for the MIPS simulator application. It initializes the primary GUI, sets up global configurations (like console visibility and output verbosity via static flags and command-line arguments), and instantiates all core functional components. It provides the main menu/buttons for users to navigate between editing assembly code, designing the processor architecture, running the simulation, viewing help, and managing console output. It essentially orchestrates the interactions between the user and the different modules of the simulator. 
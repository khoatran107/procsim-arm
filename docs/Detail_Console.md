## File: `Console.java`

**General Purpose:** This file defines the `Console` class, which creates a separate GUI window to display messages sent to `System.out` and `System.err`. This is useful for debugging and logging information from the simulator without cluttering the main simulation or editor windows.

**Class: `Console` (extends `WindowAdapter`, implements `WindowListener`, `Runnable`, `ActionListener`)**

*   **Key Fields:**
    *   `JFrame frame`: The main window for the console.
    *   `JTextArea textArea`: Displays `System.out` messages.
    *   `JTextArea textAreaErr`: Displays `System.err` messages.
    *   `JScrollPane scroll1`, `scroll2`: Scroll panes for `textArea` and `textAreaErr` respectively.
    *   `Thread reader`, `reader2`: Threads responsible for reading from the piped streams connected to `System.out` and `System.err`.
    *   `boolean quit`: Flag to signal the reader threads to terminate.
    *   `ProcSim source`: Reference to the main `ProcSim` application instance.
    *   `JPanel panel`: Holds the two scroll panes (`scroll1`, `scroll2`) in a `GridLayout`.
    *   `JPanel btnPanel`: Holds the control buttons.
    *   `JButton butOut`, `butErr`: Buttons to toggle the visibility of the `System.out` and `System.err` text areas.
    *   `PipedInputStream inStream`, `inStream2`: Piped input streams that receive data from `System.out` and `System.err` respectively.

*   **Constructor: `Console(ProcSim procSim)`**
    1.  Stores the `procSim` reference.
    2.  Initializes static flags `ProcSim.SHOWOUT` and `ProcSim.SHOWERR` to `true`.
    3.  Creates the `JFrame` ("ProcSim Console") and sets its size and position.
    4.  Initializes `textArea` and `textAreaErr` (non-editable, tab size 4).
    5.  Adds `butOut` and `butErr` to `btnPanel` and sets up their action listeners.
    6.  Wraps text areas in `JScrollPane`s and adds them to the main `panel`.
    7.  Adds `panel` (Center) and `btnPanel` (South) to the frame's content pane.
    8.  Sets `textAreaErr` to line wrap.
    9.  Adds `this` as a `WindowListener` to the frame.
    10. **Redirects System Streams:**
        *   Creates a `PipedOutputStream` connected to `inStream` and sets `System.setOut()` to a `PrintStream` wrapping this output stream.
        *   Creates a `PipedOutputStream` connected to `inStream2` and sets `System.setErr()` to a `PrintStream` wrapping this output stream.
        *   Catches and displays exceptions if redirection fails.
    11. Initializes `quit = false`.
    12. Creates and starts two daemon threads (`reader` and `reader2`), both executing the `run()` method of this `Console` instance.

*   **GUI Management & Event Handling:**
    *   `actionPerformed(ActionEvent actionEvent)`:
        *   If `butOut` is clicked: Toggles `ProcSim.SHOWOUT`, calls `updatePanels()`, and prints a message to `System.out`.
        *   If `butErr` is clicked: Toggles `ProcSim.SHOWERR`, calls `updatePanels()`, and prints a message to `System.err`.
    *   `setupPanels()`: Adds `scroll1` (for `System.out`) to `panel` if `ProcSim.SHOWOUT` is true. Adds `scroll2` (for `System.err`) to `panel` if `ProcSim.SHOWERR` is true. Repaints and shows the frame.
    *   `updatePanels()`: Calls `panel.removeAll()` and then `setupPanels()` to refresh the displayed text areas based on `ProcSim.SHOWOUT` and `ProcSim.SHOWERR` flags.
    *   `windowClosing(WindowEvent windowEvent)`: Hides and disposes the console frame.
    *   `windowClosed(WindowEvent windowEvent)`: Sets `quit = true`, notifies threads, attempts to join `reader` and `reader2` threads, and closes `inStream` and `inStream2`.

*   **Thread Logic (`run()` method - synchronized):**
    *   The method contains two `while` loops, one for each reader thread (`Thread.currentThread() == this.reader` or `this.reader2`).
    *   Inside each loop:
        1.  The thread `wait(400L)`s (pauses for 400 milliseconds).
        2.  Checks if its respective `PipedInputStream` (`inStream` or `inStream2`) has data available (`available() != 0`).
        3.  If data is available, calls `readLine()` to read the data.
        4.  Appends the read string to the corresponding `JTextArea` (`textArea` or `textAreaErr`) and scrolls to the bottom (`setCaretPosition`).
        5.  If `quit` flag is true, the thread exits the loop and returns.
    *   Handles `InterruptedException` during `wait` and `IOException` during stream reading.

*   **Stream Reading:**
    *   `readLine(PipedInputStream pipedInputStream)`: Reads all available bytes from the given `PipedInputStream`. It concatenates chunks of bytes into a string until no more bytes are available or a newline sequence is encountered (though the current implementation reads all available bytes regardless of newline within the loop and breaks if a newline is at the end or if no data is available). Returns the accumulated string.

**Relationship to other files:**

*   `ProcSim.java`: `Console` is instantiated by `ProcSim` and holds a reference to it. It also uses static boolean flags `ProcSim.SHOWOUT` and `ProcSim.SHOWERR` to control visibility of output/error panes. The `ProcSim.out()` and `ProcSim.outErr()` methods are likely wrappers around `System.out.println()` and `System.err.println()` respectively, so their output gets captured by this console.
*   Java AWT/Swing: Heavily uses these for GUI components (`JFrame`, `JTextArea`, `JScrollPane`, `JPanel`, `JButton`) and event handling.
*   Java IO: Uses `PipedInputStream`, `PipedOutputStream`, `PrintStream` for redirecting and reading system output streams.

**Summary:** The `Console.java` file provides a dedicated window for displaying `System.out` and `System.err` streams from the application. It achieves this by redirecting the standard output and error streams to `PipedInputStream`s, which are then read by separate threads. These threads append the incoming text to `JTextArea` components in a Swing `JFrame`. Users can toggle the visibility of the standard output and error message panes using buttons. This class is a useful utility for developers to see log messages and error traces without interfering with the main application GUI. 
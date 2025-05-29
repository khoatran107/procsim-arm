## File: `ViewSim.java`

**General Purpose:** This file defines the `ViewSim` class, which is the main window for visualizing and controlling the MIPS processor simulation. It extends `java.awt.Frame` and orchestrates the simulation flow, user interactions, and the display of processor state (registers, memory) and animations on the `DiagCanvas`.

**Class: `ViewSim` (extends `Frame`, implements `ActionListener`, `ChangeListener`, `ItemListener`)**

*   **Key Fields:**
    *   `Simulator sim`: Reference to the core `Simulator` object containing the processor state and architecture.
    *   `DiagCanvas dCanvas`: The canvas where the processor diagram and animations are rendered.
    *   `JScrollPane jsp`: Contains the `dCanvas`.
    *   `Frame controlFrame`: A separate window for simulation controls (start, pause, step, speed).
    *   `PComponent animComp`: The currently active component being processed or animated.
    *   `CompFrame regFrame`, `mainMemFrame`, `instrMemFrame`: Separate frames to display register, main memory, and instruction memory contents, respectively (using `CompFrame` class, not detailed here but likely a JTable-based view).
    *   `JSlider speedSlider`: GUI slider in `controlFrame` to adjust animation speed.
    *   Various `MenuItem`s (`fmLoadAss`, `fmLoad`, `fmClose`, `fmHelp`, `fmTrans`, `fmShowBusNames`) for file operations, settings, and help.
    *   `Vector<AnimationListItem> animationList`: A list of `AnimationListItem` objects, where each item groups buses that should be animated concurrently in one animation step.
    *   Numerous `Button`s (`butClose`, `butStart`, `butPause`, `butRestart`, `butStep`, `butStepIntr`, `butShowRegisters`, etc.) for controlling simulation and display.
    *   `MenuBar menuBar`, `Menu fileMenu`, `Menu settingsMenu`, `Menu helpMenu`: Standard AWT menu components.
    *   `Label lblSimName`: Displays the name of the loaded simulation.
    *   `StatusBar statusBar`: An inner class panel at the bottom to display information about the current component.
    *   `boolean step`: Flag to indicate if the simulation is in step-by-step mode.

*   **Inner Classes:**
    *   `mousePressHandler (extends MouseAdapter)`: Handles mouse clicks on the `DiagCanvas`. When a component is clicked, it sets `dCanvas.animThread.currentComp` to that component, which updates the `statusBar`.
    *   `AnimationListItem`: A simple data structure containing an array `bus[]` (indices of `DiagBus` objects) and `numBuses` to indicate how many buses are part of this animation item. This allows grouping animations that should occur simultaneously.
    *   `StatusBar (extends JPanel)`: A custom panel that displays the `description` of the `PComponent` currently focused on by `dCanvas.animThread.currentComp`.

*   **Constructor: `ViewSim(Simulator simulator)`**
    *   Initializes references to `sim` and `dCanvas`.
    *   Sets up the main frame size, location, and window closing behavior (calls `cleanupViewSim()`).
    *   Resets simulator memory and registers.
    *   Calls `setupFrame()` and `setupControlFrame()` to initialize GUI elements.
    *   Sets `dCanvas.viewSim = this` to link the canvas back to this view.
    *   Initializes all menu items, action listeners, and menu structure.

*   **GUI and Event Handling:**
    *   `actionPerformed(ActionEvent actionEvent)`: Handles all button clicks and menu item selections.
        *   Simulation Control: Start, Pause/Resume, Stop (Restart), Step.
        *   Speed Control: Super speed, Instant speed toggles.
        *   Display Windows: Show/hide `CompFrame`s for Registers, Main Memory, Instruction Memory.
        *   File Operations: Load Simulation (XML), Load Assembly, Close Simulation.
        *   Help: Shows help dialog.
    *   `itemStateChanged(ItemEvent itemEvent)`: Handles `CheckboxMenuItem` state changes for "Transparency" and "Show bus names" on the `DiagCanvas`.
    *   `stateChanged(ChangeEvent changeEvent)`: Handles changes from the `speedSlider` to update `dCanvas.animThread.speed`.
    *   Window Closing: A `WindowAdapter` calls `cleanupViewSim()` and disposes of the frame.

*   **Core Simulation Flow & Animation Management:**
    *   `startNewAnim()`: Called when starting a new execution. It clears `animationList`, processes assembler directives (from `sim.source.assembly`) to initialize registers, calls `setTables()` to update display frames, and then identifies initial "start" components and their output buses to create the first `AnimationListItem`. Finally, it calls `dCanvas.animate()` to kick off the animation thread.
    *   `nextCompAnim()`: This is the heart of the simulation advancement logic. It's called by `AnimThrd` when a set of animations completes.
        1.  Calls `findNextComp()` to determine the next `PComponent` (`this.animComp`) ready to execute.
        2.  If a component is found and it's a start component with `sim.resetEveryRound` true, calls `resetAll()` on it.
        3.  Calls `this.animComp.doOps()` to execute the component's defined operations. This updates `ProcBus` values.
        4.  Calls `setTables()` to refresh register/memory views.
        5.  Clears `newVal` and `animated` flags for input buses to the component that just executed.
        6.  Rebuilds `this.animationList`: It iterates through all `DiagBus`es. If a bus has `newVal = true` (meaning its underlying `ProcBus` value changed) and `animated = false`, it's added to an `AnimationListItem`. Buses driven by the same output component are grouped into the same `AnimationListItem` to animate together.
        7.  Returns `true` if further animation steps are generated, `false` otherwise (e.g., end of execution or error).
    *   `findNextComp()` (and its overload): Implements the logic to select the next component to execute. It searches for components whose input buses have their `newVal` flag set (indicating data has arrived) and `doneAnimOnce` (animation completed for that input), or components with met optional input conditions.
    *   `endOfExectution()`: Called when the simulation finishes, displays a message, and stops animations.

*   **Setup and Cleanup:**
    *   `setupFrame()`: Initializes the main `ViewSim` frame layout (adds `jsp` for `dCanvas`, `lblSimName`, `statusBar`).
    *   `setupControlFrame()`: Creates and configures the separate "Control Panel" `Frame` with all its buttons and the speed slider.
    *   `setupSim()`: Configures the `dCanvas` (adds mouse listener, sets animation flags), makes `controlFrame` visible, shows register/instruction memory frames, updates labels, and calculates canvas size.
    *   `cleanupViewSim()`: Called when closing. Disposes of `controlFrame` and any `CompFrame`s, resets memory/registers, and removes `dCanvas` from `jsp`.

*   **State Display (`CompFrame` Management):**
    *   `showRegisters()`, `showMainMem()`, `showIntrMem()`: These methods create (if they don't exist) and make visible the respective `CompFrame` windows. They always call `setTables()` to ensure data is current.
    *   `setTables()`: Populates the data models for the `regFrame`, `mainMemFrame`, and `instrMemFrame` tables.
        *   For registers: Displays binary value, register name, slimmed binary, and decimal value.
        *   For main memory: Displays byte address, word address (for 4-byte aligned), byte value (decimal), and word value (decimal).
        *   For instruction memory: Displays instruction address (binary and decimal), the assembly instruction string or its machine code, and comments.
        *   All three can hide empty/zero rows based on a `hideEmpty` flag in `CompFrame`.

*   **Helper Methods:**
    *   `changePauseBut()`: Toggles the label of the pause button ("Pause"/"Resume").
    *   `resetAll(PComponent pComponent)`: Resets animation flags for buses not connected as input to `pComponent`.
    *   `isAnyInputBuses(PComponent pComponent)`: Checks if a component has any input buses.
    *   `findDiagBus(DiagBus diagBus)`: Returns the index of a `DiagBus` in `dCanvas.buses`.

**Relationship to other files:**

*   `Simulator.java`: `ViewSim` uses the `Simulator` instance to access processor state (registers, memory, components, buses) and architecture details.
*   `DiagCanvas.java`: `ViewSim` displays the simulation on the `dCanvas` and interacts with its `animThread` to control animations.
*   `AnimThrd.java`: `ViewSim` provides `animationList` to `AnimThrd` and calls methods on it to control the animation lifecycle (start, pause, speed, step).
*   `PComponent.java`: `ViewSim` triggers `PComponent.doOps()` to execute component logic and uses component properties for display.
*   `DiagBus.java` & `ProcBus.java`: `ViewSim` inspects `DiagBus` flags (`newVal`, `animated`) and `ProcBus` properties (`doneAnimOnce`, `optional`) to manage animation flow and determine next components.
*   `CompFrame.java` (not analyzed yet, but inferred): Used to display tabular data for registers, main memory, and instruction memory.
*   `Assembly.java`: `ViewSim` accesses `sim.source.assembly` to get directives and instruction details for display in `instrMemFrame`.
*   `Functions.java`, `ProcFunc.java`: Used for data conversion (binary/decimal, slimming) when populating display tables.
*   `MsgBox.java`: Used for displaying messages to the user.

**Summary:** `ViewSim.java` is the primary user interface for running and observing the MIPS simulation. It manages the overall simulation loop by determining which components should execute (`nextCompAnim`), preparing animation data for `AnimThrd`, and providing GUI controls for starting, stopping, stepping, and adjusting the speed of the simulation. It also handles the display of critical processor states (registers, memory, instructions) in separate, dynamically updated table views (`CompFrame`s) and provides visual feedback via the `DiagCanvas` and a status bar. It acts as the central coordinator between the user, the simulation logic (`Simulator`, `PComponent`), and the visual animation engine (`DiagCanvas`, `AnimThrd`). 
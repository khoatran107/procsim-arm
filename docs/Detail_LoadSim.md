## File: `LoadSim.java`

**General Purpose:** This file defines the `LoadSim` class, which provides a graphical user interface (GUI) for visually creating, editing, and managing the architecture of the simulated processor. It extends `java.awt.Frame` and implements various listeners for user interaction.

**Class: `LoadSim` (extends `Frame`, implements `ActionListener`, `ItemListener`, `TextListener`, `MouseMotionListener`)**

*   **Key GUI Components & Layout:**
    *   `DiagCanvas diagCanvas`: A custom canvas (likely where the processor components and buses are drawn and manipulated) placed in the center using a `JScrollPane`.
    *   `Panel sidePanel`: A panel on the side (likely West or East) with `GridBagLayout` containing controls:
        *   `List compList`: To select processor components.
        *   `List busList`: To select buses for editing their paths.
        *   `List connectedList`: To show buses connected to a selected component.
        *   `NumericTextField txtWidth`, `txtHeight`: To set dimensions of a selected component.
        *   `Label txtOutName`, `txtInName`, `txtDoneBus`: To display bus information.
        *   Various `Button`s: "Edit Architecture..." (opens `EditXML`), "Clear" (clears bus paths), "Done" (saves and closes), "Undo" (removes last bus point), "Remove ProcBus" (clears selected bus path), "Remove Connected Buses", "Reset Connected Names", "Reset All ProcBus Names".
        *   `Checkbox chkHideBusName`: To toggle visibility of a specific bus name.
        *   `Label lblChosenSim`, `lblChosenSimFile`: Display current architecture name and file.
    *   **Menu Bar:**
        *   File Menu: New Sim, Open, Save, Save As, Reload/Parse Sim, Close.
        *   Settings Menu: Snap to Grid, Show bus names, Stick Connected Buses (CheckboxMenuItems).
        *   Help Menu: Help...

*   **Core Functionality & State:**
    *   **Initialization:** The constructor takes `ProcSim` source, sets up the frame, menus, side panel, and initializes `diagCanvas`. Critically, it creates a `ParseSimXML` instance and calls `parser.startParse(this.source.sim)` to load the initial architecture from the XML file associated with the `Simulator`.
    *   **Editing Modes:** Boolean flags (`addingComponent`, `addingBus`, `addingConnectedBus`) manage whether the user is currently focused on placing/moving components, drawing bus paths, or selecting existing buses.
    *   **Graphical Interaction (Mouse Handlers):**
        *   `mouseMoved`: Handles hover effects (e.g., changing component borders for resizing).
        *   `mouseDragged`: Manages moving selected components (updates their x,y and calls `compMoved`), moving bus name labels, or adding points to a `DiagBus` path if `addingBus` is true.
        *   `mousePressHandler` (inner class): Handles `mousePressed` (initiating component selection/move, bus label move, or starting a new bus path) and `mouseReleased` (finalizing moves or bus path drawing, potentially calculating bus name positions).
    *   **Component and Bus Manipulation:**
        *   Component positions and sizes are modified interactively and stored in `PComponent` objects (which are part of `Simulator.comps`).
        *   `DiagBus` paths (a series of x,y points) are created/modified by mouse clicks/drags. These points are stored within each `DiagBus` object in `diagCanvas.buses`.
        *   Bus name labels (`DiagBus.PermText` objects: `busLabelIn`, `busLabelOut`) can be moved, and their positions and visibility are stored.
    *   **Settings:** "Snap to Grid", "Show bus names", "Stick Connected Buses" control drawing behavior and appearance.

*   **Saving and Loading Architecture (Interaction with XML):**
    *   **Loading:** When `LoadSim` is opened or "Reload/Parse Sim" is used, `ParseSimXML.startParse()` is called. `ParseSimXML` reads the logical structure. `LoadSim` then likely uses information embedded in the XML (if previously saved by `LoadSim`) or default placements to position components and draw buses on the `diagCanvas` via `updateSim()` and `diagCanvas.repaint()`.
    *   **Saving (`saveSim()` method):**
        1.  Calls `getSaveString()` to generate an XML representation of the current architecture **including the graphical layout**.
        2.  `getSaveString()` iterates through `Simulator.comps` and `diagCanvas.buses`.
        3.  For each `PComponent`, it writes its logical definition (name, description) and also its graphical properties (`x`, `y`, `width`, `height`) as XML attributes/elements.
        4.  For each `DiagBus` (which corresponds to a logical `ProcBus`), it finds the matching `<output>` tag in the XML structure being built. It then **embeds the bus path points** (`<point x="..." y="..."/>`) and the positions/visibility of `busLabelIn` and `busLabelOut` as new sub-elements or attributes within that `<output>` tag.
        5.  The resulting XML string (containing both logical structure and graphical layout) is written to the file specified by `sim.path`.
    *   `openSim()`, `saveAs()`: Provide file dialogs and manage file paths, eventually calling `parser.startParse()` for opening or `saveSim()` for saving.
    *   `newArch(boolean z)`: Handles switching architectures, either by reloading the current path or assuming the `Simulator` object has been updated (e.g., by `EditXML`).

*   **Synchronization & Updates:**
    *   `resetSim()`: Refreshes GUI lists (`compList`, `busList`) from the `Simulator` data.
    *   `updateSim()`: Repaints `diagCanvas` and recalculates its size.
    *   `compMoved(...)`: When a component is moved, this method updates the coordinates of connected `DiagBus` points to keep them attached, respecting the `stickConnectedBuses` setting.

**Relationship to other files:**

*   `ProcSim.java`: The main application class, `LoadSim` is part of it and interacts with `source.sim` (the `Simulator` instance) and `source.diagCanvas`.
*   `DiagCanvas.java`: The canvas where the visual editing happens. `LoadSim` directly manipulates its `buses` and `comps` (which are `DiagBus` and `PComponent` visual representations).
*   `ParseSimXML.java`: Used to parse the logical architecture from XML files. `LoadSim` then adds the graphical layout information on top of this structure.
*   `Simulator.java`: Holds the logical representation of the processor architecture (`comps`, `buses`, etc.).
*   `EditXML.java`: `LoadSim` can launch `EditXML` for direct textual editing of the XML.
*   `PComponent.java`, `DiagBus.java`, `ProcBus.java`: These objects are created, manipulated, and their properties (including graphical ones like position and path points) are managed by `LoadSim` and saved to XML.
*   `ProcFunc.java`: Used for file path manipulation.
*   `MsgBox.java`: Used for dialogs.

**Summary:** `LoadSim.java` is the visual architecting tool for the simulator. It allows users to graphically place components, draw connecting buses, and arrange labels. Its most crucial aspect is its ability to save this graphical layout information back into the XML definition file by embedding coordinates, points, and visibility states within the logical XML structure. This augmented XML can then be re-parsed by `ParseSimXML` and `LoadSim` to restore both the logical connections and the visual layout. It works in tandem with `DiagCanvas` for rendering and `ParseSimXML` for interpreting the base architecture. 
## File: `DiagBus.java`

**General Purpose:** This file defines the `DiagBus` class, which extends the abstract `Bus` class. It appears to be a bus specifically designed for diagnostic or visualization purposes within a GUI. It contains numerous fields related to drawing (coordinates, points), labels, animation, and a reference to an actual `ProcBus` that it likely represents or monitors.

**Class: `DiagBus` (extends `Bus`)**

*   **Inherited Fields (from `Bus`):**
    *   `int bits`: Width of the bus, set by constructor.
    *   `String outName`: Name of the output end, set by constructor.
    *   `String binaryValue`: Current binary value on the bus.
    *   `String strValue`: Current string representation of the value on the bus.

*   **Own Fields:**
    *   `int[] x = new int[100]`: Array to store x-coordinates for drawing the bus line.
    *   `int[] y = new int[100]`: Array to store y-coordinates for drawing.
    *   `int numPoints = 0`: Number of points defined for drawing the bus.
    *   `boolean doneBus = false`: Flag, possibly indicating if the bus drawing or setup is complete.
    *   `PComponent out`: The source `PComponent` from which this diagnostic bus originates.
    *   `PComponent in`: The destination `PComponent` to which this diagnostic bus goes.
    *   `String inText = "None"`: Text associated with the input end of the bus, likely a label.
    *   `PermText busLabelOut = new PermText()`: A `PermText` object for the label at the output end.
    *   `PermText busLabelIn = new PermText()`: A `PermText` object for the label at the input end.
    *   `boolean doneAnim = false`: Flag related to animation state.
    *   `ProcBus bus`: A reference to the actual `ProcBus` that this `DiagBus` is visualizing data from.
    *   `boolean newVal = false`: Flag indicating if there's a new value on the bus.
    *   `boolean animated = false`: Flag indicating if this bus is currently being animated.
    *   `boolean dontReset = false`: Flag to prevent reset, context unclear.
    *   `boolean customNamePos = false`: Flag indicating if the label position is custom.

*   **Constructors:**
    *   `DiagBus(String str, String str2, PComponent pComponent, PComponent pComponent2, int i, ProcBus procBus)`: Initializes the `DiagBus`. `str` is `outName`, `str2` is `inText`, `pComponent` is `out` source, `pComponent2` is `in` destination, `i` is `bits`, and `procBus` is the linked `ProcBus`.
    *   `DiagBus(String str, String str2, boolean z, PComponent pComponent, PComponent pComponent2, int i, ProcBus procBus)`: Similar to the first constructor, but with an additional boolean parameter `z` whose purpose is not immediately clear from the constructor code (it's not assigned to any field).

*   **Methods:**
    *   `public void resetPoints()`: Resets drawing points (`x`, `y`, `numPoints`), the `doneBus` flag, clears label strings in `busLabelOut` and `busLabelIn`, and resets `customNamePos`.

*   **Inner Class: `PermText`**
    *   `public String str`: The text string to be displayed.
    *   `public boolean moving = false`: Flag indicating if the text is currently moving (animated).
    *   `public boolean hidden = false`: Flag indicating if the text is hidden.
    *   `public int x`, `public int y`: Coordinates for the text.
    *   `public int width`, `public int height`: Dimensions for the text area.
    *   `public PermText()`: Default constructor.

**Relationship to other files:**

*   `Bus.java`: `DiagBus` extends `Bus`.
*   `PComponent.java`: Connects an `out` (`PComponent`) to an `in` (`PComponent`) for diagnostic visualization.
*   `ProcBus.java`: A `DiagBus` is typically associated with a `ProcBus` to visualize its data.
*   GUI classes (e.g., `DiagCanvas.java`): This class is heavily geared towards visual representation, so it will be extensively used by canvas or drawing classes.

**Observations:**

*   This class clearly separates the concern of *visualizing* a bus from the `ProcBus` which handles the *logic* of data transfer.
*   The presence of two constructors with a subtle difference (the unused boolean `z`) might indicate an older, perhaps deprecated way of creating these objects, or a feature that wasn't fully implemented or was later removed.
*   The `PermText` inner class is slightly different from the one in `OutputBus.java` (e.g., includes `moving` and `hidden` flags), suggesting it might be a distinct though similarly named utility.

**Summary:** `DiagBus.java` provides the structure for visually representing a data bus in the simulator's GUI. It extends the base `Bus` class and includes all necessary fields for drawing the bus line, displaying labels, managing animation state, and linking to the actual processor bus (`ProcBus`) whose activity it reflects. This class is crucial for the visual feedback part of the simulation. 
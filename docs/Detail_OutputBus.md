## File: `OutputBus.java`

**General Purpose:** This file defines the `OutputBus` class. Like `InputBus`, it does not extend the `Bus` abstract class. It appears to represent an output point from a component, which can connect to multiple `InputBus` instances. It also contains fields and methods suggestive of GUI representation, such as coordinates for drawing lines and an inner class for displaying permanent text labels.

**Class: `OutputBus`**

*   **Fields:**
    *   `String name`: The name of this output bus.
    *   `Vector<InputBus> connectsTo`: A list of `InputBus` objects that this output bus is connected to. This signifies a one-to-many connection capability.
    *   `int newVal`: Likely a flag or counter related to new values, usage unclear without more context.
    *   `String outText`: Text associated with the output, possibly for display.
    *   `String inText`: Text associated with an input, possibly for display (context is a bit unclear for an *output* bus).
    *   `boolean decimalText`: Flag to indicate if text should be displayed in decimal format.
    *   `String value`: The current value being output by this bus (as a String).
    *   `String[][] tmpConnectsTo = new String[2][100]`: A temporary 2D array, possibly used during setup or parsing of connections before they are stored in `connectsTo`. The dimensions suggest it might store pairs of strings for up to 100 connections.
    *   `int curConTo = 0`: An index or counter related to `tmpConnectsTo`.
    *   `int[] x = new int[100]`: Array to store x-coordinates, likely for drawing the bus or its connections on a canvas.
    *   `int[] y = new int[100]`: Array to store y-coordinates, likely for drawing.
    *   `int numPoints = 0`: The number of points defined for drawing this bus.
    *   `boolean doneBus = false`: A flag, possibly indicating if the bus drawing or setup is complete.
    *   `PermText permText1 = new PermText(this);`: An instance of the inner class `PermText`.
    *   `PermText permText2 = new PermText(this);`: Another instance of `PermText`.
    *   `int bits = 32;`: The bit width for this bus, defaulting to 32. This is similar to the `Bus` abstract class.
    *   `boolean doneAnim = false`: A flag, possibly related to animation state.

*   **Constructor:**
    *   `OutputBus(String arg0)`: Initializes the `OutputBus` with a given `name` (`arg0`). Other fields are initialized with default values.

*   **Methods:**
    *   `public InputBus getConnectsTo(int arg0)`: Returns the `InputBus` at the specified index from the `connectsTo` vector.
    *   `public void resetPoints()`: Resets the drawing points (`x`, `y`, `numPoints`) and the `doneBus` flag. This suggests it's used to clear previous drawing information.

*   **Inner Class: `PermText`**
    *   `public String str`: The text string to be displayed.
    *   `public int x`: The x-coordinate for displaying the text.
    *   `public int y`: The y-coordinate for displaying the text.
    *   `public int width`: The width of the text area.
    *   `public int height`: The height of the text area.
    *   `final OutputBus this$0`: A reference to the enclosing `OutputBus` instance.
    *   `public PermText(OutputBus arg0)`: Constructor for `PermText`.

**Relationship to other files:**

*   `InputBus.java`: `OutputBus` maintains a list of `InputBus` objects it connects to.
*   `PComponent.java`: `PComponent`s will likely have one or more `OutputBus` instances to output their results.
*   GUI-related classes (e.g., `DiagCanvas.java` or similar): The fields `x`, `y`, `numPoints`, and the `PermText` class strongly suggest that `OutputBus` objects are directly involved in rendering themselves or their state on a graphical display.
*   It is **not** directly related to `Bus.java` through inheritance, but shares the `bits` field concept.

**Observations:**

*   This class combines data-carrying responsibilities with GUI representation details, which is a common pattern in older Java GUI applications but can lead to less separation of concerns.
*   The `tmpConnectsTo` array suggests a multi-stage process for establishing connections, perhaps first parsing them from a file or configuration.

**Summary:** The `OutputBus.java` file defines a class that represents an output point of a component. It can connect to multiple `InputBus` instances and includes significant data and logic for its visual representation in a GUI, including drawing paths and displaying text labels. 
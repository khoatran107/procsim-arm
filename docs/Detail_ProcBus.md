## File: `ProcBus.java`

**General Purpose:** This file defines the `ProcBus` class, which extends the abstract `Bus` class. This suggests it's a more specialized type of bus, likely used for connections between core processor components (`PComponent`). It includes features for managing source and destination components, diagnostic connections, and conditional behavior (optional buses).

**Class: `ProcBus` (extends `Bus`)**

*   **Inherited Fields (from `Bus`):**
    *   `int bits`: Default 32.
    *   `String outName`: Initialized by constructor.
    *   `String binaryValue`: Current binary value on the bus.
    *   `String strValue`: Current string representation of the value on the bus.

*   **Own Fields:**
    *   `PComponent sourceComp = new PComponent();`: The source component for this bus. It's initialized with a new `PComponent`, which might be a placeholder until properly configured.
    *   `Vector<PComponent> destComps = new Vector<>();`: A list of destination `PComponent` objects that this bus connects to.
    *   `int curConTo = 0;`: Usage unclear, possibly an index for connections during setup (though `tmpDestComps` seems more likely for that).
    *   `Vector<String> inNames = new Vector<>();`: A list of input names, likely corresponding to the input ports on the destination components that this bus connects to.
    *   `Vector<DiagBus> diagBuses = new Vector<>();`: A list of `DiagBus` objects associated with this processor bus, suggesting a way to tap into or monitor this bus for diagnostic purposes.
    *   `String[] tmpDestComps = new String[100];`: Temporary array to store destination component names during parsing or setup, similar to `tmpConnectsTo` in `OutputBus`.
    *   `int curDestComp = 0;`: Index for `tmpDestComps`.
    *   `boolean showStrVal = false;`: Flag to control whether the string value (`strValue`) should be shown (likely in the GUI).
    *   `boolean doneAnimOnce = false;`: Flag possibly used in animation logic to ensure something happens only once.
    *   `boolean optional = false;`: Flag indicating if this bus is optional.
    *   `ProcBus optDependsOn = null;`: If this bus is optional, this field can hold a reference to another `ProcBus` it depends on.
    *   `String optIf = "0";`: If optional and `optDependsOn` is set, this bus is active if `optDependsOn.binaryValue` equals this `optIf` string.

*   **Constructor:**
    *   `ProcBus(String str)`: Initializes the bus by setting its `outName` to the provided string `str` (which is inherited from the `Bus` class).

*   **Methods:**
    *   `public boolean checkOptional()`: Checks the condition for an optional bus. If `this.optional` is true, it returns true if either `optDependsOn` is null (meaning it's unconditionally optional if active) OR if `optDependsOn.binaryValue` matches `this.optIf`. If `this.optional` is false, this method returns false (this part seems to be a slight bug, it should probably return `true` if not optional, or the logic using this method should account for it. Typically, a non-optional item is always "active" or "present").

**Relationship to other files:**

*   `Bus.java`: `ProcBus` extends `Bus`, inheriting its basic properties.
*   `PComponent.java`: `ProcBus` connects a `sourceComp` (`PComponent`) to one or more `destComps` (also `PComponent`s).
*   `DiagBus.java`: `ProcBus` can have associated `DiagBus` instances, linking processor data flow to diagnostic systems.
*   Likely used by `Simulator.java` or `LoadSim.java` to construct the processor's datapath based on a configuration (possibly XML, given `ParseSimXML.java`).

**Observations:**

*   This is the first bus type encountered that properly extends the `Bus` abstract class, suggesting it's a core part of the data transfer mechanism within the simulated processor itself.
*   The `optional` logic allows for configurable datapaths or conditional signal routing, which can be useful for simulating different MIPS features or configurations.
*   The `checkOptional()` method's return `false` when `this.optional` is `false` might be an oversight. If something is not optional, it should generally be considered active or valid. Callers of this method will need to handle this behavior.

**Summary:** `ProcBus.java` defines a specialized bus for connecting processor components. It extends the base `Bus` class and adds functionality for managing multiple destinations, linking to diagnostic buses, and implementing conditional bus activity based on the state of other buses. This class is central to defining the internal datapath of the MIPS simulator. 
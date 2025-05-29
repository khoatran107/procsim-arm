## File: `ParseSimXML.java`

**General Purpose:** This file defines the `ParseSimXML` class, responsible for parsing an XML file that describes the architecture of the MIPS simulator. It uses a DOM parser (Apache Xerces) to read the XML and then populates the `Simulator` object with components, buses, operations, and their interconnections.

**Class: `ParseSimXML`**

*   **Key Fields:**
    *   `String path`: Path to the XML file.
    *   `Document doc`: The parsed w3c DOM `Document`.
    *   `Simulator sim`: Reference to the main `Simulator` object to be populated.
    *   `LoadSim source`: Reference to the `LoadSim` instance that created this parser (used to access GUI elements like `diagCanvas` and `assembly`).
    *   State variables for parsing: `outputBuses`, `operations`, `elem`, `curComp` (current component index).
    *   Constants for `elem` state (e.g., `SIMNAME`, `NAME`, `DESC`).

*   **Constructor:**
    *   `public ParseSimXML(LoadSim loadSim)`: Stores the `LoadSim` source.

*   **Main Parsing Method: `startParse(Simulator simulator)`**
    1.  Displays a "Parsing Architecture... Please Wait..." dialog (if not `this.starting` which seems to be true by default and not changed before first call).
    2.  Clears the current simulator state (`simulator.clear()`).
    3.  Uses `org.apache.xerces.parsers.DOMParser` to parse the XML file at `simulator.path` into `this.doc`.
    4.  Calls `traverseTree(this.doc)` to recursively walk the DOM and populate temporary string arrays within `PComponent` objects.
    5.  Calls `traverseTmpVars()` to process these temporary arrays, create actual objects (`ProcBus`, `CompOperation`, `InputCheck`), and establish relationships between them.
    6.  Calls `createDiagBuses()` to create `DiagBus` instances for visualization based on the `ProcBus`es.
    7.  Calls `findConnections()` (which currently just returns `true`, actual connection logic is in `traverseTmpVars`).
    8.  Handles exceptions during parsing and returns `true` on success, `false` on failure.

*   **DOM Traversal: `traverseTree(Node node)` (Recursive)**
    *   This is the primary method for reading data from the XML structure.
    *   It switches on `node.getNodeType()`. For `ELEMENT_NODE`:
        *   Identifies XML tags like `<simname>`, `<Component>`, `<name>`, `<description>`, `<inputBuses>`, `<input>`, `<outputBuses>`, `<output>`, `<to>`, `<operations>`, `<op>`, `<dependsOn>`, `<do>`, `<in>`, `<out>`, `<specialComponent>`, `<Options>`, `<SuportedISA>` (sic).
        *   For `<Component>`: Creates a new `PComponent` and adds it to `sim.comps`.
        *   For component parts (inputs, outputs, operations, etc.): Populates temporary string arrays within the current `PComponent` object (e.g., `tmpInputs`, `tmpOutputs`, `tmpOps`, `tmpOutConnectsToComp`, `tmpInputChecks`). Attributes of XML tags are read using `node.getAttributes().getNamedItem(...)`.
        *   For text content within certain tags (identified by `this.elem` state): Populates fields like `sim.name`, component name/description, hidden status.
        *   Recursively calls `traverseTree` for child nodes.

*   **Post-Traversal Processing Methods:**
    *   `public boolean traverseTmpVars()`: This is a critical and complex method that converts the temporary string data (parsed by `traverseTree`) into a live object graph:
        1.  **Create `ProcBus`es**: Iterates through each `PComponent` and its `tmpOutputs`. For each entry, creates a `ProcBus`, sets its name, bits, source component, and adds it to the component's `buses` list.
        2.  **Set `ProcBus` Optional Properties**: Revisits the created `ProcBus`es. If marked as optional in `tmpOutputs`, sets the `optional` flag and resolves `optionalDependsOn` (another `ProcBus`) and `optionalIf` (condition string) by finding the referenced component and bus.
        3.  **Connect `ProcBus` Destinations**: For each `ProcBus`, iterates through its connection definitions in `tmpOutConnectsToComp` and `tmpOutConnectsToInput`. Finds the destination `PComponent` by name and adds it to the `ProcBus.destComps` vector and the target input name to `ProcBus.inNames`.
        4.  **Handle `oddInBuses`**: For each `PComponent`'s `tmpInputs`, if no existing `ProcBus` is found to provide that input to that component, creates a new `ProcBus` (with a "None" source) and adds it to `sim.oddInBuses`. This handles inputs not directly connected from another component's output in the XML.
        5.  **Create `CompOperation`s**: For each `PComponent` and its `tmpOps`:
            *   Creates a `CompOperation`.
            *   Sets its `name`, `functionOp` (by converting string name via `getFuncOp`), and `out` string.
            *   Resolves and adds output `ProcBus`es to `compOperation.outputs` by looking up names from `tmpOutFromOps` in the component's buses.
            *   Resolves and adds input `ProcBus`es to `compOperation.inputsToOp` by looking up names from `tmpInToOps` using `findBusFromIn`.
            *   Creates `InputCheck` objects based on `tmpInputChecks`, linking them to the correct `ProcBus` (if not "always").
            *   Adds the completed `CompOperation` to the `PComponent`'s `operations` list.
        *   Returns `true` on success, `false` if errors occur (e.g., component/bus not found).

    *   `public void createDiagBuses()`:
        *   Clears any existing buses from `this.source.diagCanvas.buses`.
        *   Iterates through all `PComponent`s and their `ProcBus`es.
        *   For each `ProcBus` and each of its destination components/input names, creates a corresponding `DiagBus` and adds it to `source.diagCanvas.buses`. This links the logical `ProcBus` to a visual `DiagBus`.
        *   Does the same for buses in `sim.oddInBuses`.

*   **Helper Methods:**
    *   `getComp()`, `getComp(int i)`: Get current or specific `PComponent`.
    *   `parseSupportedISA(String str)`: Parses a comma-separated string of supported instruction mnemonics and updates `sim.source.assembly.supportedISA`.
    *   `findConnections()`: Currently just returns `true`.
    *   `findBusFromIn(String busName, PComponent destComp)`: Finds a `ProcBus` that provides the named input to the destination component.
    *   `findOutBus(String busName, PComponent sourceComp)`: Finds a `ProcBus` with the given name originating from the source component.
    *   `findComponent(String compName)`: Finds the index of a component by its name.
    *   `getFuncOp(String funcName)`: Delegates to `Functions.getFuncOp()`.

**Relationship to other files:**

*   `LoadSim.java`: `ParseSimXML` is created and used by `LoadSim` to load an architecture.
*   `Simulator.java`: The primary target for population with components, buses, etc.
*   `PComponent.java`, `ProcBus.java`, `CompOperation.java`, `InputCheck.java`, `DiagBus.java`: These are the classes instantiated and interconnected by the parser.
*   `Functions.java`: Used via `getFuncOp()`.
*   `Assembly.java`: The supported ISA list parsed from XML is passed to the `Assembly` instance.
*   Uses Apache Xerces DOMParser: `org.apache.xerces.parsers.DOMParser` and `org.w3c.dom` interfaces.

**Summary:** `ParseSimXML.java` is the XML-to-object model converter for the simulator's architecture. It performs a two-stage process: first, a DOM traversal to read all component/bus/operation data into temporary string-based structures within `PComponent` instances. Second, in `traverseTmpVars`, it resolves these strings into actual object references, building the complete graph of interconnected `PComponent`s, `ProcBus`es, and `CompOperation`s. Finally, it creates `DiagBus` instances for GUI visualization. This class is essential for defining and loading different processor configurations. 
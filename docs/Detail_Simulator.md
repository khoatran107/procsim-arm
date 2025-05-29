## File: `Simulator.java`

**General Purpose:** This class represents the core of the MIPS processor simulation. It holds the architectural definition (components, logical buses) and the runtime state of the simulated processor, including registers and main memory. It provides methods for accessing and modifying this state.

**Class: `Simulator`**

*   **Key Fields:**
    *   `ProcSim source`: Reference to the main `ProcSim` application instance.
    *   `DiagCanvas dCanv`: A direct reference to the `DiagCanvas` used for visualization. This suggests a tight coupling for updates.
    *   `String[] registers`: An array of 32 strings, representing the MIPS register file. Values are typically binary strings.
    *   `String name`: The name of the loaded simulation architecture (e.g., "MIPS R2000 Just R-Format").
    *   `String path`: The file path to the XML file defining the current architecture.
    *   `Vector<PComponent> comps`: A vector holding the logical `PComponent` objects (ALU, Memory units, etc.) that make up the processor, as defined by the parsed XML.
    *   `Vector<ProcBus> oddInBuses`: A vector of `ProcBus` objects. The specific meaning of "odd" in this context is not immediately clear from the code structure alone but might relate to how certain input buses are handled or categorized during XML parsing or component connection.
    *   `boolean resetEveryRound`: A flag, possibly related to simulation stepping or pipeline behavior.
    *   `int mainMemSize`: The size of the main memory array (default 10000 elements).
    *   `String[] mainMemory`: An array of strings representing the byte-addressable main memory. Each element likely stores an 8-bit binary string.
    *   `String lastChangedReg`: Stores the identifier of the last register that was modified (e.g., for GUI highlighting).
    *   `int lastChangedMem`: Stores the address of the last memory location that was modified.
    *   `int execInstr`: A counter for the number of executed instructions.

*   **Constructor:**
    *   `public Simulator(ProcSim procSim)`: Initializes `mainMemory` and `registers` arrays with empty strings. Stores the `procSim` reference. Crucially, it sets a static field `Functions.sim = this;`, giving the static methods in the `Functions` class direct access to this `Simulator` instance for reading/writing registers and memory during operation execution.

*   **Core Functionality:**
    *   `resetMemoryAndRegs()`: Clears all `registers` and `mainMemory` locations to empty strings.
    *   **Memory Access:**
        *   `getWordMem(int address)`: Reads a 32-bit word from `mainMemory` starting at the given byte `address`. It concatenates four 8-bit strings (from `mainMemory[address]` to `mainMemory[address+3]`), applying `ProcFunc.signExtend(..., 8)` to each. This suggests memory is byte-addressable and byte values are stored as strings. The use of `signExtend` here might be specific to how numbers are represented or a misapplication if these are just opaque byte strings forming a word.
        *   `setWordMem(int address, String wordValue)`: Writes a 32-bit `wordValue` string into four consecutive 8-bit segments in `mainMemory`. Updates `lastChangedMem`.
    *   `clear()`: Clears the `comps` and `oddInBuses` vectors. It also directly clears `this.source.loadSim.diagCanvas.comps` and `this.source.loadSim.diagCanvas.buses`, indicating direct manipulation of the `DiagCanvas` data from the simulator model.
    *   **File Path Utilities:**
        *   `getFilePath(String fullPath)`: Extracts the directory path from a full file path string.
        *   `getFileName(String fullPath)`: Extracts the file name from a full file path string.
    *   `findDiagBuses(ProcBus procBus)`: Given a logical `ProcBus`, this method searches the `DiagCanvas` (`this.source.diagCanvas.buses`) to find all visual `DiagBus` instances that correspond to (i.e., visually represent) the given logical `ProcBus`. This is likely used to trigger animations on the correct visual buses.

**Relationship to other files:**

*   `ProcSim.java`: The `Simulator` is a core component of `ProcSim`.
*   `Functions.java`: `Functions.sim` is a static reference to the `Simulator` instance, allowing `Functions.doOp()` to directly access and modify registers (`this.registers`) and memory (`this.mainMemory`).
*   `PComponent.java`, `ProcBus.java`: The `Simulator` stores collections of these logical components, which define the architecture.
*   `DiagCanvas.java`: `Simulator` holds a reference to it (`dCanv`, and also indirectly via `source.loadSim.diagCanvas`) and can clear its visual elements. `findDiagBuses` bridges logical `ProcBus`es to their visual `DiagBus` counterparts on the canvas.
*   `ParseSimXML.java`: This class is responsible for populating the `Simulator`'s `comps` and potentially `oddInBuses` vectors based on the XML architecture file.
*   `LoadSim.java`: Interacts with `Simulator` to get architecture information for its visual editor and path information.
*   `ProcFunc.java`: Used for string/path manipulation utilities.

**Summary:** The `Simulator` class acts as the central data model for the processor being simulated. It holds the logical definition of the architecture (components and their connections via `ProcBus`es) and the processor's current state (registers, memory contents). It provides the interface for the core execution logic in `Functions` to interact with this state and is also linked to the GUI elements (`DiagCanvas`, `LoadSim`) for initialization and for finding visual elements corresponding to logical ones. 
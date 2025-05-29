## File: `Functions.java`

**General Purpose:** This file is a critical part of the simulator, containing the core logic for executing various MIPS-like operations. It defines operation codes, implements the `doOp` method that performs these operations, and provides utility functions for data conversion and string representation of operations.

**Class: `Functions`**

*   **Static Fields (Operation Codes):**
    *   `OP_SUB = 0`
    *   `OP_ADD = 1`
    *   `OP_AND = 2`
    *   `OP_OR = 3`
    *   `OP_ZERO = 4` (Checks if an input is zero)
    *   `OP_MUX = 5` (Selects an input, though its detailed logic for selection isn't fully clear from `doOp` alone, seems to pass through first input)
    *   `OP_READMEM = 6`
    *   `OP_WRITEMEM = 7`
    *   `OP_OUT = 8` (Outputs a string value to potentially multiple output buses)
    *   `OP_BITOUT = 9` (Outputs specific bits of a string to different output buses)
    *   `OP_SPLIT = 10` (Extracts a range of bits from an input binary string)
    *   `OP_READREG = 11`
    *   `OP_WRITEREG = 12`
    *   `OP_GETINSTR = 13` (Fetches machine code and string representation of an instruction from memory)
    *   `OP_SHIFTLEFT = 14`
    *   `OP_SIGNEXTEND = 15`
    *   `OP_SLT = 16` (Set on Less Than)
    *   `OP_JOIN = 17` (Concatenates two binary strings)
    *   `public static Simulator sim`: A static reference to the main `Simulator` object, allowing these static functions to access global simulator state (memory, registers, GUI elements via `sim.source`).

*   **Constructor:**
    *   `Functions()`: Default private constructor, as this class primarily provides static methods.

*   **Core Static Method: `doOp`**
    *   `public static String[][] doOp(int opCode, Vector<ProcBus> inputs, Vector<ProcBus> outputs, String auxStr)`:
        *   Takes an operation code (`opCode`), a `Vector` of input `ProcBus`es, a `Vector` of output `ProcBus`es, and an auxiliary `String` (`auxStr` - used by operations like SPLIT, SHIFTLEFT, SIGNEXTEND for parameters).
        *   Initializes a 2D result array `strArr[20][2]` to store binary and string/decimal results for up to 20 outputs (though most operations use far fewer).
        *   Reads binary values from the first few input buses and converts them to integers (`i3`, `i4`).
        *   Calls `checkAllowFunc` for input buses to potentially gate the operation based on `DiagBus` states (related to optional/animated buses), returning an error result if not allowed.
        *   A large `switch` statement executes logic based on `opCode`:
            *   **Arithmetic/Logical**: `OP_SUB`, `OP_ADD`, `OP_AND`, `OP_OR`, `OP_SLT`. Results are converted to binary strings. Some also output a "zero flag" as a second result.
            *   **Memory**: `OP_READMEM` (calls `sim.getWordMem()`), `OP_WRITEMEM` (calls `sim.setWordMem()`).
            *   **Register**: `OP_READREG` (accesses `sim.registers[]`), `OP_WRITEREG` (updates `sim.registers[]` and `sim.lastChangedReg`).
            *   **Instruction Fetch**: `OP_GETINSTR` fetches instruction machine code and its string representation from `sim.source.assembly.instr[]`. Handles "exit" instructions and GUI updates for instruction memory display.
            *   **Data Manipulation**: `OP_MUX`, `OP_SPLIT`, `OP_SHIFTLEFT` (uses helper `shiftLeft()`), `OP_SIGNEXTEND` (uses `ProcFunc.signExtend()`), `OP_JOIN`.
            *   **Output**: `OP_OUT`, `OP_BITOUT` format and distribute `auxStr` or a default value to output buses.
            *   **Control**: `OP_ZERO` checks if an input is zero.
        *   After the switch, it iterates through the `strArr` results. If a binary value was produced but its string/decimal counterpart is missing, `toDec()` is called.
        *   Returns the `strArr` containing [binary_value, string_value] pairs for each output.

*   **Static Helper Methods:**
    *   `toBin(int i)`: Converts an integer to a 32-bit binary string (zero-extended by `ProcFunc.zeroExtend`).
    *   `toDec(String str, boolean z)` and `toDec(String str)`: Convert a binary string to its decimal string representation. Handles signed (2's complement if 32-bit and MSB is 1, or if boolean `z` is true) and unsigned numbers.
    *   `checkAllowFunc(ProcBus procBus)`: Checks if an operation involving this `procBus` should proceed based on its optional status and the `newVal` flags of associated `DiagBus`es.
    *   `shiftLeft(String str, int i)`: Performs a logical left shift on a binary string.
    *   `checkZero(String str)`: Converts binary string to decimal, returns "1" if zero, "0" otherwise.
    *   `toString(int i)`: Converts an operation code (`OP_*`) to its human-readable string name (e.g., "add", "readmem").
    *   `getFuncOp(String str)`: Converts a string name of an operation to its integer code (`OP_*`). Returns -1 if not found.

**Relationship to other files:**

*   `CompOperation.java`: `CompOperation.doOp()` directly calls `Functions.doOp()` to execute the logic.
*   `Simulator.java`: `Functions` holds a static reference to `Simulator` (`sim`) to access global state like memory (`sim.getWordMem`, `sim.setWordMem`), registers (`sim.registers`), and assembly/GUI components (`sim.source`).
*   `ProcBus.java`: `doOp` takes `ProcBus` vectors as inputs/outputs.
*   `ProcFunc.java`: Uses `ProcFunc.zeroExtend()` and `ProcFunc.signExtend()`.
*   `DiagBus.java`: `checkAllowFunc` inspects `DiagBus` properties.
*   `Assembly.java`: `OP_GETINSTR` accesses `sim.source.assembly`.
*   `ViewSim.java` & `DiagCanvas.java`: `OP_GETINSTR` interacts with GUI elements through `sim.source`.

**Summary:** `Functions.java` is the powerhouse of the simulator's execution logic. It defines all the elementary operations a MIPS processor might perform, from arithmetic to memory access and instruction fetching. The static `doOp` method is the central dispatcher for these operations, taking an operation code and relevant data buses, and returning the results. It heavily relies on a static `Simulator` instance for context and uses various helper functions for data conversion and specific tasks. This class is fundamental to the functional simulation of the MIPS architecture. 
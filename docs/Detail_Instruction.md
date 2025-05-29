## File: `Instruction.java`

**General Purpose:** This file defines the `Instruction` class, which acts as a data structure to hold different representations and parts of a single MIPS instruction.

**Class: `Instruction`**

*   **Fields:**
    *   `String str`: Likely the original, full string representation of the instruction as read from the assembly file.
    *   `String strNoLbl`: The instruction string without any labels.
    *   `String strMach`: The machine code representation of the instruction (as a string).
    *   `String instr`: The instruction mnemonic (e.g., `add`, `lw`, `beq`).
    *   `String param1`: The first parameter/operand of the instruction.
    *   `String param2`: The second parameter/operand of the instruction.
    *   `String param3`: The third parameter/operand of the instruction.
    *   `String comment`: Any comment associated with the instruction line. Initialized to an empty string.
    *   `String label`: Any label associated with this instruction line.
    *   `String address`: The memory address where this instruction is located.

*   **Methods:** None defined in this class. It's a plain old Java object (POJO) for storing instruction data.

**Relationship to other files:**

*   This class will likely be used by any file that deals with MIPS instructions, such as:
    *   `Assembly.java` (for parsing assembly code into these `Instruction` objects).
    *   `Simulator.java` or `ProcSim.java` (for fetching, decoding, and executing instructions).
    *   `LoadSim.java` (if it's involved in loading instructions).

**Summary:** The `Instruction.java` file provides a fundamental data structure for representing MIPS instructions within the simulator. It holds all relevant parts of an instruction, from its textual representation to its components and memory address. 
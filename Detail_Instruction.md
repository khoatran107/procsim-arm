# Detail_Instruction.md

## File: Instruction.java
**Purpose**: Represents a MIPS instruction in the simulator, storing both the assembly and machine code representations along with all relevant instruction components.

### Class Structure
```java
class Instruction {
    String str;         // Complete instruction string
    String strNoLbl;    // Instruction string without label
    String strMach;     // Machine code representation
    String instr;       // Instruction opcode/name
    String param1;      // First parameter
    String param2;      // Second parameter
    String param3;      // Third parameter
    String comment;     // Instruction comment (default "")
    String label;       // Instruction label
    String address;     // Instruction address
}
```

### Key Features
1. **Multiple Representations**:
   - Assembly format (`str`, `strNoLbl`)
   - Machine code format (`strMach`)
   - Decomposed components (`instr`, `param1`, `param2`, `param3`)

2. **Metadata**:
   - Comments for documentation
   - Labels for branching/jumping
   - Address information for memory location

3. **Instruction Components**:
   - Supports up to 3 parameters (typical MIPS format)
   - Maintains both raw and processed instruction formats

### Related Files
1. `Assembly.java` - Uses Instruction objects for assembly processing
2. `ProcSim.java` - Executes instructions during simulation
3. `Functions.java` - Likely contains utility functions for instruction processing

### Usage Context
- Core data structure for representing MIPS instructions
- Used throughout the assembly and execution process
- Supports both assembly and simulation phases
- Enables instruction visualization and debugging

### Implementation Details
- Simple data container class
- No methods defined, suggesting external processing
- Flexible string-based representation
- Supports full MIPS instruction set format

### Integration Points
- Assembly parsing and generation
- Instruction execution simulation
- Program memory representation
- Debugging and visualization support 
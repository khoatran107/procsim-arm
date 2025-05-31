# Detail_Assembly.md

## File: Assembly.java
**Purpose**: Implements the MIPS assembly code editor and assembler, handling the conversion between assembly language and machine code.

### Class Structure
```java
class Assembly extends TextEditor implements ActionListener {
    Button butAssemble;              // Assemble button
    Instruction[] instr;             // Array of parsed instructions
    int numInstr;                    // Number of instructions
    String[][] directives;           // Assembly directives
    int numDirects;                  // Number of directives
    Vector<String> supportedISA;     // Supported instruction set
    MenuItem fmSupportedISA;         // Menu item for ISA info
    boolean doneCheck;               // Validation flag
}
```

### Key Features
1. **Text Editor Integration**:
   - Extends TextEditor for code editing
   - Supports file operations (new, load, save)
   - Implements undo/redo functionality

2. **Assembly Processing**:
   - Parses assembly code into instructions
   - Handles labels and directives
   - Validates instruction support
   - Converts to machine code

3. **User Interface**:
   - Assembly-specific toolbar
   - ISA support information
   - Error reporting
   - File management

### Major Methods

1. **Constructor**:
   ```java
   public Assembly(ProcSim procSim)
   ```
   - Initializes editor interface
   - Sets up UI components
   - Configures event handlers

2. **Assembly Processing**:
   ```java
   public boolean doParse()
   ```
   - Parses assembly text
   - Creates instruction objects
   - Handles labels and comments
   - Returns success status

3. **Machine Code Generation**:
   ```java
   public String doAssemble(Instruction instruction)
   ```
   - Converts instruction to machine code
   - Handles different instruction formats
   - Performs error checking

4. **Register Handling**:
   ```java
   public String registerConvert(String str)
   static String regNumToString(int i)
   ```
   - Converts between register names and numbers
   - Handles all MIPS register formats

5. **ISA Support**:
   ```java
   public boolean isSupported(String str)
   public void checkSupportedISA()
   public String supportedISAToString()
   ```
   - Validates instruction support
   - Manages supported instruction set
   - Provides ISA information

### Related Files
1. `TextEditor.java` - Parent class for editing functionality
2. `Instruction.java` - Instruction representation
3. `ProcSim.java` - Main simulator interface
4. `Functions.java` - Utility functions

### Usage Context
- Primary interface for assembly code input
- Converts assembly to machine code
- Validates code before simulation
- Manages assembly file operations

### Implementation Details
1. **Text Processing**:
   - Line-by-line parsing
   - Comment handling
   - Label extraction
   - Directive processing

2. **Code Generation**:
   - R-format instruction handling
   - I-format instruction handling
   - J-format instruction handling
   - Register name resolution

3. **Error Handling**:
   - Syntax validation
   - Instruction support checking
   - Error reporting
   - User feedback

### Integration Points
1. **Editor Integration**:
   - Text editing capabilities
   - File operations
   - Undo/redo support

2. **Simulator Integration**:
   - Machine code generation
   - Instruction validation
   - Simulation preparation

3. **UI Integration**:
   - Custom toolbar
   - Menu items
   - Error dialogs
   - Status reporting

### Notable Features
1. **Flexible ISA Support**:
   - Dynamic instruction set
   - Support validation
   - ISA information display

2. **Comprehensive Assembly Support**:
   - All MIPS instruction formats
   - Assembly directives
   - Labels and comments
   - Register aliases

3. **User Experience**:
   - Integrated editor
   - Clear error messages
   - File management
   - Assembly status feedback 
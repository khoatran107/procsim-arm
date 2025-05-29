## File: `Assembly.java`

**General Purpose:** This file defines the `Assembly` class, which extends `TextEditor`. It provides a GUI for writing, parsing, and assembling MIPS-like assembly code into machine code. It handles directives, labels, comments, instruction parsing, and conversion to binary machine code for a defined subset of MIPS instructions.

**Class: `Assembly` (extends `TextEditor`, implements `ActionListener`)**

* **Key Fields:**
  * `Button butAssemble`: GUI button to trigger the assembly process.
  * `Instruction[] instr`: Array to hold parsed `Instruction` objects.
  * `int numInstr`: Number of actual instructions parsed.
  * `String[][] directives`: Array to store parsed assembler directives (seems to primarily handle a custom `.register` directive).
  * `int numDirects`: Number of directives found.
  * `Vector<String> supportedISA`: A list of supported instruction mnemonics. If empty, a default set is assumed.
  * `MenuItem fmSupportedISA`: Menu item to display supported instructions.
  * `boolean doneCheck`: Flag used in ISA checking within `doAssemble`.

* **Constructor:**
  * `public Assembly(ProcSim procSim)`: Initializes the editor (calls `super`), sets up the "Assemble" button, file menu items (New, Load, Save, Save As, Close), and a help menu item for "Supported Instructions". Default assembly file path is "sample just R-Format.asm".

* **GUI and Actions (`actionPerformed`):**
  * Handles actions for `butAssemble`: Calls `doParse()`. If successful, shows a success message; otherwise, a failure message.
  * Handles `butDone`: Prompts to save, then calls `doParse()`. If successful, disposes the window.
  * Handles file menu actions (`fmLoad`, `fmSaveAs`, `fmSave`, `fmNew`, `fmClose`) by calling respective methods (some inherited from `TextEditor` or implemented here, e.g., `openAss`, `saveAs`).
  * Handles `fmSupportedISA`: Displays a `MsgBox` with the list of supported instructions.
  * Also sets up `UndoableEditListener` and `AbstractAction`s for undo/redo, presumably inherited or managed by `TextEditor`.

* **Core Parsing Method: `doParse()`**
    1. Resets `numInstr` and `numDirects`.
    2. Tokenizes the text content of the editor (`this.txtDoc.getText()`) by newline characters.
    3. **First Pass (Line Identification & Basic Storage):**
        * Iterates through lines:
            * Identifies lines starting with `.register` as directives and stores them.
            * Lines not starting with `#` (comments) are treated as potential instructions. An `Instruction` object is created, and the raw line string is stored in `instr[...].str`.
    4. **Directive Processing:**
        * Iterates through stored directives.
        * Extracts `.register <reg_name> <value>`.
        * Uses `registerConvert()` for `<reg_name>` and `Functions.toBin()` for `<value>`.
    5. **Second Pass (Detailed Instruction Parsing):**
        * Iterates `i4` from 0 to `numInstr`:
            * **Labels:** Identifies and extracts a label (string before a ':') into `instr[i4].label`.
            * **Comments:** Identifies and extracts a comment (string after a '#') into `instr[i4].comment`. The instruction string `instr[i4].str` is trimmed to exclude the comment.
            * The label is also removed from `instr[i4].strNoLbl`.
            * **Special "exit" label:** If the line is empty after label/comment removal and the label is "exit", sets `instr[i4].instr = "exit"`.
            * **Machine Code Check:** If `strNoLbl` is 32 characters and looks like binary, it's treated as pre-existing machine code (`instr[i4].instr = "machine"`, `strMach` is set).
            * **Opcode and Parameters:** Otherwise, it parses `strNoLbl`:
                * The first word is `instr[i4].instr` (mnemonic).
                * The rest is tokenized by commas (`,`) to get `param1`, `param2`, `param3`. Special handling for `j` instruction (only one parameter).
    6. **Assembly Pass:**
        * Iterates `i9` from 0 to `numInstr`:
            * Assigns a binary `address` to `instr[i9].address` (effectively `i9`, zero-extended).
            * If not "exit" or "machine", calls `this.instr[i9].strMach = doAssemble(this.instr[i9])`.
            * If `doAssemble` returns "err", `doParse` returns `false`.
    7. If all steps complete without error, returns `true`.

* **Core Assembly Method: `doAssemble(Instruction instruction)`**
  * Takes a fully parsed `Instruction` object.
  * Checks if the instruction mnemonic (`instruction.instr`) is supported (uses `isSupported()`, reports error once if not).
  * **R-type Instructions** (`add`, `sub`, `and`, `or`, `slt`):
    * Opcode: "000000".
    * Funct: Determined by `instruction.instr` (e.g., `add` -> 32 -> "100000").
    * Registers: `rs` (from `param2`), `rt` (from `param3`), `rd` (from `param1`). Converted to 5-bit binary using `registerConvert()`.
    * Shamt: "00000".
    * Format: `opcode | rs | rt | rd | shamt | funct`.
  * **I-type Instructions** (`lw`, `sw`, `beq`, `addi`, `andi`, `ori`):
    * Opcode: Determined by `instruction.instr` (e.g., `lw` -> 35 -> "100011").
    * `lw`/`sw`: `rt` (from `param1`). `param2` is parsed as `offset(rs)` (e.g., `0($s0)`). `offset` becomes 16-bit immediate, `rs` is 5-bit register.
    * `beq`: `rs` (from `param1`), `rt` (from `param2`). `param3` (label) is resolved to a 16-bit signed PC-relative offset. (PC is current instruction address).
    * `addi`/`andi`/`ori`: `rt` (from `param1`), `rs` (from `param2`). `param3` (immediate) is converted to 16-bit binary (zero-extended for `andi`/`ori`, potentially sign-extended for `addi` by context, though code uses `zeroExtend` after `Integer.parseInt`).
    * Format: `opcode | rs | rt | immediate`.
  * **J-type Instructions** (`j`):
    * Opcode: "000010".
    * `param1` (label) is resolved to its instruction index. This index is converted to a 26-bit field. (Note: Standard MIPS jump uses upper PC bits + (index << 2), this seems to be a direct index, possibly word-addressed index).
    * Format: `opcode | address_index`.
  * Returns the 32-bit machine code string or "err" on failure.

* **Helper Methods:**
  * `supportedISAToString()`: Returns a formatted string of supported instructions.
  * `isSupported(String str)`: Checks if a given instruction string is in `supportedISA` (or is "machine" or "exit").
  * `checkSupportedISA()`: Iterates through parsed instructions and warns if any are unsupported.
  * `regNumToString(int i)`: Converts a register number (0-31) to its MIPS string name (e.g., "$t0", "$s0").
  * `registerConvert(String str)`: Converts a MIPS register string name (e.g., "$s0", "$zero") to its 5-bit binary representation. Returns "err" for invalid names.
  * `openAss()`: File opening logic (likely in `TextEditor`).
  * `saveAs()`, `saveAs(boolean z)`: File saving logic (likely in `TextEditor`).

**Relationship to other files:**

* `TextEditor.java`: Base class, provides text editing GUI and likely some file operations.
* `Instruction.java`: `Assembly` creates and populates `Instruction` objects.
* `ProcSim.java`: Used for output (`ProcSim.out`, `ProcSim.outErr`) and access to global state if needed (e.g., `this.source` which is `ProcSim`).
* `MsgBox.java`: Used for displaying dialog messages.
* `Functions.java`: `doParse` uses `Functions.toBin()` for directive values. `doAssemble` uses `Functions.toBin()` for branch offset calculation (indirectly via `ProcFunc.signExtend`).
* `ProcFunc.java`: Used for `zeroExtend`, `signExtend`, `slimBinary`.

**Summary:** `Assembly.java` is a comprehensive class that provides the user interface and logic for assembling MIPS-like assembly language into machine code. It parses text input, handles labels, comments, directives, and converts recognized instructions (R-type, I-type, J-type with specific MIPS opcodes/funct codes) into their 32-bit binary representations. It forms the bridge between human-readable assembly and the machine code the simulator will execute.

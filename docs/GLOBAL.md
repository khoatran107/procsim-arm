# Global Project Documentation

This document provides a high-level overview of the MIPS simulator project, its components, and their interactions. It will be updated as each file is analyzed.

## Project Overview

*   **Purpose**: Simulate the functionality of a MIPS processor.
*   **Technology**: Java, likely using Swing or AWT for the GUI (based on typical Java GUI practices and filenames like `JFrame`, `Canvas`).
*   **Input**: MIPS assembly code, possibly XML configuration files.
*   **Output**: Visual representation of the MIPS processor's state, execution flow, and potentially console output.
*   **Key Features (Inferred from filenames and `README.md`):
    *   GUI for interaction and visualization.
    *   Assembly parsing/handling.
    *   Simulation of processor components (ALU, Memory, Registers - inferred from MIPS context).
    *   XML-based configuration loading/saving.
    *   Animation of the simulation process.
*   **Main Class**: `ProcSim.java` is the entry point and main container for the application. It initializes the primary GUI, sets up global configurations (console visibility, output verbosity via static flags and command-line arguments), and instantiates all core functional components. It orchestrates user interactions and navigation between different modules (assembly editor, architecture designer, simulation view).

## Core Concepts (to be detailed later)

*   **Instruction Cycle**: How the simulator fetches, decodes, executes instructions.
*   **Data Path**: How data flows between different MIPS components.
*   **Control Logic**: How the simulator controls the operations of different components.
*   **GUI Interaction**: How users interact with the simulator and how the GUI reflects the simulator's state.

## Main Components (Initial high-level guess, to be refined)

*   **Main Application**:
    *   `ProcSim`: The main class in `ProcSim.java` (extends `Frame`). It serves as the entry point, initializes all core components (`Assembly`, `LoadSim`, `ViewSim`, `Simulator`, `DiagCanvas`, `Console`), manages the main application window with buttons to launch these components, and handles command-line arguments for global settings.
*   **Simulator Engine & State**:
    *   `Simulator`: A class in `Simulator.java` that holds the processor's architectural definition (`PComponent`s, `ProcBus`es) and its runtime state (registers, main memory). It provides state access for `Functions` and links to `DiagCanvas` for visual updates.
    *   `AnimThrd`: A class in `AnimThrd.java` (implements `Runnable`) that manages the animation of data flow on the `DiagCanvas`. It runs in a separate thread, processing animation steps from `ViewSim` and coordinating individual `Anim` objects.
    *   `Anim`: A class in `Anim.java` representing a single animated data value moving along a `DiagBus`. It uses Bresenham's algorithm to plot its course and is managed by `AnimThrd`.
*   **GUI**: The user interface for controlling and observing the simulation.
    *   `ViewSim`: The main class in `ViewSim.java` (extends `Frame`) for the simulation window. It orchestrates the simulation execution, manages animation sequences via `AnimThrd` and `AnimationListItem` (an inner class), provides GUI controls (buttons, menus, speed slider) for user interaction, and displays processor state (registers, memory, instructions) in separate `CompFrame` windows. It determines the next component to execute using `nextCompAnim()` and updates the `DiagCanvas`.
    *   `DiagCanvas`: A class in `DiagCanvas.java` (extends `JScrollPane`) that handles all custom drawing of the processor architecture diagram (components, bus paths, names, animations). Used by `LoadSim` and `ViewSim`.
    *   `LoadSim`: A class in `LoadSim.java` (extends `Frame`) that provides the main graphical editor for creating and modifying processor architectures. It uses `DiagCanvas` for drawing, interacts with `ParseSimXML` for loading/saving the architecture (including graphical layout information embedded in the XML), and provides UI controls for component and bus manipulation.
    *   `TextEditor`: An abstract class in `TextEditor.java` (extends `JFrame`) providing a base for text editors with file operations, edit operations (undo, copy, paste, find), menus, and a button panel. Used by `Assembly` and `EditXML`.
    *   `NumericTextField`: A custom `java.awt.TextField` in `NumericTextField.java` that restricts input to numeric characters and essential control keys (backspace, delete, newline, tab) using a `KeyAdapter`.
    *   `MsgBox`: A utility class in `MsgBox.java` (extends `Dialog`) for displaying simple modal dialog boxes with either "OK" or "Yes"/"No" buttons. The result (`id` field) indicates the user's choice.
*   **Parser/Loader**: Modules for loading assembly code and configuration files.
    *   `Assembly`: A class in `Assembly.java` (extends `TextEditor`) that provides a GUI for writing MIPS assembly. Its `doParse()` method tokenizes and parses lines into `Instruction` objects (handling labels, comments, directives). Its `doAssemble()` method converts parsed `Instruction` objects into 32-bit MIPS machine code strings for R, I, and J type instructions.
    *   `ParseSimXML`: A class in `ParseSimXML.java` that parses an XML architecture definition file using a DOM parser. It first populates temporary string-based structures in `PComponent` objects during DOM traversal (`traverseTree`). Then, `traverseTmpVars()` resolves these strings into a live object graph of `PComponent`s, `ProcBus`es, `CompOperation`s, and `InputCheck`s. Finally, `createDiagBuses()` sets up `DiagBus` instances for GUI visualization.
    *   `EditXML`: A class in `EditXML.java` (extends `TextEditor`) that provides a GUI for editing the XML architecture file. It allows parsing the edited XML (via `ParseSimXML`) and applying changes to the `Simulator` instance.
*   **Processor Components**: Representations of ALU, registers, memory, buses, etc.
    *   `Bus`: An abstract class in `Bus.java` serving as a base for different bus types. Defines common properties like `bits` (width, default 32), `outName`, `binaryValue`, and `strValue`.
    *   `InputBus`: A class in `InputBus.java` representing a named input connection for a `PComponent`. It holds a `name`, a reference to the source `PComponent`, and a `String value`. It does not extend the `Bus` class.
    *   `OutputBus`: A class in `OutputBus.java` representing an output connection from a component. It can connect to multiple `InputBus` instances and includes fields for its `name`, `value`, connected `InputBus` list (`connectsTo`), and GUI rendering data (coordinates, points, `PermText` inner class for labels). It also does not extend `Bus` but has a `bits` field.
    *   `ProcBus`: A class in `ProcBus.java` that **extends** `Bus`. It connects a `PComponent` source to one or more `PComponent` destinations. It includes fields for managing these connections, linking to `DiagBus` instances, and handling optional bus behavior based on dependencies.
    *   `DiagBus`: A class in `DiagBus.java` that **extends** `Bus`. It's designed for GUI visualization of buses, linking a source `PComponent` to a destination `PComponent` and often mirroring a `ProcBus`. Contains fields for drawing coordinates, points, labels (`PermText` inner class), and animation state.
    *   `PComponent`: A class in `PComponent.java` representing a generic processor component (e.g., ALU, Register File). Manages `name`, `description`, `CompOperation` list, associated `ProcBus` list, GUI properties (coordinates, size, hidden), and temporary arrays for parsing connections/operations from configuration.
    *   `CompOperation`: A class in `CompOperation.java` that defines a specific operation for a `PComponent`. It holds an operation ID (`functionOp`), lists of input `ProcBus`es, output `ProcBus`es, and `InputCheck` conditions. Its `doOp()` method executes the operation (via `Functions.doOp()`) and updates output buses and `DiagBus` for visualization.
*   **Core Logic & Utilities**:
    *   `Functions`: A class in `Functions.java` containing static methods and constants for defining and executing all core MIPS-like operations (arithmetic, memory, register, instruction fetch, data manipulation). Its `doOp()` method is central to the simulation. Includes utilities for data conversion (binary/decimal) and operation string representations. Holds a static reference to the `Simulator` instance.
    *   `ProcFunc`: A utility class in `ProcFunc.java` providing static helper methods for binary string manipulation (e.g., `zeroExtend`, `signExtend`, `slimBinary`), a rounding function, and file system utilities (`copyFile`, `fileSep`).
    *   `InputCheck`: A class in `InputCheck.java` used to define conditions for `CompOperation` execution. Checks the `binaryValue` of a `ProcBus` against a string or can be set to always pass. Contains a suspect 10-iteration loop in its `check()` method.
    *   `Console`: A class in `Console.java` that provides a separate GUI window (JFrame) to display `System.out` and `System.err` streams. It uses `JTextArea`s and `PipedInputStream`s with reader threads to capture and show log/error messages.
*   **Data Structures**:
    *   `Instruction`: A class in `Instruction.java` that holds various string representations (original, no label, machine code), components (mnemonic, parameters, label, comment), and the memory address of a MIPS instruction.

## Build and Run

As per `README.md`:

**Build:**
```
javac -cp xercesImpl-2.12.2.jar -d . defpackage/*.java
jar cvf myprogram.jar defpackage/
```

**Run (Linux):**
```
java -cp "myprogram.jar:xercesImpl-2.12.2.jar" defpackage.ProcSim
```

**Run (Windows):**
```
java -cp "myprogram.jar;xercesImpl-2.12.2.jar" defpackage.ProcSim
```

This indicates that `xercesImpl-2.12.2.jar` is a dependency (likely for XML parsing) and `defpackage.ProcSim` is the main class.

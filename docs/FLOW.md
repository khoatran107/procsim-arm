# Flow of Code Analysis

This document outlines the order in which the Java files in the `defpackage` directory will be analyzed to understand the MIPS simulator project.

## Analysis Order:

1.  **`Instruction.java`**: Likely defines the structure or representation of a MIPS instruction. This is a fundamental building block.
2.  **`Bus.java`**: A generic bus, could be a base class for `InputBus`, `OutputBus`, `ProcBus`, `DiagBus`.
3.  **`InputBus.java`**: Likely handles input to a component.
4.  **`OutputBus.java`**: Likely handles output from a component.
5.  **`ProcBus.java`**: Potentially a specialized bus for processor components.
6.  **`DiagBus.java`**: Possibly a bus used for diagnostics or visualization.
7.  **`PComponent.java`**: Could be a base class for processor components (e.g., ALU, Register File, Memory).
8.  **`CompOperation.java`**: Might define operations that components can perform.
9.  **`Functions.java`**: Could contain utility functions or mathematical operations, possibly for the ALU.
10. **`ProcFunc.java`**: May contain functions specific to processor operations or control logic.
11. **`InputCheck.java`**: Likely provides input validation functionalities.
12. **`NumericTextField.java`**: A custom UI component for numeric input, probably used in the GUI.
13. **`Assembly.java`**: Handles MIPS assembly language, possibly parsing or converting it.
14. **`ParseSimXML.java`**: For parsing simulator configuration or state from XML files.
15. **`EditXML.java`**: For editing simulator configuration or state in XML files.
16. **`LoadSim.java`**: Could be responsible for loading the simulator's initial state or a program.
17. **`Simulator.java`**: A core class that likely orchestrates the simulation process.
18. **`ProcSim.java`**: The main entry point of the application, setting up the simulator and GUI.
19. **`Anim.java`**: Could be related to animation logic.
20. **`AnimThrd.java`**: Likely a thread for managing animations, ensuring the UI remains responsive.
21. **`Console.java`**: A UI component representing a console or output window.
22. **`MsgBox.java`**: A UI component for displaying messages or dialogs.
23. **`DiagCanvas.java`**: A UI component for drawing or visualizing diagnostic information.
24. **`CompFrame.java`**: A UI frame that might display components or their states.
25. **`TextEditor.java`**: A simple text editor, perhaps for MIPS assembly code or configuration files.
26. **`ViewSim.java`**: The main view or GUI class that ties all UI elements together.
27. **`manifest.txt`**: Describes the JAR file contents. (Not Java code, but good to note)
28. **`myprogram.jar`**: The compiled program. (Not source code for analysis)
29. **`mylibrary.jar`**: A library used by the program. (Not source code for analysis) 
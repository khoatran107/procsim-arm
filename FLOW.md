# FLOW.md

## Project Overview
This is a MIPS simulator implemented in Java with a GUI interface. The project appears to simulate MIPS processor components and their interactions.

## File Categories

### Core MIPS Components
1. `Assembly.java` - MIPS Assembly handling
2. `Instruction.java` - MIPS Instruction representation
3. `ProcSim.java` - Main processor simulation
4. `ProcFunc.java` - Processor functions
5. `ProcBus.java` - Processor bus communication

### GUI Components
1. `DiagCanvas.java` - Diagram canvas for visualization
2. `CompFrame.java` - Component frame handling
3. `Console.java` - Console interface
4. `ViewSim.java` - Simulation view
5. `TextEditor.java` - Text editor component

### Bus Communication
1. `Bus.java` - Base bus class
2. `InputBus.java` - Input bus handling
3. `OutputBus.java` - Output bus handling
4. `DiagBus.java` - Diagram bus visualization

### Utility Classes
1. `Functions.java` - General utility functions
2. `NumericTextField.java` - Numeric text input handling
3. `MsgBox.java` - Message box dialogs
4. `InputCheck.java` - Input validation

### Animation & Simulation
1. `Anim.java` - Animation base class
2. `AnimThrd.java` - Animation thread handling
3. `Simulator.java` - Simulation controller

### XML Handling
1. `ParseSimXML.java` - XML parsing for simulation
2. `EditXML.java` - XML editing functionality
3. `LoadSim.java` - Simulation loading from XML

## Reading Order (Bottom-up Approach)

### Phase 1: Core Data Structures
1. `Bus.java` - Base communication ✓
2. `Instruction.java` - Basic instruction structure
3. `ProcBus.java` - Processor communication

### Phase 2: Core MIPS Implementation
4. `Assembly.java` - Assembly handling
5. `ProcFunc.java` - Processor functions
6. `ProcSim.java` - Main processor simulation

### Phase 3: GUI Foundation
7. `NumericTextField.java` - Basic input
8. `MsgBox.java` - User interaction
9. `TextEditor.java` - Code editing

### Phase 4: Simulation Components
10. `Functions.java` - Utility functions
11. `CompOperation.java` - Component operations
12. `Simulator.java` - Simulation control

### Phase 5: Visualization
13. `DiagCanvas.java` - Diagram rendering
14. `ViewSim.java` - Simulation view
15. `CompFrame.java` - Component visualization

### Phase 6: XML & Configuration
16. `ParseSimXML.java` - Configuration parsing
17. `EditXML.java` - Configuration editing
18. `LoadSim.java` - Simulation loading 
# GLOBAL.md

## System Architecture

### 1. Bus Communication System
The simulator implements a hierarchical bus system for component communication:

#### Base Structure (`Bus.java`)
- Abstract base class defining core bus properties
- 32-bit width matching MIPS architecture
- Supports both binary and string value representation
- Named output identification system

#### Bus Hierarchy
1. **Processor Bus** (`ProcBus.java`)
   - Handles actual MIPS processor communication
   - Supports multiple destinations
   - Implements conditional operation
   - Manages component connectivity
   - Integrates with visualization system

2. **Diagram Bus** (`DiagBus.java`)
   - Visualization layer for bus connections
   - Handles animation and display
   - Represents physical connections

3. **Input/Output Buses** (`InputBus.java`, `OutputBus.java`)
   - Specialized buses for I/O operations
   - Interface with external components

#### Bus Features
1. **Data Transfer**
   - 32-bit data paths
   - Binary and string representations
   - Named connection points

2. **Connectivity**
   - One-to-many relationships
   - Dynamic connection management
   - Named input/output ports

3. **Conditional Operation**
   - Optional bus activation
   - Dependency-based behavior
   - Conditional data flow

### 2. Assembly System
The simulator provides a comprehensive assembly language processing system:

#### Editor Component (`Assembly.java`)
- Integrated text editor for assembly code
- File management capabilities
- Syntax highlighting and error reporting
- Assembly to machine code conversion

#### Assembly Processing
1. **Parsing**:
   - Line-by-line text processing
   - Label and comment extraction
   - Directive handling
   - Instruction validation

2. **Code Generation**:
   - R-format instruction support
   - I-format instruction support
   - J-format instruction support
   - Register name resolution
   - Machine code generation

3. **Validation**:
   - Instruction set checking
   - Syntax validation
   - Error reporting
   - User feedback

#### Instruction Handling
- Complete instruction representation
- Multiple format support
- Label and addressing support
- Comment preservation

### 3. Instruction System
The simulator uses a comprehensive instruction representation system:

#### Instruction Structure (`Instruction.java`)
- Complete representation of MIPS instructions
- Maintains both assembly and machine code formats
- Supports instruction metadata (labels, comments)
- Handles all MIPS instruction formats (R, I, J)

#### Processing Flow
1. **Assembly Parsing**
   - Text to instruction conversion
   - Label and comment handling
   - Parameter extraction

2. **Execution**
   - Machine code generation
   - Instruction simulation
   - State updates

3. **Visualization**
   - Instruction state display
   - Execution animation
   - Debug information

### 4. Processor Functions
The simulator provides essential utility functions for MIPS operations:

#### Binary Operations (`ProcFunc.java`)
1. **Number Manipulation**:
   - Zero extension
   - Sign extension
   - Binary formatting
   - Width management

2. **Format Handling**:
   - 32-bit number representation
   - Compact binary format
   - Sign preservation
   - Bit width control

3. **System Utilities**:
   - File operations
   - Cross-platform support
   - Layout management
   - Grid alignment

#### Integration
1. **Assembly Support**:
   - Instruction encoding
   - Register value handling
   - Binary conversion

2. **Simulation Support**:
   - Data representation
   - State management
   - Value manipulation

3. **UI Support**:
   - Component alignment
   - Visual formatting
   - Layout utilities

### 5. Main Simulation System
The simulator is built around a central controller that manages all components:

#### Core Controller (`ProcSim.java`)
1. **Application Management**:
   - Component initialization
   - Event coordination
   - System configuration
   - Resource management

2. **User Interface**:
   - Main window management
   - Component layout
   - Event handling
   - Visual feedback

3. **Integration Hub**:
   - Assembly system coordination
   - Simulation control
   - Visualization management
   - Debug system

#### Component Interaction
1. **Assembly Integration**:
   - Code editor management
   - Assembly processing
   - Machine code generation
   - Error handling

2. **Simulation Control**:
   - Processor state management
   - Execution control
   - Component coordination
   - Data flow management

3. **Visualization**:
   - Component display
   - Animation control
   - Status feedback
   - Debug output

### 6. User Interface Components
The simulator provides a comprehensive set of specialized UI components:

#### Core Windows
1. **Main Window** (`ProcSim.java`):
   - Application entry point
   - Main control buttons
   - Component coordination
   - Status display

2. **Simulation View** (`ViewSim.java`):
   - Processor visualization
   - Control panel
   - Animation controls
   - Component viewers

3. **Architecture Editor** (`LoadSim.java`):
   - Processor design interface
   - Component placement
   - Bus connections
   - Grid alignment

#### Text Editors
1. **Base Editor** (`TextEditor.java`):
   - Abstract base class
   - File operations
   - Undo/redo support
   - Find functionality

2. **Assembly Editor** (`Assembly.java`):
   - MIPS code editing
   - Syntax support
   - Assembly controls
   - Machine code generation

3. **XML Editor** (`EditXML.java`):
   - Architecture editing
   - XML validation
   - Component configuration
   - Layout management

#### Specialized Components
1. **Numeric Input** (`NumericTextField.java`):
   - Filtered numeric entry
   - Real-time validation
   - Control character support
   - Clean user experience

2. **Component Frames** (`CompFrame.java`):
   - Register display
   - Memory visualization
   - Value inspection
   - Dynamic updates

3. **Message Dialogs** (`MsgBox.java`):
   - User notifications
   - Confirmation dialogs
   - Error messages
   - Clean interaction

4. **Console Window** (`Console.java`):
   - Output redirection
   - Error tracking
   - Message filtering
   - Debug support

#### Integration Features
1. **Event Handling**:
   - Consistent listener pattern
   - Action coordination
   - State management
   - User interaction

2. **Visual Feedback**:
   - Real-time updates
   - Animation support
   - Status indicators
   - Error highlighting

3. **Layout Management**:
   - Flexible positioning
   - Component organization
   - Screen adaptation
   - Resolution handling

#### Notable Characteristics
1. **User Experience**:
   - Intuitive controls
   - Immediate feedback
   - Error prevention
   - Clean interaction

2. **Integration**:
   - Component coordination
   - Event propagation
   - State synchronization
   - Resource management

3. **Customization**:
   - Visual options
   - Behavior settings
   - Layout control
   - Display preferences

### 7. Project Structure
The project is organized into several key subsystems:

#### Core MIPS Implementation
- Handles actual MIPS processor simulation
- Manages instruction execution and data flow
- Implements processor components and their interactions

#### GUI Layer
- Provides visual representation of the processor
- Enables user interaction with the simulation
- Displays real-time state of components and buses

#### Animation System
- Visualizes data flow through buses
- Animates processor operations
- Helps in understanding processor behavior

#### Configuration Management
- XML-based configuration system
- Supports loading and saving processor states
- Enables customization of simulator behavior

### 8. Design Patterns
The system appears to use several design patterns:

1. **Abstract Factory**
   - Base classes for major components
   - Specialized implementations for specific needs

2. **Observer Pattern**
   - Components observe bus states
   - GUI updates based on state changes

3. **Composite Pattern**
   - Hierarchical organization of components
   - Tree-like structure for processor elements

4. **Data Container Pattern**
   - Clean separation of data and processing
   - Centralized data structures
   - External processing logic

5. **Bridge Pattern**
   - Separation of bus implementation from visualization
   - Decoupled component interaction
   - Flexible communication system 
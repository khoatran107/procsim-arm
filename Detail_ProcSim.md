# Detail_ProcSim.md

## File: ProcSim.java
**Purpose**: Main controller class for the MIPS processor simulator, managing the GUI interface and coordinating between different components of the simulation.

### Class Structure
```java
class ProcSim extends Frame implements ActionListener {
    // Debug flags
    static boolean CONSOLE = true;
    static boolean SHOWOUT = false;
    static boolean SHOWERR = false;
    static boolean HOLDOUT = false;

    // Core components
    Simulator sim;
    Assembly assembly;
    LoadSim loadSim;
    DiagCanvas diagCanvas;
    ViewSim viewSim;
    Console console;
    static HelpViewer helpViewer;

    // UI Components
    Button butAddCode;
    Button butLoadSim;
    Button butViewSim;
    Button butConsole;
    Button butExit;
    Panel mainPanel;
    Panel buttonPanel;
    Label lblAssem;
    Label lblSim;
    Label lblSimName;
}
```

### Key Features
1. **Application Control**:
   - Main entry point
   - Component initialization
   - Event handling
   - System configuration

2. **GUI Management**:
   - Window setup
   - Component layout
   - Event listeners
   - Visual feedback

3. **Simulation Coordination**:
   - Assembly code handling
   - Processor simulation
   - Visual representation
   - Debug output

### Major Methods

1. **Initialization**:
   ```java
   public ProcSim()
   public ProcSim(boolean z)
   private void setupFrame(boolean z)
   ```
   - Creates main application window
   - Initializes components
   - Sets up UI layout

2. **Event Handling**:
   ```java
   public void actionPerformed(ActionEvent actionEvent)
   ```
   - Handles button clicks
   - Controls component visibility
   - Manages simulation flow

3. **Output Control**:
   ```java
   public static void out(String str)
   public static void outErr(String str)
   public static void outLine(String str)
   ```
   - Debug output management
   - Error reporting
   - Console integration

### Related Files
1. `Assembly.java` - Assembly code handling
2. `Simulator.java` - Core simulation
3. `DiagCanvas.java` - Visual representation
4. `ViewSim.java` - Simulation visualization

### Usage Context
- Application entry point
- Main window management
- Component coordination
- User interaction handling

### Implementation Details
1. **Window Management**:
   - Frame-based main window
   - Component layout management
   - Event handling system
   - Visual styling

2. **Component Integration**:
   - Assembly editor connection
   - Simulator coordination
   - Visual canvas management
   - Console integration

3. **Debug System**:
   - Configurable output levels
   - Error tracking
   - Console redirection
   - Status reporting

### Integration Points
1. **Assembly System**:
   - Code editor integration
   - Assembly processing
   - Machine code generation

2. **Simulation System**:
   - Processor simulation
   - State management
   - Execution control

3. **Visualization System**:
   - Diagram canvas
   - Component display
   - Animation control

### Notable Features
1. **User Interface**:
   - Clean, organized layout
   - Intuitive controls
   - Visual feedback
   - Help system

2. **Debug Support**:
   - Configurable output
   - Error tracking
   - Console integration
   - Status reporting

3. **Cross-platform**:
   - OS detection
   - Platform-specific adjustments
   - Consistent behavior
   - Resource management 
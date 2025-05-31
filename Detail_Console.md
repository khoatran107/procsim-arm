# Detail_Console.md

## File: Console.java
**Purpose**: Implements a specialized console window for capturing and displaying system output and error messages in the MIPS simulator, providing real-time debug information and error tracking.

### Class Structure
```java
public class Console extends WindowAdapter 
    implements WindowListener, Runnable, ActionListener {
    // Core Components
    public JFrame frame;
    private JTextArea textArea;
    private JTextArea textAreaErr;
    private JScrollPane scroll1;
    private JScrollPane scroll2;
    
    // Thread Management
    private Thread reader;
    private Thread reader2;
    private boolean quit;
    
    // Stream Management
    private final PipedInputStream inStream;
    private final PipedInputStream inStream2;
    
    // UI Components
    private JPanel panel;
    private JPanel btnPanel;
    private JButton butOut;
    private JButton butErr;
}
```

### Key Features
1. **Output Redirection**:
   - System.out capture
   - System.err capture
   - Real-time display
   - Message filtering

2. **Display Management**:
   - Split view interface
   - Scrollable output
   - Toggle visibility
   - Clear functionality

3. **Thread Safety**:
   - Asynchronous processing
   - Safe stream handling
   - Clean shutdown
   - Resource management

### Major Methods

1. **Initialization**:
   ```java
   public Console(ProcSim procSim)
   ```
   - Creates console window
   - Sets up streams
   - Initializes components
   - Starts reader threads

2. **Panel Management**:
   ```java
   public void setupPanels()
   public void updatePanels()
   ```
   - Panel configuration
   - Visibility control
   - Layout management
   - Display updates

3. **Event Handling**:
   ```java
   public void actionPerformed(ActionEvent)
   public void windowClosed(WindowEvent)
   public void windowClosing(WindowEvent)
   ```
   - Button responses
   - Window management
   - Clean shutdown
   - Resource cleanup

### Related Files
1. `ProcSim.java` - Main application
2. `Assembly.java` - Code processing
3. `ViewSim.java` - Simulation view
4. `LoadSim.java` - Architecture loading

### Usage Context
- Debug output display
- Error message tracking
- System message capture
- Development support

### Implementation Details
1. **Stream Management**:
   - Piped stream setup
   - System output capture
   - Error stream handling
   - Buffer management

2. **Thread Control**:
   - Reader thread creation
   - Daemon thread usage
   - Synchronization
   - Safe termination

3. **UI Organization**:
   - Split panel layout
   - Button controls
   - Scroll management
   - Visual feedback

### Integration Points
1. **System Integration**:
   - Output redirection
   - Error capture
   - Message routing
   - Status reporting

2. **UI Integration**:
   - Window management
   - Component layout
   - Event handling
   - Visual consistency

3. **Thread Integration**:
   - Asynchronous processing
   - Event dispatch
   - Resource sharing
   - State management

### Notable Features
1. **Message Control**:
   - Selective display
   - Message filtering
   - Output toggling
   - Clear functionality

2. **Visual Design**:
   - Clean interface
   - Intuitive controls
   - Clear organization
   - Effective feedback

3. **Resource Management**:
   - Safe initialization
   - Clean shutdown
   - Memory efficiency
   - Thread safety 
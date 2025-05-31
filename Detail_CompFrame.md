# Detail_CompFrame.md

## File: CompFrame.java
**Purpose**: Implements a specialized frame for displaying and managing processor component states, including registers, main memory, and instruction memory, with support for different display modes and value formatting.

### Class Structure
```java
class CompFrame extends JFrame implements ActionListener {
    // Core Components
    public JTable table;
    JScrollPane scrollPane;
    
    // Control Buttons
    JButton okButton;
    JButton hideEmptyButton;
    JButton showMachineButton;
    
    // Display State
    boolean hideEmpty;
    boolean showMachine;
    boolean showMachineOld;
    
    // UI Components
    JPanel btnPanel;
    JPanel topPanel;
    JPanel titlePanel;
    JLabel lblTitle;
    
    // Component Type
    boolean registers;
    boolean mainMem;
    boolean instrMem;
    
    // Data Management
    int MAX_VALS;
    int size;
    MyTableModel tModel;
    int[] widths;
    MyCellRenderer mcr;
}
```

### Key Features
1. **Component Display**:
   - Register visualization
   - Memory state display
   - Instruction memory view
   - Value formatting

2. **Display Control**:
   - Empty value hiding
   - Machine code toggle
   - Size management
   - Column control

3. **Visual Organization**:
   - Table-based layout
   - Custom rendering
   - Scrollable view
   - Clean interface

### Major Methods

1. **Initialization**:
   ```java
   public CompFrame(String str, int i, ViewSim viewSim)
   ```
   - Creates component frame
   - Sets up display type
   - Initializes table
   - Configures layout

2. **Display Management**:
   ```java
   public void setWidths()
   ```
   - Column width control
   - Table formatting
   - Display optimization
   - Layout adjustment

3. **Custom Components**:
   ```java
   class MyCellRenderer extends DefaultTableCellRenderer
   class MyTableModel extends AbstractTableModel
   ```
   - Cell rendering
   - Data management
   - Value formatting
   - Display control

### Related Files
1. `ViewSim.java` - Simulation view
2. `ProcSim.java` - Main application
3. `Simulator.java` - State management
4. `Assembly.java` - Code processing

### Usage Context
- Component state display
- Value inspection
- Memory monitoring
- Register tracking

### Implementation Details
1. **Table Management**:
   - Custom model
   - Cell rendering
   - Column control
   - Selection handling

2. **Display Control**:
   - Value filtering
   - Format switching
   - Size adaptation
   - Layout management

3. **Component Types**:
   - Register display
   - Main memory view
   - Instruction memory
   - Type-specific behavior

### Integration Points
1. **Simulation Integration**:
   - State synchronization
   - Value updates
   - Format conversion
   - Display coordination

2. **UI Integration**:
   - Window management
   - Component layout
   - Event handling
   - Visual consistency

3. **Data Integration**:
   - Value formatting
   - State tracking
   - Update propagation
   - Change notification

### Notable Features
1. **Display Modes**:
   - Value hiding
   - Machine code view
   - Format switching
   - Size adaptation

2. **Visual Design**:
   - Clean interface
   - Intuitive controls
   - Clear organization
   - Effective feedback

3. **Performance**:
   - Efficient updates
   - Memory management
   - Display optimization
   - Resource control 
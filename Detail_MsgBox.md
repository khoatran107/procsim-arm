# Detail_MsgBox.md

## File: MsgBox.java
**Purpose**: Implements a modal dialog box for displaying messages, confirmations, and alerts to the user, providing a consistent interface for user interaction and feedback.

### Class Structure
```java
class MsgBox extends Dialog implements ActionListener {
    // State
    boolean id;
    boolean yesno;
    
    // Controls
    Button ok;
    Button can;
    
    // Constructors
    MsgBox(Frame frame, String str, boolean z)
    MsgBox(Frame frame, String str, String str2, boolean z)
}
```

### Key Features
1. **Dialog Types**:
   - Simple messages
   - Two-line messages
   - Yes/No confirmations
   - OK acknowledgments

2. **Modal Behavior**:
   - User focus control
   - Required response
   - Clean interaction
   - State management

3. **Visual Design**:
   - Centered display
   - Clean layout
   - Consistent style
   - Clear feedback

### Major Methods

1. **Initialization**:
   ```java
   MsgBox(Frame frame, String str, boolean z)
   MsgBox(Frame frame, String str, String str2, boolean z)
   ```
   - Creates dialog
   - Sets up message
   - Configures buttons
   - Centers display

2. **Button Management**:
   ```java
   void addOKCancelPanel(boolean z)
   void createOKButton(Panel panel)
   void createCancelButton(Panel panel)
   ```
   - Button creation
   - Layout setup
   - Event binding
   - Style application

3. **Frame Setup**:
   ```java
   void createFrame()
   ```
   - Window positioning
   - Size calculation
   - Event handling
   - Display setup

### Related Files
1. `ProcSim.java` - Main application
2. `Assembly.java` - Code processing
3. `LoadSim.java` - Architecture loading
4. `ViewSim.java` - Simulation view

### Usage Context
- User notifications
- Action confirmations
- Error messages
- Status updates

### Implementation Details
1. **Dialog Creation**:
   - Modal window setup
   - Message formatting
   - Button configuration
   - Event binding

2. **Layout Management**:
   - Component organization
   - Size calculation
   - Position centering
   - Style application

3. **Event Handling**:
   - Button responses
   - Window events
   - State tracking
   - Result reporting

### Integration Points
1. **UI Integration**:
   - Window management
   - Modal behavior
   - Event propagation
   - Visual consistency

2. **User Interaction**:
   - Response capture
   - State tracking
   - Result reporting
   - Clean closure

3. **System Integration**:
   - Error reporting
   - Status updates
   - Action confirmation
   - Flow control

### Notable Features
1. **Dialog Types**:
   - Single message
   - Two-line message
   - Yes/No options
   - OK only

2. **Visual Design**:
   - Clean interface
   - Centered display
   - Clear buttons
   - Consistent style

3. **Interaction Model**:
   - Modal focus
   - Required response
   - Clear options
   - Clean closure 
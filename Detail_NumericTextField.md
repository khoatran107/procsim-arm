# Detail_NumericTextField.md

## File: NumericTextField.java
**Purpose**: Implements a specialized text field component that only accepts numeric input, providing input validation for numeric values in the MIPS simulator.

### Class Structure
```java
public class NumericTextField extends TextField {
    // Constructors
    public NumericTextField(String str, int i)
    public NumericTextField(int i)

    // Anonymous inner class for key event handling
    KeyAdapter keyListener {
        public void keyTyped(KeyEvent keyEvent)
    }
}
```

### Key Features
1. **Input Validation**:
   - Accepts only numeric characters
   - Allows control characters (backspace, delete, etc.)
   - Immediate validation during typing

2. **User Experience**:
   - Real-time feedback
   - Non-disruptive validation
   - Clean input handling

3. **Control Characters**:
   - Backspace ('\b')
   - Delete (127)
   - Newline ('\n')
   - Tab ('\t')

### Methods

1. **Constructors**:
   ```java
   public NumericTextField(String str, int i)
   ```
   - Creates field with initial text and size
   - Sets up key listener
   - Initializes validation

   ```java
   public NumericTextField(int i)
   ```
   - Creates empty field with size
   - Delegates to main constructor

2. **Event Handling**:
   ```java
   public void keyTyped(KeyEvent keyEvent)
   ```
   - Validates each keystroke
   - Filters non-numeric input
   - Allows control characters

### Related Files
1. `ViewSim.java` - Uses for numeric input
2. `LoadSim.java` - Component configuration
3. `Assembly.java` - Numeric value handling

### Usage Context
- Numeric value input
- Parameter configuration
- Size specifications
- Register values

### Implementation Details
1. **Input Processing**:
   - Character-by-character validation
   - Event consumption for invalid input
   - Control character handling

2. **Validation Logic**:
   ```java
   if (keyChar != '\b' && 
       keyChar != 127 && 
       keyChar != '\n' && 
       keyChar != '\t' && 
       !Character.isDigit(keyChar)) {
       keyEvent.consume();
   }
   ```
   - Checks for control characters
   - Validates numeric input
   - Consumes invalid events

3. **Event Management**:
   - KeyAdapter implementation
   - Event consumption
   - Character filtering

### Integration Points
1. **UI Integration**:
   - Standard TextField extension
   - AWT component compatibility
   - Event system integration

2. **Input Handling**:
   - Real-time validation
   - Error prevention
   - User feedback

3. **Value Management**:
   - Numeric constraints
   - Size limitations
   - Format control

### Notable Features
1. **Validation Approach**:
   - Proactive validation
   - Non-blocking design
   - Clean user experience

2. **Control Support**:
   - Navigation keys allowed
   - Editing keys enabled
   - Format control

3. **Integration Design**:
   - Simple inheritance model
   - Standard event handling
   - Minimal overhead 
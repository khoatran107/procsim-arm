# Detail_ProcBus.md

## File: ProcBus.java
**Purpose**: Implements the processor-specific bus functionality, managing connections between processor components and handling data transfer in the MIPS simulation.

### Class Structure
```java
public class ProcBus extends Bus {
    PComponent sourceComp = new PComponent();           // Source component
    Vector<PComponent> destComps = new Vector<>();      // Destination components
    int curConTo = 0;                                   // Current connection index
    Vector<String> inNames = new Vector<>();            // Input names
    Vector<DiagBus> diagBuses = new Vector<>();        // Visual representations
    String[] tmpDestComps = new String[100];           // Temporary component storage
    int curDestComp = 0;                               // Current destination index
    boolean showStrVal = false;                        // Display string value flag
    boolean doneAnimOnce = false;                      // Animation state
    boolean optional = false;                          // Optional bus flag
    ProcBus optDependsOn = null;                      // Dependency bus
    String optIf = "0";                               // Dependency condition
}
```

### Key Features
1. **Component Connectivity**:
   - Single source component
   - Multiple destination components
   - Named input/output connections

2. **Visualization Support**:
   - Links to diagram buses
   - Animation state tracking
   - String value display option

3. **Conditional Operation**:
   - Optional bus functionality
   - Dependency-based activation
   - Conditional value checking

### Methods
1. **Constructor**:
   ```java
   ProcBus(String str) {
       this.outName = str;
   }
   ```
   - Initializes bus with output name

2. **checkOptional()**:
   ```java
   public boolean checkOptional() {
       if (this.optional) {
           return this.optDependsOn == null || 
                  this.optDependsOn.binaryValue.equals(this.optIf);
       }
       return false;
   }
   ```
   - Checks if optional bus should be active
   - Based on dependency and condition

### Related Files
1. `Bus.java` - Parent class
2. `PComponent.java` - Connected components
3. `DiagBus.java` - Visualization elements

### Usage Context
- Core component of the MIPS processor simulation
- Manages data flow between processor components
- Supports conditional and optional connections
- Enables visualization of data transfer

### Implementation Details
- Extends base Bus class
- Uses vectors for dynamic connections
- Supports temporary component storage
- Implements conditional bus behavior

### Integration Points
- Processor component connections
- Data transfer management
- Visualization system
- Conditional execution support

### Notable Features
1. **Dynamic Connectivity**:
   - Multiple destination support
   - Named connection points
   - Flexible component linking

2. **Visualization Integration**:
   - Direct connection to diagram system
   - Animation state tracking
   - Visual representation management

3. **Conditional Behavior**:
   - Optional bus activation
   - Dependency-based operation
   - Conditional value checking 
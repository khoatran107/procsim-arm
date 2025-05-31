# Detail_Bus.md

## File: Bus.java
**Purpose**: Serves as the abstract base class for all bus types in the MIPS simulator, providing core bus functionality and data structures.

### Class Structure
```java
abstract class Bus {
    int bits = 32;                // Number of bits in the bus (default 32)
    String outName = "None";      // Output name identifier
    String binaryValue = "";      // Binary value being transmitted
    String strValue = "";         // String representation of the value
}
```

### Key Features
1. **Data Width**: Fixed at 32 bits, matching MIPS architecture
2. **Value Representation**: 
   - Binary form (`binaryValue`)
   - String form (`strValue`)
3. **Identification**: Uses `outName` to identify the bus output

### Related Files
1. `ProcBus.java` - Processor-specific bus implementation
2. `DiagBus.java` - Visualization/diagram bus implementation
3. `InputBus.java` - Input bus handling
4. `OutputBus.java` - Output bus handling

### Usage Context
- Base class for the bus hierarchy in the simulator
- Provides fundamental bus properties used throughout the system
- Abstract class indicating specialized implementations are required

### Implementation Details
- The class is kept minimal and abstract to allow for different bus implementations
- 32-bit width is hardcoded to match MIPS architecture requirements
- String representations allow for both binary and human-readable values
- No methods are defined, leaving implementation details to child classes

### Integration Points
- Forms the foundation of the simulator's communication system
- Used by processor components for data exchange
- Supports both simulation and visualization layers
- Enables consistent bus handling across the application 
# Detail_ProcFunc.md

## File: ProcFunc.java
**Purpose**: Provides utility functions for MIPS processor operations, particularly focusing on binary number manipulation and file operations.

### Class Structure
```java
class ProcFunc {
    // Static utility methods only
    // No instance variables or constructors
}
```

### Key Methods

1. **Binary Number Extension**:
   ```java
   public static String zeroExtend(String str, int i)
   ```
   - Extends binary number with leading zeros
   - Maintains specified bit width
   - Handles truncation if necessary

2. **Binary Number Formatting**:
   ```java
   static String slimBinary(String str)
   public static String slimBinary(String str, boolean z, boolean z2)
   ```
   - Formats 32-bit binary numbers
   - Removes leading zeros
   - Optional formatting options

3. **Sign Extension**:
   ```java
   public static String signExtend(String str, int i)
   ```
   - Extends binary number preserving sign
   - Handles positive and negative numbers
   - Supports arbitrary bit widths

4. **Rounding**:
   ```java
   public static int round(int i, int i2)
   ```
   - Rounds numbers to nearest multiple
   - Used for grid alignment
   - Handles negative numbers

5. **File Operations**:
   ```java
   public static void copyFile(String str, String str2)
   public static String fileSep()
   ```
   - File copying utility
   - System-independent path handling

### Key Features
1. **Binary Operations**:
   - Zero extension
   - Sign extension
   - Binary formatting
   - Width management

2. **Number Handling**:
   - Binary string manipulation
   - Numeric rounding
   - Format conversion

3. **System Utilities**:
   - File operations
   - Path handling
   - Cross-platform support

### Implementation Details

1. **Binary String Manipulation**:
   ```java
   // Zero Extension
   "0" + originalString   // Prepends zeros

   // Sign Extension
   "1" + originalString   // Prepends ones for negative
   "0" + originalString   // Prepends zeros for positive
   ```

2. **Binary Formatting**:
   ```java
   // Slim Format Examples
   "00001010" → "0..1010"  // Standard format
   "00000000" → "0..0"     // All zeros
   ```

3. **File Operations**:
   ```java
   // Buffered File Copy
   byte[] buffer = new byte[1024];
   while ((bytesRead = in.read(buffer)) != -1) {
       out.write(buffer, 0, bytesRead);
   }
   ```

### Usage Context
- Binary number manipulation for MIPS operations
- File handling for simulator state
- UI component alignment
- Cross-platform compatibility

### Related Files
1. `Assembly.java` - Uses binary manipulation
2. `ProcSim.java` - Core simulation functions
3. `Functions.java` - General utility functions

### Integration Points
1. **Assembly Processing**:
   - Binary number formatting
   - Instruction encoding
   - Register value handling

2. **Simulation**:
   - Value representation
   - Data manipulation
   - State management

3. **UI**:
   - Component alignment
   - Grid snapping
   - Layout management

### Notable Features
1. **Binary Manipulation**:
   - Efficient string operations
   - Format preservation
   - Width management
   - Sign handling

2. **System Independence**:
   - Cross-platform file operations
   - Path separation handling
   - Buffer management

3. **Performance Considerations**:
   - Buffered file operations
   - Efficient string manipulation
   - Optimized number handling 
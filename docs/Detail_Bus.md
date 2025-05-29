## File: `Bus.java`

**General Purpose:** This file defines the `Bus` abstract class, which serves as a base class for various types of buses used in the MIPS processor simulation. It establishes common properties for buses.

**Abstract Class: `Bus`**

*   **Fields:**
    *   `int bits = 32;`: Represents the width of the bus in bits. It's initialized to 32, which is typical for MIPS architecture.
    *   `String outName = "None";`: A name for the bus output, initialized to "None". This might be used for identification or display purposes.
    *   `String binaryValue = "";`: Stores the current value on the bus as a binary string. Initialized to an empty string.
    *   `String strValue = "";`: Stores the current value on the bus as a generic string (could be decimal, hex, or other formats depending on context). Initialized to an empty string.

*   **Methods:** None defined in this abstract class. Subclasses are expected to implement specific bus behaviors.

**Relationship to other files:**

*   This abstract class is intended to be extended by other more specific bus classes. Based on the file list, potential subclasses are:
    *   `InputBus.java`
    *   `OutputBus.java`
    *   `ProcBus.java`
    *   `DiagBus.java`
*   Components that interact with buses (e.g., ALU, Memory, Registers, which would be part of `PComponent.java` or similar) will likely reference instances of these bus subclasses.

**Summary:** The `Bus.java` file provides a foundational abstract class for representing data pathways (buses) within the MIPS simulator. It defines common attributes like bit width and value storage, expecting concrete bus types to extend it and provide specific functionalities. 
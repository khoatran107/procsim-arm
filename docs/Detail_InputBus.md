## File: `InputBus.java`

**General Purpose:** This file defines the `InputBus` class. Contrary to what might be expected from its name and the presence of a `Bus.java` abstract class, `InputBus` does **not** extend `Bus`. Instead, it appears to represent a named input connection for a `PComponent`, specifying the source component of the input.

**Class: `InputBus`**

*   **Fields:**
    *   `String name`: The name of this input bus (e.g., "ALUSrc1", "WriteReg").
    *   `PComponent source`: A reference to the `PComponent` that is the source of the data for this input.
    *   `String value`: The current value on this input bus. The type is `String`, suggesting it might hold binary, hex, or decimal representations.

*   **Constructor:**
    *   `InputBus(String arg0, PComponent arg1)`: Initializes a new `InputBus` with a given name (`arg0`) and source component (`arg1`). The `value` field is not explicitly initialized in the constructor, so it will default to `null`.

*   **Methods:** None defined beyond the constructor.

**Relationship to other files:**

*   `PComponent.java`: `InputBus` objects hold a reference to a `source` which is a `PComponent`. This implies that `PComponent` instances will have `InputBus` objects associated with them, representing their inputs.
*   Any class that creates or configures `PComponent` instances will likely also create and connect `InputBus` objects.
*   It is **not** directly related to `Bus.java` through inheritance.

**Observations & Questions:**

*   The name `InputBus` is somewhat misleading if it doesn't extend the `Bus` class. It seems more like an "InputPort" or "InputConnection" that carries a value from a source `PComponent`.
*   The `value` field being a `String` might require conversions if numerical operations are performed with it.
*   How the `value` field is updated is not defined in this class; it's likely managed externally by the `source` component or the simulation logic.

**Summary:** The `InputBus.java` file defines a class to represent a named input for a `PComponent`, linking it to a source component and holding its current string value. It plays a role in defining the data flow connections between processor components. 
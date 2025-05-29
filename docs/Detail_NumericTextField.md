## File: `NumericTextField.java`

**General Purpose:** This file defines a custom GUI component `NumericTextField` that extends `java.awt.TextField`. Its primary purpose is to restrict user input to only numeric characters (digits), along with essential control keys like backspace, delete, newline, and tab.

**Class: `NumericTextField` (extends `java.awt.TextField`)**

*   **Constructors:**
    *   `public NumericTextField(String str, int i)`:
        *   Calls the `super` constructor of `TextField` with the initial text `str` and number of columns `i`.
        *   Adds an anonymous `KeyAdapter` as a key listener to itself.
            *   The `keyTyped(KeyEvent keyEvent)` method of this adapter is overridden.
            *   It checks the typed character (`keyEvent.getKeyChar()`).
            *   If the character is NOT a backspace (`'\b'`), delete (ASCII 127), newline (`'\n'`), tab (`'\t'`), and is NOT a digit (`Character.isDigit(keyChar)`), then the `keyEvent` is consumed (`keyEvent.consume()`). This prevents non-numeric characters from appearing in the text field.
    *   `public NumericTextField(int i)`:
        *   A convenience constructor that calls the primary constructor `this("", i)`, creating an empty numeric text field with `i` columns.

**Relationship to other files:**

*   This class will be used by any GUI components that require numeric input from the user, for example:
    *   Setting memory addresses or values.
    *   Inputting parameters for simulation control (e.g., number of cycles).
    *   Likely used in classes like `ViewSim.java`, `CompFrame.java`, or other UI-related setup classes.

**Summary:** `NumericTextField.java` provides a specialized text input field for GUIs, ensuring that users can only enter digits and essential editing keys. This is a common utility component in applications requiring structured numeric input. 
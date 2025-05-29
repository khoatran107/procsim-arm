## File: `MsgBox.java`

**General Purpose:** This file defines the `MsgBox` class, a utility for displaying simple modal dialog boxes to the user. It extends `java.awt.Dialog` and can show messages with either an "OK" button or "Yes"/"No" buttons.

**Class: `MsgBox` (extends `Dialog`, implements `ActionListener`)**

*   **Key Fields:**
    *   `boolean id`: Stores the result of the dialog. Set to `true` if "OK" or "Yes" is pressed, otherwise remains `false` (default).
    *   `Button ok`: The "OK" or "Yes" button.
    *   `Button can`: The "No" button (only present if `yesno` is true).
    *   `boolean yesno`: A flag passed during construction. If `true`, the dialog will have "Yes" and "No" buttons. If `false`, it will have a single "OK" button.

*   **Constructors:**
    *   `MsgBox(Frame frame, String str, boolean z)`:
        *   Calls `super(frame, "Message", true)` to create a modal dialog parented to `frame`.
        *   Sets `this.yesno = z`.
        *   Sets the layout to `BorderLayout`.
        *   Adds a `Label` with the message `str` to the "Center".
        *   Calls `addOKCancelPanel(z)` to add the appropriate buttons.
        *   Packs, calls `createFrame()` for positioning and window listener, and then sets the dialog visible.
    *   `MsgBox(Frame frame, String str, String str2, boolean z)`:
        *   Similar to the first constructor.
        *   Instead of a single label, it creates a `Panel` with a `GridLayout(2, 1)` to display two messages (`str` and `str2`) one above the other, each within its own `FlowLayout` panel for centering.
        *   If `str2` is empty, its label is not added.
        *   Calls `addOKCancelPanel(z)`, packs, calls `createFrame()`, and sets visible.

*   **GUI Setup Methods:**
    *   `void addOKCancelPanel(boolean z)`:
        *   Creates a `Panel` with `FlowLayout`.
        *   Calls `createOKButton()` to add the primary button.
        *   If `this.yesno` is true, calls `createCancelButton()` to add the "No" button.
        *   Adds this button panel to the "South" of the dialog.
    *   `void createOKButton(Panel panel)`:
        *   If `this.yesno` is true, creates `this.ok` as a `Button` with label "Yes".
        *   Otherwise, creates `this.ok` as a `Button` with label "OK".
        *   Adds the button to the passed `panel` and registers `this` as its `ActionListener`.
    *   `void createCancelButton(Panel panel)`:
        *   Creates `this.can` as a `Button` with label "No".
        *   Adds the button to the passed `panel` and registers `this` as its `ActionListener`.
    *   `void createFrame()`:
        *   Gets screen dimensions to center the dialog (`setLocation(...)`).
        *   Adds a `WindowAdapter` to handle `windowClosing` events (when the user clicks the 'X' button on the dialog window) by calling `dispose()` on the dialog.

*   **Event Handling (`actionPerformed(ActionEvent actionEvent)`):**
    *   If the event source is `this.ok` (the "OK" or "Yes" button):
        *   Sets `this.id = true`.
        *   Calls `setVisible(false)` to hide and effectively close the dialog.
    *   If the event source is `this.can` (the "No" button):
        *   Calls `setVisible(false)` to hide and close the dialog. `this.id` remains `false`.

**How to Use (Inferred):**

```java
// For an OK dialog
MsgBox message = new MsgBox(parentFrame, "Operation Complete!", false);
// message.id will be true if OK was pressed, false if window closed via 'X'

// For a Yes/No dialog
MsgBox confirm = new MsgBox(parentFrame, "Are you sure?", true);
if (confirm.id) {
    // User clicked Yes
} else {
    // User clicked No or closed via 'X'
}
```

**Relationship to other files:**

*   Used by various classes throughout the application (e.g., `ProcSim`, `ViewSim`, `Assembly`, `EditXML`, `LoadSim`) to display informational messages, warnings, or simple confirmations.
*   Java AWT: Extends `java.awt.Dialog` and uses `Frame`, `Label`, `Button`, `Panel`, layouts, and event listeners.

**Summary:** `MsgBox.java` provides a straightforward way to create simple, modal message dialogs. It supports both a basic "OK" confirmation style and a "Yes/No" question style. The result of the user's interaction (OK/Yes or No/Close) can be determined by checking the public `id` field after the dialog has been dismissed. It is a common utility class for user interaction in GUI applications. 
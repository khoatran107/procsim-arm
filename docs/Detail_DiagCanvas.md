## File: `DiagCanvas.java`

**General Purpose:** This class extends `JScrollPane` and is responsible for all custom drawing of the processor architecture diagram. It renders components, buses (as paths with arrowheads), bus names, and animations of data values flowing on buses. It's used by `LoadSim` for graphical editing and by `ViewSim` (presumably) for displaying the simulation.

**Class: `DiagCanvas` (extends `JScrollPane`)**

*   **Key Fields:**
    *   `ProcSim source`: Reference to the main application instance.
    *   `ViewSim viewSim`: Reference to the simulation view window (used to determine if in view mode `b_ViewSim`).
    *   `AnimThrd animThread`: Manages animation data and state.
    *   `Vector<PComponent> comps`: Stores `PComponent` objects to be drawn.
    *   `Vector<DiagBus> buses`: Stores `DiagBus` objects (visual representation of buses) to be drawn.
    *   `int curEditBus`, `int curEditComp`: Indices of the bus or component currently being edited/highlighted (primarily used by `LoadSim`).
    *   Various `Color` and `boolean` flags for appearance (gradients, bus name visibility, transparency).

*   **Core Drawing Logic (`paint(Graphics g)`):**
    1.  **Setup:** Clears the canvas, sets anti-aliasing.
    2.  **Draw Components:** Iterates `this.comps` and calls `drawComponent()` for each.
    3.  **Draw Buses:** Iterates `this.buses` and calls `drawBus()` for each. Highlights the `curEditBus` in red if in `LoadSim` editing mode, or highlights buses with `newVal` in red during simulation view if animations are not instant.
    4.  **Draw ProcBus Endpoints:** Draws small orange circles at the start and end-points of each `DiagBus` path.
    5.  **Draw ProcBus Names:** If `showBusNames` is true, iterates `this.buses` and calls `drawBusNames()` to render the `busLabelOut` and `busLabelIn` (`DiagBus.PermText` objects) for each bus, applying transparency if enabled.
    6.  **Draw Animations:** If not `animThread.instantSpeed`, iterates through `this.animThread.anims` and calls `drawAnimation()` for each active animation.
    7.  **Draw Status:** If `b_ViewSim` is true, calls `drawStatus()` (currently empty).

*   **Detailed Drawing Methods:**
    *   `drawComponent(int index, Graphics2D g2d, Font font)`:
        *   Draws a `PComponent` as a rounded rectangle with a gradient fill.
        *   Highlights the component with a thicker red or blue border if it's selected for editing in `LoadSim`, is the source/destination of the bus being edited, or is the currently active component in an animation during simulation view.
        *   Draws the component's name centered within it.
    *   `drawBus(DiagBus bus, Graphics2D g2d)` / `drawBus(int busIndex, Graphics2D g2d)`:
        *   Sets the stroke thickness based on `bus.bits` (wider buses are thicker lines).
        *   Draws a series of connected lines based on the x,y points stored in `bus.x[]` and `bus.y[]`.
        *   Calls `getArrow()` to draw arrowheads at the end of the bus and near its start.
    *   `drawBusNames(int busIndex, Graphics2D g2d, Font font, String text, DiagBus.PermText permTextLabel)`:
        *   Draws the given `text` (a bus name label) at `permTextLabel.x, permTextLabel.y`.
        *   Draws a yellow rounded rectangle as a background for the text.
        *   Highlights the border in red if `permTextLabel.moving` is true.
    *   `drawAnimation(DiagBus bus, int busIndex, Anim anim, Font font, Graphics2D g2d)`:
        *   Renders an animation (`anim`) on a `bus`.
        *   Draws the animation string (`anim.animStr`) with a green (or lighter green) rounded rectangle background.
        *   May add a subscript "2" if the value is binary.
        *   Draws a small orange circle at the animation's current position on the bus.
    *   `getArrow(int x1, int y1, int x2, int y2, int offset, boolean atStart)`: Utility method that calculates and returns a `Polygon` representing an arrowhead for a line segment, placed with an `offset` and optionally at the `atStart` of the segment.

*   **Other Important Methods:**
    *   `calcCanvasSize(Dimension viewPortSize)`: Calculates the total dimensions required to encompass all components and bus points, then sets its own preferred size (used by the `JScrollPane`).
    *   `addComponent(PComponent comp)`, `addBus(DiagBus bus)`: Adds components/buses to the internal vectors for drawing.
    *   `findBus(String name, PComponent outComp, PComponent inComp)`: Finds a `DiagBus` connecting specific components with a given output name.
    *   `showAnim(boolean forceRedraw)`: Prepares repaint regions for active animations.
    *   `animate()`: Calls `animThread.animate()` to advance animation states.
    *   `checkConnected(PComponent comp)`: Checks if a component has any buses connected to it.

**Relationship to other files:**

*   `LoadSim.java`: Creates and uses `DiagCanvas` as the primary drawing surface for the architecture editor. `LoadSim` controls `curEditBus` and `curEditComp` and provides component/bus data.
*   `ViewSim.java`: (Presumably) uses `DiagCanvas` to display the simulation, triggering animations. `b_ViewSim` flag differentiates behavior.
*   `ProcSim.java`: Provides the top-level application context.
*   `PComponent.java`, `DiagBus.java`: These are the data objects that `DiagCanvas` renders.
*   `AnimThrd.java`, `Anim.java`: Used for managing and displaying animations on buses.
*   Swing/AWT: Extends `JScrollPane` and uses `Graphics2D` for drawing.

**Summary:** `DiagCanvas` is the heart of the visual representation in ProcSim. It's a custom-painted component that draws the processor components, the bus lines connecting them (including arrowheads), bus names, and animated data values flowing through the buses during simulation. It works closely with `LoadSim` for interactive editing and with `AnimThrd` for animations. Its `paint` method orchestrates the drawing of all these elements based on the current state of the simulator and UI. 
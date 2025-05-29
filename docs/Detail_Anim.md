## File: `Anim.java`

**General Purpose:** This class represents a single animated data value moving along a `DiagBus` on the `DiagCanvas`. It calculates its path segment by segment and updates its position frame by frame, effectively creating the visual effect of data flowing.

**Class: `Anim`**

*   **Key Fields:**
    *   `String animStr`: The string representation of the data value being animated (e.g., hex or binary).
    *   `int x`, `int y`: The current on-screen coordinates of the animation.
    *   `DiagCanvas source`: A reference to the `DiagCanvas` on which this animation is drawn.
    *   `DiagBus bus`: The `DiagBus` object whose path this animation follows.
    *   **Line Algorithm Variables:** `x0`, `y0`, `x1`, `y1` (start/end of current segment), `steep` (for Bresenham algorithm), `deltax`, `deltay`, `error`, `deltaerr`, `xx`, `yy` (current point in algorithm's orientation), `xstep`, `ystep` (step size, incorporates `frameSkip`).
    *   `int pointNum`: The index of the current line segment of the `bus` path being traversed (from `bus.x[pointNum]` and `bus.y[pointNum]` to `bus.x[pointNum+1]` and `bus.y[pointNum+1]`).
    *   `boolean animating`: Flag indicating if this specific animation is currently active and should be processed.
    *   `int frameSkip`: The number of pixels the animation should advance in each `doPlot()` call. This is dynamically calculated based on the total length of the bus and the global animation speed set in `AnimThrd`.
    *   `int length`: Total pixel length of the bus path.
    *   `int width`, `int height`: Rendered dimensions of `animStr`, set by `DiagCanvas`.
    *   `boolean changingDirection`: True when `setup()` is called for a new segment.
    *   `boolean animDimSet`: True once `DiagCanvas` has calculated and set `width`/`height`.

*   **Constructor:**
    *   `Anim(DiagCanvas diagCanvas, DiagBus diagBus)`: Initializes references to `source` and `bus`. Sets the initial position (`x`, `y`) to the start of the bus. Determines `animStr` based on `bus.bus.showStrVal` (either `bus.bus.strValue` or `ProcFunc.slimBinary(bus.bus.binaryValue)`). Calls `calcFrameSkip()`.

*   **Core Animation Logic:**
    *   `setup()`: Prepares the animation for traversing the next line segment in `this.bus.points`. It increments `pointNum`, sets up `x0,y0,x1,y1` for the new segment, and initializes variables for Bresenham's line algorithm (including handling `steep` lines by swapping axes for the algorithm).
    *   `doPlot()`: This is the main method called by `AnimThrd` in its animation loop to advance this `Anim` by one step.
        1.  Checks if the global animation speed has changed; if so, calls `calcFrameSkip()` to adjust step size.
        2.  If the animation has not reached the end of the current line segment (`xx < x1` etc.):
            *   Advances the animation along the segment using Bresenham's line algorithm (`xx += xstep; error += deltaerr; ...`).
            *   Updates `this.x` and `this.y` (actual screen coordinates) from the algorithm's `xx, yy`, un-swapping if `steep`.
        3.  If the end of the current segment is reached:
            *   If there are more segments in `this.bus.points` (`pointNum < bus.numPoints - 2`), it calls `setup()` to prepare for the next segment.
            *   If it was the last segment, it sets `this.animating = false`, snaps `this.x, this.y` to the bus's exact endpoint, and calls `this.source.animThread.doneAnim(this)` to notify `AnimThrd` that this specific animation has completed.
    *   `reset()`: Resets the animation to start again from the beginning of the bus. Sets `animating = true`, repositions `x,y` to the bus start, resets `pointNum`, reloads `animStr`, and calls `setup()`.

*   **Helper Methods:**
    *   `calcFrameSkip()`: Calculates an appropriate `frameSkip` value (pixels per step) based on the total `length` of the bus path and the current global animation `speed` from `AnimThrd`. The goal is to make animations appear to move at a perceived consistent speed regardless of bus length or global speed setting.
    *   `calcLength()`: Calculates the total pixel length of the `DiagBus` path by summing the lengths of all its line segments.
    *   `isHoriz()`, `isVert()`: Check if the current bus segment is purely horizontal or vertical. Used by `DiagCanvas` for optimizing repaint regions.

**Relationship to other files:**

*   `AnimThrd.java`: Creates and manages `Anim` objects. Calls `setup()`, `doPlot()`, and `reset()` on them. `Anim` calls `AnimThrd.doneAnim()` upon completion.
*   `DiagCanvas.java`: `Anim` gets its drawing context and bus path data from `DiagCanvas`. The `DiagCanvas` renders the `Anim` object (its `animStr` at `x,y`).
*   `DiagBus.java`: Each `Anim` is associated with a `DiagBus` and follows its defined path (`points`). It also reads the value to animate from `DiagBus.bus.strValue` or `DiagBus.bus.binaryValue`.
*   `ProcFunc.java`: Uses `ProcFunc.slimBinary()` to format binary strings for display.

**Summary:** An `Anim` object encapsulates the state and behavior of a single data value visually traversing a specific `DiagBus`. It uses Bresenham's line algorithm to move along the bus's path segment by segment, adjusting its step size (`frameSkip`) based on overall bus length and global animation speed to maintain a relatively consistent visual speed. It reports its completion back to `AnimThrd`. 
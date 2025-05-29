## File: `AnimThrd.java`

**General Purpose:** This class implements `Runnable` and manages the animation of data flow on the `DiagCanvas`. It runs in a separate thread, processing a list of animation steps provided by `ViewSim` and coordinating individual `Anim` objects that represent data moving along buses.

**Class: `AnimThrd` (implements `Runnable`)**

*   **Key Fields:**
    *   `DiagCanvas dCanv`: The canvas on which animations are drawn.
    *   `Thread t`: The animation thread itself.
    *   `Vector<ViewSim.AnimationListItem> animationList`: A list of animation steps/sequences provided by `ViewSim`. Each `AnimationListItem` specifies which buses are involved in a particular animation phase.
    *   `PComponent currentComp`, `lastComp`: Track the component associated with the current/last animation.
    *   `Vector<Anim> anims`: Holds the currently active `Anim` objects. Each `Anim` instance is responsible for animating a value on a single `DiagBus`.
    *   `boolean animating`: True if the main animation loop in `run()` is active.
    *   `int speed`: Controls the animation speed (milliseconds per frame/step).
    *   Control Flags: `stopAnim` (to terminate the current animation sequence), `pause` (to pause), `instantSpeed` (to skip visual animation and update state instantly), `step` (to enable step-by-step execution), `doOneStep`.

*   **Core Animation Lifecycle:**
    1.  **Initiation (`animate(int speed)`):**
        *   Called by an external class (likely `ViewSim` or `ProcSim`) to start or restart an animation sequence with a given `speed`.
        *   Clears any existing `anims`.
        *   If the animation thread `t` is not running, it calls `startAnim()`.
        *   If `t` is already running, it sets `stopAnim = true` to signal the current `run()` loop to exit; `startAnim()` will then typically be called by the controlling logic once the old thread terminates or by this method itself after trying to stop the old one.
    2.  **Starting a Sequence (`startAnim()`):**
        *   Retrieves the `animationList` from `dCanv.viewSim`.
        *   Processes the first `AnimationListItem` (`this.numAnimItem = 0` initially) by calling `addAnim()` for each bus specified in it.
        *   Creates and starts a new `Thread(this)` to execute the `run()` method.
    3.  **Running the Animation (`run()` method - executed in the separate thread):**
        *   Calls `setup()` on all `Anim` objects in `this.anims`.
        *   Enters a `while (calcAnimsLeft() > 0)` loop, which continues as long as there are `Anim` objects still animating:
            *   For each active `Anim` object, calls its `doPlot()` method (which should update the animation's state for one frame, e.g., move its position along a bus path).
            *   If not `instantSpeed`, `Thread.yield()`s.
            *   Sleeps for `this.speed` milliseconds (if `speed > 0` and not `instantSpeed`).
            *   Handles `pause` and `step` logic (by yielding/sleeping or waiting for `doOneStep`).
            *   Checks `stopAnim`; if true, clears `anims`, resets the canvas, and exits the thread.
            *   Calls `dCanv.showAnim()` to trigger `DiagCanvas` to repaint the necessary areas.
        *   Once the loop finishes (all `Anim` objects in the current step are done), it calls `restart()` to clean up.
    4.  **Adding Individual Animations (`addAnim(int busIndex, boolean force)`):**
        *   Creates an `Anim` object for the specified `DiagBus`.
        *   Adds it to `this.anims` if it's not a duplicate (or if `force` is true). If duplicate, resets the existing one.
        *   Sets `this.currentComp`.
    5.  **Animation Completion (`doneAnim(Anim anim)`):**
        *   Called by an `Anim` object when it finishes its path.
        *   If `calcAnimsLeft() == 0` (all anims in the current step are done), it calls `nextAnim()`.
    6.  **Proceeding to Next Step (`nextAnim()`):**
        *   If there are more items in `dCanv.viewSim.animationList`, it calls `addAnim()` for the buses in the next `AnimationListItem`.
        *   If the `animationList` is exhausted, it may loop back or attempt to get a new sequence from `dCanv.viewSim.nextCompAnim()`.
        *   Ensures the animation thread `t` is alive and starts it if not.
    7.  **Cleanup (`restart()`):**
        *   Clears the `anims` vector.
        *   Resets flags on the `DiagBus` objects that were animated (e.g., `newVal = false`).
        *   Sets `this.animating = false`.

*   **Other Methods:**
    *   `getCurrentAnimComp()`: Returns the component currently associated with an animation.
    *   `alreadyExists(Anim anim)`: Checks for duplicate `Anim` objects for the same bus.
    *   `calcWaitTime()`: Calculates an adjusted `waitTime` based on `speed` (though `waitTime` isn't directly used in the `run()` loop's sleep).

**Relationship to other files:**

*   `DiagCanvas.java`: `AnimThrd` operates on a `DiagCanvas`, calling its `showAnim()` and `resetImage()` methods. `AnimThrd` gets animation data from `DiagCanvas.viewSim`.
*   `Anim.java`: `AnimThrd` creates, manages, and calls methods (`setup`, `doPlot`, `reset`) on `Anim` objects.
*   `ViewSim.java`: Provides the `animationList` (presumably `Vector<ViewSim.AnimationListItem>`) which acts as a script for the animation sequences. Also, `ViewSim.nextCompAnim()` seems to trigger the next high-level animation phase.
*   `PComponent.java`, `DiagBus.java`: Animations are associated with these elements.
*   `ProcSim.java`: Provides global context and logging (`ProcSim.out`).

**Summary:** `AnimThrd` is the engine that drives visual animations on the `DiagCanvas`. It takes high-level animation descriptions from `ViewSim`, breaks them down into individual `Anim` tasks for each bus, and then manages the frame-by-frame progression of these animations in a separate thread. It handles speed control, pausing, stepping, and the overall sequence of animated events. 
## File: `CompOperation.java`

**General Purpose:** This file defines the `CompOperation` class, which encapsulates a specific operation that a `PComponent` can perform. It includes conditions (input checks) for the operation, the actual function to be executed (via `Functions.doOp`), and how the results are propagated to output buses and visualized.

**Class: `CompOperation`**

*   **Fields:**
    *   `String name`: Name of the operation (not explicitly initialized or used in the provided code, but good practice to have).
    *   `int functionOp`: An integer identifier for the specific function to be performed. This likely maps to a method or case in `Functions.doOp()`.
    *   `Vector<InputCheck> inputChecks`: A list of `InputCheck` objects. The operation will only be performed if all these checks pass (`doCheck()` method).
    *   `Vector<ProcBus> inputsToOp`: A list of `ProcBus` objects that provide the input values for this operation.
    *   `Vector<ProcBus> outputs`: A list of `ProcBus` objects where the results of this operation will be written.
    *   `String out`: Seems to be an output string, possibly for a specific type of output or a default value. Its exact usage in `Functions.doOp()` is not clear from this file alone.
    *   `Simulator sim`: A reference to the main `Simulator` object, providing access to global simulator state and components (like `DiagCanvas` for GUI updates).

*   **Constructors:**
    *   `CompOperation(int i, Vector<ProcBus> vector, Vector<InputCheck> vector2, Vector<ProcBus> vector3, Simulator simulator)`: Initializes the operation with its function ID (`i`), input buses (`vector`), input checks (`vector2`), output buses (`vector3`), and the simulator instance (`simulator`).
    *   `CompOperation()`: A default constructor that initializes the vector fields.

*   **Methods:**
    *   `private boolean checkToDoNewVal(PComponent pComponent)`: This method iterates through `DiagBus` instances in the simulator's `DiagCanvas`. It checks if any `DiagBus` connected to the input of `pComponent` has `newVal` set to true and its associated `ProcBus` is optional and active. If such a bus is found, it returns `false` (don't do new value propagation, perhaps to avoid redundant updates if already flagged). Otherwise, it returns `true`.
    *   `public boolean doOp()`: This is the core method to execute the operation.
        1.  Calls `doCheck()` to ensure all input conditions are met.
        2.  If checks pass, it calls `Functions.doOp(this.functionOp, this.inputsToOp, this.outputs, this.out)` to perform the actual computation. This static method returns a 2D string array `String[][]` (likely [output_index][value_or_property]).
        3.  It then iterates up to 10 potential outputs from the result of `Functions.doOp()`.
        4.  For each valid result (not "Error") that corresponds to an actual output bus:
            *   It retrieves the target `ProcBus` from the `outputs` vector.
            *   If the result string for the value (`doOp[i][1]`) is "Exit", it signals the end of execution via `this.sim.source.viewSim.endOfExectution()`.
            *   It iterates through all `DiagBus` instances to find the one associated with the current output `ProcBus`. If found, and if certain conditions are met (related to animation state `doneAnimOnce`, the result of `checkToDoNewVal`, or if the current bus value is different from the new value), it sets `diagBus.newVal = true` to trigger a visual update.
            *   Updates the `binaryValue` (after zero-extending) and `strValue` of the output `ProcBus`.
        5.  Returns `true` if the checks passed (and thus the operation was attempted).
        6.  Returns `false` if `doCheck()` failed.
    *   `public boolean doCheck()`: Iterates through all `InputCheck` objects in `inputChecks`. If any `check()` method returns `false`, this method returns `false`. Otherwise (if all checks pass), it returns `true`.
    *   `public String toString()`: Returns a string representation of the operation, determined by `Functions.toString(this.functionOp)`.

**Relationship to other files:**

*   `PComponent.java`: `PComponent`s have a list of `CompOperation`s that define their behavior.
*   `InputCheck.java`: `CompOperation` uses `InputCheck` objects to validate conditions before execution.
*   `ProcBus.java`: `CompOperation` reads from input `ProcBus`es and writes to output `ProcBus`es.
*   `Functions.java`: The core logic of the operation is delegated to the static `Functions.doOp()` method. `Functions.toString()` is used for string representation.
*   `Simulator.java`: `CompOperation` holds a reference to the `Simulator` to access global elements like `DiagCanvas` (via `sim.source.diagCanvas`) and `ViewSim` (via `sim.source.viewSim`) for GUI updates and program termination.
*   `DiagBus.java`: `CompOperation` updates `DiagBus.newVal` to trigger visual updates in the GUI.
*   `ProcFunc.java`: Uses `ProcFunc.zeroExtend()` for padding binary values.

**Observations:**

*   This class orchestrates a single computational step within a component, including condition checking, execution, and result propagation (both data and visual).
*   The interaction with `DiagBus` for setting `newVal` is crucial for the animation/visualization aspect of the simulator.
*   The hardcoded loop limit of 10 for outputs in `doOp()` suggests a fixed maximum number of outputs an operation can produce or affect.
*   The `checkToDoNewVal` method has a somewhat complex condition, aiming to optimize or control when `DiagBus.newVal` is set, likely tied to animation and preventing redundant updates.

**Summary:** `CompOperation.java` defines an executable operation within a processor component. It links input conditions (`InputCheck`), input data sources (`ProcBus`), output data sinks (`ProcBus`), and the actual computational logic (externalized to `Functions.java`). It plays a key role in the simulation cycle by performing calculations and triggering GUI updates through `DiagBus` and the main `Simulator` reference. 
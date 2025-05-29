## File: `ProcFunc.java`

**General Purpose:** This file defines the `ProcFunc` class (Processor Functions), which serves as a utility library providing various static helper methods. These functions are primarily used for string manipulation of binary numbers (zero extension, sign extension, slimming for display) and basic file operations (copying files, getting file separator).

**Class: `ProcFunc`**

*   **Static Methods:**
    *   `public static String zeroExtend(String str, int i)`:
        *   Pads the input binary string `str` with leading zeros until it reaches the target length `i`.
        *   If `str` is already of length `i`, it's returned as is.
        *   If `str` is longer than `i`, it truncates `str` from the left, effectively taking the rightmost `i` characters (least significant bits).
        *   Example: `zeroExtend("101", 8)` would return `"00000101"`.
        *   Example: `zeroExtend("11110000101", 8)` would return `"0000101"`.

    *   `static String slimBinary(String str)`:
        *   Overload for `slimBinary` that calls the more detailed version with default flags (`false`, `false`).

    *   `public static String slimBinary(String str, boolean z, boolean z2)`:
        *   Attempts to create a more compact representation of a 32-bit binary string `str`, primarily for display.
        *   If `str` is not 32 bits long and `z` is `false`, returns `str` as is.
        *   If the most significant bit (MSB) is '1', returns `str` as is (likely to preserve negative numbers in 2's complement).
        *   If MSB is '0', it tries to find the first '1' from the left in the remaining part of the string.
        *   If no '1' is found (all zeros after MSB): returns "0..0" if `z2` is `false`, or "0" if `z2` is `true`.
        *   If a '1' is found, it returns a condensed string: `MSB + ".." + remaining_significant_bits` (e.g., "0..101") if `z2` is `false`, or just `remaining_significant_bits` (e.g., "101") if `z2` is `true`.
        *   The `z` flag seems to force processing even if not 32 bits. The `z2` flag controls the exact format of the slimmed output.

    *   `public static String signExtend(String str, int i)`:
        *   Extends the input binary string `str` to the target length `i`, preserving its sign.
        *   If `str` is already of length `i`, it's returned as is.
        *   If `str` is 32 bits long, it truncates from the left to length `i` (takes rightmost `i` bits).
        *   If `str` is shorter than `i`:
            *   If `str` is empty or its MSB is '0' (positive or zero), it's padded with leading '0's.
            *   If its MSB is '1' (negative), it's padded with leading '1's.

    *   `public static int round(int i, int i2)`:
        *   If `i` (input integer) is less than or equal to 0, returns 0.
        *   Otherwise, returns `(i / 10) * 10`. This effectively rounds the integer `i` down to the nearest multiple of 10.
        *   The second parameter `i2` is **not used** in the function body.

    *   `public static void copyFile(String sourceStr, String destStr) throws Exception`:
        *   A standard utility to copy a file from `sourceStr` path to `destStr` path using `FileInputStream` and `FileOutputStream` with a 1024-byte buffer.

    *   `public static String fileSep()`:
        *   Returns the system-dependent file separator character (e.g., "\" on Windows, "/" on Linux/macOS) as a `String`.

**Relationship to other files:**

*   `Functions.java`: Uses `ProcFunc.zeroExtend()` and `ProcFunc.signExtend()` extensively for preparing binary strings for operations or display.
*   `CompOperation.java`: Indirectly uses `ProcFunc.zeroExtend()` via `Functions.doOp()`.
*   Potentially other classes that deal with binary string manipulation or need file utilities.

**Summary:** `ProcFunc.java` is a utility class that provides a collection of static helper functions. Its primary focus is on manipulating string representations of binary numbers (zero extension, sign extension, and a custom "slimming" function for display) and offering basic file system utilities like file copying and retrieving the system's file separator. These functions support various data processing and preparation tasks within the simulator. 
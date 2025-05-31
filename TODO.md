# TODO: MIPS to LEGv8 Conversion

## Overview
This document outlines the necessary changes to convert the MIPS processor simulator to support LEGv8 instructions. The conversion requires modifications to instruction formats, register handling, assembly processing, and visualization components.

## 1. Instruction System Changes

### 1.1 Instruction Format (`Instruction.java`)
- [ ] Update instruction formats to support LEGv8's formats:
  - R-format: `opcode(11) | Rm(5) | shamt(6) | Rn(5) | Rd(5)`
  - D-format: `opcode(11) | address(9) | Rn(5) | Rd(5)`
  - I-format: `opcode(10) | immediate(12) | Rn(5) | Rd(5)`
  - B-format: `opcode(6) | address(26)`
  - CB-format: `opcode(8) | address(19) | Rt(5)`
  - IW-format: `opcode(11) | immediate(16) | Rd(5)`

### 1.2 Instruction Set (`Assembly.java`)
- [ ] Implement LEGv8 instruction set support:
  - Data Transfer: `LDUR`, `STUR`, `MOVK`, `MOVZ`
  - Arithmetic: `ADD`, `SUB`, `ADDI`, `SUBI`
  - Logical: `AND`, `ORR`, `EOR`, `ANDI`, `ORRI`, `EORI`
  - Comparison: `CMP`, `CMPI`
  - Branching: `B`, `BL`, `BR`, `B.cond`, `CBZ`, `CBNZ`
  - Shift: `LSL`, `LSR`

### 1.3 Register System
- [ ] Update register naming scheme:
  - X0-X30 general-purpose registers
  - XZR (X31) zero register
  - SP stack pointer
  - PC program counter
  - NZCV flags

## 2. Assembly Processing

### 2.1 Assembly Parser (`Assembly.java`)
- [ ] Modify assembly code parsing:
  - Update instruction recognition
  - Implement new format parsing
  - Add LEGv8 syntax validation
  - Update error checking

### 2.2 Machine Code Generation
- [ ] Update binary code generation:
  - Implement LEGv8 instruction encoding
  - Update immediate value handling
  - Modify address calculation
  - Add new format support

### 2.3 Register Management
- [ ] Modify register handling:
  ```java
  public String registerConvert(String reg) {
      // Update for X0-X30, XZR, SP
  }
  ```

## 3. UI Updates

### 3.1 Register Display (`CompFrame.java`)
- [ ] Update register visualization:
  - Modify register count (31 GP + SP)
  - Update register naming
  - Add flag display (NZCV)
  - Adjust column widths

### 3.2 Instruction Display
- [ ] Update instruction visualization:
  - Modify format display
  - Update bit field highlighting
  - Add new instruction support
  - Update tooltips

### 3.3 Memory Interface
- [ ] Review memory organization:
  - Verify byte addressing
  - Update memory access patterns
  - Check alignment requirements
  - Update display format

## 4. Simulation Logic

### 4.1 Execution Engine (`Simulator.java`)
- [ ] Update instruction execution:
  - Implement LEGv8 operation logic
  - Add condition code handling
  - Update branching logic
  - Implement new addressing modes

### 4.2 State Management
- [ ] Modify processor state handling:
  - Add flag register support
  - Update PC management
  - Implement stack operations
  - Add new status tracking

## 5. Testing and Validation

### 5.1 Test Suite
- [ ] Create LEGv8 test cases:
  - Basic instruction tests
  - Addressing mode tests
  - Branch condition tests
  - Flag operation tests

### 5.2 Sample Programs
- [ ] Update example programs:
  - Convert MIPS examples to LEGv8
  - Add new LEGv8-specific examples
  - Update documentation
  - Verify functionality

## 6. Documentation

### 6.1 Code Documentation
- [ ] Update internal documentation:
  - Add LEGv8 instruction comments
  - Update method descriptions
  - Document format changes
  - Add new features

### 6.2 User Documentation
- [ ] Update user guides:
  - Add LEGv8 instruction reference
  - Update assembly syntax guide
  - Document new features
  - Add conversion notes

## 7. Configuration

### 7.1 Project Settings
- [ ] Update configuration files:
  - Modify build scripts
  - Update version numbers
  - Add new dependencies
  - Update file headers

### 7.2 Default Values
- [ ] Review default configurations:
  - Update memory size
  - Adjust register counts
  - Modify display settings
  - Update error messages

## Notes
- Maintain backward compatibility if possible
- Consider adding a mode switch between MIPS and LEGv8
- Document all changes in commit messages
- Test thoroughly before releasing
- Consider performance implications
- Keep UI consistent with existing design 
package defpackage;
// Constants and definitions equivalent to Verilog include files
class Constants {
    // From aluop.vh
    public static final long ALUOPSIZE = 6;
    public static final long ALUOP_AND = 0b00;
    public static final long ALUOP_ORR = 0b01;
    public static final long ALUOP_ADD = 0b10;
    public static final long ALUOP_EOR = 0b11;
    
    // From bus.vh
    public static final long COUNTERSIZE = 3;
    public static final long BYTESIZE = 8;
    public static final long REGADDRSIZE = 5;
    public static final long OPCODESIZE = 11;
    public static final long WORDSIZE = 64;
    public static final long INSTSIZE = 32;
    public static final long SHAMTSIZE = 6;
    public static final long SHORTSIZE = 26;
    
    // From control.vh
    public static final long CONTROLSIZE = 12;

    public static final long SIGNLOAD = 11;
    public static final long REG1LOC = 10;
    public static final long REG2LOC = 9;
    public static final long IDEX_CONTROLSIZE = 9;
    public static final long USEMOV = 8;
    public static final long ALU1SRC = 7;
    public static final long ALU2SRC = 6;
    public static final long EXMEM_CONTROLSIZE = 6;
    public static final long SETFLAGS = 5;
    public static final long MEMREAD = 4;
    public static final long MEMWRITE = 3;
    public static final long MEMWB_CONTROLSIZE = 3;
    public static final long REGWRITE = 2;
    public static final long PCTOREG = 1;
    public static final long MEMTOREG = 0;
    
    // From flags.vh
    public static final long FLAGSIZE = 4;
    public static final long NEGATIVE = 3;
    public static final long ZERO = 2;
    public static final long OVERFLOW = 1;
    public static final long CARRY = 0;
    
    // From forward.vh
    public static final long FORWARDSIZE = 2;
    public static final long FORWARDUSE = 1;
    public static final long FORWARDSRC = 0;
    
    // From memory.vh
    public static final long MEMSIZESIZE = 2;
    public static final long MEMPROGSIZE = 128;
    public static final long MEMDATASIZE = 128;
    
    // From movop.vh
    public static final long MOVOPSIZE = 3;
    public static final long MOVSHIFT00 = 0b00;
    public static final long MOVSHIFT16 = 0b01;
    public static final long MOVSHIFT32 = 0b10;
    public static final long MOVSHIFT48 = 0b11;
    
    // From opcode.vh
    public static final long SHIFT_MASK = 0b11111111110;
    public static final long SHIFT_BITSET = 0b11010011010;
    public static final long R_MASK = 0b10011110001;
    public static final long R_BITSET = 0b10001010000;
    public static final long I_MASK = 0b10011100110;
    public static final long I_BITSET = 0b10010000000;
    public static final long B_MASK = 0b01111100000;
    public static final long B_BITSET = 0b00010100000;
    public static final long BR_MASK = 0b11111111111;
    public static final long BR_BITSET = 0b11010110000;
    public static final long CB_MASK = 0b11111110000;
    public static final long CB_BITSET = 0b10110100000;
    public static final long BFLAG_MASK = 0b11111111000;
    public static final long BFLAG_BITSET = 0b01010100000;
    public static final long BRANCH_MASK = 0b00011100000;
    public static final long BRANCH_BITSET = 0b00010100000;
    public static final long LDUR_MASK = 0b00111111111;
    public static final long LDUR_BITSET = 0b00111000010;
    public static final long STUR_MASK = 0b00111111111;
    public static final long STUR_BITSET = 0b00111000000;
    public static final long MOV_MASK = 0b11011111100;
    public static final long MOV_BITSET = 0b11010010100;
    
    // From registers.vh
    public static final long XLR = 30;
    public static final long XZR = 31;
}


// Utility class for bit operations
class BitUtils {
    public static long signExtend(long value, long bits) {
        long shift = 64 - bits;
        return (value << shift) >> shift;
    }
    
    public static long mask(long value, long bits) {
        return value & ((1L << bits) - 1);
    }
    
    public static long getBit(long value, long bit) {
        return (long)((value >> bit) & 1);
    }
    
    public static long setBit(long value, long bit, long bitValue) {
        if (bitValue == 1) {
            return value | (1L << bit);
        } else {
            return value & ~(1L << bit);
        }
    }
    
    public static long getBits(long value, long high, long low) {
        return (long)((value >> low) & ((1L << (high - low + 1)) - 1));
    }
}


// Control Unit module equivalent
class ControlUnit {
    public static class ControlSignals {
        public long control;
        public long aluop;
        public long movop;
        public long memsize;
        
        public ControlSignals(long control, long aluop, long movop, long memsize) {
            this.control = control;
            this.aluop = aluop;
            this.movop = movop;
            this.memsize = memsize;
        }
    }
    
    public static ControlSignals decode(long opcode) {
        boolean op_mov, op_b, op_cb, op_bflag, op_shift, op_ldur, op_stur, op_i, op_r, op_ri, op_branch, op_restoreg;
        long ri_upper, ri_lower;
        boolean setflags;
        
        op_b = (opcode & Constants.B_MASK) == Constants.B_BITSET;
        op_cb = (opcode & Constants.CB_MASK) == Constants.CB_BITSET;
        op_bflag = (opcode & Constants.BFLAG_MASK) == Constants.BFLAG_BITSET;
        op_shift = (opcode & Constants.SHIFT_MASK) == Constants.SHIFT_BITSET;
        op_mov = (opcode & Constants.MOV_MASK) == Constants.MOV_BITSET;
        op_ldur = (opcode & Constants.LDUR_MASK) == Constants.LDUR_BITSET;
        op_stur = (opcode & Constants.STUR_MASK) == Constants.STUR_BITSET;
        op_i = (opcode & Constants.I_MASK) == Constants.I_BITSET;
        op_r = (opcode & Constants.R_MASK) == Constants.R_BITSET;
        op_ri = op_r || op_i;
        op_branch = op_cb || op_bflag || op_b;
        op_restoreg = op_mov || op_shift || op_ri;
        
        // upper 3 bits and lower 3:1 bits for R- and I- format opcodes
        ri_upper = (opcode >> (Constants.OPCODESIZE - 3)) & 0x7;
        ri_lower = (opcode >> 1) & 0x7;
        
        // whether instruction is a set-flags instruction
        setflags = ((ri_upper == 0b111 && ri_lower == 0b000) ||     // AND(I)S
                   (ri_upper == 0b101 && ri_lower == 0b100) ||      // ADD(I)S
                   (ri_upper == 0b111 && ri_lower == 0b100));       // SUB(I)S
        
        long control = 0;
        control = BitUtils.setBit(control, Constants.SIGNLOAD, ((0b10111000100 & opcode) != 0) ? 1 : 0);
        control = BitUtils.setBit(control, Constants.REG1LOC, (op_cb || op_shift || op_bflag) ? 1 : 0);
        control = BitUtils.setBit(control, Constants.REG2LOC, (op_cb || op_ldur || op_stur || op_mov) ? 1 : 0);
        control = BitUtils.setBit(control, Constants.USEMOV, op_mov ? 1 : 0);
        control = BitUtils.setBit(control, Constants.ALU1SRC, op_branch ? 1 : 0);
        control = BitUtils.setBit(control, Constants.ALU2SRC, (op_ldur || op_stur || op_i || op_branch) ? 1 : 0);
        control = BitUtils.setBit(control, Constants.SETFLAGS, (op_ri && setflags) ? 1 : 0);
        control = BitUtils.setBit(control, Constants.MEMREAD, op_ldur ? 1 : 0);
        control = BitUtils.setBit(control, Constants.MEMWRITE, op_stur ? 1 : 0);
        control = BitUtils.setBit(control, Constants.PCTOREG, (op_b && ((opcode >> (Constants.OPCODESIZE - 1)) & 1) == 1) ? 1 : 0);
        control = BitUtils.setBit(control, Constants.MEMTOREG, op_ldur ? 1 : 0);
        control = BitUtils.setBit(control, Constants.REGWRITE, 
            (BitUtils.getBit(control, Constants.PCTOREG) == 1 || 
             BitUtils.getBit(control, Constants.MEMTOREG) == 1 || 
             op_restoreg) ? 1 : 0);
        
        // control the operation to be performed by MOV
        long movop = movControl(BitUtils.getBit(opcode, Constants.OPCODESIZE - 3), opcode & 0x3);
        
        // control the operation to be performed by ALU
        long aluop = aluControl(ri_upper, ri_lower, opcode & 1, op_shift, op_ri);
        
        long memsize = memControl(opcode);
        return new ControlSignals(control, aluop, movop, memsize);
    }
    
    private static long movControl(long movkeep, long movlsb) {
        return (movkeep << 2) | movlsb;
    }
    
    private static long aluControl(long ri_upper, long ri_lower, long shiftdir, boolean op_shift, boolean op_ri) {
        boolean alu_inva, alu_invb, alu_shift, alu_shdir;
        long alu_op, alu_op_ri;
        
        alu_inva = false;
        alu_invb = op_ri && ((ri_upper >> 1) == 0b11 && ri_lower == 0b100);
        alu_shift = op_shift;
        alu_shdir = op_shift && (shiftdir == 1);
        
        if (ri_upper == 0b100 && ri_lower == 0b100) alu_op_ri = Constants.ALUOP_ADD; // ADD(I)
        else if (ri_upper == 0b110 && ri_lower == 0b100) alu_op_ri = Constants.ALUOP_ADD; // SUB(I)
        else if (ri_upper == 0b100 && ri_lower == 0b000) alu_op_ri = Constants.ALUOP_AND; // AND(I)
        else if (ri_upper == 0b101 && ri_lower == 0b000) alu_op_ri = Constants.ALUOP_ORR; // ORR(I)
        else if (ri_upper == 0b110 && ri_lower == 0b000) alu_op_ri = Constants.ALUOP_EOR; // EOR(I)
        else if (ri_upper == 0b111 && ri_lower == 0b000) alu_op_ri = Constants.ALUOP_AND; // AND(I)S
        else if (ri_upper == 0b101 && ri_lower == 0b100) alu_op_ri = Constants.ALUOP_ADD; // ADD(I)S
        else if (ri_upper == 0b111 && ri_lower == 0b100) alu_op_ri = Constants.ALUOP_ADD; // SUB(I)S
        else alu_op_ri = Constants.ALUOP_ADD;
        
        if (op_shift) alu_op = Constants.ALUOP_ORR;
        else if (op_ri) alu_op = alu_op_ri;
        else alu_op = Constants.ALUOP_ADD;
        
        return ((alu_inva ? 1 : 0) << 5) | ((alu_invb ? 1 : 0) << 4) | 
               ((alu_shift ? 1 : 0) << 3) | ((alu_shdir ? 1 : 0) << 2) | alu_op;
    }

    private static long memControl(long opcode) {
        return opcode >> 9;
    }
}
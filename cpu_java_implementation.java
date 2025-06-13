


// Flags Register module equivalent
class FlagsRegister {
    private int flags = 0;
    
    public void update(boolean setflags, int flagstoset) {
        if (setflags) {
            this.flags = flagstoset & 0xF;
        }
    }
    
    public void reset() {
        this.flags = 0;
    }
    
    public int getFlags() {
        return flags;
    }
}


// Program Memory module equivalent
class ProgramMemory {
    private byte[] data = new byte[Constants.MEMPROGSIZE];
    
    public void loadProgram(byte[] program) {
        System.arraycopy(program, 0, data, 0, Math.min(program.length, data.length));
    }
    
    public int getInstruction(long pc) {
        int instruction = 0;
        for (int i = 0; i < 4; i++) {
            instruction = (instruction << 8) | (data[(int)(pc + i)] & 0xFF);
        }
        return instruction;
    }
}


// Program Counter module equivalent
class ProgramCounter {
    private long pc = 0;
    
    public void update(boolean stall, boolean branch, long branchpc) {
        if (!stall) {
            pc = branch ? branchpc : pc + 4;
        }
    }
    
    public void reset() {
        pc = 0;
    }
    
    public long getPC() {
        return pc;
    }
}

// Register File module equivalent
class RegisterFile {
    private long[] registers = new long[1 << Constants.REGADDRSIZE];
    
    public void reset() {
        for (int i = 0; i < registers.length; i++) {
            registers[i] = 0;
        }
    }
    
    public long read(int index) {
        return registers[index & 0x1F];
    }
    
    public void write(int index, long value, boolean wren) {
        if (wren && (index & 0x1F) != Constants.XZR) {
            registers[index & 0x1F] = value;
        }
    }
}


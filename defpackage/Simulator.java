package defpackage;

import java.util.Vector;

/* loaded from: ProcSim.jar:Simulator.class */
class Simulator {
    ProcSim source;
    DiagCanvas dCanv;
    String[] registers;
    String name = "None";
    String path = "MIPS R2000 Just R-Format.xml";
    Vector<PComponent> comps = new Vector<>();
    Vector<ProcBus> oddInBuses = new Vector<>();
    boolean resetEveryRound = true;
    int mainMemSize = 10000;
    String lastChangedReg = "-1";
    int lastChangedMem = -1;
    int execInstr = 0;
    String[] mainMemory = new String[this.mainMemSize];

    public Simulator(ProcSim procSim) {
        for (int i = 0; i < this.mainMemSize; i++) {
            this.mainMemory[i] = "";
        }
        this.registers = new String[32];
        for (int i2 = 0; i2 < 32; i2++) {
            this.registers[i2] = "";
        }
        this.source = procSim;
        Functions.sim = this;
    }

    public void resetMemoryAndRegs() {
        for (int i = 0; i < this.mainMemSize; i++) {
            this.mainMemory[i] = "";
        }
        for (int i2 = 0; i2 < 32; i2++) {
            this.registers[i2] = "";
        }
    }
    
    public String getDoubleWordMem(long address) {
        int i = (int)address;
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < 8; j++) {
            result.append(ProcFunc.signExtend(this.mainMemory[i + j], 8));
        }
        return result.toString();
    }
    
    public void setDoubleWordMem(long address, String str) {
        int i = (int)address;
        for (int j = 0; j < 8; j++) {
            this.mainMemory[i + j] = str.substring(j * 8, (j + 1) * 8);
        }
        this.lastChangedMem = i;
    }

    public String getWordMem(long address) {
        int i = (int)address;
        return ProcFunc.signExtend(this.mainMemory[i], 8) + ProcFunc.signExtend(this.mainMemory[i + 1], 8) + ProcFunc.signExtend(this.mainMemory[i + 2], 8) + ProcFunc.signExtend(this.mainMemory[i + 3], 8);
    }

    public void setWordMem(long address, String str) {
        int i = (int)address;
        this.mainMemory[i] = str.substring(0, 8);
        this.mainMemory[i + 1] = str.substring(8, 16);
        this.mainMemory[i + 2] = str.substring(16, 24);
        this.mainMemory[i + 3] = str.substring(24, 32);
        this.lastChangedMem = i;
    }

    public void clear() {
        this.comps.clear();
        this.oddInBuses.clear();
        if (this.source.loadSim != null && this.source.loadSim.diagCanvas != null) {
            if (this.source.loadSim.diagCanvas.comps != null) {
                this.source.loadSim.diagCanvas.comps.clear();
            }
            if (this.source.loadSim.diagCanvas.buses != null) {
                this.source.loadSim.diagCanvas.buses.clear();
            }
        }
    }

    public String getFilePath(String str) {
        for (int length = str.length() - 1; length >= 0; length--) {
            if (str.substring(length, length + 1).equals(ProcFunc.fileSep())) {
                return str.substring(0, length + 1);
            }
        }
        return ".\\";
    }

    public String getFileName(String str) {
        for (int length = str.length() - 1; length >= 0; length--) {
            if (str.substring(length, length + 1).equals(ProcFunc.fileSep())) {
                return str.substring(length + 1, str.length());
            }
        }
        return str;
    }

    public Vector<DiagBus> findDiagBuses(ProcBus procBus) {
        Vector<DiagBus> vector = new Vector<>();
        for (int i = 0; i < this.source.diagCanvas.buses.size(); i++) {
            DiagBus diagBus = this.source.diagCanvas.buses.get(i);
            if (diagBus.bus.equals(procBus)) {
                vector.add(diagBus);
            }
        }
        return vector;
    }

    public void setRegister(int regNum, String value) {
        if (regNum != 31) 
            this.registers[regNum] = value;
    }
}
package defpackage;

import java.util.Vector;

/* loaded from: ProcSim.jar:Simulator.class */
class Simulator {
    ProcSim source;
    DiagCanvas dCanv;
    String[] registers;
    String name = "None";
    String path = "./examples/Full_Instructions.xml";
    Vector<PComponent> comps = new Vector<>();
    Vector<ProcBus> oddInBuses = new Vector<>();
    boolean resetEveryRound = true;
    int mainMemSize = 10000;
    String lastChangedReg = "-1";
    int lastChangedMem = -1;
    int execInstr = 0;
    DataMemory dataMemory = new DataMemory();
    FlagsRegister flagsRegister = new FlagsRegister();

    public Simulator(ProcSim procSim) {
        dataMemory.reset();
        this.registers = new String[32];
        for (int i2 = 0; i2 < 32; i2++) {
            this.registers[i2] = "";
        }
        this.source = procSim;
        Functions.sim = this;
    }

    public void resetMemoryAndRegs() {
        this.execInstr = 0;
        dataMemory.reset();
        for (int i2 = 0; i2 < 32; i2++) {
            this.registers[i2] = "";
        }
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
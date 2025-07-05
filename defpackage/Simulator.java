package defpackage;

import java.util.Vector;

/* loaded from: ProcSim.jar:Simulator.class */
class Simulator {
    ProcSim source;
    DiagCanvas dCanv;
    String name = "None";
    String path = "./examples/Full_Instructions.xml";
    Vector<PComponent> comps = new Vector<>();
    Vector<ProcBus> oddInBuses = new Vector<>();
    boolean resetEveryRound = true;
    int mainMemSize = 10000;
    String lastChangedReg = "-1";
    int lastChangedMem = -1;

    int execInstr = 0;
    String[] registers; // registers
    DataMemory dataMemory = new DataMemory();
    FlagsRegister flagsRegister = new FlagsRegister();

    // Backup storage
    String busToPCBackup;
    private String[] registersBackup;
    private DataMemory dataMemoryBackup;
    private FlagsRegister flagsRegisterBackup;
    
    /**
     * Saves the current state of the simulator to backup storage
     */
    public void saveBackup() {
        for (int i = 0; i < comps.size(); i++) {
            if (comps.get(i).isStartComp) {
                busToPCBackup = comps.get(i).operations.get(0).inputsToOp.get(0).binaryValue;
                break;
            }
        }
        // Backup registers (deep copy)
        if (registers != null) {
            registersBackup = new String[registers.length];
            System.arraycopy(registers, 0, registersBackup, 0, registers.length);
        } else {
            registersBackup = null;
        }
        
        // Backup data memory (assuming DataMemory has a copy constructor or clone method)
        // If DataMemory doesn't have these, you'll need to implement a deep copy method
        if (dataMemory != null) {
            dataMemoryBackup = dataMemory.clone(); // or new DataMemory(dataMemory)
        } else {
            dataMemoryBackup = null;
        }
        
        // Backup flags register (assuming FlagsRegister has a copy constructor or clone method)
        // If FlagsRegister doesn't have these, you'll need to implement a deep copy method
        if (flagsRegister != null) {
            flagsRegisterBackup = flagsRegister.clone(); // or new FlagsRegister(flagsRegister)
        } else {
            flagsRegisterBackup = null;
        }
    }
    
    /**
     * Restores the simulator state from backup storage
     * @throws IllegalStateException if no backup has been saved
     */
    public void applyBackup() {
        for (int i = 0; i < comps.size(); i++) {
            if (comps.get(i).isStartComp) {
                comps.get(i).operations.get(0).inputsToOp.get(0).binaryValue = busToPCBackup;
                break;
            }
        }

        if (registersBackup == null && dataMemoryBackup == null && flagsRegisterBackup == null) {
            throw new IllegalStateException("No backup available to restore");
        }
        
        // Restore registers (deep copy)
        if (registersBackup != null) {
            registers = new String[registersBackup.length];
            System.arraycopy(registersBackup, 0, registers, 0, registersBackup.length);
        } else {
            registers = null;
        }
        
        // Restore data memory
        if (dataMemoryBackup != null) {
            dataMemory = dataMemoryBackup.clone(); // or new DataMemory(dataMemoryBackup)
        } else {
            dataMemory = new DataMemory();
        }
        
        // Restore flags register
        if (flagsRegisterBackup != null) {
            flagsRegister = flagsRegisterBackup.clone(); // or new FlagsRegister(flagsRegisterBackup)
        } else {
            flagsRegister = new FlagsRegister();
        }
    }
    
    /**
     * Checks if a backup is available
     * @return true if a backup has been saved, false otherwise
     */
    public boolean hasBackup() {
        return registersBackup != null || dataMemoryBackup != null || flagsRegisterBackup != null;
    }
    
    /**
     * Clears the backup storage
     */
    public void clearBackup() {
        registersBackup = null;
        dataMemoryBackup = null;
        flagsRegisterBackup = null;
    }

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
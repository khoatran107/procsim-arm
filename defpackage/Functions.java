package defpackage;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.event.TableModelEvent;

import defpackage.ControlUnit.ControlSignals;

/* loaded from: ProcSim.jar:Functions.class */
public class Functions {
    public static Simulator sim;

    Functions() {
    }

    public static String toBin(long i) {
        String binaryString = Long.toBinaryString(i);
        if (binaryString.length() == 64) {
            return binaryString;
        }
        return ProcFunc.zeroExtend(binaryString, 64);
    }

    public static String toDec(String str, boolean z) {
        if (str.equals("")) {
            str = "0";
        }
        if (str.substring(0, 1).equals("0")) {
            return Long.toString(Long.parseLong(str, 2));
        }
        if (z) {
            String str2 = "";
            boolean z2 = false;
            for (int length = str.length() - 1; length >= 0; length--) {
                String substring = str.substring(length, length + 1);
                if (z2) {
                    substring = substring.equals("0") ? "1" : "0";
                }
                str2 = substring + str2;
                if (substring.equals("1")) {
                    z2 = true;
                }
            }
            return Long.toString(-Long.parseLong(str2, 2));
        }
        try {
            if (str.equals("")) {
                str = "0";
            }
            return Long.toString(Long.parseLong(str, 2));
        } catch (Exception e) {
            ProcSim.outErr("Error: Cannot convert bin to dec: " + str);
            return "0";
        }
    }

    public static String toDec(String str) {
        if (str.length() != 64) {
            try {
                if (str.equals("")) {
                    str = "0";
                }
                return Long.toString(Long.parseLong(str, 2));
            } catch (Exception e) {
            }
        } else if (str.substring(0, 1).equals("0")) {
            return Long.toString(Long.parseLong(str, 2));
        }
        String str2 = "";
        boolean z = false;
        for (int length = str.length() - 1; length >= 0; length--) {
            String substring = str.substring(length, length + 1);
            if (z) {
                substring = substring.equals("0") ? "1" : "0";
            }
            str2 = substring + str2;
            if (substring.equals("1")) {
                z = true;
            }
        }
        return Long.toString(-Long.parseLong(str2, 2));
    }

    private static boolean checkAllowFunc(ProcBus procBus) {
        if (!procBus.checkOptional()) {
            return true;
        }
        Vector<DiagBus> findDiagBuses = sim.findDiagBuses(procBus);
        boolean z = false;
        for (int i = 0; i < findDiagBuses.size(); i++) {
            if (findDiagBuses.get(i).newVal) {
                z = true;
            }
        }
        if (!z) {
            ProcSim.outLine("\nCannot do this function yet as bus: " + procBus.outName + " is not set");
        }
        return z;
    }

    public static String[][] doOp(String func, Vector<ProcBus> inputs, Vector<ProcBus> outputs, String out) {
        String[][] strArr = new String[20][2];
        for (int i2 = 0; i2 < 20; i2++) {
            strArr[i2][0] = "Error";
            strArr[i2][1] = "Error";
        }
        String str2 = "";
        String str3 = "";
        long i3 = 0;
        long i4 = 0;
        if (inputs != null) {
            if (inputs.size() >= 1) {
                str2 = inputs.get(0).binaryValue;
                i3 = Long.parseLong(toDec(str2));
                if (!checkAllowFunc(inputs.get(0))) {
                    return strArr;
                }
            }
            if (inputs.size() >= 2) {
                str3 = inputs.get(1).binaryValue;
                i4 = Long.parseLong(toDec(str3));
                if (!checkAllowFunc(inputs.get(1))) {
                    return strArr;
                }
            }
            if (inputs.size() >= 3) {
                Long.parseLong(toDec(inputs.get(2).binaryValue));
                if (!checkAllowFunc(inputs.get(2))) {
                    return strArr;
                }
            }
        }
        int size = outputs != null ? outputs.size() : 0;
        switch (func) {
            case "control":
                {
                    long opcode = Integer.parseInt(inputs.get(0).binaryValue, 2);
                    ControlSignals controlSignals = ControlUnit.decode(opcode);
                    strArr[0][0] = toBin(controlSignals.aluop).substring(64 - (int)Constants.ALUOPSIZE);
                    strArr[1][0] = toBin(controlSignals.movop).substring(64 - (int)Constants.MOVOPSIZE);
                    strArr[2][0] = toBin(controlSignals.memsize).substring(64 - (int)Constants.MEMSIZESIZE);
                    for (int i = 0; i < Constants.CONTROLSIZE; i++) {
                        strArr[i + 3][0] = ((controlSignals.control >> (Constants.CONTROLSIZE - 1 - i)) & 1) == 1 ? "1" : "0";
                    }
                    break;
                }
            case "signExtend":
                int instruction = Integer.parseInt(inputs.get(0).binaryValue, 2);
                long signExtendedValue = SignExtension.extend(instruction);
                strArr[0][0] = toBin(signExtendedValue);
                break;
            case "flagControl":
                {
                    int newFlags = Integer.parseInt(inputs.get(0).binaryValue, 2);
                    boolean setFlags = inputs.get(0).binaryValue == "1" ? true: false;
                    sim.flagsRegister.update(setFlags, newFlags);
                    int flags = sim.flagsRegister.getFlags();
                    strArr[0][0] = toBin(flags).substring(64 - (int)Constants.FLAGSIZE);
                    break;
                }
            case "branchControl":
                {
                    int opcode = Integer.parseInt(inputs.get(0).binaryValue, 2);
                    long readData2 = Long.parseLong(inputs.get(1).binaryValue, 2);
                    int rd = Integer.parseInt(inputs.get(2).binaryValue, 2);
                    int flags = Integer.parseInt(inputs.get(3).binaryValue, 2);
                    boolean branch = BranchControl.shouldBranch(opcode, readData2, rd, flags);
                    strArr[0][0] = branch? "1" : "0";
                    break;
                }
            case "alu":
                {
                    long a = Long.parseLong(inputs.get(0).binaryValue, 2);
                    long b = Long.parseLong(inputs.get(1).binaryValue, 2);
                    int shamt = Integer.parseInt(inputs.get(2).binaryValue, 2);
                    int aluop = Integer.parseInt(inputs.get(3).binaryValue, 2);
                    ALU.Result result = ALU.execute(a, b, shamt, aluop);
                    strArr[0][0] = toBin(result.res);
                    strArr[1][0] = toBin(result.flags).substring(64 - (int)Constants.FLAGSIZE);
                    break;
                }
            case "mov":
                {
                    long readData2 = Long.parseLong(inputs.get(0).binaryValue, 2);
                    long extended = Long.parseLong(inputs.get(1).binaryValue, 2);
                    int movop = Integer.parseInt(inputs.get(2).binaryValue, 2);
                    long movResult = MOV.execute(readData2, extended, movop);
                    strArr[0][0] = toBin(movResult);
                    break;
                }
            case "readmem":
                {
                    long address = Long.parseLong(inputs.get(0).binaryValue, 2);
                    int memSizeLog = Integer.parseInt(inputs.get(1).binaryValue, 2);
                    boolean signLoad = inputs.get(2).binaryValue == "1";
                    long readResult = sim.dataMemory.read(address, memSizeLog, signLoad);
                    strArr[0][0] = toBin(readResult);
                    break;
                }
            case "writemem":
                {
                    long address = Long.parseLong(inputs.get(0).binaryValue, 2);
                    long writeData = Long.parseLong(inputs.get(1).binaryValue, 2);
                    int memSizeLog = Integer.parseInt(inputs.get(2).binaryValue, 2);
                    sim.dataMemory.write(address, writeData, memSizeLog);
                    break;
                }
            case "donothing":
                {
                    break;
                }
            case "incrementPC":
                {
                    long PC = Long.parseLong(inputs.get(0).binaryValue, 2);
                    long newPC = PC + 4;
                    strArr[0][0] = toBin(newPC);                    
                    break;
                }
            case "out":
                ProcSim.outLine("Outputing bin string ");
                if (out == null) {
                    out = "0";
                    ProcSim.outErr("Error: OutString not set in func 'out'");
                }
                for (int i5 = 0; i5 < size; i5++) {
                    strArr[i5][0] = ProcFunc.zeroExtend(out, outputs.get(0).bits);
                }
                break;
            case "bitout":
                ProcSim.outLine("Outputing bit string ");
                if (out == null) {
                    out = ProcFunc.zeroExtend("0", size);
                    ProcSim.outErr("Error: OutString not set in func 'bitout'");
                }
                if (outputs.size() > out.length()) {
                    out = ProcFunc.zeroExtend(out, size);
                    ProcSim.outErr("Error: OutString does not have enough bits to fill all buses in func 'bitout'");
                }
                int i6 = 0;
                for (int i7 = 0; i7 < size; i7++) {
                    int i8 = outputs.get(i7).bits;
                    strArr[i7][0] = out.substring(i6, i6 + i8);
                    i6 += i8;
                }
                break;
            case "split":
                ProcSim.outLine("Split ");
                if (out == null) {
                    out = "31-0";
                    ProcSim.outErr("Error: OutString not set in func 'split'");
                }
                int indexOf = out.indexOf("-");
                if (indexOf < 0) {
                    ProcSim.outErr("Error 10 in outstring in func 'split', (should be e.g. '31-26') - " + out);
                    break;
                } else {
                    int length = str2.length() - 1;
                    int parseInt = length - Integer.parseInt(out.substring(indexOf + 1, out.length()));
                    int parseInt2 = length - Integer.parseInt(out.substring(0, indexOf));
                    if (parseInt2 > parseInt) {
                        ProcSim.outErr("Error in outstring in func 'split', (should be e.g. '31-26') - " + out);
                        break;
                    } else if (parseInt > str2.length()) {
                        ProcSim.outErr(
                                "Error in outstring in func 'split', split value is bigger than input value - " + out);
                        break;
                    } else if (parseInt2 < 0 || parseInt < 0) {
                        ProcSim.outErr("Error in outstring in func 'split', split value is smaller than zero - " + out);
                        break;
                    } else {
                        strArr[0][0] = str2.substring(parseInt2, parseInt + 1);
                        break;
                    }
                }
            case "readreg":
                strArr[0][0] = sim.registers[(int) i3];
                ProcSim.outLine("Read reg ");
                break;
            case "writereg":
                sim.setRegister((int) i3, str3);
                ProcSim.outLine("Wrote Reg ");
                sim.lastChangedReg = str2;
                break;
            case "getInstruction":
                ProcSim.outLine("Getting instruction ");
                int instrIdx = (int) i3 / 4;
                if (instrIdx >= sim.source.assembly.numRealInstr) {
                    ProcSim.out("Found end of instructions - stopping execution");
                    strArr[0][0] = "0";
                    strArr[0][1] = "Exit";
                    break;
                }
                int lineIdx = sim.source.assembly.realInstr.get(instrIdx);
                if (sim.source.assembly.numInstr == 0) {
                    ProcSim.outErr("No instructions found! - stopping execution");
                    strArr[0][0] = "0";
                    strArr[0][1] = "Exit";
                    break;
                } else if (sim.source.assembly.instr == null || sim.source.assembly.instr[lineIdx] == null) {
                    ProcSim.outErr(
                            "Error 11: cannot find the requested instruction from instruction memory at address: "
                                    + Long.toString(i3));
                    break;
                } else {
                    sim.execInstr = (int) i3 / 4;
                    if (sim.source.viewSim.instrMemFrame != null && sim.source.viewSim.instrMemFrame.isVisible()) {
                        sim.source.viewSim.instrMemFrame.tModel
                                .fireTableChanged(new TableModelEvent(sim.source.viewSim.instrMemFrame.tModel));
                        Thread thread = sim.source.diagCanvas.animThread.t;
                        Thread.yield();
                        sim.source.viewSim.instrMemFrame.yourLabel.setText(sim.source.assembly.instr[lineIdx].comment);
                    }
                    strArr[0][0] = sim.source.assembly.instr[lineIdx].strMach;
                    strArr[0][1] = sim.source.assembly.instr[lineIdx].strNoLbl;
                    break;
                }
            case "mux":
                {
                    strArr[0][0] = inputs.get(0).binaryValue;
                    ProcSim.outLine("Mux operation ");
                    break;
                }
        }
        if (strArr[0][0].equals("Error")) {
            ProcSim.outLine("No new vals ");
        }
        for (int i9 = 0; i9 < 20; i9++) {
            if (!strArr[i9][0].equals("Error") && strArr[i9][1].equals("Error")) {
                strArr[i9][1] = toDec(strArr[i9][0]);
            }
        }
        return strArr;
    }

    private static String shiftLeft(String str, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            str = str.substring(1, str.length()) + "0";
        }
        return str;
    }

    private static String checkZero(String str) {
        return Long.parseLong(toDec(str)) == 0 ? "1" : "0";
    }

}


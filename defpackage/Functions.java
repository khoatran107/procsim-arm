package defpackage;

import java.util.Vector;
import javax.swing.event.TableModelEvent;
import defpackage.instruction.Instruction;

/* loaded from: ProcSim.jar:Functions.class */
public class Functions {
    public static final int OP_SUB = 0;
    public static final int OP_ADD = 1;
    public static final int OP_AND = 2;
    public static final int OP_OR = 3;
    public static final int OP_ZERO = 4;
    public static final int OP_MUX = 5;
    public static final int OP_READMEM = 6;
    public static final int OP_WRITEMEM = 7;
    public static final int OP_OUT = 8;
    public static final int OP_BITOUT = 9;
    public static final int OP_SPLIT = 10;
    public static final int OP_READREG = 11;
    public static final int OP_WRITEREG = 12;
    public static final int OP_GETINSTR = 13;
    public static final int OP_SHIFTLEFT = 14;
    public static final int OP_SIGNEXTEND = 15;
    public static final int OP_SLT = 16;
    public static final int OP_JOIN = 17;
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
        if (str.length() != 32) {
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

    public static String[][] doOp(int i, Vector<ProcBus> vector, Vector<ProcBus> vector2, String out) {
        String[][] strArr = new String[20][2];
        for (int i2 = 0; i2 < 20; i2++) {
            strArr[i2][0] = "Error";
            strArr[i2][1] = "Error";
        }
        String str2 = "";
        String str3 = "";
        long i3 = 0;
        long i4 = 0;
        if (vector != null) {
            if (vector.size() >= 1) {
                str2 = vector.get(0).binaryValue;
                i3 = Long.parseLong(toDec(str2));
                if (!checkAllowFunc(vector.get(0))) {
                    return strArr;
                }
            }
            if (vector.size() >= 2) {
                str3 = vector.get(1).binaryValue;
                i4 = Long.parseLong(toDec(str3));
                if (!checkAllowFunc(vector.get(1))) {
                    return strArr;
                }
            }
            if (vector.size() >= 3) {
                Long.parseLong(toDec(vector.get(2).binaryValue));
                if (!checkAllowFunc(vector.get(2))) {
                    return strArr;
                }
            }
        }
        int size = vector2 != null ? vector2.size() : 0;
        switch (i) {
            case OP_SUB /* 0 */:
                strArr[0][0] = toBin(i3 - i4);
                strArr[1][0] = checkZero(strArr[0][0]);
                ProcSim.outLine("Sub operation ");
                break;
            case OP_ADD /* 1 */:
                strArr[0][0] = toBin(i3 + i4);
                strArr[1][0] = checkZero(strArr[0][0]);
                ProcSim.outLine("Add operation ");
                break;
            case OP_AND /* 2 */:
                strArr[0][0] = toBin(i3 & i4);
                strArr[1][0] = checkZero(strArr[0][0]);
                ProcSim.outLine("And operation ");
                break;
            case OP_OR /* 3 */:
                strArr[0][0] = toBin(i3 | i4);
                strArr[1][0] = checkZero(strArr[0][0]);
                ProcSim.outLine("Or operation ");
                break;
            case OP_ZERO /* 4 */:
                strArr[0][0] = checkZero(str2);
                ProcSim.outLine("Zero operation ");
                break;
            case OP_MUX /* 5 */:
                strArr[0][0] = str2;
                ProcSim.outLine("Mux operation ");
                break;
            case OP_READMEM /* 6 */:
                ProcSim.outLine("Reading from mem ");
                strArr[0][0] = sim.getDoubleWordMem(i3);
                break;
            case OP_WRITEMEM /* 7 */:
                ProcSim.outLine("Writing to mem ");
                sim.setDoubleWordMem(i3, str3);
                break;
            case OP_OUT /* 8 */:
                ProcSim.outLine("Outputing bin string ");
                if (out == null) {
                    out = "0";
                    ProcSim.outErr("Error: OutString not set in func 'out'");
                }
                for (int i5 = 0; i5 < size; i5++) {
                    strArr[i5][0] = ProcFunc.zeroExtend(out, vector2.get(0).bits);
                }
                break;
            case OP_BITOUT /* 9 */:
                ProcSim.outLine("Outputing bit string ");
                if (out == null) {
                    out = ProcFunc.zeroExtend("0", size);
                    ProcSim.outErr("Error: OutString not set in func 'bitout'");
                }
                if (vector2.size() > out.length()) {
                    out = ProcFunc.zeroExtend(out, size);
                    ProcSim.outErr("Error: OutString does not have enough bits to fill all buses in func 'bitout'");
                }
                int i6 = 0;
                for (int i7 = 0; i7 < size; i7++) {
                    int i8 = vector2.get(i7).bits;
                    strArr[i7][0] = out.substring(i6, i6 + i8);
                    i6 += i8;
                }
                break;
            case OP_SPLIT /* 10 */:
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
                        ProcSim.outErr("Error in outstring in func 'split', split value is bigger than input value - " + out);
                        break;
                    } else if (parseInt2 < 0 || parseInt < 0) {
                        ProcSim.outErr("Error in outstring in func 'split', split value is smaller than zero - " + out);
                        break;
                    } else {
                        strArr[0][0] = str2.substring(parseInt2, parseInt + 1);
                        break;
                    }
                }
                // break;
            case OP_READREG /* 11 */:
                strArr[0][0] = sim.registers[(int)i3];
                ProcSim.outLine("Read reg ");
                break;
            case OP_WRITEREG /* 12 */:
                sim.registers[(int)i3] = str3;
                ProcSim.outLine("Wrote Reg ");
                sim.lastChangedReg = str2;
                break;
            case OP_GETINSTR /* 13 */:
                ProcSim.outLine("Getting instruction ");
                int instrIdx = (int)i3 / 4;
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
                    ProcSim.outErr("Error 11: cannot find the requested instruction from instruction memory at address: " + Long.toString(i3));
                    break;
                } else {
                    sim.execInstr = (int)i3 / 4;
                    if (sim.source.viewSim.instrMemFrame != null && sim.source.viewSim.instrMemFrame.isVisible()) {
                        sim.source.viewSim.instrMemFrame.tModel.fireTableChanged(new TableModelEvent(sim.source.viewSim.instrMemFrame.tModel));
                        Thread thread = sim.source.diagCanvas.animThread.t;
                        Thread.yield();
                        sim.source.viewSim.instrMemFrame.yourLabel.setText(sim.source.assembly.instr[lineIdx].comment);
                    }
                    strArr[0][0] = sim.source.assembly.instr[lineIdx].strMach;
                    strArr[0][1] = sim.source.assembly.instr[lineIdx].strNoLbl;
                    break;
                }
                // break;
            case OP_SHIFTLEFT /* 14 */:
                ProcSim.outLine("Shiftleft ");
                if (out == null) {
                    out = "0";
                    ProcSim.outErr("Error: OutString not set in func 'shiftleft'");
                }
                strArr[0][0] = shiftLeft(str2, Integer.parseInt(out));
                break;
            case OP_SIGNEXTEND /* 15 */:
                ProcSim.outLine("Sign extend ");
                strArr[0][0] = ProcFunc.signExtend(str2, Integer.parseInt(out));
                break;
            case OP_SLT /* 16 */:
                // EMPTY
                ProcSim.outLine("SLT operation ");
                break;
            case OP_JOIN /* 17 */:
                strArr[0][0] = str2 + str3;
                break;
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

    public static String toString(int i) {
        switch (i) {
            case OP_SUB /* 0 */:
                return "sub";
            case OP_ADD /* 1 */:
                return "add";
            case OP_AND /* 2 */:
                return "and";
            case OP_OR /* 3 */:
                return "or";
            case OP_ZERO /* 4 */:
                return "zero";
            case OP_MUX /* 5 */:
                return "multiplexor";
            case OP_READMEM /* 6 */:
                return "readmem";
            case OP_WRITEMEM /* 7 */:
                return "writemem";
            case OP_OUT /* 8 */:
                return "out";
            case OP_BITOUT /* 9 */:
                return "bitout";
            case OP_SPLIT /* 10 */:
                return "split";
            case OP_READREG /* 11 */:
                return "readreg";
            case OP_WRITEREG /* 12 */:
                return "writereg";
            case OP_GETINSTR /* 13 */:
                return "getInstruction";
            case OP_SHIFTLEFT /* 14 */:
                return "shiftleft";
            case OP_SIGNEXTEND /* 15 */:
                return "signextend";
            case OP_SLT /* 16 */:
                return "slt";
            case OP_JOIN /* 17 */:
                return "join";
            default:
                return Long.toString(i);
        }
    }

    public static int getFuncOp(String str) {
        if (str.equals("mux")) {
            return 5;
        }
        if (str.equals("or")) {
            return 3;
        }
        if (str.equals("zero")) {
            return 4;
        }
        if (str.equals("multiplexor")) {
            return 5;
        }
        if (str.equals("add")) {
            return 1;
        }
        if (str.equals("and")) {
            return 2;
        }
        if (str.equals("sub")) {
            return 0;
        }
        if (str.equals("readmem")) {
            return 6;
        }
        if (str.equals("writemem")) {
            return 7;
        }
        if (str.equals("bitout")) {
            return 9;
        }
        if (str.equals("out")) {
            return 8;
        }
        if (str.equals("split")) {
            return 10;
        }
        if (str.equals("readreg")) {
            return 11;
        }
        if (str.equals("writereg")) {
            return 12;
        }
        if (str.equals("getInstruction")) {
            return 13;
        }
        if (str.equals("shiftleft")) {
            return 14;
        }
        if (str.equals("signextend")) {
            return 15;
        }
        if (str.equals("slt")) {
            return 16;
        }
        if (str.equals("join")) {
            return 17;
        }
        return -1;
    }
}

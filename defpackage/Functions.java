package defpackage;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.event.TableModelEvent;

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

    public static String[][] doOp(String i, Vector<ProcBus> vector, Vector<ProcBus> vector2, String out) {
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
            case "sub":
                strArr[0][0] = toBin(i3 - i4);
                ProcSim.outLine("Sub operation ");
                break;
            case "add":
                strArr[0][0] = toBin(i3 + i4);
                ProcSim.outLine("Add operation ");
                break;
            case "and":
                strArr[0][0] = toBin(i3 & i4);
                ProcSim.outLine("And operation ");
                break;
            case "or":
                strArr[0][0] = toBin(i3 | i4);
                ProcSim.outLine("Or operation ");
                break;
            case "zero":
                strArr[0][0] = checkZero(str2);
                System.out.println(strArr[0][0]);
                ProcSim.outLine("Zero operation ");
                break;
            case "mux":
                strArr[0][0] = str2;
                System.out.println(strArr[0][0]);
                ProcSim.outLine("Mux operation ");
                break;
            case "readmem":
                ProcSim.outLine("Reading from mem ");
                strArr[0][0] = sim.getDoubleWordMem(i3);
                break;
            case "writemem":
                ProcSim.outLine("Writing to mem ");
                sim.setDoubleWordMem(i3, str3);
                break;
            case "out":
                ProcSim.outLine("Outputing bin string ");
                if (out == null) {
                    out = "0";
                    ProcSim.outErr("Error: OutString not set in func 'out'");
                }
                for (int i5 = 0; i5 < size; i5++) {
                    strArr[i5][0] = ProcFunc.zeroExtend(out, vector2.get(0).bits);
                }
                break;
            case "bitout":
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
            case "shiftleft":
                ProcSim.outLine("Shiftleft ");
                if (out == null) {
                    out = "0";
                    ProcSim.outErr("Error: OutString not set in func 'shiftleft'");
                }
                strArr[0][0] = shiftLeft(str2, Integer.parseInt(out));
                break;
            case "signextend":
                ProcSim.outLine("Sign extend ");
                strArr[0][0] = ProcFunc.signExtend(str2, Integer.parseInt(out));
                break;
            case "join":
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

}

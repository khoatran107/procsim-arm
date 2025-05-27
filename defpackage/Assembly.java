package defpackage;

import java.awt.Button;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/* loaded from: ProcSim.jar:Assembly.class */
class Assembly extends TextEditor implements ActionListener {
    Button butAssemble;
    Instruction[] instr;
    int numInstr;
    String[][] directives;
    int numDirects;
    Vector<String> supportedISA;
    MenuItem fmSupportedISA;
    boolean doneCheck;

    public Assembly(ProcSim procSim) {
        super(procSim, "Assembly/Machine Code");
        this.butAssemble = new Button("Assemble");
        this.numInstr = 0;
        this.directives = new String[50][3];
        this.numDirects = 0;
        this.supportedISA = new Vector<>();
        this.path = "sample just R-Format.asm";
        this.buttonPanel.add(this.butAssemble);
        setupFrame(0.4d, 0.6d);
        this.butAssemble.addActionListener(this);
        this.fmNew.addActionListener(this);
        this.fmLoad.addActionListener(this);
        this.fmSaveAs.addActionListener(this);
        this.fmSave.addActionListener(this);
        this.butDone.addActionListener(this);
        this.fmClose.addActionListener(this);
        this.fmSupportedISA = new MenuItem("Supported Instructions...");
        this.fmSupportedISA.addActionListener(this);
        this.helpMenu.add(this.fmSupportedISA);
        this.fileMenu.addSeparator();
        this.fileMenu.add(this.fmClose);
    }

    public String supportedISAToString() {
        if (this.supportedISA.size() == 0) {
            return "Unknown ISA - Could be: add, sub, and, or, slt, lw, sw, beq, addi, andi, ori, j.";
        }
        String str = "Supported ISA: ";
        for (int i = 0; i < this.supportedISA.size(); i++) {
            str = str + this.supportedISA.get(i) + ", ";
        }
        return str.substring(0, str.length() - 2) + ".";
    }

    /* renamed from: Assembly$1, reason: invalid class name */
    /* loaded from: ProcSim.jar:Assembly$1.class */
    class AnonymousClass1 extends WindowAdapter {
        final Assembly this$0;

        AnonymousClass1(Assembly arg0) {
            this.this$0 = arg0;
        }

        public void windowClosing(WindowEvent arg0) {
            this.this$0.dispose();
        }
    }

    public boolean isSupported(String str) {
        if (this.supportedISA.size() == 0 || str.equals("machine") || str.equals("exit")) {
            return true;
        }
        for (int i = 0; i < this.supportedISA.size(); i++) {
            if (this.supportedISA.get(i).equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void checkSupportedISA() {
        for (int i = 0; i < this.numInstr; i++) {
            if (!isSupported(this.instr[i].instr)) {
                ProcSim.outErr("-Instruction not supported: " + this.instr[i].instr + "(will continue, but not recommended!)");
            }
        }
    }

    /* renamed from: Assembly$2, reason: invalid class name */
    /* loaded from: ProcSim.jar:Assembly$2.class */
    class AnonymousClass2 implements UndoableEditListener {
        final Assembly this$0;

        AnonymousClass2(Assembly arg0) {
            this.this$0 = arg0;
        }

        public void undoableEditHappened(UndoableEditEvent arg0) {
            this.this$0.undo.addEdit(arg0.getEdit());
        }
    }

    @Override // defpackage.TextEditor
    public void actionPerformed(ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);
        if (actionEvent.getSource() == this.fmClose) {
            dispose();
        }
        if (actionEvent.getSource() == this.butAssemble) {
            if (doParse()) {
                new MsgBox(this, "===Assemble Succeded===", false).dispose();
                ProcSim.outErr("\n===Assemble Succeded===\n");
            } else {
                new MsgBox(this, "===Assemble Failed===", false).dispose();
                ProcSim.outErr("\n======!!-Assemble Failed-!!=======\n");
            }
        }
        if (actionEvent.getSource() == this.butDone) {
            MsgBox msgBox = new MsgBox(this, "Save changes?", true);
            msgBox.dispose();
            if (msgBox.id) {
                saveFile(this.path);
            }
            if (doParse()) {
                ProcSim.out("\n===Assemble Succeded===\n");
                dispose();
            } else {
                ProcSim.outErr("\n======!!-Assemble Failed-!!=======\n");
            }
        }
        if (actionEvent.getSource() == this.fmLoad) {
            openAss();
        }
        if (actionEvent.getSource() == this.fmSaveAs) {
            saveAs();
        }
        if (actionEvent.getSource() == this.fmSave) {
            saveFile(this.path);
        }
        if (actionEvent.getSource() == this.fmNew) {
            MsgBox msgBox2 = new MsgBox(this, "Save changes before creating new?", true);
            msgBox2.dispose();
            if (msgBox2.id) {
                saveFile(this.path);
            }
            this.txtDoc.setText("");
            saveAs(true);
        }
        if (actionEvent.getSource() == this.fmSupportedISA) {
            new MsgBox(this, supportedISAToString(), false).dispose();
        }
    }

    /* renamed from: Assembly$3, reason: invalid class name */
    /* loaded from: ProcSim.jar:Assembly$3.class */
    class AnonymousClass3 extends AbstractAction {
        final Assembly this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Assembly arg0, String arg1) {
            super(arg1);
            this.this$0 = arg0;
        }

        public void actionPerformed(ActionEvent arg0) {
            try {
                if (this.this$0.undo.canUndo()) {
                    this.this$0.undo.undo();
                }
            } catch (CannotUndoException e) {
            }
        }
    }

    /* renamed from: Assembly$4, reason: invalid class name */
    /* loaded from: ProcSim.jar:Assembly$4.class */
    class AnonymousClass4 extends AbstractAction {
        final Assembly this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Assembly arg0, String arg1) {
            super(arg1);
            this.this$0 = arg0;
        }

        public void actionPerformed(ActionEvent arg0) {
            try {
                if (this.this$0.undo.canRedo()) {
                    this.this$0.undo.redo();
                }
            } catch (CannotRedoException e) {
            }
        }
    }

    public boolean doParse() {
        this.source.lblAssem.setText("   Assembly: " + this.path);
        ProcSim.out("\n\n====Parsing Started====\n");
        this.numInstr = 0;
        this.numDirects = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(this.txtDoc.getText(), "\n");
        int countTokens = stringTokenizer.countTokens();
        this.instr = new Instruction[countTokens];
        for (int i = 0; i < countTokens; i++) {
            String trim = stringTokenizer.nextToken().trim();
            if (!trim.equals("")) {
                if (trim.length() > ".register".length() && trim.substring(0, ".register".length()).equals(".register")) {
                    this.directives[this.numDirects][0] = trim;
                    this.numDirects++;
                } else if (!trim.substring(0, 1).equals("#")) {
                    this.instr[this.numInstr] = new Instruction();
                    this.instr[this.numInstr].str = new String(trim);
                    ProcSim.out("Instr" + Integer.toString(this.numInstr) + ": " + this.instr[this.numInstr].str);
                    this.numInstr++;
                }
            }
        }
        for (int i2 = 0; i2 < this.numDirects; i2++) {
            String trim2 = this.directives[i2][0].trim();
            for (int i3 = 0; i3 < trim2.length(); i3++) {
                if (trim2.substring(i3, i3 + 1).equals("#")) {
                    trim2 = trim2.substring(0, i3).trim();
                }
            }
            int indexOf = trim2.indexOf(" ");
            if (indexOf > -1 && trim2.substring(0, indexOf).equals(".register")) {
                this.directives[i2][0] = trim2.substring(0, indexOf);
                String trim3 = trim2.substring(indexOf, trim2.length()).trim();
                int indexOf2 = trim3.indexOf(" ");
                this.directives[i2][1] = registerConvert(trim3.substring(0, indexOf2));
                this.directives[i2][2] = Functions.toBin(Integer.parseInt(trim3.substring(indexOf2, trim3.length()).trim()));
            }
        }
        for (int i4 = 0; i4 < this.numInstr; i4++) {
            String trim4 = this.instr[i4].str.trim();
            boolean z = false;
            for (int i5 = 0; i5 < trim4.length() && !z; i5++) {
                if (trim4.substring(i5, i5 + 1).equals(":")) {
                    z = true;
                    this.instr[i4].label = trim4.substring(0, i5).trim();
                    ProcSim.out("Label:" + this.instr[i4].label);
                }
            }
            if (!z) {
                this.instr[i4].label = "";
            }
        }
        for (int i6 = 0; i6 < this.numInstr; i6++) {
            String trim5 = this.instr[i6].str.trim();
            for (int i7 = 0; i7 < trim5.length(); i7++) {
                if (trim5.substring(i7, i7 + 1).equals("#")) {
                    this.instr[i6].comment = trim5.substring(i7, trim5.length()).trim();
                    trim5 = trim5.substring(0, i7).trim();
                }
            }
            this.instr[i6].str = trim5;
            for (int i8 = 0; i8 < trim5.length(); i8++) {
                if (trim5.substring(i8, i8 + 1).equals(":")) {
                    trim5 = trim5.substring(i8 + 1, trim5.length()).trim();
                }
            }
            this.instr[i6].strNoLbl = trim5;
            if (trim5.equals("") && this.instr[i6].label.compareToIgnoreCase("exit") == 0) {
                this.instr[i6].instr = "exit";
                ProcSim.out("InstrName" + Integer.toString(i6) + ": Exit Label");
            } else if (trim5.length() == 32 && (trim5.substring(0, 1).equals("1") || trim5.substring(0, 1).equals("0"))) {
                ProcSim.out("OK: Is machine code already");
                this.instr[i6].strMach = trim5;
                this.instr[i6].str = ProcFunc.slimBinary(trim5);
                this.instr[i6].strNoLbl = ProcFunc.slimBinary(trim5);
                this.instr[i6].instr = "machine";
            } else {
                int indexOf3 = trim5.indexOf(" ");
                if (indexOf3 == -1) {
                    ProcSim.outErr("Error, instruction incomplete, instruction: " + Integer.toString(i6));
                    return false;
                }
                this.instr[i6].instr = trim5.substring(0, indexOf3);
                String trim6 = trim5.substring(indexOf3, trim5.length()).trim();
                ProcSim.out("InstrName" + Integer.toString(i6) + ": " + this.instr[i6].instr);
                int indexOf4 = trim6.indexOf(",");
                if (this.instr[i6].instr.equals("j")) {
                    this.instr[i6].param1 = trim6.trim();
                } else {
                    if (indexOf4 == -1) {
                        ProcSim.outErr("Error, no comma found in instruction: " + Integer.toString(i6));
                        return false;
                    }
                    this.instr[i6].param1 = trim6.substring(0, indexOf4);
                    ProcSim.out("param1-" + Integer.toString(i6) + ": " + this.instr[i6].param1);
                    String trim7 = trim6.substring(indexOf4 + 1, trim6.length()).trim();
                    int indexOf5 = trim7.indexOf(",");
                    if (indexOf5 != -1) {
                        this.instr[i6].param2 = trim7.substring(0, indexOf5);
                        ProcSim.out("param2-" + Integer.toString(i6) + ": " + this.instr[i6].param2);
                        trim7 = trim7.substring(indexOf5 + 1, trim7.length()).trim();
                    }
                    if (indexOf5 == -1) {
                        this.instr[i6].param2 = trim7.substring(0, trim7.length()).trim();
                        ProcSim.out("param2-" + Integer.toString(i6) + ": " + this.instr[i6].param2);
                        this.instr[i6].param3 = "";
                    } else {
                        this.instr[i6].param3 = trim7.substring(0, trim7.length()).trim();
                        ProcSim.out("param3-" + Integer.toString(i6) + ": " + this.instr[i6].param3);
                    }
                }
            }
        }
        this.doneCheck = false;
        for (int i9 = 0; i9 < this.numInstr; i9++) {
            this.instr[i9].address = ProcFunc.zeroExtend(Integer.toBinaryString(i9), 32);
            if (!this.instr[i9].instr.equals("exit") && !this.instr[i9].instr.equals("machine")) {
                this.instr[i9].strMach = doAssemble(this.instr[i9]);
                ProcSim.out("Machine code " + Integer.toString(i9) + ": " + this.instr[i9].strMach);
                if (this.instr[i9].strMach.equals("err")) {
                    ProcSim.outErr("Error in instruction : " + Integer.toString(i9));
                    return false;
                }
            }
        }
        return true;
    }

    public String doAssemble(Instruction instruction) {
        String str;
        String binaryString;
        String zeroExtend;
        String registerConvert;
        String str2 = "err";
        String str3 = "err";
        String str4 = instruction.instr;
        if (!this.doneCheck && !isSupported(str4)) {
            this.doneCheck = true;
            ProcSim.outErr("Instruction not supported: " + str4 + " (will continue, but not recommended!)");
        }
        if (str4.equals("add") || str4.equals("sub") || str4.equals("and") || str4.equals("or") || str4.equals("slt")) {
            if (str4.equals("add")) {
                str = Integer.toBinaryString(32);
            } else if (str4.equals("sub")) {
                str = Integer.toBinaryString(34);
            } else if (str4.equals("and")) {
                str = Integer.toBinaryString(36);
            } else if (str4.equals("or")) {
                str = Integer.toBinaryString(37);
            } else if (str4.equals("slt")) {
                str = Integer.toBinaryString(42);
            } else {
                str = "0";
            }
            String zeroExtend2 = ProcFunc.zeroExtend(str, 6);
            String registerConvert2 = registerConvert(instruction.param2);
            String registerConvert3 = registerConvert(instruction.param3);
            String registerConvert4 = registerConvert(instruction.param1);
            if (registerConvert2.equals("err") || registerConvert3.equals("err") || registerConvert4.equals("err")) {
                ProcSim.outErr("Error in instruction: " + instruction.str);
                return "err";
            }
            return "000000" + registerConvert2 + registerConvert3 + registerConvert4 + "00000" + zeroExtend2;
        }
        if (str4.equals("lw") || str4.equals("sw") || str4.equals("beq") || str4.equals("addi") || str4.equals("andi") || str4.equals("ori")) {
            if (str4.equals("lw")) {
                binaryString = Integer.toBinaryString(35);
            } else if (str4.equals("sw")) {
                binaryString = Integer.toBinaryString(43);
            } else if (str4.equals("beq")) {
                binaryString = Integer.toBinaryString(4);
            } else if (str4.equals("addi")) {
                binaryString = Integer.toBinaryString(8);
            } else if (str4.equals("andi")) {
                binaryString = Integer.toBinaryString(12);
            } else {
                binaryString = Integer.toBinaryString(13);
            }
            zeroExtend = ProcFunc.zeroExtend(binaryString, 6);
            if (str4.equals("beq")) {
                str2 = registerConvert(instruction.param1);
                registerConvert = registerConvert(instruction.param2);
                int i = -1;
                for (int i2 = 0; i2 < this.numInstr; i2++) {
                    if (this.instr[i2].label.equals(instruction.param3)) {
                        i = i2;
                    }
                }
                if (i == -1) {
                    ProcSim.outErr("Error, branch label not found: " + instruction.param3);
                    return "err";
                }
                int parseInt = (i - Integer.parseInt(instruction.address, 2)) - 1;
                str3 = ProcFunc.signExtend(Functions.toBin(parseInt), 16);
                ProcSim.out("beq address offset: " + Integer.toString(parseInt) + " : " + str3);
            } else {
                registerConvert = registerConvert(instruction.param1);
                if (str4.equals("addi") || str4.equals("andi") || str4.equals("ori")) {
                    str2 = registerConvert(instruction.param2);
                    try {
                        str3 = ProcFunc.zeroExtend(Integer.toBinaryString(Integer.parseInt(instruction.param3)), 16);
                    } catch (Exception e) {
                        ProcSim.outErr("Error: " + str4 + " requires an integer constant for its final parameter");
                        return "err";
                    }
                } else {
                    String str5 = instruction.param2;
                    boolean z = false;
                    for (int i3 = 0; i3 < str5.length() && !z; i3++) {
                        try {
                            if (str5.substring(i3, i3 + 1).equals("(")) {
                                z = true;
                                String trim = str5.substring(0, i3).trim();
                                String trim2 = str5.substring(i3 + 1, str5.length() - 1).trim();
                                ProcSim.out("LW or SW Address Base: " + trim2 + " & Offset:" + trim);
                                str3 = ProcFunc.zeroExtend(Integer.toBinaryString(Integer.parseInt(trim)), 16);
                                str2 = registerConvert(trim2);
                            }
                        } catch (Exception e2) {
                            z = false;
                        }
                    }
                    if (!z) {
                        ProcSim.outErr("Error, address in incorrect format (eg '0($s2)' ): " + instruction.param3);
                        return "err";
                    }
                }
            }
        } else if (str4.equals("j")) {
            str2 = "";
            registerConvert = "";
            zeroExtend = ProcFunc.zeroExtend(Integer.toBinaryString(2), 6);
            int i4 = -1;
            for (int i5 = 0; i5 < this.numInstr; i5++) {
                if (this.instr[i5].label.equals(instruction.param1)) {
                    i4 = i5;
                }
            }
            if (i4 == -1) {
                ProcSim.outErr("Error, jump label not found: " + instruction.param1);
                return "err";
            }
            str3 = ProcFunc.signExtend(Functions.toBin(i4), 26);
            ProcSim.out("jump address points to: " + Integer.toString(i4) + " : " + str3);
        } else {
            ProcSim.outErr("Error, function not found: " + str4);
            return "err";
        }
        if (str2.equals("err") || registerConvert.equals("err")) {
            ProcSim.outErr("Error in instruction: " + instruction.str);
            return "err";
        }
        return zeroExtend + str2 + registerConvert + str3;
    }

    static String regNumToString(int i) {
        String str;
        if (i < 0 || i > 32) {
            return "0";
        }
        if (i == 0) {
            str = "$0";
        } else if (i == 1) {
            str = "$at";
        } else {
            try {
                if (i > 1 && i < 4) {
                    str = "$v" + Integer.toString(i - 2);
                } else if (i > 3 && i < 8) {
                    str = "$a" + Integer.toString(i - 4);
                } else if (i > 7 && i < 16) {
                    str = "$t" + Integer.toString(i - 8);
                } else if (i > 15 && i < 24) {
                    str = "$s" + Integer.toString(i - 16);
                } else if (i > 23 && i < 26) {
                    str = "$t" + Integer.toString(i - 16);
                } else if (i == 26) {
                    str = "$k0";
                } else if (i == 27) {
                    str = "$k1";
                } else if (i == 28) {
                    str = "$gp";
                } else if (i == 29) {
                    str = "$sp";
                } else if (i == 30) {
                    str = "$fp";
                } else {
                    if (i != 31) {
                        ProcSim.outErr("ERROR in register, unknown register number: " + Integer.toString(i));
                        return "err";
                    }
                    str = "$ra";
                }
            } catch (Exception e) {
                ProcSim.outErr("ERROR in converting reg num: " + Integer.toString(i));
                return "err";
            }
        }
        return str;
    }

    public String registerConvert(String str) {
        String binaryString;
        if (!str.substring(0, 1).equals("$")) {
            ProcSim.outErr("ERROR in register, no '$' found: " + str);
            return "err";
        }
        try {
            if (str.equals("$zero")) {
                binaryString = Integer.toBinaryString(0);
            } else if (str.equals("$0")) {
                binaryString = Integer.toBinaryString(0);
            } else if (str.equals("$at")) {
                binaryString = Integer.toBinaryString(1);
            } else if (str.substring(1, 2).equals("v")) {
                binaryString = Integer.toBinaryString(Integer.parseInt(str.substring(2, 3)) + 2);
            } else if (str.substring(1, 2).equals("a")) {
                binaryString = Integer.toBinaryString(Integer.parseInt(str.substring(2, 3)) + 4);
            } else if (str.substring(1, 2).equals("t") && Integer.parseInt(str.substring(2, 3)) <= 7) {
                binaryString = Integer.toBinaryString(Integer.parseInt(str.substring(2, 3)) + 8);
            } else if (str.substring(1, 2).equals("s")) {
                binaryString = Integer.toBinaryString(Integer.parseInt(str.substring(2, 3)) + 16);
            } else if (str.substring(1, 2).equals("t")) {
                binaryString = Integer.toBinaryString(Integer.parseInt(str.substring(2, 3)) + 16);
            } else if (str.equals("$k0")) {
                binaryString = Integer.toBinaryString(26);
            } else if (str.equals("$k1")) {
                binaryString = Integer.toBinaryString(27);
            } else if (str.equals("$gp")) {
                binaryString = Integer.toBinaryString(28);
            } else if (str.equals("$sp")) {
                binaryString = Integer.toBinaryString(29);
            } else if (str.equals("$fp")) {
                binaryString = Integer.toBinaryString(30);
            } else {
                if (!str.equals("$ra")) {
                    ProcSim.outErr("ERROR in register, unknown register symbol: " + str);
                    return "err";
                }
                binaryString = Integer.toBinaryString(31);
            }
            if (binaryString.length() < 5) {
                binaryString = ProcFunc.zeroExtend(binaryString, 5);
            }
            return binaryString;
        } catch (Exception e) {
            ProcSim.outErr("ERROR in register: " + str);
            return "err";
        }
    }

    public void openAss() {
        String fileDialog = fileDialog(false, new Frame(), "Open Assembly File...", ".\\", "*.asm");
        if (fileDialog.equals(".\\\\null") || fileDialog.equals(".\\null")) {
            return;
        }
        System.out.println(fileDialog);
        this.path = fileDialog;
        openFile(fileDialog);
    }

    public boolean saveAs() {
        return saveAs(false);
    }

    public boolean saveAs(boolean z) {
        String fileDialog;
        if (z) {
            fileDialog = fileDialog(true, new Frame(), "Create New Assembly File...", ".\\", "newAssembly.asm");
            this.path = "newAssembly.asm";
        } else {
            fileDialog = fileDialog(true, new Frame(), "Save Assembly File As...", ".\\", this.source.sim.getFileName(this.path));
        }
        if (fileDialog.equals(this.path) && !z) {
            saveFile(this.path);
            return true;
        }
        if (fileDialog.length() > 5 && (fileDialog.substring(0, 6).equals(".\\null") || fileDialog.substring(0, 7).equals(".\\\\null"))) {
            return false;
        }
        this.path = fileDialog;
        saveFile(this.path);
        return true;
    }
}
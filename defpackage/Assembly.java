package defpackage;

import java.awt.Button;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;
import java.util.HashMap;

import defpackage.instruction.Instruction;
import defpackage.instruction.UndefinedLabelException;
import defpackage.instruction.ImmediateOutOfBoundsException;
import defpackage.lexer.TextLine;
import defpackage.instruction.Decoder;
import defpackage.instruction.Mnemonic;

/* loaded from: ProcSim.jar:Assembly.class */
class Assembly extends TextEditor implements ActionListener {
    Button butAssemble;
    int numInstr;
    String[][] directives;
    int numDirects;
    Vector<String> supportedISA;
    MenuItem fmSupportedISA;
    boolean doneCheck;
    ArrayList<TextLine> code;
    ArrayList<Error> compileErrors;
    HashMap<String, Integer> branchTable;
    ArrayList<Instruction> cpuInstructions;
    Instruction[] instr;

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
        this.numDirects = 0;
        String rawText = this.txtDoc.getText();
        String[] lines = rawText.split("\\R"); // Splits on any linebreak: \n, \r\n, \r
        code = new ArrayList<TextLine>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                code.add(new TextLine(line));
            }
        }
        branchTable = new HashMap<String, Integer>();
		cpuInstructions = new ArrayList<Instruction>();
		compileErrors = new ArrayList<Error>();
        parseCode();
		populateBranchTable();
		decodeInstructions();
        instr = cpuInstructions.toArray(new Instruction[0]);
        this.numInstr = instr.length;
        int labelLen, mnemonicLen;
        labelLen = mnemonicLen = 0;
        for (int i = 0; i < this.numInstr; i++) {
            String currentLabel = code.get(i).getLabel();
            labelLen = Math.max(labelLen, currentLabel == null ? 0: currentLabel.length());
            Mnemonic currentMnemonic = code.get(i).getMnemonic();
            mnemonicLen = Math.max(mnemonicLen, currentMnemonic == null ? 0: currentMnemonic.nameUpper.length());
        }

        for (int i = 0; i < this.numInstr; i++) {
            instr[i].str = instr[i].strNoLbl = code.get(i).getLinePadded(labelLen, mnemonicLen);
            if (!instr[i].isEmpty()) {
                instr[i].strMach = Instruction.getInstructionMachineCode(instr[i], i);
            }
            instr[i].comment = code.get(i).getComment();
        }
        for (int i = 0; i < compileErrors.size(); i++) {
            ProcSim.outErr(compileErrors.get(i).getMsg() + " on line " + compileErrors.get(i).getLineNumber());
        }
        return compileErrors.isEmpty();
    }

	/**
	 * For each line of source code: attempt to generate tokens and then parse.
	 */
	/*
	 * Any errors are stored in the compileErrors arraylist
	 */
	public void parseCode() {
		for (int i=0; i<code.size(); i++) {
			if (!code.get(i).getLine().isEmpty()) {
				code.get(i).tokenize();
				if (code.get(i).getNumTokens()>0) { // Why would there be an error message is the number of tokens is greater than 0?
					String errorMsg = code.get(i).parse();
					if (errorMsg != null) {
						compileErrors.add(new Error(errorMsg, i));
					}
				}
			}
		}
	}
	
	/**
	 * For each label in the source code, an entry is inserted into the branch lookup table
	 * mapping from the label to the index of the instruction immediately after it.
	 */
	public void populateBranchTable() {
		String label;
		Mnemonic mnem;
		int instructionCount = 0;
		for (int i=0; i<code.size(); i++) {
			label = code.get(i).getLabel();
			mnem = code.get(i).getMnemonic();
			if (label != null) {
				branchTable.put(label, instructionCount);
			}
			if (mnem != null) {
				instructionCount++;
			}
		}
	}
	
	/**
	 * Attempt to generate a list of instructions from the parsed lines of source code
	 */
	/*
	 * Errors are stored in the compileErros arraylist
	 */
	public void decodeInstructions() {
		TextLine line;
		for (int i=0; i<code.size(); i++) {
			line = code.get(i);
			if (line.getMnemonic() != null) {
				try {
					cpuInstructions.add(Decoder.getInstruction(
							line.getMnemonic(), line.getArgs(), i, branchTable));
				} catch (UndefinedLabelException ule) {
					compileErrors.add(new Error(ule.getMessage(), i));
				} catch (ImmediateOutOfBoundsException ioobe) {
					compileErrors.add(new Error(ioobe.getMessage(), i));
				}
			} else {
                System.out.println(line);
                cpuInstructions.add(new Instruction());
            }
		}
	}

    static String regNumToString(int regNum) {
        if (regNum < 0 || regNum > 32) {
            return "0";
        }
		switch (regNum) {
		case 31 : return "XZR";
		case 30 : return "LR";
		case 29 : return "FP";
		case 28 : return "SP";
		default : return "X" + regNum;
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
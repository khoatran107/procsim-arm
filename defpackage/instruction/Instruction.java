package defpackage.instruction;

import defpackage.cpu.ControlUnitConfiguration;

/**
 * An <code>Instruction</code> object is used to represent each LEGv8 instruction in the user's program.
 * <p>
 * The <code>CPU</code> class executes the operation defined by each <code>Instruction</code>
 * 
 * @see CPU
 * 
 * @author Jonathan Wright, 2016
 */
public class Instruction {
	
	/**
	 * @param mnemonic			the instruction mnemonic
	 * @param args				the array of arguments for this instruction i.e. register indices and immediates
	 * @param editorLineNumber	the line in the code editor of this instruction
	 * @param controlSignals	the control signals required to execute this instruction
	 * 
	 * @see Mnemonic
	 * @see ControlUnitConfiguration
	 * @see CPU
	 */
	public Instruction() {
		this.isEmpty = true;
	}
	
	public Instruction(Mnemonic mnemonic, int[] args, int editorLineNumber, 
			ControlUnitConfiguration controlSignals) {
		this.mnemonic = mnemonic;
		this.args = args;
		this.editorLineNumber = editorLineNumber;
		this.controlSignals = controlSignals;
		this.isEmpty = false;
	}
	
	/**
	 * @return	the instruction mnemonic
	 */
	public Mnemonic getMnemonic() {
		return mnemonic;
	}
	
	/**
	 * @return	the array of arguments for this instruction i.e. registers and immediates
	 */
	public int[] getArgs() {
		return args;
	}
	
	/**
	 * @return	the line in the code editor of this instruction
	 */
	public int getLineNumber() {
		return editorLineNumber;
	}
	
	/**
	 * @return	the control signals required to execute this instruction
	 */
	public ControlUnitConfiguration getControlSignals() {
		return controlSignals;
	}

	public boolean isEmpty() {
		return isEmpty;
	}
	
	public static String getInstructionMachineCode(Instruction ins, int instructionIndex) {
		Mnemonic m = ins.getMnemonic();
		switch (m.type) {
			case MNEMONIC_RRR:
				return getMachineCodeRRR(ins);
			case MNEMONIC_RRI:
				if (m.equals(Mnemonic.LSL) || m.equals(Mnemonic.LSR)) {
					return getMachineCodeShift(ins);
				} else {
					return getMachineCodeRRI(ins);
				}
			case MNEMONIC_RISI:
				return getMachineCodeRISI(ins);
			case MNEMONIC_RM:
				return getMachineCodeRM(ins);
			case MNEMONIC_RRM:
				return getMachineCodeRRM(ins);
			case MNEMONIC_RL:
				return getMachineCodeRL(ins, instructionIndex);
			case MNEMONIC_L:
				if (m == Mnemonic.B || m == Mnemonic.BL) {
					return getMachineCodeL(ins, instructionIndex);
				} else {
					return getMachineCodeBCond(ins, instructionIndex);
				}
			default:
				return "";
		}
	}
	
	static String getMachineCodeRRR(Instruction ins) {
		String[] fields = new String[5];
		fields[0] = ins.getMnemonic().opcode;
		fields[1] = getRegBinary(ins.getArgs()[2]);
		fields[2] = getImmBinary(0, 6, false);
		fields[3] = getRegBinary(ins.getArgs()[1]);
		fields[4] = getRegBinary(ins.getArgs()[0]);
		return String.join("", fields);
	}
	
	static String getMachineCodeShift(Instruction ins) {
		String[] fields = new String[5];
		fields[0] = ins.getMnemonic().opcode;
		fields[1] = getImmBinary(0, 5, false);
		fields[2] = getImmBinary(ins.getArgs()[2], 6, false);
		fields[3] = getRegBinary(ins.getArgs()[1]);
		fields[4] = getRegBinary(ins.getArgs()[0]);
		return String.join("", fields);
	}
	
	static String getMachineCodeRRI(Instruction ins) {
		String[] fields = new String[4];
		fields[0] = ins.getMnemonic().opcode;
		fields[1] = getImmBinary(ins.getArgs()[2], 12, false);
		fields[2] = getRegBinary(ins.getArgs()[1]);
		fields[3] = getRegBinary(ins.getArgs()[0]);
		return String.join("", fields);
	}
	
	static String getMachineCodeRM(Instruction ins) {
		String[] fields = new String[5];
		fields[0] = ins.getMnemonic().opcode;
		fields[1] = getImmBinary(ins.getArgs()[2], 9, true);
		fields[2] = "00";
		fields[3] = getRegBinary(ins.getArgs()[1]);
		fields[4] = getRegBinary(ins.getArgs()[0]);
		return String.join("", fields);
	}
	
	static String getMachineCodeRRM(Instruction ins) {
		String[] fields = new String[5];
		fields[0] = ins.getMnemonic().opcode;
		fields[1] = getRegBinary(ins.getArgs()[0]);
		fields[2] = getImmBinary(31, 6, false);
		fields[3] = getRegBinary(ins.getArgs()[2]);
		fields[4] = getRegBinary(ins.getArgs()[1]);
		return String.join("", fields);
	}
	
	static String getMachineCodeRISI(Instruction ins) {
		String shift = "";
		switch (ins.getArgs()[2]) {
			case 0: shift = "00"; break;
			case 16: shift = "01"; break;
			case 32: shift = "10"; break;
			case 48: shift = "11"; break;
		}
		String[] fields = new String[3];
		fields[0] = ins.getMnemonic().opcode + shift;
		fields[1] = getImmBinary(ins.getArgs()[1], 16, false);
		fields[2] = getRegBinary(ins.getArgs()[0]);
		return String.join("", fields);
	}
	
	static String getMachineCodeRL(Instruction ins, int instructionIndex) {
		String[] fields = new String[3];
		fields[0] = ins.getMnemonic().opcode;
		fields[1] = getImmBinary(ins.getArgs()[1] - instructionIndex, 19, true);
		fields[2] = getRegBinary(ins.getArgs()[0]);
		return String.join("", fields);
	}
	
	static String getMachineCodeL(Instruction ins, int instructionIndex) {
		String[] fields = new String[2];
		fields[0] = ins.getMnemonic().opcode;
		fields[1] = getImmBinary(ins.getArgs()[0] - instructionIndex, 26, true);
		return String.join("", fields);
	}
	
	static String getRegBinary(int regNum) {
		String regBinary = Integer.toBinaryString(regNum);
		while (regBinary.length() < 5) {
			regBinary = "0" + regBinary;
		}
		return regBinary;
	}
	
	static String getImmBinary(int value, int numBits, boolean signed) {
		String immBinary;
		if (signed && value < 0) {
			immBinary = Integer.toBinaryString(value & 0x0fffffff);
			while (immBinary.length() > numBits) {
				immBinary = immBinary.substring(1);
			}
			return immBinary;
		}
		immBinary = Integer.toBinaryString(value);
		while (immBinary.length() < numBits) {
			immBinary = "0" + immBinary;
		}
		return immBinary;
	}

	static String getMachineCodeBCond(Instruction ins, int instructionIndex) {
        String[] fields = new String[3];
        fields[0] = ins.getMnemonic().opcode;
        int offset = ins.getArgs()[0] - instructionIndex;
        fields[1] = getImmBinary(offset, 19, true);
        String cond_val_4bit;
        switch (ins.getMnemonic()) {
            case BEQ:  cond_val_4bit = "0000"; break;
            case BNE:  cond_val_4bit = "0001"; break;
            case BHS:  cond_val_4bit = "0010"; break;
            case BLO:  cond_val_4bit = "0011"; break;
            case BMI:  cond_val_4bit = "0100"; break;
            case BPL:  cond_val_4bit = "0101"; break;
            case BVS:  cond_val_4bit = "0110"; break;
            case BVC:  cond_val_4bit = "0111"; break;
            case BHI:  cond_val_4bit = "1000"; break;
            case BLS:  cond_val_4bit = "1001"; break;
            case BGE:  cond_val_4bit = "1010"; break;
            case BLT:  cond_val_4bit = "1011"; break;
            case BGT:  cond_val_4bit = "1100"; break;
            case BLE:  cond_val_4bit = "1101"; break;
            default:
                System.err.println("Warning: Unhandled B.cond mnemonic in getMachineCodeBCond: " + ins.getMnemonic());
                cond_val_4bit = "0000";
                break;
        }
        fields[2] = "0" + cond_val_4bit;
        return String.join("", fields);
    }

	private Mnemonic mnemonic;
	private int[] args;
	private int editorLineNumber;
	private ControlUnitConfiguration controlSignals;
	private boolean isEmpty;

	public String strNoLbl;
    public String strMach;
    public String instr = "";
	public String str;
	public String comment;
}

package defpackage;

import java.util.Vector;

/* loaded from: ProcSim.jar:PComponent.class */
public class PComponent {
    String name;
    String description;
    Vector<CompOperation> operations;
    Vector<ProcBus> buses;
    String[][] tmpInputs;
    int curIn;
    String[][] tmpOutputs;
    int curOut;
    String[][] tmpOutConnectsToComp;
    String[][] tmpOutConnectsToInput;
    int curOutConnectsTo;
    String[][] tmpOps;
    String[][] tmpInToOps;
    String[][] tmpOutFromOps;
    String[][][] tmpInputChecks;
    int curOps;
    int[] curInCheck;
    int[] curInOp;
    int[] curOutOp;
    int x;
    int y;
    int width;
    int height;
    boolean hidden;
    boolean isStartComp;
    String imgPath = "";

    PComponent(String str) {
        this.operations = new Vector<>();
        this.buses = new Vector<>();
        this.tmpInputs = new String[2][100];
        this.curIn = 0;
        this.tmpOutputs = new String[9][100];
        this.curOut = 0;
        this.tmpOutConnectsToComp = new String[50][100];
        this.tmpOutConnectsToInput = new String[50][100];
        this.curOutConnectsTo = 0;
        this.tmpOps = new String[3][100];
        this.tmpInToOps = new String[20][100];
        this.tmpOutFromOps = new String[20][100];
        this.tmpInputChecks = new String[2][20][100];
        this.curOps = 0;
        this.curInCheck = new int[100];
        this.curInOp = new int[100];
        this.curOutOp = new int[100];
        this.x = -999;
        this.y = -999;
        this.width = 90;
        this.height = 90;
        this.hidden = false;
        this.isStartComp = false;
        this.name = str;
    }

    PComponent() {
        this.operations = new Vector<>();
        this.buses = new Vector<>();
        this.tmpInputs = new String[2][100];
        this.curIn = 0;
        this.tmpOutputs = new String[9][100];
        this.curOut = 0;
        this.tmpOutConnectsToComp = new String[50][100];
        this.tmpOutConnectsToInput = new String[50][100];
        this.curOutConnectsTo = 0;
        this.tmpOps = new String[3][100];
        this.tmpInToOps = new String[20][100];
        this.tmpOutFromOps = new String[20][100];
        this.tmpInputChecks = new String[2][20][100];
        this.curOps = 0;
        this.curInCheck = new int[100];
        this.curInOp = new int[100];
        this.curOutOp = new int[100];
        this.x = -999;
        this.y = -999;
        this.width = 90;
        this.height = 90;
        this.hidden = false;
        this.isStartComp = false;
    }

    public CompOperation getOps(int i) {
        return this.operations.get(i);
    }

    public boolean doOps() {
        boolean z = false;
        for (int i = 0; i < this.operations.size(); i++) {
            if (getOps(i).doOp()) {
                z = true;
            }
        }
        if (!z && this.operations.size() > 0) {
            ProcSim.outErr("Error - Failed to find an operation to perform on the current inputs for component: " + this.name);
            return false;
        }
        return true;
    }
}
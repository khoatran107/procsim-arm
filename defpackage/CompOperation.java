package defpackage;

import java.util.Vector;

/* loaded from: ProcSim.jar:CompOperation.class */
public class CompOperation {
    String name;
    int functionOp;
    Vector<InputCheck> inputChecks;
    Vector<ProcBus> inputsToOp;
    Vector<ProcBus> outputs;
    String out;
    Simulator sim;

    CompOperation(int i, Vector<ProcBus> vector, Vector<InputCheck> vector2, Vector<ProcBus> vector3, Simulator simulator) {
        this.inputChecks = new Vector<>();
        this.inputsToOp = new Vector<>();
        this.outputs = new Vector<>();
        this.functionOp = i;
        this.inputChecks = vector2;
        this.inputsToOp = vector;
        this.outputs = vector3;
        this.sim = simulator;
    }

    CompOperation() {
        this.inputChecks = new Vector<>();
        this.inputsToOp = new Vector<>();
        this.outputs = new Vector<>();
    }

    private boolean checkToDoNewVal(PComponent pComponent) {
        for (int i = 0; i < this.sim.source.diagCanvas.buses.size(); i++) {
            DiagBus diagBus = this.sim.source.diagCanvas.buses.get(i);
            if (diagBus.in.equals(pComponent) && diagBus.bus.checkOptional() && diagBus.newVal) {
                return false;
            }
        }
        return true;
    }

    public boolean doOp() {
        if (doCheck()) {
            String[][] doOp = Functions.doOp(this.functionOp, this.inputsToOp, this.outputs, this.out);
            for (int i = 0; i < 10; i++) {
                if (!doOp[i][0].equals("Error") && i < this.outputs.size()) {
                    ProcBus procBus = this.outputs.get(i);
                    if (doOp[i][1].equals("Exit")) {
                        this.sim.source.viewSim.endOfExectution();
                        return true;
                    }
                    for (int i2 = 0; i2 < this.sim.source.diagCanvas.buses.size(); i2++) {
                        DiagBus diagBus = this.sim.source.diagCanvas.buses.get(i2);
                        if (diagBus.bus.equals(procBus) && (!procBus.doneAnimOnce || checkToDoNewVal(procBus.sourceComp) || !procBus.binaryValue.equals(ProcFunc.zeroExtend(doOp[i][0], procBus.bits)))) {
                            diagBus.newVal = true;
                        }
                    }
                    procBus.binaryValue = ProcFunc.zeroExtend(doOp[i][0], procBus.bits);
                    procBus.strValue = doOp[i][1];
                }
            }
            return true;
        }
        return false;
    }

    public boolean doCheck() {
        boolean z = true;
        for (int i = 0; i < this.inputChecks.size(); i++) {
            if (!this.inputChecks.get(i).check()) {
                z = false;
            }
        }
        return z;
    }

    public String toString() {
        return Functions.toString(this.functionOp);
    }
}
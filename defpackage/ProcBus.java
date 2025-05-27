package defpackage;

import java.util.Vector;

/* loaded from: ProcSim.jar:ProcBus.class */
public class ProcBus extends Bus {
    PComponent sourceComp = new PComponent();
    Vector<PComponent> destComps = new Vector<>();
    int curConTo = 0;
    Vector<String> inNames = new Vector<>();
    Vector<DiagBus> diagBuses = new Vector<>();
    String[] tmpDestComps = new String[100];
    int curDestComp = 0;
    boolean showStrVal = false;
    boolean doneAnimOnce = false;
    boolean optional = false;
    ProcBus optDependsOn = null;
    String optIf = "0";

    ProcBus(String str) {
        this.outName = str;
    }

    public boolean checkOptional() {
        if (this.optional) {
            return this.optDependsOn == null || this.optDependsOn.binaryValue.equals(this.optIf);
        }
        return false;
    }
}
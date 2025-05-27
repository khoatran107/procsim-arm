package defpackage;

import java.util.Vector;

/* loaded from: ProcSim.jar:OutputBus.class */
public class OutputBus {
    String name;
    Vector<InputBus> connectsTo;
    int newVal;
    String outText;
    String inText;
    boolean decimalText;
    String value;
    String[][] tmpConnectsTo = new String[2][100];
    int curConTo = 0;
    int[] x = new int[100];
    int[] y = new int[100];
    int numPoints = 0;
    boolean doneBus = false;
    public PermText permText1 = new PermText(this);
    public PermText permText2 = new PermText(this);
    int bits = 32;
    boolean doneAnim = false;

    OutputBus(String arg0) {
        this.name = arg0;
    }

    public InputBus getConnectsTo(int arg0) {
        return this.connectsTo.get(arg0);
    }

    public void resetPoints() {
        this.numPoints = 0;
        for (int i = 0; i < 100; i++) {
            this.x[i] = -1;
            this.y[i] = -1;
        }
        this.doneBus = false;
    }

    /* loaded from: ProcSim.jar:OutputBus$PermText.class */
    public class PermText {
        public String str;
        public int x;
        public int y;
        public int width;
        public int height;
        final OutputBus this$0;

        public PermText(OutputBus arg0) {
            this.this$0 = arg0;
        }
    }
}
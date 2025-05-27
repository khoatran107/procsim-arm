package defpackage;

/* loaded from: ProcSim.jar:DiagBus.class */
public class DiagBus extends Bus {
    int[] x;
    int[] y;
    int numPoints;
    boolean doneBus;
    PComponent out;
    PComponent in;
    String inText;
    public PermText busLabelOut;
    public PermText busLabelIn;
    boolean doneAnim;
    ProcBus bus;
    boolean newVal;
    boolean animated;
    boolean dontReset;
    boolean customNamePos;

    DiagBus(String str, String str2, PComponent pComponent, PComponent pComponent2, int i, ProcBus procBus) {
        this.x = new int[100];
        this.y = new int[100];
        this.numPoints = 0;
        this.doneBus = false;
        this.inText = "None";
        this.busLabelOut = new PermText();
        this.busLabelIn = new PermText();
        this.doneAnim = false;
        this.newVal = false;
        this.animated = false;
        this.dontReset = false;
        this.customNamePos = false;
        this.bus = procBus;
        resetPoints();
        this.out = pComponent;
        this.in = pComponent2;
        this.outName = str;
        this.inText = str2;
        this.bits = i;
    }

    DiagBus(String str, String str2, boolean z, PComponent pComponent, PComponent pComponent2, int i, ProcBus procBus) {
        this.x = new int[100];
        this.y = new int[100];
        this.numPoints = 0;
        this.doneBus = false;
        this.inText = "None";
        this.busLabelOut = new PermText();
        this.busLabelIn = new PermText();
        this.doneAnim = false;
        this.newVal = false;
        this.animated = false;
        this.dontReset = false;
        this.customNamePos = false;
        this.bus = procBus;
        resetPoints();
        this.out = pComponent;
        this.in = pComponent2;
        this.outName = str;
        this.inText = str2;
        this.bits = i;
    }

    public void resetPoints() {
        this.numPoints = 0;
        for (int i = 0; i < 100; i++) {
            this.x[i] = -20;
            this.y[i] = -20;
        }
        this.doneBus = false;
        this.busLabelOut.str = null;
        this.busLabelIn.str = null;
        this.customNamePos = false;
    }

    /* loaded from: ProcSim.jar:DiagBus$PermText.class */
    public class PermText {
        public String str;
        public boolean moving = false;
        public boolean hidden = false;
        public int x;
        public int y;
        public int width;
        public int height;

        public PermText() {
        }
    }
}
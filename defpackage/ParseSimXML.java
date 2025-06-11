package defpackage;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: ProcSim.jar:ParseSimXML.class */
class ParseSimXML {
    String path;
    Document doc;
    Simulator sim;
    LoadSim source;
    Vector<ProcBus> buses;
    final int SIMNAME = 1;
    final int NAME = 2;
    final int DESC = 3;
    final int HID = 4;
    final int INPS = 5;
    final int OUTS = 6;
    final int OPINOUT = 7;
    final int ISA = 8;
    boolean outputBuses = false;
    boolean operations = false;
    int elem = 0;
    boolean starting = true;
    int curComp = -1;

    public ParseSimXML(LoadSim loadSim) {
        this.source = loadSim;
    }

    public boolean startParse(Simulator simulator) {
        Dialog dialog = null;
        if (!this.starting) {
            dialog = new Dialog(this.source, "Parsing Architecture", false);
            Panel panel = new Panel(new FlowLayout(0));
            Panel panel2 = new Panel(new FlowLayout(0));
            panel.add(new Label("Parsing Architecture"), 0);
            panel2.add(new Label("Please Wait..."), 0);
            dialog.add(panel2, "South");
            dialog.add(panel, "Center");
            dialog.pack();
            dialog.setResizable(false);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dialog.setLocation(((int) (screenSize.getWidth() / 2.0d)) - (dialog.getWidth() / 2), ((int) (screenSize.getHeight() / 2.0d)) - (dialog.getHeight() / 2));
            dialog.setVisible(true);
        }
        ProcSim.out("Starting parse of file: " + simulator.path);
        this.sim = simulator;
        this.path = this.sim.path;
        this.sim.clear();
        this.curComp = -1;
        this.elem = -1;
        try {
            DOMParser dOMParser = new DOMParser();
            dOMParser.parse(this.path);
            this.doc = dOMParser.getDocument();
            traverseTree(this.doc);
            traverseTmpVars();
            createDiagBuses();
            if (!findConnections()) {
                ProcSim.outErr("Error in XML Document - Error in an output bus connection (<to>)!");
                ProcSim.outErr("\n==XML Failed Parsing==\n");
                if (!this.starting) {
                    dialog.setVisible(false);
                    return false;
                }
                return false;
            }
            ProcSim.out("\n==XML Succesfully Parsed==\n");
            if (!this.starting) {
                dialog.setVisible(false);
                return true;
            }
            return true;
        } catch (Exception e) {
            ProcSim.outErr("Error in XML Document...");
            e.printStackTrace(System.out);
            ProcSim.outErr(e.getMessage());
            ProcSim.outErr("\n==XML Failed Parsing==\n");
            if (!this.starting) {
                dialog.setVisible(false);
                return false;
            }
            return false;
        }
    }

    public PComponent getComp(int i) {
        return this.sim.comps.get(i);
    }

    public PComponent getComp() {
        return this.sim.comps.get(this.curComp);
    }

    public void traverseTree(Node node) throws Exception {
        if (node == null) {
            return;
        }
        switch (node.getNodeType()) {
            case Functions.OP_ADD /* 1 */:
                String nodeName = node.getNodeName();
                if (nodeName.equals("simname")) {
                    this.elem = 1;
                    break;
                } else if (nodeName.equals("Component")) {
                    this.operations = false;
                    this.outputBuses = false;
                    this.elem = -1;
                    this.curComp++;
                    this.sim.comps.add(new PComponent());
                    break;
                } else if (nodeName.equals("name")) {
                    this.elem = 2;
                    break;
                } else if (nodeName.equals("description")) {
                    this.elem = 3;
                    break;
                } else if (nodeName.equals("hidden")) {
                    this.elem = 4;
                    break;
                } else if (nodeName.equals("inputBuses")) {
                    this.outputBuses = false;
                    this.operations = false;
                    this.elem = 5;
                    break;
                } else if (nodeName.equals("input") && this.elem == 5) {
                    NamedNodeMap attributes = node.getAttributes();
                    String value = ((Attr) attributes.getNamedItem("name")).getValue();
                    String value2 = ((Attr) attributes.getNamedItem("bits")).getValue();
                    getComp().tmpInputs[0][getComp().curIn] = value;
                    getComp().tmpInputs[1][getComp().curIn] = value2;
                    getComp().curIn++;
                    break;
                } else if (nodeName.equals("outputBuses")) {
                    this.outputBuses = true;
                    this.operations = false;
                    this.elem = -1;
                    break;
                } else if (nodeName.equals("output") && this.outputBuses) {
                    this.elem = 6;
                    NamedNodeMap attributes2 = node.getAttributes();
                    String value3 = ((Attr) attributes2.getNamedItem("name")).getValue();
                    String value4 = ((Attr) attributes2.getNamedItem("bits")).getValue();
                    Attr attr = (Attr) attributes2.getNamedItem("optional");
                    Attr attr2 = (Attr) attributes2.getNamedItem("optionalDependsOn");
                    Attr attr3 = (Attr) attributes2.getNamedItem("optionalComp");
                    Attr attr4 = (Attr) attributes2.getNamedItem("optionalIf");
                    Attr attr5 = (Attr) attributes2.getNamedItem("alternativeStr");
                    getComp().tmpOutputs[0][getComp().curOut] = value3;
                    getComp().tmpOutputs[1][getComp().curOut] = value4;
                    if (attr != null && attr.getValue().equals("true")) {
                        getComp().tmpOutputs[4][getComp().curOut] = "true";
                    } else {
                        getComp().tmpOutputs[4][getComp().curOut] = "false";
                    }
                    if (attr2 != null) {
                        getComp().tmpOutputs[5][getComp().curOut] = attr2.getValue();
                    }
                    if (attr3 != null) {
                        getComp().tmpOutputs[6][getComp().curOut] = attr3.getValue();
                    }
                    if (attr4 != null) {
                        getComp().tmpOutputs[7][getComp().curOut] = attr4.getValue();
                    }
                    if (attr5 != null) {
                        getComp().tmpOutputs[8][getComp().curOut] = attr5.getValue();
                    }
                    getComp().curOut++;
                    break;
                } else if (nodeName.equals("to") && this.elem == 6) {
                    NamedNodeMap attributes3 = node.getAttributes();
                    Attr attr6 = (Attr) attributes3.getNamedItem("name");
                    if (attr6 == null) {
                        throw new Exception("Missing 'name' value in <outputBuses><output><to name=' '> at component: " + this.sim.comps.get(this.curComp).name);
                    }
                    getComp().tmpOutConnectsToInput[getComp().curOutConnectsTo][getComp().curOut - 1] = attr6.getValue();
                    Attr attr7 = (Attr) attributes3.getNamedItem("comp");
                    if (attr7 == null) {
                        throw new Exception("Missing 'comp' value in <outputBuses><output><to comp=' '> at component: " + this.sim.comps.get(this.curComp).name);
                    }
                    getComp().tmpOutConnectsToComp[getComp().curOutConnectsTo][getComp().curOut - 1] = attr7.getValue();
                    getComp().curOutConnectsTo++;
                    break;
                } else if (nodeName.equals("operations")) {
                    this.outputBuses = false;
                    this.operations = true;
                    this.elem = -1;
                    break;
                } else if (nodeName.equals("op") && this.operations) {
                    Attr attr8 = (Attr) node.getAttributes().getNamedItem("name");
                    if (attr8 == null) {
                        throw new Exception("Missing 'name' value in <operations><op name=' '> at component: " + this.sim.comps.get(this.curComp).name);
                    }
                    getComp().tmpOps[0][getComp().curOps] = attr8.getValue();
                    getComp().curOps++;
                    break;
                } else if (nodeName.equals("dependsOn") && this.operations) {
                    NamedNodeMap attributes4 = node.getAttributes();
                    Attr attr9 = (Attr) attributes4.getNamedItem("which");
                    if (attr9 == null) {
                        throw new Exception("Missing 'which' value in <operations><op><dependsOn which=' '> at component: " + this.sim.comps.get(this.curComp).name);
                    }
                    Attr attr10 = (Attr) attributes4.getNamedItem("if");
                    if (attr10 == null) {
                        throw new Exception("Missing 'if' value in <operations><op><dependsOn if=' '> at component: " + this.sim.comps.get(this.curComp).name);
                    }
                    getComp().tmpInputChecks[0][getComp().curInCheck[getComp().curOps - 1]][getComp().curOps - 1] = attr9.getValue();
                    getComp().tmpInputChecks[1][getComp().curInCheck[getComp().curOps - 1]][getComp().curOps - 1] = attr10.getValue();
                    int[] iArr = getComp().curInCheck;
                    int i = getComp().curOps - 1;
                    iArr[i] = iArr[i] + 1;
                    break;
                } else if (nodeName.equals("do") && this.operations) {
                    NamedNodeMap attributes5 = node.getAttributes();
                    Attr attr11 = (Attr) attributes5.getNamedItem("func");
                    if (attr11 == null) {
                        throw new Exception("Missing 'func' value in <operations><op><do func=' '> at component: " + this.sim.comps.get(this.curComp).name);
                    }
                    getComp().tmpOps[1][getComp().curOps - 1] = attr11.getValue();
                    Attr attr12 = (Attr) attributes5.getNamedItem("out");
                    if (attr12 != null) {
                        getComp().tmpOps[2][getComp().curOps - 1] = attr12.getValue();
                        break;
                    }
                } else if (nodeName.equals("out") && this.operations) {
                    getComp().tmpOutFromOps[getComp().curOutOp[getComp().curOps - 1]][getComp().curOps - 1] = ((Attr) node.getAttributes().getNamedItem("name")).getValue();
                    int[] iArr2 = getComp().curOutOp;
                    int i2 = getComp().curOps - 1;
                    iArr2[i2] = iArr2[i2] + 1;
                    break;
                } else if (nodeName.equals("in") && this.operations) {
                    getComp().tmpInToOps[getComp().curInOp[getComp().curOps - 1]][getComp().curOps - 1] = ((Attr) node.getAttributes().getNamedItem("name")).getValue();
                    int[] iArr3 = getComp().curInOp;
                    int i3 = getComp().curOps - 1;
                    iArr3[i3] = iArr3[i3] + 1;
                    break;
                } else if (!nodeName.equals("Simulator") && ((!nodeName.equals("inputs") || !this.operations) && (!nodeName.equals("outputs") || !this.operations))) {
                    if (nodeName.equals("specialComponent")) {
                        if (((Attr) node.getAttributes().getNamedItem("comp")).getValue().equals("StartComp")) {
                            getComp().isStartComp = true;
                            break;
                        }
                    } else if (nodeName.equals("Options")) {
                        Attr attr13 = (Attr) node.getAttributes().getNamedItem("resetEveryRound");
                        if (attr13 != null) {
                            if (attr13.getValue().equals("true")) {
                                this.sim.resetEveryRound = true;
                                break;
                            } else {
                                this.sim.resetEveryRound = false;
                                break;
                            }
                        }
                    } else if (nodeName.equals("SuportedISA")) {
                        this.elem = 8;
                        break;
                    } else {
                        ProcSim.out("WARNING: Unrecognised tag - " + nodeName);
                        break;
                    }
                }
                break;
            case Functions.OP_OR /* 3 */:
                String trim = node.getNodeValue().trim();
                if (trim.indexOf("\n") < 0 && trim.length() > 0) {
                    switch (this.elem) {
                        case Functions.OP_ADD /* 1 */:
                            this.sim.name = trim;
                            this.elem = -1;
                            break;
                        case Functions.OP_AND /* 2 */:
                            this.sim.comps.get(this.curComp).name = trim;
                            this.elem = -1;
                            break;
                        case Functions.OP_OR /* 3 */:
                            this.sim.comps.get(this.curComp).description = trim;
                            this.elem = -1;
                            break;
                        case Functions.OP_ZERO /* 4 */:
                            if (trim.equals("true")) {
                                this.sim.comps.get(this.curComp).hidden = true;
                            }
                            this.elem = -1;
                            break;
                        case Functions.OP_OUT /* 8 */:
                            parseSupportedISA(trim);
                            this.elem = -1;
                            break;
                    }
                } else {
                    return;
                }
        }
        NodeList childNodes = node.getChildNodes();
        if (childNodes != null) {
            int length = childNodes.getLength();
            for (int i4 = 0; i4 < length; i4++) {
                traverseTree(childNodes.item(i4));
            }
        }
    }

    public void parseSupportedISA(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int countTokens = stringTokenizer.countTokens();
        this.sim.source.assembly.supportedISA.clear();
        for (int i = 0; i < countTokens; i++) {
            this.sim.source.assembly.supportedISA.add(stringTokenizer.nextToken().trim());
        }
        this.sim.source.assembly.checkSupportedISA();
    }

    public boolean findConnections() {
        return true;
    }

    public ProcBus findBusFromIn(String str, PComponent pComponent) {
        for (int i = 0; i < this.sim.comps.size(); i++) {
            for (int i2 = 0; i2 < getComp(i).buses.size(); i2++) {
                for (int i3 = 0; i3 < getComp(i).buses.get(i2).inNames.size(); i3++) {
                    if (getComp(i).buses.get(i2).destComps.size() - 1 < i3) {
                        return null;
                    }
                    if (getComp(i).buses.get(i2).inNames.get(i3).equals(str) && getComp(i).buses.get(i2).destComps.get(i3).equals(pComponent)) {
                        return getComp(i).buses.get(i2);
                    }
                }
            }
        }
        for (int i4 = 0; i4 < this.sim.oddInBuses.size(); i4++) {
            if (this.sim.oddInBuses.get(i4).inNames.get(0).equals(str) && this.sim.oddInBuses.get(i4).destComps.get(0).equals(pComponent)) {
                return this.sim.oddInBuses.get(i4);
            }
        }
        return null;
    }

    public ProcBus findOutBus(String str, PComponent pComponent) {
        for (int i = 0; i < pComponent.buses.size(); i++) {
            if (pComponent.buses.get(i).outName.equals(str)) {
                return pComponent.buses.get(i);
            }
        }
        return null;
    }

    public int findComponent(String str) {
        for (int i = 0; i < this.sim.comps.size(); i++) {
            if (this.sim.comps.get(i).name.equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public int getFuncOp(String str) {
        return Functions.getFuncOp(str);
    }

    public boolean traverseTmpVars() {
        for (int i = 0; i < this.sim.comps.size(); i++) {
            try {
                for (int i2 = 0; i2 < getComp(i).curOut; i2++) {
                    ProcBus procBus = new ProcBus(getComp(i).tmpOutputs[0][i2]);
                    procBus.bits = Integer.parseInt(getComp(i).tmpOutputs[1][i2]);
                    procBus.sourceComp = getComp(i);
                    if (getComp(i).tmpOutputs[8][i2] != null && getComp(i).tmpOutputs[8][i2].equals("true")) {
                        procBus.showStrVal = true;
                    }
                    getComp(i).buses.add(procBus);
                }
            } catch (Exception e) {
                ProcSim.outErr("Error parsing simulation XML document -- ");
                e.printStackTrace();
                return false;
            }
        }
        for (int i3 = 0; i3 < this.sim.comps.size(); i3++) {
            for (int i4 = 0; i4 < getComp(i3).curOut; i4++) {
                ProcBus procBus2 = getComp(i3).buses.get(i4);
                if (getComp(i3).tmpOutputs[4][i4].equals("true")) {
                    procBus2.optional = true;
                    if (getComp(i3).tmpOutputs[6][i4] == null) {
                        continue;
                    } else {
                        int findComponent = findComponent(getComp(i3).tmpOutputs[6][i4]);
                        if (findComponent == -1) {
                            ProcSim.outErr("Failed to find component: " + getComp(i3).tmpOutputs[6][i4] + " in 'optional' statement");
                            return false;
                        }
                        ProcBus findOutBus = findOutBus(getComp(i3).tmpOutputs[5][i4], getComp(findComponent));
                        if (findOutBus == null) {
                            ProcSim.outErr("Failed to find outputbus: " + getComp(i3).tmpOutputs[5][i4] + " in component: " + getComp(findComponent).name + " in 'optional' statement");
                            return false;
                        }
                        procBus2.optDependsOn = findOutBus;
                        procBus2.optIf = getComp(i3).tmpOutputs[7][i4];
                    }
                }
            }
        }
        for (int i5 = 0; i5 < this.sim.comps.size(); i5++) {
            for (int i6 = 0; i6 < getComp(i5).curOut; i6++) {
                for (int i7 = 0; i7 < getComp(i5).curOutConnectsTo; i7++) {
                    for (int i8 = 0; i8 < this.sim.comps.size(); i8++) {
                        if (getComp(i8).name.equals(getComp(i5).tmpOutConnectsToComp[i7][i6])) {
                            getComp(i5).buses.get(i6).destComps.add(getComp(i8));
                        }
                    }
                    if (getComp(i5).tmpOutConnectsToInput[i7][i6] != null) {
                        getComp(i5).buses.get(i6).inNames.add(getComp(i5).tmpOutConnectsToInput[i7][i6]);
                    }
                }
            }
        }
        for (int i9 = 0; i9 < this.sim.comps.size(); i9++) {
            for (int i10 = 0; i10 < getComp(i9).curIn; i10++) {
                String str = getComp(i9).tmpInputs[0][i10];
                boolean z = false;
                for (int i11 = 0; i11 < this.sim.comps.size() && !z; i11++) {
                    for (int i12 = 0; i12 < getComp(i11).buses.size() && !z; i12++) {
                        for (int i13 = 0; i13 < getComp(i11).buses.get(i12).inNames.size() && !z; i13++) {
                            if (getComp(i11).buses.get(i12).inNames.get(i13).equals(str)) {
                                z = true;
                            }
                        }
                    }
                }
                if (!z) {
                    ProcBus procBus3 = new ProcBus("None");
                    procBus3.outName = "None";
                    procBus3.destComps.add(getComp(i9));
                    procBus3.inNames.add(str);
                    procBus3.bits = Integer.parseInt(getComp(i9).tmpInputs[1][i10]);
                    this.sim.oddInBuses.add(procBus3);
                }
            }
        }
        for (int i14 = 0; i14 < this.sim.comps.size(); i14++) {
            for (int i15 = 0; i15 < getComp(i14).curOps; i15++) {
                CompOperation compOperation = new CompOperation();
                compOperation.sim = this.sim;
                compOperation.name = getComp(i14).tmpOps[0][i15];
                compOperation.functionOp = getComp(i14).tmpOps[1][i15];
                compOperation.out = getComp(i14).tmpOps[2][i15];
                for (int i16 = 0; i16 < getComp(i14).curOutOp[i15]; i16++) {
                    ProcBus findOutBus2 = findOutBus(getComp(i14).tmpOutFromOps[i16][i15], getComp(i14));
                    if (findOutBus2 == null) {
                        ProcSim.outErr("Warning: Cannot find output bus: " + getComp(i14).tmpOutFromOps[i16][i15] + " in <operation><outputs>, in comp:" + getComp(i14).name);
                        return false;
                    }
                    compOperation.outputs.add(findOutBus2);
                }
                for (int i17 = 0; i17 < getComp(i14).curInOp[i15]; i17++) {
                    ProcBus findBusFromIn = findBusFromIn(getComp(i14).tmpInToOps[i17][i15], getComp(i14));
                    if (findBusFromIn == null) {
                        ProcSim.outErr("Warning: Cannot find input bus: " + getComp(i14).tmpInToOps[i17][i15] + ", in <operation><inputs>, in comp:" + getComp(i14).name);
                        return false;
                    }
                    compOperation.inputsToOp.add(findBusFromIn);
                }
                for (int i18 = 0; i18 < getComp(i14).curInCheck[i15]; i18++) {
                    InputCheck inputCheck = new InputCheck();
                    inputCheck.checkagainst = getComp(i14).tmpInputChecks[1][i18][i15];
                    if (getComp(i14).tmpInputChecks[0][i18][i15].equals("always")) {
                        inputCheck.always = true;
                        compOperation.inputChecks.add(inputCheck);
                    } else {
                        ProcBus findBusFromIn2 = findBusFromIn(getComp(i14).tmpInputChecks[0][i18][i15], getComp(i14));
                        if (findBusFromIn2 == null) {
                            ProcSim.outErr("Warning: Cannot find depends on bus (input bus): " + getComp(i14).tmpInputChecks[0][i18][i15] + ", in <operation><dependsOn>, in component: " + getComp(i14).name + " (could be a misspelling in the buses output component's '<to>')");
                            return false;
                        }
                        inputCheck.input = findBusFromIn2;
                        compOperation.inputChecks.add(inputCheck);
                    }
                }
                getComp(i14).operations.add(compOperation);
            }
        }
        return true;
    }

    public void createDiagBuses() {
        this.source.diagCanvas.buses.clear();
        for (int i = 0; i < this.sim.comps.size(); i++) {
            for (int i2 = 0; i2 < getComp(i).buses.size(); i2++) {
                ProcBus procBus = getComp(i).buses.get(i2);
                if (getComp(i).buses.get(i2).destComps.size() == 0) {
                    DiagBus diagBus = new DiagBus(procBus.outName, null, procBus.sourceComp, null, procBus.bits, procBus);
                    getComp(i).buses.get(i2).diagBuses.add(diagBus);
                    this.source.diagCanvas.buses.add(diagBus);
                } else {
                    for (int i3 = 0; i3 < getComp(i).buses.get(i2).destComps.size(); i3++) {
                        DiagBus diagBus2 = new DiagBus(procBus.outName, procBus.inNames.get(i3), procBus.sourceComp, procBus.destComps.get(i3), procBus.bits, procBus);
                        getComp(i).buses.get(i2).diagBuses.add(diagBus2);
                        this.source.diagCanvas.buses.add(diagBus2);
                    }
                }
            }
        }
        for (int i4 = 0; i4 < this.sim.oddInBuses.size(); i4++) {
            this.source.diagCanvas.buses.add(new DiagBus("None", this.sim.oddInBuses.get(i4).inNames.get(0), null, this.sim.oddInBuses.get(i4).destComps.get(0), this.sim.oddInBuses.get(i4).bits, this.sim.oddInBuses.get(i4)));
        }
    }
}
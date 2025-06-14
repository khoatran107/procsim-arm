package defpackage;

import java.awt.Button;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;

import defpackage.instruction.Instruction;

/* loaded from: ProcSim.jar:ViewSim.class */
class ViewSim extends Frame implements ActionListener, ChangeListener, ItemListener {
    Simulator sim;
    DiagCanvas dCanvas;
    JScrollPane jsp;
    Frame controlFrame;
    PComponent animComp;
    CompFrame regFrame;
    CompFrame mainMemFrame;
    CompFrame instrMemFrame;
    JSlider speedSlider;
    MenuItem fmLoadAss;
    MenuItem fmLoad;
    MenuItem fmClose;
    MenuItem fmHelp;
    CheckboxMenuItem fmTrans;
    CheckboxMenuItem fmShowBusNames;
    Vector<AnimationListItem> animationList = new Vector<>();
    Button butClose = new Button(" Close ");
    Button butStart = new Button(" Start Execution ");
    Button butPause = new Button("  Pause  ");
    Button butRestart = new Button("Stop");
    Button butStep = new Button("Step");
    Button butStepIntr = new Button("Step Instr");
    Button butShowRegisters = new Button("Registers");
    Button butShowMainMem = new Button("Main Memory");
    Button butShowIntrMem = new Button("Instruction Memory");
    Button butSuperSpeed = new Button("Super");
    Button butInstantSpeed = new Button("Instant");
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    Menu settingsMenu = new Menu("Settings");
    Menu helpMenu = new Menu("Help");
    Label lblSimName = new Label("");
    mousePressHandler mPressHandler = new mousePressHandler();
    boolean step = false;
    Frame frame = this;
    StatusBar statusBar = new StatusBar();

    public ViewSim(Simulator simulator) {
        this.sim = simulator;
        this.dCanvas = this.sim.source.diagCanvas;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.width * 0.85d), (int) (screenSize.height * 0.9d));
        setLocation(((int) ((screenSize.width / 2) - ((screenSize.width * 0.9d) / 2.0d))) - 50, ((int) ((screenSize.height / 2) - ((screenSize.height * 0.9d) / 2.0d))) - 20);
        addWindowListener(new WindowAdapter() { // from class: ViewSim.1
            public void windowClosing(WindowEvent windowEvent) {
                ViewSim.this.cleanupViewSim();
                ViewSim.this.dCanvas.animThread.restart();
                ViewSim.this.dispose();
            }
        });
        this.sim.resetMemoryAndRegs();
        setupFrame();
        setupControlFrame();
        this.dCanvas.viewSim = this;
        this.fmTrans = new CheckboxMenuItem("Transparency");
        this.fmTrans.setShortcut(new MenuShortcut(84));
        this.fmTrans.addItemListener(this);
        this.fmTrans.setState(true);
        this.fmLoad = new MenuItem("Open Sim...");
        this.fmLoad.setShortcut(new MenuShortcut(79));
        this.fmLoad.addActionListener(this);
        this.fmLoadAss = new MenuItem("Open Assembly...");
        this.fmLoadAss.setShortcut(new MenuShortcut(65));
        this.fmLoadAss.addActionListener(this);
        this.fmClose = new MenuItem("Close Sim");
        this.fmClose.setShortcut(new MenuShortcut(67));
        this.fmClose.addActionListener(this);
        this.fmHelp = new MenuItem("Help...");
        this.fmHelp.setShortcut(new MenuShortcut(112));
        this.fmHelp.addActionListener(this);
        this.fmShowBusNames = new CheckboxMenuItem("Show bus names");
        this.fmShowBusNames.setShortcut(new MenuShortcut(66));
        this.fmShowBusNames.addItemListener(this);
        this.fmShowBusNames.setState(true);
        this.fileMenu.add(this.fmLoad);
        this.fileMenu.add(this.fmLoadAss);
        this.fileMenu.addSeparator();
        this.fileMenu.add(this.fmClose);
        this.helpMenu.add(this.fmHelp);
        this.settingsMenu.add(this.fmTrans);
        this.settingsMenu.add(this.fmShowBusNames);
        this.menuBar.add(this.fileMenu);
        this.menuBar.add(this.settingsMenu);
        this.menuBar.add(this.helpMenu);
        setMenuBar(this.menuBar);
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource() == this.fmTrans) {
            this.dCanvas.b_Transparency = !this.dCanvas.b_Transparency;
            this.fmTrans.setState(this.dCanvas.b_Transparency);
            this.dCanvas.repaint();
        }
        if (itemEvent.getSource() == this.fmShowBusNames) {
            this.dCanvas.showBusNames = !this.dCanvas.showBusNames;
            this.fmShowBusNames.setState(this.dCanvas.showBusNames);
            this.dCanvas.repaint();
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.fmHelp) {
            this.sim.source.showHelp();
        }
        if (actionEvent.getSource() == this.butStart) {
            this.dCanvas.animThread.pause = false;
            changePauseBut();
            if (!this.dCanvas.animThread.animating) {
                this.sim.resetMemoryAndRegs();
                this.dCanvas.animThread.restart();
                startNewAnim();
            }
            if (this.step && !this.dCanvas.animThread.step) {
                this.dCanvas.animThread.nextAnim();
                this.step = false;
            }
        }
        if (actionEvent.getSource() == this.butPause) {
            this.dCanvas.animThread.pause = !this.dCanvas.animThread.pause;
            changePauseBut();
            if (this.step && !this.dCanvas.animThread.step) {
                this.dCanvas.animThread.nextAnim();
                this.step = false;
            }
        }
        if (actionEvent.getSource() == this.butRestart) {
            if (this.step && !this.dCanvas.animThread.step) {
                this.dCanvas.animThread.nextAnim();
                this.step = false;
            }
            if (this.dCanvas.animThread.animating) {
                this.dCanvas.animThread.restart();
                this.dCanvas.animThread.pause = false;
                changePauseBut();
            }
        }
        if (actionEvent.getSource() == this.butShowRegisters) {
            showRegisters();
        }
        if (actionEvent.getSource() == this.butShowMainMem) {
            showMainMem();
        }
        if (actionEvent.getSource() == this.butShowIntrMem) {
            showIntrMem();
        }
        if (actionEvent.getSource() == this.butSuperSpeed) {
            this.dCanvas.animThread.superSpeed = !this.dCanvas.animThread.superSpeed;
            if (this.dCanvas.animThread.superSpeed) {
                this.butSuperSpeed.setLabel("Norm");
            } else {
                this.butSuperSpeed.setLabel("Super");
            }
        }
        if (actionEvent.getSource() == this.butInstantSpeed) {
            this.dCanvas.animThread.instantSpeed = !this.dCanvas.animThread.instantSpeed;
            if (this.dCanvas.animThread.instantSpeed) {
                ProcSim.HOLDOUT = true;
                this.butInstantSpeed.setLabel("Norm");
            } else {
                ProcSim.HOLDOUT = false;
                this.butInstantSpeed.setLabel("Instant");
                this.dCanvas.showAnim(true);
                this.dCanvas.repaint();
            }
        }
        if (actionEvent.getSource() == this.butClose || actionEvent.getSource() == this.fmClose) {
            cleanupViewSim();
            this.dCanvas.animThread.restart();
            dispose();
        }
        if (actionEvent.getSource() == this.fmLoad) {
            if (this.dCanvas.animThread.animating) {
                this.dCanvas.animThread.restart();
                this.dCanvas.animThread.pause = false;
                changePauseBut();
            }
            this.sim.source.loadSim.openSim();
            this.sim.resetMemoryAndRegs();
            setupSim();
        }
        if (actionEvent.getSource() == this.fmLoadAss) {
            if (this.dCanvas.animThread.animating) {
                this.dCanvas.animThread.restart();
                this.dCanvas.animThread.pause = false;
                changePauseBut();
            }
            this.sim.source.assembly.openAss();
            this.sim.source.assembly.doParse();
            this.sim.resetMemoryAndRegs();
            setupSim();
        }
        if (actionEvent.getSource() == this.butStep) {
            if (this.step && !this.dCanvas.animThread.step) {
                this.dCanvas.animThread.nextAnim();
            }
            if (!this.dCanvas.animThread.animating) {
                this.sim.resetMemoryAndRegs();
                this.dCanvas.animThread.restart();
                startNewAnim();
            }
            this.dCanvas.animThread.step = true;
            this.dCanvas.animThread.pause = true;
            this.step = true;
            changePauseBut();
        }
    }

    private void changePauseBut() {
        if (this.dCanvas.animThread.pause) {
            this.butPause.setLabel("Resume");
        } else {
            this.butPause.setLabel("  Pause  ");
        }
    }

    public void stateChanged(ChangeEvent changeEvent) {
        if (changeEvent.getSource().equals(this.speedSlider) && !this.speedSlider.getValueIsAdjusting()) {
            this.dCanvas.animThread.speed = 400 - this.speedSlider.getValue();
            this.dCanvas.animThread.calcWaitTime();
        }
    }

    public void endOfExectution() {
        ProcSim.out("Execution ENDED");
        new MsgBox(this, "Execution finished!", false).dispose();
        this.dCanvas.animThread.restart();
    }

    public void startNewAnim() {
        this.animationList.clear();
        ProcSim.out("STARTING EXECUTION");
        ProcSim.out("Executing directives");
        Assembly assembly = this.sim.source.assembly;
        for (int i = 0; i < assembly.numDirects; i++) {
            this.sim.registers[Integer.parseInt(Functions.toDec(assembly.directives[i][1]))] = assembly.directives[i][2];
        }
        setTables();
        AnimationListItem animationListItem = new AnimationListItem();
        int i2 = 0;
        for (int i3 = 0; i3 < this.dCanvas.buses.size(); i3++) {
            DiagBus diagBus = this.dCanvas.buses.get(i3);
            diagBus.newVal = false;
            diagBus.animated = false;
            diagBus.bus.doneAnimOnce = false;
            if (diagBus.out.isStartComp) {
                this.animComp = this.dCanvas.buses.get(i3).out;
                diagBus.animated = true;
                diagBus.newVal = true;
                diagBus.bus.binaryValue = ProcFunc.zeroExtend("0", 32);
                diagBus.bus.strValue = "0";
                animationListItem.bus[i2] = i3;
                animationListItem.numBuses++;
                i2++;
            }
        }
        this.animationList.add(animationListItem);
        this.dCanvas.animate();
    }

    private void setupFrame() {
        this.jsp = new JScrollPane();
        this.jsp.setOpaque(false);
        this.jsp.setDoubleBuffered(true);
        add(this.jsp, "Center");
        add(this.lblSimName, "North");
        add(this.statusBar, "South");
    }

    public void setupControlFrame() {
        this.controlFrame = new Frame("Control Panel");
        this.controlFrame.setAlwaysOnTop(true);
        Panel panel = new Panel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double d = 0.15d;
        double d2 = 0.35d;
        int i = 10;
        if (screenSize.width < 1200) {
            d = 0.2d;
            d2 = 0.39d;
            i = 1;
        }
        this.controlFrame.setPreferredSize(new Dimension((int) (screenSize.width * d), (int) (screenSize.height * d2)));
        this.controlFrame.setLocation((int) (screenSize.width - (screenSize.width * d)), i);
        this.speedSlider = new JSlider(0, 0, 400, 400 - this.dCanvas.animThread.speed);
        this.speedSlider.addChangeListener(this);
        this.speedSlider.setMajorTickSpacing(50);
        this.speedSlider.setMinorTickSpacing(1);
        this.speedSlider.setPaintTicks(true);
        this.speedSlider.setPreferredSize(new Dimension((int) (screenSize.width * 0.14d), this.speedSlider.getPreferredSize().height));
        this.butStart.addActionListener(this);
        this.butPause.addActionListener(this);
        this.butRestart.addActionListener(this);
        this.butShowRegisters.addActionListener(this);
        this.butShowMainMem.addActionListener(this);
        this.butShowIntrMem.addActionListener(this);
        this.butSuperSpeed.addActionListener(this);
        this.butClose.addActionListener(this);
        this.butInstantSpeed.addActionListener(this);
        this.butStep.addActionListener(this);
        this.butClose.setFont(new Font("", 1, 12));
        this.butStart.setFont(new Font("", 1, 12));
        panel.add(this.butStart);
        panel.add(this.butPause);
        panel.add(this.butRestart);
        panel.add(this.butStep);
        panel.add(new Label("                                          "));
        panel.add(this.butShowRegisters);
        panel.add(this.butShowMainMem);
        panel.add(this.butShowIntrMem);
        panel.add(new Label("                                          "));
        panel.add(new Label("Speed:     "));
        panel.add(this.butSuperSpeed);
        panel.add(this.butInstantSpeed);
        panel.add(this.speedSlider);
        if (screenSize.width > 1200) {
            panel.add(new Label("                                          "));
        }
        panel.add(this.butClose);
        this.controlFrame.add(panel, "Center");
        this.controlFrame.pack();
    }

    public void setupSim() {
        this.dCanvas.addMouseListener(this.mPressHandler);
        this.dCanvas.animThread.step = false;
        this.dCanvas.animThread.pause = false;
        this.step = false;
        changePauseBut();
        this.dCanvas.calcCanvasSize(this.jsp.getSize());
        this.jsp.setViewportView(this.dCanvas);
        this.controlFrame.setVisible(true);
        showRegisters();
        showIntrMem();
        this.instrMemFrame.showMachineOld = !this.instrMemFrame.showMachine;
        setTables();
        ProcSim.HOLDOUT = this.dCanvas.animThread.instantSpeed;
        this.lblSimName.setText(this.sim.name);
        setTitle("Simulation: " + this.sim.path);
        this.dCanvas.calcCanvasSize(this.jsp.getSize());
    }

    public void cleanupViewSim() {
        this.dCanvas.removeMouseListener(this.mPressHandler);
        this.controlFrame.dispose();
        if (this.regFrame != null) {
            this.regFrame.dispose();
        }
        if (this.mainMemFrame != null) {
            this.mainMemFrame.dispose();
        }
        if (this.instrMemFrame != null) {
            this.instrMemFrame.dispose();
        }
        this.sim.resetMemoryAndRegs();
        this.jsp.remove(this.dCanvas);
        ProcSim.HOLDOUT = false;
    }

    /* loaded from: ProcSim.jar:ViewSim$mousePressHandler.class */
    class mousePressHandler extends MouseAdapter {
        mousePressHandler() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            for (int i = 0; i < ViewSim.this.sim.comps.size(); i++) {
                if (x > ViewSim.this.sim.comps.get(i).x && y > ViewSim.this.sim.comps.get(i).y && x < ViewSim.this.sim.comps.get(i).x + ViewSim.this.sim.comps.get(i).width && y < ViewSim.this.sim.comps.get(i).y + ViewSim.this.sim.comps.get(i).height) {
                    ViewSim.this.dCanvas.animThread.currentComp = ViewSim.this.sim.comps.get(i);
                    ViewSim.this.dCanvas.repaint();
                    if (!ViewSim.this.dCanvas.animThread.instantSpeed) {
                        ViewSim.this.statusBar.repaint();
                    }
                }
            }
        }
    }

    /* loaded from: ProcSim.jar:ViewSim$AnimationListItem.class */
    class AnimationListItem {
        int[] bus = new int[20];
        int numBuses = 0;

        AnimationListItem() {
        }
    }

    public void setTables() {
        if (this.regFrame != null && this.regFrame.isVisible()) {
            int i = 0;
            for (int i2 = 0; i2 < this.regFrame.MAX_VALS; i2++) {
                if (!Functions.toDec(this.sim.registers[i2]).equals("0") || !this.regFrame.hideEmpty || i2 == 0) {
                    i++;
                }
            }
            if (this.regFrame.size != i) {
                this.regFrame.size = i;
                this.regFrame.newModel(i, 4);
                this.regFrame.table.setModel(this.regFrame.tModel);
                this.regFrame.setWidths();
            }
            int i3 = 0;
            for (int i4 = 0; i4 < this.regFrame.MAX_VALS; i4++) {
                if (!Functions.toDec(this.sim.registers[i4]).equals("0") || !this.regFrame.hideEmpty || i4 == 0) {
                    this.regFrame.tModel.data[i3][0] = ProcFunc.zeroExtend(Functions.toBin(i4), 5);
                    this.regFrame.tModel.data[i3][1] = Assembly.regNumToString(i4);
                    this.regFrame.tModel.data[i3][2] = ProcFunc.slimBinary(ProcFunc.zeroExtend(this.sim.registers[i4], 32));
                    this.regFrame.tModel.data[i3][3] = Functions.toDec(this.sim.registers[i4]);
                    i3++;
                }
            }
            this.regFrame.tModel.fireTableChanged(new TableModelEvent(this.regFrame.tModel));
        }
        if (this.mainMemFrame != null && this.mainMemFrame.isVisible()) {
            int i5 = 0;
            boolean z = false;
            for (int i6 = 0; i6 < this.mainMemFrame.MAX_VALS; i6++) {
                if (i6 % 4 == 0) {
                    if (Long.toString(this.sim.dataMemory.read(i6, 4, false)).equals("0")) {
                        z = false;
                    } else {
                        z = true;
                    }
                }
                if (z || !this.mainMemFrame.hideEmpty || i6 == 0) {
                    i5++;
                }
            }
            if (this.mainMemFrame.size != i5) {
                this.mainMemFrame.size = i5;
                this.mainMemFrame.newModel(i5, 4);
                this.mainMemFrame.table.setModel(this.mainMemFrame.tModel);
                this.mainMemFrame.setWidths();
            }
            int i7 = 0;
            boolean z2 = false;
            for (int i8 = 0; i8 < this.mainMemFrame.MAX_VALS; i8++) {
                if (i8 % 4 == 0) {
                    if (Long.toString(this.sim.dataMemory.read(i8, 4, false)).equals("0")) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                }
                if (z2 || !this.mainMemFrame.hideEmpty || i8 == 0) {
                    this.mainMemFrame.tModel.data[i7][0] = Integer.toString(i8);
                    if (i8 % 4 == 0) {
                        this.mainMemFrame.tModel.data[i7][1] = Integer.toString(i8 / 4);
                        this.mainMemFrame.tModel.data[i7][3] = Long.toString(this.sim.dataMemory.read(i8, 4, false));
                    } else {
                        this.mainMemFrame.tModel.data[i7][1] = "";
                        this.mainMemFrame.tModel.data[i7][3] = "";
                    }
                    this.mainMemFrame.tModel.data[i7][2] = Long.toString(this.sim.dataMemory.read(i8, 8, true));
                    i7++;
                }
            }
            this.mainMemFrame.tModel.fireTableChanged(new TableModelEvent(this.mainMemFrame.tModel));
        }
        if (this.instrMemFrame != null && this.instrMemFrame.isVisible() && this.instrMemFrame.showMachineOld != this.instrMemFrame.showMachine) {
            if (this.instrMemFrame.MAX_VALS != this.sim.source.assembly.numInstr) {
                this.instrMemFrame.MAX_VALS = this.sim.source.assembly.numInstr;
                this.instrMemFrame.size = this.instrMemFrame.MAX_VALS;
                this.instrMemFrame.newModel(this.instrMemFrame.MAX_VALS, 4);
                this.instrMemFrame.table.setModel(this.instrMemFrame.tModel);
                this.instrMemFrame.setWidths();
            }
            this.instrMemFrame.showMachineOld = this.instrMemFrame.showMachine;
            for (int i9 = 0; i9 < this.instrMemFrame.MAX_VALS; i9++) {
                Instruction currentInstruction = this.sim.source.assembly.instr[i9];
                if (!this.sim.source.assembly.instr[i9].isEmpty()) {
                    this.instrMemFrame.tModel.data[i9][0] = ProcFunc.slimBinary(ProcFunc.zeroExtend(Functions.toBin(currentInstruction.realInstrIdx * 4), 6), true, true);
                    this.instrMemFrame.tModel.data[i9][1] = Integer.toString(currentInstruction.realInstrIdx * 4);
                }
                this.instrMemFrame.tModel.data[i9][3] = currentInstruction.comment;
                if (this.instrMemFrame.showMachine) {
                    this.instrMemFrame.tModel.data[i9][2] = currentInstruction.strMach;
                } else {
                    this.instrMemFrame.tModel.data[i9][2] = currentInstruction.str;
                }
            }
            this.instrMemFrame.tModel.fireTableChanged(new TableModelEvent(this.instrMemFrame.tModel));
        }
    }

    public boolean nextCompAnim() {
        int findNextComp = findNextComp();
        if (findNextComp == -1) {
            return false;
        }
        if (this.sim.comps.get(findNextComp).isStartComp) {
            boolean z = false;
            for (int i = findNextComp + 1; i < this.sim.comps.size() && !z; i++) {
                int findNextComp2 = findNextComp(i);
                if (findNextComp2 > -1) {
                    findNextComp = findNextComp2;
                    z = true;
                }
            }
            if (!z) {
                ProcSim.out("Doing Start Component");
            }
            if (z) {
                ProcSim.out("Found a non-start component to do!");
            }
        }
        if (this.sim.comps.get(findNextComp).isStartComp && this.sim.resetEveryRound) {
            resetAll(this.sim.comps.get(findNextComp));
        }
        if (findNextComp == -1) {
            ProcSim.outErr("No components with set inputs found: Ending animation.");
            return false;
        }
        this.animComp = this.sim.comps.get(findNextComp);
        ProcSim.outLine("next: " + this.animComp.name + " -- ");
        if (!this.animComp.doOps()) {
            return false;
        }
        ProcSim.out("");
        setTables();
        for (int i2 = 0; i2 < this.dCanvas.buses.size(); i2++) {
            DiagBus diagBus = this.dCanvas.buses.get(i2);
            if (diagBus.in.equals(this.animComp)) {
                diagBus.newVal = false;
                diagBus.animated = false;
            }
        }
        this.animationList.clear();
        for (int i3 = 0; i3 < this.dCanvas.buses.size(); i3++) {
            DiagBus diagBus2 = this.dCanvas.buses.get(i3);
            if (diagBus2.newVal && !diagBus2.animated) {
                diagBus2.bus.doneAnimOnce = true;
                diagBus2.animated = true;
                boolean z2 = false;
                for (int i4 = 0; i4 < this.animationList.size() && !z2; i4++) {
                    AnimationListItem animationListItem = this.animationList.get(i4);
                    for (int i5 = 0; i5 < animationListItem.numBuses && !z2; i5++) {
                        if (animationListItem.bus[i5] == i3) {
                            z2 = true;
                        }
                        if (this.dCanvas.buses.get(animationListItem.bus[i5]).out.equals(diagBus2.out)) {
                            animationListItem.bus[animationListItem.numBuses] = i3;
                            animationListItem.numBuses++;
                            z2 = true;
                        }
                    }
                }
                if (!z2) {
                    AnimationListItem animationListItem2 = new AnimationListItem();
                    animationListItem2.bus[0] = i3;
                    animationListItem2.numBuses = 1;
                    this.animationList.add(animationListItem2);
                }
            }
        }
        return true;
    }

    public int findDiagBus(DiagBus diagBus) {
        return this.dCanvas.buses.indexOf(diagBus);
    }

    private int findNextComp() {
        return findNextComp(0);
    }

    private int findNextComp(int i) {
        for (int i2 = i; i2 < this.sim.comps.size(); i2++) {
            int i3 = -1;
            boolean z = true;
            for (int i4 = 0; i4 < this.dCanvas.buses.size(); i4++) {
                ProcBus procBus = this.dCanvas.buses.get(i4).bus;
                if (this.dCanvas.buses.get(i4).in != null && this.dCanvas.buses.get(i4).in.equals(this.sim.comps.get(i2))) {
                    if (i3 < 0) {
                        if (procBus.optional && this.dCanvas.buses.get(i4).newVal) {
                            i3 = i4;
                        }
                    } else if (!procBus.doneAnimOnce) {
                        z = false;
                    }
                }
            }
            if (i3 > -1 && z) {
                return i2;
            }
        }
        for (int i5 = i; i5 < this.sim.comps.size(); i5++) {
            PComponent pComponent = this.sim.comps.get(i5);
            boolean z2 = true;
            for (int i6 = 0; i6 < this.dCanvas.buses.size(); i6++) {
                ProcBus procBus2 = this.dCanvas.buses.get(i6).bus;
                if (this.dCanvas.buses.get(i6).in.equals(this.sim.comps.get(i5)) && !this.dCanvas.buses.get(i6).newVal && !this.dCanvas.buses.get(i6).bus.checkOptional()) {
                    z2 = false;
                }
                if (this.dCanvas.buses.get(i6).out.equals(pComponent) && procBus2.doneAnimOnce && !isAnyInputBuses(pComponent)) {
                    z2 = false;
                }
            }
            if (z2) {
                return i5;
            }
        }
        return -1;
    }

    public void resetAll(PComponent pComponent) {
        for (int i = 0; i < this.dCanvas.buses.size(); i++) {
            DiagBus diagBus = this.dCanvas.buses.get(i);
            if (!diagBus.in.equals(pComponent)) {
                diagBus.animated = false;
                diagBus.newVal = false;
                diagBus.bus.doneAnimOnce = false;
            }
        }
        this.dCanvas.animThread.anims.clear();
    }

    private boolean isAnyInputBuses(PComponent pComponent) {
        for (int i = 0; i < this.dCanvas.buses.size(); i++) {
            if (this.dCanvas.buses.get(i).in.equals(pComponent)) {
                return true;
            }
        }
        return false;
    }

    public void showRegisters() {
        if (this.regFrame == null) {
            this.regFrame = new CompFrame("Registers", 0, this);
        }
        this.regFrame.setVisible(true);
        setTables();
    }

    public void showMainMem() {
        if (this.mainMemFrame == null) {
            this.mainMemFrame = new CompFrame("Main Memory", 1, this);
        }
        this.mainMemFrame.setVisible(true);
        setTables();
    }

    public void showIntrMem() {
        if (this.instrMemFrame == null) {
            this.instrMemFrame = new CompFrame("Instruction Memory", 2, this);
        }
        this.instrMemFrame.setVisible(true);
        setTables();
    }

    /* loaded from: ProcSim.jar:ViewSim$StatusBar.class */
    public class StatusBar extends JPanel {
        public StatusBar() {
            setPreferredSize(new Dimension(ViewSim.this.frame.getWidth(), 25));
            setSize(ViewSim.this.frame.getWidth(), 25);
        }

        public void paint(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Font font = new Font("Arial", 0, 14);
            graphics2D.setFont(font);
            int height = graphics2D.getFontMetrics(font).getHeight();
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(0, 0, ViewSim.this.frame.getWidth(), 30);
            graphics2D.setColor(Color.black);
            PComponent currentAnimComp = ViewSim.this.dCanvas.animThread.getCurrentAnimComp();
            if (currentAnimComp != null) {
                graphics2D.drawString("Component Description: " + currentAnimComp.description, 5, height);
            }
        }
    }
}
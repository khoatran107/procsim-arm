package defpackage;

import defpackage.DiagBus;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JScrollPane;

/* loaded from: ProcSim.jar:LoadSim.class */
class LoadSim extends Frame implements ActionListener, ItemListener, TextListener, MouseMotionListener {
    ProcSim source;
    MenuItem fmNew;
    MenuItem fmReload;
    MenuItem fmSaveAs;
    MenuItem fmSave;
    MenuItem fmOpen;
    MenuItem fmHelp;
    MenuItem fmClose;
    CheckboxMenuItem fmSnapGrid;
    CheckboxMenuItem fmShowBusNames;
    CheckboxMenuItem fmStickConnectedBuses;
    DiagCanvas diagCanvas;
    ParseSimXML parser;
    Simulator sim;
    EditXML editXML;
    JScrollPane jsp;
    List compList;
    NumericTextField txtWidth;
    NumericTextField txtHeight;
    List busList;
    Label txtOutName;
    Label txtInName;
    Label txtDoneBus;
    List connectedList;
    Button butUndo;
    Button butRemoveBus;
    int addingComponentNum;
    DiagBus.PermText busNameMove;
    Panel mainPanel = new Panel(new BorderLayout());
    Panel buttonPanel = new Panel(new FlowLayout());
    Button butEdit = new Button("Edit Architecture...");
    Button butClear = new Button("Clear");
    Button butDone = new Button("Done");
    Button butRemoveConnectedBuses = new Button("Remove Connected Buses");
    Button butResetConnectedBusNames = new Button("Reset Connected Names");
    Button butResetBusNames = new Button("Reset All ProcBus Names");
    Checkbox chkHideBusName = new Checkbox("Hide ProcBus Name");
    Label lblChosenSim = new Label("Name: None");
    Label lblChosenSimFile = new Label("File: None");
    Panel sidePanel = new Panel(new GridBagLayout());
    boolean addingComponent = true;
    boolean addingBus = false;
    boolean addingConnectedBus = false;
    boolean mouseDown = false;
    String saveString = "";
    boolean snapGrid = true;
    int snapAmount = 7;
    boolean showBusNames = true;
    boolean stickConnectedBuses = false;
    String[][] connectedBuses = new String[2][100];
    int numConnectedBuses = 0;
    mousePressHandler mPressHandler = new mousePressHandler();
    int compMoveFromX = -1;
    int compMoveFromY = -1;
    int busNameMoveFromX = -1;
    int busNameMoveFromY = -1;
    boolean changesMade = false;
    boolean editingXML = false;

    public LoadSim(ProcSim procSim) {
        this.source = procSim;
        this.sim = this.source.sim;
        addWindowListener(new WindowAdapter() { // from class: LoadSim.1
            public void windowClosing(WindowEvent windowEvent) {
                if (LoadSim.this.editingXML) {
                    return;
                }
                LoadSim.this.clearupLoadSim();
                LoadSim.this.dispose();
            }
        });
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("File");
        Menu menu2 = new Menu("Settings");
        Menu menu3 = new Menu("Help");
        this.fmNew = new MenuItem("New Sim");
        this.fmNew.setShortcut(new MenuShortcut(78));
        this.fmNew.addActionListener(this);
        this.fmSave = new MenuItem("Save");
        this.fmSave.setShortcut(new MenuShortcut(83));
        this.fmSave.addActionListener(this);
        this.fmOpen = new MenuItem("Open...");
        this.fmOpen.setShortcut(new MenuShortcut(79));
        this.fmOpen.addActionListener(this);
        this.fmReload = new MenuItem("Reload/Parse Sim");
        this.fmReload.setShortcut(new MenuShortcut(82));
        this.fmReload.addActionListener(this);
        this.fmSaveAs = new MenuItem("Save As...");
        this.fmSaveAs.addActionListener(this);
        this.fmHelp = new MenuItem("Help... ");
        this.fmHelp.setShortcut(new MenuShortcut(112));
        this.fmHelp.addActionListener(this);
        this.fmClose = new MenuItem("Close ");
        this.fmClose.setShortcut(new MenuShortcut(67));
        this.fmClose.addActionListener(this);
        menu.add(this.fmNew);
        menu.add(this.fmOpen);
        menu.addSeparator();
        menu.add(this.fmSave);
        menu.add(this.fmSaveAs);
        menu.add(this.fmReload);
        menu.addSeparator();
        menu.add(this.fmClose);
        menu3.add(this.fmHelp);
        this.fmSnapGrid = new CheckboxMenuItem("Snap to Grid");
        this.fmSnapGrid.setShortcut(new MenuShortcut(71));
        this.fmSnapGrid.addItemListener(this);
        this.fmSnapGrid.setState(true);
        this.fmShowBusNames = new CheckboxMenuItem("Show bus names");
        this.fmShowBusNames.setShortcut(new MenuShortcut(66));
        this.fmShowBusNames.addItemListener(this);
        this.fmShowBusNames.setState(true);
        this.fmStickConnectedBuses = new CheckboxMenuItem("Stick Connected Buses");
        this.fmStickConnectedBuses.addItemListener(this);
        this.fmStickConnectedBuses.setState(false);
        menu2.add(this.fmSnapGrid);
        menu2.add(this.fmShowBusNames);
        menu2.addSeparator();
        menu2.add(this.fmStickConnectedBuses);
        menuBar.add(menu);
        menuBar.add(menu2);
        menuBar.add(menu3);
        setMenuBar(menuBar);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.width * 0.9d), (int) (screenSize.height * 0.9d));
        setTitle("Simulation Architecture: " + this.sim.path);
        setLocation((int) ((screenSize.width / 2) - ((screenSize.width * 0.9d) / 2.0d)), (int) ((screenSize.height / 2) - ((screenSize.height * 0.9d) / 2.0d)));
        setupFrame();
        this.diagCanvas = this.source.diagCanvas;
        this.parser = new ParseSimXML(this);
        // disable default path
        // this.parser.startParse(this.source.sim);
        this.jsp = new JScrollPane();
        this.jsp.setOpaque(false);
        this.jsp.setDoubleBuffered(true);
        add(this.jsp, "Center");
        setupSidePanel();
        this.diagCanvas.calcCanvasSize(this.jsp.getSize());
        this.diagCanvas.curEditComp = 0;
        updateSim();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() { // from class: LoadSim.2
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 27) {
                    LoadSim.this.setAddComp();
                    return false;
                }
                return false;
            }
        });
        addComponentListener(new ComponentAdapter() { // from class: LoadSim.3
            public void componentResized(ComponentEvent componentEvent) {
                LoadSim.this.diagCanvas.calcCanvasSize(LoadSim.this.jsp.getSize());
            }
        });
    }

    public void setupLoadSim() {
        this.diagCanvas.addMouseListener(this.mPressHandler);
        this.diagCanvas.addMouseMotionListener(this);
        this.jsp.setViewportView(this.diagCanvas);
    }

    public void clearupLoadSim() {
        this.diagCanvas.removeMouseListener(this.mPressHandler);
        this.diagCanvas.removeMouseMotionListener(this);
        this.jsp.remove(this.diagCanvas);
        this.diagCanvas.curEditBus = -1;
        this.diagCanvas.curEditComp = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAddComp() {
        this.connectedList.select(-1);
        this.busList.select(-1);
        this.diagCanvas.curEditBus = -1;
        this.diagCanvas.curEditComp = this.compList.getSelectedIndex();
        this.addingComponent = true;
        this.addingBus = false;
        this.addingConnectedBus = false;
        this.txtWidth.setText(Integer.toString(this.sim.comps.get(this.compList.getSelectedIndex()).width));
        this.txtHeight.setText(Integer.toString(this.sim.comps.get(this.compList.getSelectedIndex()).height));
        showConnectedBuses();
        this.compMoveFromX = -1;
        this.compMoveFromY = -1;
        this.addingComponentNum = this.compList.getSelectedIndex();
        this.diagCanvas.curEditComp = this.addingComponentNum;
        this.diagCanvas.repaint();
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (this.editingXML) {
            return;
        }
        if (itemEvent.getSource() == this.compList) {
            setAddComp();
        }
        if (itemEvent.getSource() == this.busList) {
            this.connectedList.select(-1);
            this.addingComponent = false;
            this.addingBus = true;
            this.addingConnectedBus = false;
            this.diagCanvas.curEditBus = this.busList.getSelectedIndex();
            this.diagCanvas.curEditComp = -1;
            this.txtOutName.setText("Output Name: " + this.diagCanvas.buses.get(this.busList.getSelectedIndex()).outName);
            this.txtInName.setText("Input Name: " + this.diagCanvas.buses.get(this.busList.getSelectedIndex()).inText);
            showDoneBus();
            this.diagCanvas.repaint();
        }
        if (itemEvent.getSource() == this.connectedList) {
            this.busList.select(-1);
            this.addingComponent = false;
            this.addingBus = true;
            this.addingConnectedBus = true;
            this.diagCanvas.curEditBus = getSelectedBus();
            this.busList.select(Integer.parseInt(this.connectedBuses[1][this.connectedList.getSelectedIndex()]));
            this.txtOutName.setText("Output Name: " + this.diagCanvas.buses.get(getSelectedBus()).outName);
            this.txtInName.setText("Input Name: " + this.diagCanvas.buses.get(getSelectedBus()).inText);
            showDoneBus();
            this.diagCanvas.repaint();
        }
        if (itemEvent.getSource() == this.fmSnapGrid) {
            this.snapGrid = !this.snapGrid;
            this.fmSnapGrid.setState(this.snapGrid);
        }
        if (itemEvent.getSource() == this.fmShowBusNames) {
            this.showBusNames = !this.showBusNames;
            this.fmShowBusNames.setState(this.showBusNames);
            addBusNames();
            this.diagCanvas.repaint();
        }
        if (itemEvent.getSource() == this.fmStickConnectedBuses) {
            this.stickConnectedBuses = !this.stickConnectedBuses;
            this.fmStickConnectedBuses.setState(this.stickConnectedBuses);
        }
        if (itemEvent.getSource() == this.chkHideBusName) {
            this.busNameMove.hidden = this.chkHideBusName.getState();
            this.diagCanvas.repaint();
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (this.editingXML) {
            return;
        }
        if (actionEvent.getSource() == this.fmClose) {
            this.diagCanvas.curEditBus = -1;
            clearupLoadSim();
            dispose();
        }
        if (actionEvent.getSource() == this.fmHelp) {
            this.source.showHelp();
        }
        if (actionEvent.getSource() == this.butDone) {
            saveChanges();
            this.changesMade = false;
            this.diagCanvas.curEditBus = -1;
            clearupLoadSim();
            dispose();
        }
        if (actionEvent.getSource() == this.butEdit) {
            saveChanges();
            if (this.editXML == null) {
                this.editXML = new EditXML(this.source);
            }
            this.editXML.setVisible(true);
            this.editXML.starting();
            this.editingXML = true;
        }
        if (actionEvent.getSource() == this.fmReload) {
            this.diagCanvas.curEditBus = -1;
            this.parser.startParse(this.source.sim);
            resetSim();
            updateSim();
        }
        if (actionEvent.getSource() == this.butUndo) {
            if (this.addingComponent) {
                return;
            }
            this.addingComponent = false;
            this.addingBus = true;
            doUndo();
        }
        if (actionEvent.getSource() == this.butRemoveBus) {
            if (this.addingComponent) {
                return;
            }
            DiagBus diagBus = this.diagCanvas.buses.get(getSelectedBus());
            if (!diagBus.doneBus) {
                setAddComp();
            }
            diagBus.resetPoints();
            this.diagCanvas.repaint();
        }
        if (actionEvent.getSource() == this.butClear) {
            MsgBox msgBox = new MsgBox(this, "Are you sure you wish to clear all Buses?", true);
            msgBox.dispose();
            if (!msgBox.id) {
                return;
            }
            for (int i = 0; i < this.diagCanvas.buses.size(); i++) {
                this.diagCanvas.buses.get(i).resetPoints();
            }
            this.addingComponent = true;
            this.addingBus = false;
            this.diagCanvas.repaint();
        }
        if (actionEvent.getSource() == this.butResetBusNames) {
            MsgBox msgBox2 = new MsgBox(this, "Are you sure you wish to reset all ProcBus Names?", true);
            msgBox2.dispose();
            if (!msgBox2.id) {
                return;
            }
            addBusNames(true);
            this.diagCanvas.repaint();
        }
        if (actionEvent.getSource() == this.butResetConnectedBusNames) {
            resetConnectedBusNames();
            this.diagCanvas.repaint();
        }
        if (actionEvent.getSource() == this.fmSaveAs) {
            saveAs();
        }
        if (actionEvent.getSource() == this.fmNew) {
            MsgBox msgBox3 = new MsgBox(this, "Save Sim before creating new?", true);
            msgBox3.dispose();
            if (msgBox3.id) {
                saveSim();
            }
            if (!saveAs(true)) {
                return;
            }
            this.parser.startParse(this.sim);
            resetSim();
            updateSim();
        }
        if (actionEvent.getSource() == this.fmOpen) {
            openSim();
        }
        if (actionEvent.getSource() == this.fmSave) {
            saveSim();
        }
        if (actionEvent.getSource() == this.butRemoveConnectedBuses) {
            PComponent pComponent = this.diagCanvas.comps.get(this.addingComponentNum);
            for (int i2 = 0; i2 < this.diagCanvas.buses.size(); i2++) {
                DiagBus diagBus2 = this.diagCanvas.buses.get(i2);
                if (diagBus2.in.equals(pComponent) || diagBus2.out.equals(pComponent)) {
                    diagBus2.resetPoints();
                }
            }
            this.addingComponent = true;
            this.addingBus = false;
            this.diagCanvas.repaint();
        }
    }

    private void saveChanges() {
        if (this.changesMade) {
            MsgBox msgBox = new MsgBox(this, "Do you wish to save any changes?", true);
            msgBox.dispose();
            if (msgBox.id) {
                saveSim();
            }
        }
    }

    public void openSim() {
        this.diagCanvas.curEditBus = -1;
        String loadFile = loadFile(new Frame(), "Open Simulator...", ".\\", "*.xml");
        if (!loadFile.substring(loadFile.length() - 3, loadFile.length()).equals("xml")) {
            new MsgBox(this, "Can only open .xml files", false).dispose();
        }
        if (loadFile.equals(".\\\\null") || loadFile.equals(".\\null")) {
            return;
        }
        this.source.sim.path = loadFile;
        this.parser.startParse(this.source.sim);
        resetSim();
        updateSim();
    }

    public void saveAs() {
        saveAs(false);
    }

    public boolean saveAs(boolean z) {
        String saveFile;
        if (z) {
            saveFile = saveFile(new Frame(), "Create New Sim...", ".\\", "NewSim.xml");
        } else {
            saveFile = saveFile(new Frame(), "Save Simulator...", ".\\", this.sim.getFileName(this.source.sim.path));
        }
        if (saveFile.equals(this.sim.path) && !z) {
            saveSim();
            return true;
        }
        if (saveFile.equals(".\\\\null.xml") || saveFile.equals(".\\null.xml")) {
            return false;
        }
        new File(saveFile.substring(0, saveFile.length() - 3) + "sim").delete();
        try {
            ProcFunc.copyFile(this.sim.path, saveFile);
        } catch (Exception e) {
            ProcSim.outErr("Error saving file - " + e.toString());
        }
        this.sim.path = saveFile;
        if (!z) {
            saveSim();
            return true;
        }
        return true;
    }

    public void addBusNames() {
        addBusNames(false);
    }

    public void addBusNames(boolean z) {
        if (this.showBusNames) {
            for (int i = 0; i < this.diagCanvas.buses.size(); i++) {
                DiagBus diagBus = this.diagCanvas.buses.get(i);
                int i2 = diagBus.x[0];
                int i3 = diagBus.y[0];
                if (z || diagBus.busLabelOut.x <= 0) {
                    diagBus.busLabelOut.hidden = false;
                    diagBus.busLabelOut.x = i2 - 2;
                    diagBus.busLabelOut.y = i3;
                }
                diagBus.busLabelOut.str = diagBus.outName;
                if (diagBus.numPoints - 1 > -1) {
                    int i4 = diagBus.x[diagBus.numPoints - 1];
                    int i5 = diagBus.y[diagBus.numPoints - 1];
                    if (z || diagBus.busLabelIn.x <= 0) {
                        diagBus.busLabelIn.hidden = false;
                        diagBus.busLabelIn.x = i4;
                        diagBus.busLabelIn.y = i5;
                    }
                    diagBus.busLabelIn.str = diagBus.inText;
                }
            }
            return;
        }
        for (int i6 = 0; i6 < this.diagCanvas.buses.size(); i6++) {
            DiagBus diagBus2 = this.diagCanvas.buses.get(i6);
            diagBus2.busLabelOut.str = null;
            diagBus2.busLabelIn.str = null;
        }
    }

    public int getSelectedBus() {
        int selectedIndex;
        if (this.addingConnectedBus && this.connectedList.getSelectedIndex() > -1) {
            selectedIndex = Integer.parseInt(this.connectedBuses[1][this.connectedList.getSelectedIndex()]);
        } else {
            selectedIndex = this.busList.getSelectedIndex();
        }
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        return selectedIndex;
    }

    void doUndo() {
        DiagBus diagBus = this.diagCanvas.buses.get(getSelectedBus());
        if (diagBus.numPoints == 0) {
            return;
        }
        diagBus.x[diagBus.numPoints] = -20;
        diagBus.y[diagBus.numPoints] = -20;
        diagBus.numPoints--;
        diagBus.x[diagBus.numPoints] = -20;
        diagBus.y[diagBus.numPoints] = -20;
        if (diagBus.numPoints == 1) {
            diagBus.numPoints--;
        }
        diagBus.x[diagBus.numPoints] = -20;
        diagBus.y[diagBus.numPoints] = -20;
        diagBus.doneBus = false;
        this.diagCanvas.repaint();
    }

    public void textValueChanged(TextEvent textEvent) {
        if (textEvent.getSource() == this.txtHeight) {
            if (this.txtHeight.getText().equals("") || Integer.parseInt(this.txtHeight.getText()) < 0 || Integer.parseInt(this.txtHeight.getText()) == this.sim.comps.get(this.compList.getSelectedIndex()).height) {
                return;
            }
            this.sim.comps.get(this.compList.getSelectedIndex()).height = Integer.parseInt(this.txtHeight.getText());
            this.diagCanvas.repaint();
            compMoved(this.sim.comps.get(this.addingComponentNum).x, this.sim.comps.get(this.addingComponentNum).y, true);
            return;
        }
        if (textEvent.getSource() != this.txtWidth || this.txtWidth.getText().equals("") || Integer.parseInt(this.txtWidth.getText()) < 0 || Integer.parseInt(this.txtWidth.getText()) == this.sim.comps.get(this.compList.getSelectedIndex()).width) {
            return;
        }
        this.sim.comps.get(this.compList.getSelectedIndex()).width = Integer.parseInt(this.txtWidth.getText());
        this.diagCanvas.repaint();
        compMoved(this.sim.comps.get(this.addingComponentNum).x, this.sim.comps.get(this.addingComponentNum).y, true);
    }

    public void setupFrame() {
        this.buttonPanel.add(this.butEdit);
        this.buttonPanel.add(this.butClear);
        this.buttonPanel.add(this.butDone);
        this.butEdit.addActionListener(this);
        this.butDone.addActionListener(this);
        this.butClear.addActionListener(this);
        add(this.buttonPanel, "South");
        add(this.mainPanel, "Center");
        add(this.lblChosenSim, "North");
        this.mainPanel.add(this.lblChosenSimFile, "North");
    }

    public String loadFile(Frame frame, String str, String str2, String str3) {
        String str4;
        FileDialog fileDialog = new FileDialog(frame, str, 0);
        fileDialog.setFile(str3);
        fileDialog.setDirectory(str2);
        fileDialog.setLocation(50, 50);
        fileDialog.setVisible(true);
        if (fileDialog.getDirectory().substring(fileDialog.getDirectory().length() - 1, fileDialog.getDirectory().length()).equals(fileSep())) {
            str4 = fileDialog.getDirectory() + fileDialog.getFile();
        } else {
            str4 = fileDialog.getDirectory() + fileSep() + fileDialog.getFile();
        }
        if (str4.substring(str4.length() - 4, str4.length()).equals(".sim")) {
            str4 = str4.substring(0, str4.length() - 3) + "xml";
        }
        return str4;
    }

    public String saveFile(Frame frame, String str, String str2, String str3) {
        String str4;
        FileDialog fileDialog = new FileDialog(frame, str, 1);
        fileDialog.setFile(str3);
        fileDialog.setDirectory(str2);
        fileDialog.setLocation(50, 50);
        fileDialog.setVisible(true);
        if (fileDialog.getDirectory().substring(fileDialog.getDirectory().length() - 1, fileDialog.getDirectory().length()).equals(fileSep())) {
            str4 = fileDialog.getDirectory() + fileDialog.getFile();
        } else {
            str4 = fileDialog.getDirectory() + fileSep() + fileDialog.getFile();
        }
        if (str4.substring(str4.length() - 4, str4.length()).equals(".sim")) {
            str4 = str4.substring(0, str4.length() - 3) + "xml";
        }
        if (!str4.substring(str4.length() - 3, str4.length()).equals("xml")) {
            str4 = str4 + ".xml";
        }
        return str4;
    }

    public String fileSep() {
        return ProcFunc.fileSep();
    }

    public void resetSim() {
        this.compList.removeAll();
        this.busList.removeAll();
        this.connectedList.removeAll();
        this.diagCanvas.buses.clear();
        this.parser.createDiagBuses();
    }

    public void updateSim() {
        String str;
        String str2;
        this.lblChosenSimFile.setText("File: " + this.source.sim.path);
        this.lblChosenSim.setText("Name: " + this.source.sim.name);
        this.diagCanvas.comps = this.sim.comps;
        for (int i = 0; i < this.sim.comps.size(); i++) {
            this.compList.add(this.sim.comps.get(i).name);
        }
        this.compList.select(0);
        for (int i2 = 0; i2 < this.diagCanvas.buses.size(); i2++) {
            if (this.diagCanvas.buses.get(i2).in == null) {
                str = "None";
            } else {
                str = this.diagCanvas.buses.get(i2).in.name;
            }
            if (this.diagCanvas.buses.get(i2).out == null) {
                str2 = "None";
            } else {
                str2 = this.diagCanvas.buses.get(i2).out.name;
            }
            this.busList.add(str2 + "->" + str);
        }
        try {
            this.txtWidth.setText(Integer.toString(this.sim.comps.get(this.compList.getSelectedIndex()).width));
            this.txtHeight.setText(Integer.toString(this.sim.comps.get(this.compList.getSelectedIndex()).height));
            loadSim();
            addBusNames();
            showDoneBus();
            showConnectedBuses();
        } catch (Exception e) {
            ProcSim.outErr("Error in loaded XML doc - Simulation not ready!");
        }
        this.diagCanvas.repaint();
        setTitle("Simulation Architecture: " + this.sim.path);
        this.source.lblSim.setText("   SimPath: " + this.source.sim.path);
        this.source.lblSimName.setText("   Name: " + this.source.sim.name);
        this.diagCanvas.calcCanvasSize(this.jsp.getSize());
    }

    public void mouseMoved(MouseEvent mouseEvent) {
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.editingXML || this.sim.comps.size() == 0) {
            return;
        }
        this.changesMade = true;
        this.mouseDown = true;
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        if (this.snapGrid) {
            x = ProcFunc.round(x, this.snapAmount);
            y = ProcFunc.round(y, this.snapAmount);
        }
        if (this.addingBus) {
            DiagBus diagBus = this.diagCanvas.buses.get(getSelectedBus());
            if (diagBus.doneBus) {
                return;
            }
            if (diagBus.out != null && diagBus.out.x < 0 && diagBus.out.y < 0) {
                return;
            }
            if (diagBus.in != null && diagBus.numPoints > 0 && x > diagBus.in.x && y > diagBus.in.y && x < diagBus.in.x + diagBus.in.width && y < diagBus.in.y + diagBus.in.height) {
                int[] calcClosestPoint = calcClosestPoint(x, y, diagBus.in.x, diagBus.in.y, diagBus.in.width, diagBus.in.height);
                x = calcClosestPoint[0];
                y = calcClosestPoint[1];
                this.diagCanvas.showAttach = true;
            } else {
                this.diagCanvas.showAttach = false;
            }
            if (diagBus.numPoints == 0 && diagBus.out != null) {
                int[] calcClosestPoint2 = calcClosestPoint(x, y, diagBus.out.x, diagBus.out.y, diagBus.out.width, diagBus.out.height);
                x = calcClosestPoint2[0];
                y = calcClosestPoint2[1];
            } else if (diagBus.numPoints != 0 || diagBus.out == null) {
            }
            diagBus.x[diagBus.numPoints] = x;
            diagBus.y[diagBus.numPoints] = y;
            this.diagCanvas.repaint();
        }
        if (this.addingComponent && this.busNameMoveFromX > -1) {
            this.busNameMove.x = mouseEvent.getX() - this.busNameMoveFromX;
            this.busNameMove.y = (mouseEvent.getY() - this.busNameMoveFromY) + this.busNameMove.height;
            this.diagCanvas.repaint();
        }
        if (this.addingComponent && this.compMoveFromX > -1) {
            this.addingComponentNum = this.compList.getSelectedIndex();
            PComponent pComponent = this.sim.comps.get(this.addingComponentNum);
            if (this.diagCanvas.checkConnected(this.sim.comps.get(this.addingComponentNum))) {
                compMoved(x - this.compMoveFromX, y - this.compMoveFromY);
            }
            pComponent.x = x - this.compMoveFromX;
            pComponent.y = y - this.compMoveFromY;
            this.diagCanvas.repaint();
        }
    }

    /* loaded from: ProcSim.jar:LoadSim$mousePressHandler.class */
    class mousePressHandler extends MouseAdapter {
        mousePressHandler() {
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            if (LoadSim.this.editingXML || LoadSim.this.sim.comps.size() == 0) {
                return;
            }
            LoadSim.this.mouseDown = false;
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            if (LoadSim.this.snapGrid) {
                x = ProcFunc.round(x, LoadSim.this.snapAmount);
                y = ProcFunc.round(y, LoadSim.this.snapAmount);
            }
            if (LoadSim.this.addingBus) {
                DiagBus diagBus = LoadSim.this.diagCanvas.buses.get(LoadSim.this.getSelectedBus());
                if (diagBus.doneBus || (diagBus.out != null && diagBus.out.x < 0 && diagBus.out.y < 0)) {
                    LoadSim.this.setAddComp();
                    return;
                }
                if (diagBus.in != null && diagBus.numPoints > 0 && x > diagBus.in.x && y > diagBus.in.y && x < diagBus.in.x + diagBus.in.width && y < diagBus.in.y + diagBus.in.height) {
                    diagBus.doneBus = true;
                    LoadSim.this.addingComponent = true;
                    LoadSim.this.addingBus = false;
                    LoadSim.this.setAddComp();
                }
                if (diagBus.x[diagBus.numPoints] == -1 || diagBus.y[diagBus.numPoints] == -1) {
                    return;
                }
                diagBus.numPoints++;
                if (diagBus.numPoints > 99) {
                    diagBus.numPoints = 99;
                }
                LoadSim.this.showDoneBus();
                LoadSim.this.calcBusNamePos(diagBus);
                LoadSim.this.diagCanvas.repaint();
            }
            LoadSim.this.compMoveFromX = -1;
            LoadSim.this.compMoveFromY = -1;
            LoadSim.this.busNameMoveFromX = -1;
            LoadSim.this.diagCanvas.calcCanvasSize(LoadSim.this.jsp.getSize());
        }

        public void mousePressed(MouseEvent mouseEvent) {
            if (LoadSim.this.editingXML || LoadSim.this.sim.comps.size() == 0) {
                return;
            }
            LoadSim.this.mouseDown = true;
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            if (LoadSim.this.snapGrid) {
                x = ProcFunc.round(x, LoadSim.this.snapAmount);
                y = ProcFunc.round(y, LoadSim.this.snapAmount);
            }
            if (LoadSim.this.addingBus) {
                DiagBus diagBus = LoadSim.this.diagCanvas.buses.get(LoadSim.this.getSelectedBus());
                if (diagBus.doneBus) {
                    return;
                }
                if (diagBus.out != null && diagBus.out.x < 0 && diagBus.out.y < 0) {
                    return;
                }
                LoadSim.this.calcBusNamePos(diagBus);
                if (diagBus.in != null && diagBus.numPoints > 0 && x > diagBus.in.x && y > diagBus.in.y && x < diagBus.in.x + diagBus.in.width && y < diagBus.in.y + diagBus.in.height) {
                    int[] calcClosestPoint = LoadSim.this.calcClosestPoint(x, y, diagBus.in.x, diagBus.in.y, diagBus.in.width, diagBus.in.height);
                    x = calcClosestPoint[0];
                    y = calcClosestPoint[1];
                    LoadSim.this.diagCanvas.showAttach = true;
                } else {
                    LoadSim.this.diagCanvas.showAttach = false;
                }
                if (diagBus.numPoints == 0 && diagBus.out != null) {
                    int[] calcClosestPoint2 = LoadSim.this.calcClosestPoint(x, y, diagBus.out.x, diagBus.out.y, diagBus.out.width, diagBus.out.height);
                    x = calcClosestPoint2[0];
                    y = calcClosestPoint2[1];
                }
                diagBus.x[diagBus.numPoints] = x;
                diagBus.y[diagBus.numPoints] = y;
                LoadSim.this.diagCanvas.repaint();
            }
            if (LoadSim.this.addingComponent) {
                LoadSim.this.addingComponentNum = LoadSim.this.compList.getSelectedIndex();
                PComponent pComponent = LoadSim.this.sim.comps.get(LoadSim.this.addingComponentNum);
                if (LoadSim.this.busNameMove != null) {
                    LoadSim.this.busNameMove.moving = false;
                }
                for (int i = 0; i < LoadSim.this.diagCanvas.buses.size(); i++) {
                    DiagBus diagBus2 = LoadSim.this.diagCanvas.buses.get(i);
                    int i2 = diagBus2.busLabelOut.width + 2;
                    int i3 = diagBus2.busLabelOut.height - 1;
                    int i4 = diagBus2.busLabelOut.x - 4;
                    int i5 = (diagBus2.busLabelOut.y - diagBus2.busLabelOut.height) + 2;
                    int x2 = mouseEvent.getX();
                    int y2 = mouseEvent.getY();
                    if (!diagBus2.busLabelOut.hidden && x2 > i4 && y2 > i5 && x2 < i4 + i2 && y2 < i5 + i3) {
                        LoadSim.this.setMovingBusName(diagBus2.busLabelOut, x2, y2);
                        diagBus2.customNamePos = true;
                        return;
                    }
                    int i6 = diagBus2.busLabelIn.width + 2;
                    int i7 = diagBus2.busLabelIn.height - 1;
                    int i8 = diagBus2.busLabelIn.x - 4;
                    int i9 = (diagBus2.busLabelIn.y - diagBus2.busLabelIn.height) + 2;
                    if (!diagBus2.busLabelIn.hidden && x2 > i8 && y2 > i9 && x2 < i8 + i6 && y2 < i9 + i7) {
                        LoadSim.this.setMovingBusName(diagBus2.busLabelIn, x2, y2);
                        diagBus2.customNamePos = true;
                        return;
                    }
                }
                for (int i10 = 0; i10 < LoadSim.this.sim.comps.size(); i10++) {
                    if (x > LoadSim.this.sim.comps.get(i10).x && y > LoadSim.this.sim.comps.get(i10).y && x < LoadSim.this.sim.comps.get(i10).x + LoadSim.this.sim.comps.get(i10).width && y < LoadSim.this.sim.comps.get(i10).y + LoadSim.this.sim.comps.get(i10).height) {
                        LoadSim.this.compList.select(i10);
                        LoadSim.this.addingComponentNum = i10;
                        LoadSim.this.compMoveFromX = x - LoadSim.this.sim.comps.get(LoadSim.this.addingComponentNum).x;
                        LoadSim.this.compMoveFromY = y - LoadSim.this.sim.comps.get(LoadSim.this.addingComponentNum).y;
                        LoadSim.this.txtWidth.setText(Integer.toString(LoadSim.this.sim.comps.get(i10).width));
                        LoadSim.this.txtHeight.setText(Integer.toString(LoadSim.this.sim.comps.get(i10).height));
                        LoadSim.this.diagCanvas.curEditComp = LoadSim.this.addingComponentNum;
                        LoadSim.this.showConnectedBuses();
                        LoadSim.this.diagCanvas.curEditBus = -1;
                    }
                }
                if (pComponent.x < 0 && pComponent.y < 0) {
                    pComponent.x = x - (pComponent.width / 2);
                    pComponent.y = y - (pComponent.height / 2);
                    LoadSim.this.compMoveFromX = pComponent.width / 2;
                    LoadSim.this.compMoveFromY = pComponent.height / 2;
                }
                LoadSim.this.diagCanvas.repaint();
            }
        }
    }

    public void setMovingBusName(DiagBus.PermText permText, int i, int i2) {
        permText.moving = true;
        this.busNameMoveFromX = i - permText.x;
        this.busNameMoveFromY = i2 - (permText.y - permText.height);
        this.busNameMove = permText;
        this.chkHideBusName.setState(this.busNameMove.hidden);
        this.diagCanvas.repaint();
    }

    private void resetConnectedBusNames() {
        PComponent pComponent = this.sim.comps.get(this.compList.getSelectedIndex());
        for (int i = 0; i < this.diagCanvas.buses.size(); i++) {
            if ((this.diagCanvas.buses.get(i).in == pComponent && this.diagCanvas.buses.get(i).x[this.diagCanvas.buses.get(i).numPoints] > 0) || (this.diagCanvas.buses.get(i).doneBus && this.diagCanvas.buses.get(i).in == pComponent)) {
                DiagBus diagBus = this.diagCanvas.buses.get(i);
                diagBus.busLabelIn.hidden = false;
                if (this.showBusNames && diagBus.numPoints - 1 > -1) {
                    int i2 = diagBus.x[diagBus.numPoints - 1];
                    int i3 = diagBus.y[diagBus.numPoints - 1];
                    diagBus.busLabelIn.x = i2;
                    diagBus.busLabelIn.y = i3;
                    diagBus.busLabelIn.str = diagBus.inText;
                    diagBus.customNamePos = false;
                }
            } else if ((this.diagCanvas.buses.get(i).out == pComponent && this.diagCanvas.buses.get(i).x[0] > 0) || (this.diagCanvas.buses.get(i).doneBus && this.diagCanvas.buses.get(i).out == pComponent)) {
                DiagBus diagBus2 = this.diagCanvas.buses.get(i);
                diagBus2.busLabelOut.hidden = false;
                if (this.showBusNames) {
                    int i4 = diagBus2.x[0];
                    int i5 = diagBus2.y[0];
                    diagBus2.busLabelOut.x = i4 - 2;
                    diagBus2.busLabelOut.y = i5;
                    diagBus2.busLabelOut.str = diagBus2.outName;
                    diagBus2.customNamePos = false;
                }
            }
        }
    }

    public void calcBusNamePos(DiagBus diagBus) {
        if (this.showBusNames) {
            int i = diagBus.x[0];
            int i2 = diagBus.y[0];
            diagBus.busLabelOut.x = i - 2;
            diagBus.busLabelOut.y = i2;
            diagBus.busLabelOut.str = diagBus.outName;
            if (diagBus.numPoints - 1 > -1) {
                int i3 = diagBus.x[diagBus.numPoints - 1];
                int i4 = diagBus.y[diagBus.numPoints - 1];
                diagBus.busLabelIn.x = i3;
                diagBus.busLabelIn.y = i4;
                diagBus.busLabelIn.str = diagBus.inText;
            }
        }
    }

    public void compMoved(int i, int i2) {
        compMoved(i, i2, false);
    }

    public void compMoved(int i, int i2, boolean z) {
        int i3 = this.sim.comps.get(this.addingComponentNum).x;
        int i4 = this.sim.comps.get(this.addingComponentNum).y;
        int i5 = this.sim.comps.get(this.addingComponentNum).width;
        int i6 = this.sim.comps.get(this.addingComponentNum).height;
        PComponent pComponent = this.sim.comps.get(this.addingComponentNum);
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        for (int i7 = 0; i7 < this.diagCanvas.buses.size(); i7++) {
            if ((this.diagCanvas.buses.get(i7).in == pComponent && this.diagCanvas.buses.get(i7).x[this.diagCanvas.buses.get(i7).numPoints] > 0) || (this.diagCanvas.buses.get(i7).doneBus && this.diagCanvas.buses.get(i7).in == pComponent)) {
                vector.add(this.diagCanvas.buses.get(i7));
            } else if ((this.diagCanvas.buses.get(i7).out == pComponent && this.diagCanvas.buses.get(i7).x[0] > 0) || (this.diagCanvas.buses.get(i7).doneBus && this.diagCanvas.buses.get(i7).out == pComponent)) {
                vector2.add(this.diagCanvas.buses.get(i7));
            }
        }
        for (int i8 = 0; i8 < vector2.size(); i8++) {
            DiagBus diagBus = (DiagBus) vector2.get(i8);
            int i9 = diagBus.x[0];
            int i10 = diagBus.y[0];
            if (!this.stickConnectedBuses || z) {
                int[] calcClosestPoint = calcClosestPoint(diagBus.x[1], diagBus.y[1], i, i2, i5, i6);
                diagBus.x[0] = calcClosestPoint[0];
                diagBus.y[0] = calcClosestPoint[1];
            } else {
                int i11 = i9 - pComponent.x;
                int i12 = i10 - pComponent.y;
                diagBus.x[0] = i11 + i;
                diagBus.y[0] = i12 + i2;
            }
            if (!diagBus.customNamePos) {
                calcBusNamePos(diagBus);
            }
        }
        for (int i13 = 0; i13 < vector.size(); i13++) {
            DiagBus diagBus2 = (DiagBus) vector.get(i13);
            int i14 = diagBus2.x[diagBus2.numPoints - 1];
            int i15 = diagBus2.y[diagBus2.numPoints - 1];
            if (!this.stickConnectedBuses || z) {
                int[] calcClosestPoint2 = calcClosestPoint(diagBus2.x[diagBus2.numPoints - 2], diagBus2.y[diagBus2.numPoints - 2], i, i2, i5, i6);
                diagBus2.x[diagBus2.numPoints - 1] = calcClosestPoint2[0];
                diagBus2.y[diagBus2.numPoints - 1] = calcClosestPoint2[1];
            } else {
                int i16 = i14 - pComponent.x;
                int i17 = i15 - pComponent.y;
                diagBus2.x[diagBus2.numPoints - 1] = i16 + i;
                diagBus2.y[diagBus2.numPoints - 1] = i17 + i2;
            }
            if (!diagBus2.customNamePos) {
                calcBusNamePos(diagBus2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] calcClosestPoint(int i, int i2, int i3, int i4, int i5, int i6) {
        int[] iArr = new int[2];
        int i7 = i2 - i4;
        int i8 = (i3 + i5) - i;
        int i9 = (i4 + i6) - i2;
        int i10 = i - i3;
        if (i7 < i8 && i7 < i9 && i7 < i10) {
            i2 = i4;
            if (i > i3 + i5) {
                i = i3 + i5;
            }
            if (i < i3) {
                i = i3;
            }
        } else if (i8 < i7 && i8 < i9 && i8 < i10) {
            i = i3 + i5;
            if (i2 > i4 + i6) {
                i2 = i4 + i6;
            }
            if (i2 < i4) {
                i2 = i4;
            }
        } else if (i9 < i7 && i9 < i8 && i9 < i10) {
            i2 = i4 + i6;
            if (i > i3 + i5) {
                i = i3 + i5;
            }
            if (i < i3) {
                i = i3;
            }
        } else {
            i = i3;
            if (i2 > i4 + i6) {
                i2 = i4 + i6;
            }
            if (i2 < i4) {
                i2 = i4;
            }
        }
        iArr[0] = i;
        iArr[1] = i2;
        return iArr;
    }

    public void loadSim() {
        int findBus;
        int findBus2;
        File file = new File(this.sim.path.substring(0, this.sim.path.length() - 3) + "sim");
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                    stringBuffer.append("\n");
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e2) {
                ProcSim.outErr("Simulation Diagram not found!");
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
                return;
            } catch (IOException e4) {
                e4.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
            }
            try {
                this.saveString = stringBuffer.toString();
                StringTokenizer stringTokenizer = new StringTokenizer(this.saveString, "\n");
                String nextToken = stringTokenizer.nextToken();
                String nextToken2 = stringTokenizer.nextToken();
                String nextToken3 = stringTokenizer.nextToken();
                StringTokenizer stringTokenizer2 = new StringTokenizer(nextToken, ",");
                int countTokens = stringTokenizer2.countTokens();
                for (int i = 0; i < countTokens / 5; i++) {
                    int findComponent = this.parser.findComponent(stringTokenizer2.nextToken());
                    if (findComponent > -1) {
                        this.sim.comps.get(findComponent).x = Integer.parseInt(stringTokenizer2.nextToken());
                        this.sim.comps.get(findComponent).y = Integer.parseInt(stringTokenizer2.nextToken());
                        this.sim.comps.get(findComponent).height = Integer.parseInt(stringTokenizer2.nextToken());
                        this.sim.comps.get(findComponent).width = Integer.parseInt(stringTokenizer2.nextToken());
                    }
                }
                ProcSim.out("Loaded component positions");
                StringTokenizer stringTokenizer3 = new StringTokenizer(nextToken2, "|");
                int countTokens2 = stringTokenizer3.countTokens();
                String[] strArr = new String[countTokens2];
                for (int i2 = 0; i2 < countTokens2; i2++) {
                    strArr[i2] = stringTokenizer3.nextToken();
                }
                for (int i3 = 0; i3 < countTokens2; i3++) {
                    if (strArr[i3] != null) {
                        StringTokenizer stringTokenizer4 = new StringTokenizer(strArr[i3], ",");
                        int findComponent2 = this.parser.findComponent(stringTokenizer4.nextToken());
                        int findComponent3 = this.parser.findComponent(stringTokenizer4.nextToken());
                        if (findComponent2 > -1 && findComponent3 > -1 && (findBus2 = this.diagCanvas.findBus(stringTokenizer4.nextToken(), this.sim.comps.get(findComponent2), this.sim.comps.get(findComponent3))) > -1) {
                            this.diagCanvas.buses.get(findBus2).numPoints = (stringTokenizer4.countTokens() - 1) / 2;
                            if (stringTokenizer4.countTokens() - 1 > 0) {
                                for (int i4 = 0; i4 < this.diagCanvas.buses.get(findBus2).numPoints; i4++) {
                                    this.diagCanvas.buses.get(findBus2).x[i4] = Integer.parseInt(stringTokenizer4.nextToken());
                                    this.diagCanvas.buses.get(findBus2).y[i4] = Integer.parseInt(stringTokenizer4.nextToken());
                                }
                            }
                            if (stringTokenizer4.nextToken().equals("1")) {
                                this.diagCanvas.buses.get(findBus2).doneBus = true;
                            } else {
                                this.diagCanvas.buses.get(findBus2).doneBus = false;
                            }
                        }
                    }
                }
                ProcSim.out("Loaded bus positions");
                StringTokenizer stringTokenizer5 = new StringTokenizer(nextToken3, ",");
                int countTokens3 = stringTokenizer5.countTokens();
                for (int i5 = 0; i5 < countTokens3 / 9; i5++) {
                    int findComponent4 = this.parser.findComponent(stringTokenizer5.nextToken());
                    int findComponent5 = this.parser.findComponent(stringTokenizer5.nextToken());
                    if (findComponent4 > -1 && findComponent5 > -1 && (findBus = this.diagCanvas.findBus(stringTokenizer5.nextToken(), this.sim.comps.get(findComponent4), this.sim.comps.get(findComponent5))) > -1) {
                        this.diagCanvas.buses.get(findBus).busLabelOut.x = Integer.parseInt(stringTokenizer5.nextToken());
                        this.diagCanvas.buses.get(findBus).busLabelOut.y = Integer.parseInt(stringTokenizer5.nextToken());
                        if (stringTokenizer5.nextToken().equals("1")) {
                            this.diagCanvas.buses.get(findBus).busLabelOut.hidden = true;
                        } else {
                            this.diagCanvas.buses.get(findBus).busLabelOut.hidden = false;
                        }
                        this.diagCanvas.buses.get(findBus).busLabelIn.x = Integer.parseInt(stringTokenizer5.nextToken());
                        this.diagCanvas.buses.get(findBus).busLabelIn.y = Integer.parseInt(stringTokenizer5.nextToken());
                        if (stringTokenizer5.nextToken().equals("1")) {
                            this.diagCanvas.buses.get(findBus).busLabelIn.hidden = true;
                        } else {
                            this.diagCanvas.buses.get(findBus).busLabelIn.hidden = false;
                        }
                    }
                }
                ProcSim.out("Loaded bus name positions");
                this.txtHeight.setText(Integer.toString(this.sim.comps.get(0).height));
                this.txtWidth.setText(Integer.toString(this.sim.comps.get(0).width));
            } catch (Exception e6) {
                ProcSim.outErr("Error in simulator diagram file - ");
                e6.printStackTrace();
            }
            ProcSim.out("Loaded simulator diagram");
        } catch (Throwable th) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                    throw th;
                }
            }
            throw th;
        }
    }

    public String getSaveString() {
        String str;
        String str2;
        String str3;
        String str4 = "";
        for (int i = 0; i < this.sim.comps.size(); i++) {
            str4 = ((((str4 + this.sim.comps.get(i).name + ",") + Integer.toString(this.sim.comps.get(i).x) + ",") + Integer.toString(this.sim.comps.get(i).y) + ",") + Integer.toString(this.sim.comps.get(i).height) + ",") + Integer.toString(this.sim.comps.get(i).width) + ",";
        }
        String str5 = str4.substring(0, str4.length() - 1) + "\n";
        for (int i2 = 0; i2 < this.diagCanvas.buses.size(); i2++) {
            String str6 = str5 + this.diagCanvas.buses.get(i2).out.name + ",";
            if (this.diagCanvas.buses.get(i2).in != null) {
                str2 = str6 + this.diagCanvas.buses.get(i2).in.name + ",";
            } else {
                str2 = str6 + " ,";
            }
            String str7 = str2 + this.diagCanvas.buses.get(i2).outName + ",";
            for (int i3 = 0; i3 < this.diagCanvas.buses.get(i2).numPoints; i3++) {
                str7 = (str7 + Integer.toString(this.diagCanvas.buses.get(i2).x[i3]) + ",") + Integer.toString(this.diagCanvas.buses.get(i2).y[i3]) + ",";
            }
            if (this.diagCanvas.buses.get(i2).doneBus) {
                str3 = str7 + "1";
            } else {
                str3 = str7 + "0";
            }
            str5 = str3 + "|";
        }
        String str8 = str5.substring(0, str5.length() - 1) + "\n";
        for (int i4 = 0; i4 < this.diagCanvas.buses.size(); i4++) {
            String str9 = str8 + this.diagCanvas.buses.get(i4).out.name + ",";
            if (this.diagCanvas.buses.get(i4).in != null) {
                str = str9 + this.diagCanvas.buses.get(i4).in.name + ",";
            } else {
                str = str9 + " ,";
            }
            str8 = ((((((str + this.diagCanvas.buses.get(i4).outName + ",") + Integer.toString(this.diagCanvas.buses.get(i4).busLabelOut.x) + ",") + Integer.toString(this.diagCanvas.buses.get(i4).busLabelOut.y) + ",") + (this.diagCanvas.buses.get(i4).busLabelOut.hidden ? "1" : "0") + ",") + Integer.toString(this.diagCanvas.buses.get(i4).busLabelIn.x) + ",") + Integer.toString(this.diagCanvas.buses.get(i4).busLabelIn.y) + ",") + (this.diagCanvas.buses.get(i4).busLabelIn.hidden ? "1" : "0") + ",";
        }
        return str8.substring(0, str8.length() - 1);
    }

    public void saveSim() {
        File file = new File(this.sim.path.substring(0, this.sim.path.length() - 3) + "sim");
        this.saveString = getSaveString();
        BufferedWriter bufferedWriter = null;
        try {
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(this.saveString);
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                        ProcSim.out("Diagram Saved");
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
                ProcSim.outErr("Error saving simulation diagram: " + e2.toString());
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                        ProcSim.out("Diagram Saved");
                    } catch (Exception e3) {
                    }
                }
            }
        } catch (Throwable th) {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    ProcSim.out("Diagram Saved");
                } catch (Exception e4) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public void setupSidePanel() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new Insets(0, 6, 0, 6);
        this.sidePanel.setLayout(gridBagLayout);
        Label label = new Label("Components");
        Label label2 = new Label("Connected Buses");
        Label label3 = new Label("All Buses");
        new Label("  ");
        this.butResetBusNames.addActionListener(this);
        this.chkHideBusName.addItemListener(this);
        this.butResetConnectedBusNames.addActionListener(this);
        this.butRemoveConnectedBuses.addActionListener(this);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagLayout.setConstraints(label, gridBagConstraints);
        this.compList = new List(8, false);
        this.compList.addItemListener(this);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagLayout.setConstraints(this.compList, gridBagConstraints);
        gridBagConstraints.gridheight = 1;
        if (this.sim.comps != null && this.sim.comps.size() > 0) {
            this.txtHeight = new NumericTextField(Integer.toString(this.sim.comps.get(0).height), 4);
            this.txtWidth = new NumericTextField(Integer.toString(this.sim.comps.get(0).width), 4);
        } else {
            this.txtHeight = new NumericTextField("90", 4);
            this.txtWidth = new NumericTextField("90", 4);
        }
        this.txtWidth.addTextListener(this);
        this.txtHeight.addTextListener(this);
        Panel panel = new Panel(new FlowLayout(2));
        panel.add(new Label("Height"));
        panel.add(this.txtHeight);
        Panel panel2 = new Panel(new FlowLayout(2));
        panel2.add(new Label("Width"));
        panel2.add(this.txtWidth);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = 13;
        gridBagLayout.setConstraints(panel, gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagLayout.setConstraints(panel2, gridBagConstraints);
        gridBagConstraints.anchor = 10;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagLayout.setConstraints(label2, gridBagConstraints);
        this.connectedList = new List(7, false);
        this.connectedList.addItemListener(this);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 3;
        gridBagLayout.setConstraints(this.connectedList, gridBagConstraints);
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagLayout.setConstraints(label3, gridBagConstraints);
        this.busList = new List(4, false);
        this.busList.addItemListener(this);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagLayout.setConstraints(this.busList, gridBagConstraints);
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = 13;
        this.txtOutName = new Label("Output Name: None");
        this.txtInName = new Label("Input Name: None");
        this.txtDoneBus = new Label("Done ProcBus: No");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagLayout.setConstraints(this.txtOutName, gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagLayout.setConstraints(this.txtInName, gridBagConstraints);
        gridBagConstraints.anchor = 10;
        Panel panel3 = new Panel(new FlowLayout(0));
        this.butRemoveBus = new Button("Remove Bus");
        this.butRemoveBus.addActionListener(this);
        this.butUndo = new Button("Undo Point");
        this.butUndo.addActionListener(this);
        panel3.add(this.butRemoveBus);
        panel3.add(this.butUndo);
        this.butRemoveBus.getPreferredSize();
        this.butUndo.getPreferredSize();
        panel3.setPreferredSize(new Dimension(140, 70));
        gridBagConstraints.anchor = 13;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridheight = 3;
        gridBagLayout.setConstraints(panel3, gridBagConstraints);
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = 10;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagLayout.setConstraints(this.butRemoveConnectedBuses, gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagLayout.setConstraints(this.butResetConnectedBusNames, gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagLayout.setConstraints(this.butResetBusNames, gridBagConstraints);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagLayout.setConstraints(this.chkHideBusName, gridBagConstraints);
        this.sidePanel.add(label);
        this.sidePanel.add(this.compList);
        this.sidePanel.add(panel);
        this.sidePanel.add(panel2);
        this.sidePanel.add(label2);
        this.sidePanel.add(this.connectedList);
        this.sidePanel.add(label3);
        this.sidePanel.add(this.busList);
        this.sidePanel.add(this.txtOutName);
        this.sidePanel.add(this.txtInName);
        this.sidePanel.add(panel3);
        this.sidePanel.add(this.butRemoveConnectedBuses);
        this.sidePanel.add(this.butResetConnectedBusNames);
        this.sidePanel.add(this.butResetBusNames);
        this.sidePanel.add(this.chkHideBusName);
        add("East", this.sidePanel);
    }

    void showDoneBus() {
        if (this.diagCanvas.buses.get(getSelectedBus()).doneBus) {
            this.txtDoneBus.setText("Done ProcBus: Yes");
        } else {
            this.txtDoneBus.setText("Done ProcBus: No");
        }
    }

    void showConnectedBuses() {
        this.numConnectedBuses = 0;
        this.connectedList.removeAll();
        for (int i = 0; i < this.diagCanvas.buses.size(); i++) {
            boolean z = false;
            DiagBus diagBus = this.diagCanvas.buses.get(i);
            if (this.diagCanvas.curEditComp == -1) {
                this.diagCanvas.curEditComp = 0;
            }
            for (int i2 = 0; i2 < this.diagCanvas.comps.get(this.diagCanvas.curEditComp).buses.size() && !z; i2++) {
                if (diagBus.out == this.diagCanvas.comps.get(this.diagCanvas.curEditComp) || diagBus.in == this.diagCanvas.comps.get(this.diagCanvas.curEditComp)) {
                    this.connectedBuses[0][this.numConnectedBuses] = (diagBus.out == null ? "None" : diagBus.out.name) + "->" + (diagBus.in == null ? "None" : diagBus.in.name);
                    this.connectedList.add(this.connectedBuses[0][this.numConnectedBuses]);
                    this.connectedBuses[1][this.numConnectedBuses] = Integer.toString(i);
                    this.numConnectedBuses++;
                    z = true;
                }
            }
        }
    }

    public void newArch(boolean z) {
        setVisible(true);
        this.editingXML = false;
        resetSim();
        updateSim();
    }
}
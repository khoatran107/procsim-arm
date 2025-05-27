package defpackage;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.UIManager;

/* loaded from: ProcSim.jar:ProcSim.class */
class ProcSim extends Frame implements ActionListener {
    static boolean CONSOLE = true;
    static boolean SHOWOUT = false;
    static boolean SHOWERR = false;
    static boolean HOLDOUT = false;
    Simulator sim;
    Assembly assembly;
    LoadSim loadSim;
    DiagCanvas diagCanvas;
    ViewSim viewSim;
    Console console;
    static HelpViewer helpViewer;
    Button butAddCode = new Button(" Assembly Code... ");
    Button butLoadSim = new Button(" Processor Architecture... ");
    Button butViewSim = new Button(" Start Simulation ");
    Button butConsole = new Button("Show/Hide Console");
    Button butExit = new Button("  Exit  ");
    Panel mainPanel = new Panel(new BorderLayout());
    Panel buttonPanel = new Panel(new GridLayout(0, 1));
    Label lblAssem = new Label("");
    Label lblSim = new Label("");
    Label lblSimName = new Label("");

    public ProcSim() {
        setupFrame(false);
    }

    public ProcSim(boolean z) {
        setupFrame(z);
    }

    private void setupFrame(boolean z) {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.gif"));
        UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        System.out.println("ProcSim Loaded");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            this.console = new Console(this);
        } catch (Exception e2) {
        }
        if (!CONSOLE) {
            SHOWOUT = false;
            SHOWERR = false;
            this.console.frame.dispose();
        } else {
            this.console.frame.setVisible(true);
        }
        SHOWOUT = !z;
        if (!SHOWOUT) {
            this.console.updatePanels();
        }
        Dialog dialog = new Dialog(this, "Proc Sim", false);
        Panel panel = new Panel(new FlowLayout(0));
        Panel panel2 = new Panel(new FlowLayout(0));
        panel.add(new Label("Loading Simulator"), 0);
        panel2.add(new Label("Please Wait..."), 0);
        dialog.add(panel2, "South");
        dialog.add(panel, "Center");
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocation(((int) (screenSize.getWidth() - getWidth())) / 2, ((int) (screenSize.getHeight() - getHeight())) / 2);
        dialog.addWindowListener(new WindowAdapter() { // from class: ProcSim.1
            public void windowClosing(WindowEvent windowEvent) {
                ProcSim.this.dispose();
                System.exit(0);
            }
        });
        dialog.setVisible(true);
        setSize(430, 330);
        setTitle("ProcSim v2.0 - by James Garton");
        setLocation(((int) (screenSize.getWidth() - getWidth())) / 2, ((int) (screenSize.getHeight() - getHeight())) / 2);
        addWindowListener(new WindowAdapter() { // from class: ProcSim.2
            public void windowClosing(WindowEvent windowEvent) {
                ProcSim.this.dispose();
                System.exit(0);
            }
        });
        Font font = new Font("Arial", 0, 10);
        Font font2 = new Font("Arial", 0, 16);
        Font font3 = new Font("Arial", 1, 16);
        Panel panel3 = new Panel(new FlowLayout(1, 10, 10));
        Panel panel4 = new Panel(new FlowLayout(0, 10, 0));
        Panel panel5 = new Panel(new FlowLayout(0, 10, 0));
        Panel panel6 = new Panel(new FlowLayout(1, 10, 5));
        this.buttonPanel.add(new Label("                                      "));
        this.butViewSim.addActionListener(this);
        this.butViewSim.setFont(font3);
        panel3.add(this.butViewSim);
        panel3.setBackground(new Color(206, 223, 246));
        add(panel3, "North");
        this.butAddCode.addActionListener(this);
        this.butAddCode.setFont(font2);
        panel4.add(this.butAddCode);
        this.buttonPanel.add(panel4);
        this.lblAssem.setAlignment(0);
        this.buttonPanel.add(this.lblAssem);
        this.butLoadSim.addActionListener(this);
        this.butLoadSim.setFont(font2);
        panel5.add(this.butLoadSim);
        this.buttonPanel.add(panel5);
        this.lblSim.setAlignment(0);
        this.lblSimName.setAlignment(0);
        this.buttonPanel.add(this.lblSim);
        this.buttonPanel.add(this.lblSimName);
        this.butConsole.addActionListener(this);
        this.butConsole.setFont(font);
        this.buttonPanel.add(new Label("                                       "));
        this.butExit.addActionListener(this);
        this.butExit.setFont(font2);
        panel6.add(this.butExit);
        add(panel6, "South");
        panel6.setBackground(new Color(206, 223, 246));
        this.mainPanel.add(this.buttonPanel, "Center");
        this.mainPanel.setBackground(new Color(249, 250, 222));
        add(this.mainPanel, "Center");
        this.assembly = new Assembly(this);
        this.assembly.openFile("sample just R-Format.asm");
        this.assembly.doParse();
        this.diagCanvas = new DiagCanvas(this);
        this.sim = new Simulator(this);
        this.loadSim = new LoadSim(this);
        this.viewSim = new ViewSim(this.sim);
        setVisible(true);
        dialog.dispose();
        this.loadSim.parser.starting = false;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.butExit) {
            System.exit(0);
        }
        if (actionEvent.getSource() == this.butAddCode) {
            if (this.viewSim.isVisible()) {
                new MsgBox(this, "Please close the simulation window first.", false).dispose();
                return;
            } else {
                out("Showing input assembly window...");
                this.assembly.setVisible(true);
            }
        }
        if (actionEvent.getSource() == this.butLoadSim) {
            this.diagCanvas.b_ViewSim = false;
            this.diagCanvas.showBusNames = true;
            if (this.viewSim.isVisible()) {
                new MsgBox(this, "Please close the simulation window first.", false).dispose();
                return;
            }
            out("Showing load simulator window...");
            if (!this.loadSim.isVisible()) {
                this.loadSim.setupLoadSim();
                this.loadSim.setVisible(true);
            }
        }
        if (actionEvent.getSource() == this.butViewSim) {
            if (this.loadSim.isVisible() || this.assembly.isVisible()) {
                new MsgBox(this, "Finish editing the architecture first", false).dispose();
                return;
            }
            this.diagCanvas.b_ViewSim = true;
            out("Showing View simulation window...");
            this.viewSim.setupSim();
            this.viewSim.setVisible(true);
        }
        if (actionEvent.getSource() == this.butConsole) {
            if (this.console == null) {
                this.console = new Console(this);
                return;
            }
            this.console.hidden = !this.console.hidden;
            this.console.frame.setVisible(!this.console.hidden);
        }
    }

    public static void main(String[] strArr) {
        System.out.println("Optional command line params: \n-noconsole (removes custom console)\n-noout (removes debug messages but keeps warning/error messages)");
        String property = System.getProperty("os.name");
        System.out.println("Starting ProcSim under " + property + "...");
        if (property.toUpperCase().indexOf("WIN") <= -1) {
            System.out.println("\n--------------------\nRUNNING IN WINDOWS IS RECOMMENDED\n---------------------");
        }
        boolean z = false;
        if (strArr.length > 0) {
            if (strArr[0].equals("-noconsole")) {
                CONSOLE = false;
            }
            if (strArr[0].equals("-noout")) {
                z = true;
            }
        }
        new ProcSim(z);
    }

    public static void out(String str) {
        if (!SHOWOUT || HOLDOUT) {
            return;
        }
        System.out.println(str);
    }

    public static void outErr(String str) {
        if (SHOWERR) {
            System.err.println(str);
        }
    }

    public static void outLine(String str) {
        if (!SHOWOUT || HOLDOUT) {
            return;
        }
        System.out.print(str);
    }

    public static void outErrLine(String str) {
        if (!SHOWERR || HOLDOUT) {
            return;
        }
        System.err.print(str);
    }

    public int getCenterX() {
        return ((int) getLocation().getX()) + (getWidth() / 2);
    }

    public int getCenterY() {
        return ((int) getLocation().getY()) + (getHeight() / 2);
    }

    public void showHelp() {
        if (helpViewer == null) {
            helpViewer = new HelpViewer(this);
        }
        helpViewer.setVisible(true);
    }

    /* loaded from: ProcSim.jar:ProcSim$HelpViewer.class */
    class HelpViewer extends TextEditor {
        public HelpViewer(ProcSim procSim) {
            super(procSim, "Help - Readme", true);
            this.txtDoc.setLineWrap(true);
            this.txtDoc.setWrapStyleWord(true);
            setupFrame(0.5d, 0.6d);
            openFile("README.txt");
            this.txtDoc.setEditable(false);
        }
    }
}

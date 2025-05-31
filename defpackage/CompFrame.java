package defpackage;

import java.awt.Rectangle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;


/* loaded from: ProcSim.jar:CompFrame.class */
class CompFrame extends JFrame implements ActionListener {
    public JTable table;
    JScrollPane scrollPane;
    JButton okButton;
    JButton hideEmptyButton;
    JButton showMachineButton;
    boolean hideEmpty;
    boolean showMachine;
    boolean showMachineOld;
    JPanel btnPanel;
    JPanel topPanel;
    JPanel titlePanel;
    JLabel lblTitle;
    JPanel yourPanel;
    JLabel yourLabel;
    boolean registers;
    boolean mainMem;
    boolean instrMem;
    int MAX_VALS;
    int size;
    ViewSim viewSim;
    MyTableModel tModel;
    int[] widths;
    MyCellRenderer mcr;
    int justChanged;

    public CompFrame(String str, int i, ViewSim viewSim) {

        super(str);
        Dimension r0 = Toolkit.getDefaultToolkit().getScreenSize();;
        this.hideEmpty = false;
        this.showMachine = false;
        this.showMachineOld = false;
        this.registers = false;
        this.mainMem = false;
        this.instrMem = false;
        this.MAX_VALS = 256;
        this.size = 0;
        this.widths = new int[10];
        this.mcr = new MyCellRenderer();
        this.justChanged = -1;
        this.viewSim = viewSim;
        double d = 0.15d;
        if (i == 0) {
            this.registers = true;
        } else if (i == 1) {
            this.mainMem = true;
        } else {
            this.instrMem = true;
        }
        if (this.registers) {
            this.MAX_VALS = 32;
            this.hideEmpty = true;
            d = 0.16d;
        } else if (this.instrMem) {
            this.MAX_VALS = 64;
            this.hideEmpty = true;
            d = 0.22d;
        } else if (this.mainMem) {
            this.hideEmpty = true;
        }
        double d2 = 0.3d;
        double d3 = 1.9d;
        if (Toolkit.getDefaultToolkit().getScreenSize().width < 1200) {
            if (this.registers) {
                d = 0.18d;
            } else if (this.instrMem) {
                d = 0.25d;
            } else if (this.mainMem) {
                d = 0.15d;
            }
            d2 = 0.25d;
            d3 = 1.8d;
        }
        setPreferredSize(new Dimension((int) (r0.width * d), (int) (r0.height * 0.3d)));
        setSize(new Dimension((int) (r0.width * d), (int) (r0.height * 0.3d)));
        if (this.instrMem) {
            setLocation((int) (r0.width - (r0.width * d)), (int) (((r0.height / 2) - ((r0.height * d2) / 2.0d)) * d3));
        } else if (this.mainMem) {
            setLocation(((int) (r0.width - ((r0.width * d) * 2.0d))) - 15, (int) (((r0.height / 2) - ((r0.height * d2) / 2.0d)) * 1.03d));
        } else {
            setLocation((int) (r0.width - (r0.width * d)), (int) (((r0.height / 2) - ((r0.height * d2) / 2.0d)) * 1.03d));
        }
        setAlwaysOnTop(true);
        Container contentPane = getContentPane();
        setBackground(Color.gray);
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new BorderLayout());
        contentPane.add(this.topPanel);
        this.btnPanel = new JPanel();
        this.titlePanel = new JPanel();
        this.okButton = new JButton(" Done ");
        this.okButton.addActionListener(this);
        this.hideEmptyButton = new JButton(" Show All ");
        this.hideEmptyButton.addActionListener(this);
        this.showMachineButton = new JButton("Machine Code");
        this.showMachineButton.addActionListener(this);
        if (!this.instrMem) {
            this.btnPanel.add(this.hideEmptyButton);
        }
        if (this.instrMem) {
            this.btnPanel.add(this.showMachineButton);
        }
        this.btnPanel.add(this.okButton);
        contentPane.add(this.btnPanel, "South");
        this.btnPanel.setBackground(Color.YELLOW);
        this.lblTitle = new JLabel(str, 0);
        this.lblTitle.setFont(new Font("", 1, 12));
        this.titlePanel.setBackground(Color.ORANGE);
        this.titlePanel.add(this.lblTitle);
        this.topPanel.add(this.titlePanel, "North");
        if (this.instrMem) {
            this.yourPanel = new JPanel();
            this.yourLabel = new JLabel("Select an instruction to see its comment.");
            this.yourLabel.setFont(new Font("", 1, 12));
            this.yourPanel.add(this.yourLabel);
            this.yourPanel.setBackground(Color.YELLOW);
            this.topPanel.add(this.yourPanel, "South");
        }
        setupTable();
    }

    public void setButtons() {
        if (this.hideEmpty) {
            this.hideEmptyButton.setText(" Show All ");
        } else {
            this.hideEmptyButton.setText("Hide Empty");
        }
        if (this.showMachine) {
            this.showMachineButton.setText(" Assembly ");
        } else {
            this.showMachineButton.setText("Machine Code");
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.okButton) {
            setVisible(false);
        }
        if (actionEvent.getSource() == this.hideEmptyButton) {
            this.hideEmpty = !this.hideEmpty;
            this.viewSim.setTables();
            setButtons();
        }
        if (actionEvent.getSource() == this.showMachineButton) {
            this.showMachine = !this.showMachine;
            this.viewSim.setTables();
            setButtons();
        }
    }

    public void newModel(int i, int i2) {
        if (this.tModel != null) {
            for (int i3 = 0; i3 < this.tModel.getColumnCount(); i3++) {
                this.widths[i3] = this.table.getColumnModel().getColumn(i3).getPreferredWidth();
            }
        }
        this.tModel = new MyTableModel(i, i2);
        if (this.registers) {
            this.tModel.columnNames[0] = "Reg";
            this.tModel.columnNames[1] = "Reg";
            this.tModel.columnNames[2] = "Bin Val";
            this.tModel.columnNames[3] = "Dec Val";
            return;
        }
        if (this.mainMem) {
            this.tModel.columnNames[0] = "ByteAddr";
            this.tModel.columnNames[1] = "WordAddr";
            this.tModel.columnNames[2] = "ByteVal";
            this.tModel.columnNames[3] = "WordVal";
            return;
        }
        if (this.instrMem) {
            this.tModel.columnNames[0] = "WordAddr";
            this.tModel.columnNames[1] = "Addr";
            this.tModel.columnNames[2] = "Instruction";
            this.tModel.columnNames[3] = "Comment";
        }
    }

    public void setupTable() {
        if (this.instrMem) {
            this.MAX_VALS = this.viewSim.sim.source.assembly.numInstr;
        }
        newModel(this.MAX_VALS, 4);
        // if (this.registers) {
        //     for (int i = 0; i < this.MAX_VALS; i++) {
        //         this.tModel.data[i][0] = ProcFunc.zeroExtend(Functions.toBin(i), 5);
        //         this.tModel.data[i][1] = Assembly.regNumToString(i);
        //         this.tModel.data[i][2] = ProcFunc.slimBinary(ProcFunc.zeroExtend("0", 32));
        //         this.tModel.data[i][3] = "0";
        //     }
        // } else if (this.mainMem) {
        //     for (int i = 0; i < this.MAX_VALS; i++) {
        //         this.tModel.data[i][0] = Integer.toString(i);
        //         if (i % 4 == 0) {
        //             this.tModel.data[i][1] = Integer.toString(i / 4);
        //             this.tModel.data[i][3] = "0";
        //         } else {
        //             this.tModel.data[i][1] = "";
        //             this.tModel.data[i][3] = "";
        //         }
        //         this.tModel.data[i][2] = "0";
        //     }
        // } else if (this.instrMem) {
        //     int realInstructionIndex = 0;
        //     for (int i = 0; i < this.MAX_VALS; i++) {
        //         if (!this.viewSim.sim.source.assembly.instr[i].isEmpty()) {
        //             this.tModel.data[i][0] = ProcFunc.slimBinary(ProcFunc.zeroExtend(Functions.toBin(realInstructionIndex * 4), 6), true, true);
        //             this.tModel.data[i][1] = Integer.toString(realInstructionIndex * 4);
        //             realInstructionIndex++;
        //             System.out.println(this.viewSim.sim.source.assembly.instr[i].str);
        //         }
        //         this.tModel.data[i][2] = this.viewSim.sim.source.assembly.instr[i].str;
        //         this.tModel.data[i][3] = this.viewSim.sim.source.assembly.instr[i].comment;
        //     }
        // }

        this.table = new JTable(this.tModel);
        try {
            this.table.setDefaultRenderer(Class.forName("java.lang.String"), this.mcr);
        } catch (ClassNotFoundException e) {
            ProcSim.outErr("class not found");
        }
        this.table.setFont(new Font("Courier", 0, 12));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (this.registers) {
            this.table.getColumnModel().getColumn(0).setMaxWidth(46);
            this.table.getColumnModel().getColumn(0).setMinWidth(1);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(43);
            this.table.getColumnModel().getColumn(1).setMaxWidth(40);
            this.table.getColumnModel().getColumn(1).setMinWidth(30);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(30);
            this.table.getColumnModel().getColumn(2).setMinWidth(30);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(3).setMinWidth(30);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(30);
            this.table.setEnabled(false);
        } else if (this.mainMem) {
            this.table.getColumnModel().getColumn(0).setPreferredWidth(20);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(10);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(20);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(20);
            this.table.setEnabled(false);
        } else if (this.instrMem) {
            this.table.getColumnModel().getColumn(0).setPreferredWidth(20);
            this.table.getColumnModel().getColumn(0).setMinWidth(10);
            this.table.getColumnModel().getColumn(1).setMinWidth(10);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(10);
            if (screenSize.width < 1200) {
                this.table.getColumnModel().getColumn(2).setPreferredWidth(150);
            } else {
                this.table.getColumnModel().getColumn(2).setPreferredWidth(180);
            }
            this.table.getColumnModel().getColumn(3).setPreferredWidth(10);
            this.table.setEnabled(true);
            this.table.setSelectionMode(0);
            this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() { // from class: CompFrame.1
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    ListSelectionModel listSelectionModel = (ListSelectionModel) listSelectionEvent.getSource();
                    if (!listSelectionModel.isSelectionEmpty()) {
                        CompFrame.this.yourLabel.setText(CompFrame.this.viewSim.sim.source.assembly.instr[listSelectionModel.getMinSelectionIndex()].comment);
                    }
                }
            });
        }
        this.table.getColumnModel().setColumnSelectionAllowed(false);
        this.table.setCellSelectionEnabled(false);
        this.table.setDragEnabled(false);
        this.table.setFocusable(false);
        this.table.setShowHorizontalLines(true);
        this.table.setRowSelectionAllowed(true);
        this.table.setColumnSelectionAllowed(false);
        this.table.setSelectionForeground(Color.white);
        this.table.setSelectionBackground(Color.red);
        this.scrollPane = new JScrollPane(this.table);
        this.topPanel.add(this.scrollPane, "Center");
        this.table.getTableHeader().setReorderingAllowed(false);
    }

    public void setWidths() {
        for (int i = 0; i < this.tModel.getColumnCount(); i++) {
            this.table.getColumnModel().getColumn(i).setPreferredWidth(this.widths[i]);
        }
    }

    /* loaded from: ProcSim.jar:CompFrame$MyCellRenderer.class */
    class MyCellRenderer extends DefaultTableCellRenderer {
        MyCellRenderer() {
        }

        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z, boolean z2, int i, int i2) {
            Component tableCellRendererComponent = super.getTableCellRendererComponent(jTable, obj, z, z2, i, i2);
            boolean z3 = false;
            if (z && CompFrame.this.instrMem) {
                tableCellRendererComponent.setBackground(new Color(255, 255, 204));
                tableCellRendererComponent.setForeground(Color.blue);
                z3 = true;
            } else {
                tableCellRendererComponent.setForeground(Color.black);
            }
            if (!CompFrame.this.hideEmpty && CompFrame.this.registers && i < CompFrame.this.size && !CompFrame.this.tModel.data[i][3].equals("0")) {
                tableCellRendererComponent.setBackground(new Color(255, 255, 204));
                z3 = true;
            }
            if (CompFrame.this.instrMem && CompFrame.this.viewSim.sim.execInstr == i) {
                tableCellRendererComponent.setBackground(new Color(153, 255, 204));
                z3 = true;
            }
            if (CompFrame.this.registers && !CompFrame.this.viewSim.sim.lastChangedReg.equals("-1") && CompFrame.this.tModel != null && CompFrame.this.tModel.data != null && CompFrame.this.tModel.data[i][0] != null && CompFrame.this.tModel.data[i][0].equals(CompFrame.this.viewSim.sim.lastChangedReg)) {
                tableCellRendererComponent.setBackground(new Color(153, 255, 204));
                z3 = true;
            }
            if (!z3) {
                tableCellRendererComponent.setBackground(Color.white);
            }
            return tableCellRendererComponent;
        }
    }

    /* loaded from: ProcSim.jar:CompFrame$MyTableModel.class */
    public class MyTableModel extends AbstractTableModel {
        public String[] columnNames;
        public String[][] data;

        MyTableModel(int i, int i2) {
            this.columnNames = new String[i2];
            this.data = new String[i][i2];
        }

        public int getColumnCount() {
            return this.columnNames.length;
        }

        public int getRowCount() {
            return this.data.length;
        }

        public String getColumnName(int i) {
            return this.columnNames[i];
        }

        public Object getValueAt(int i, int i2) {
            return this.data[i][i2];
        }

        public Class getColumnClass(int i) {
            try {
                return getValueAt(0, i).getClass();
            } catch (Exception e) {
                return "".getClass();
            }
        }

        public boolean isCellEditable(int i, int i2) {
            return false;
        }

        public void setValueAt(Object obj, int i, int i2) {
            this.data[i][i2] = (String) obj;
            fireTableCellUpdated(i, i2);
        }
    }
}

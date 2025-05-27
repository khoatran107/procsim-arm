package defpackage;

import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/* loaded from: ProcSim.jar:EditXML.class */
class EditXML extends TextEditor implements ActionListener {
    Button butParse;
    Button butCancel;
    String oldDoc;

    public EditXML(ProcSim procSim) {
        super(procSim, "Edit Processor Architecture");
        this.butParse = new Button("Parse");
        this.butCancel = new Button("Cancel");
        this.oldDoc = "";
        this.buttonPanel.add(this.butCancel);
        this.buttonPanel.add(this.butParse);
        setupFrame(0.6d, 0.8d);
        this.butCancel.addActionListener(this);
        this.butParse.addActionListener(this);
        this.fmNew.addActionListener(this);
        this.fmLoad.addActionListener(this);
        this.fmSaveAs.addActionListener(this);
        this.fmSave.addActionListener(this);
        this.butDone.addActionListener(this);
        setDefaultCloseOperation(0);
        addWindowListener(new WindowAdapter() { // from class: EditXML.1
            public void windowClosing(WindowEvent windowEvent) {
                if (EditXML.this.butCancel.isEnabled()) {
                    EditXML.this.source.loadSim.newArch(true);
                    EditXML.this.dispose();
                    return;
                }
                EditXML.this.saveFile(EditXML.this.source.sim.path);
                if (EditXML.this.doParse()) {
                    ProcSim.out("\n===Parsing Succeded===\n");
                    EditXML.this.source.loadSim.newArch(false);
                    EditXML.this.dispose();
                } else {
                    ProcSim.outErr("\n======!!-Parsing Failed-!!=======\n");
                    new MsgBox(EditXML.this.source.console.frame, "===Parsing Failed===", "Please fix the problems before continuing", false).dispose();
                }
            }
        });
    }

    @Override // defpackage.TextEditor
    public void actionPerformed(ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);
        if (actionEvent.getSource() == this.butCancel) {
            this.source.loadSim.newArch(true);
            dispose();
        }
        if (actionEvent.getSource() == this.butParse) {
            this.butCancel.setEnabled(false);
            saveFile(this.source.sim.path);
            if (doParse()) {
                new MsgBox(this, "===Parsing Succeded===", false).dispose();
                ProcSim.outErr("\n===Parsing Succeded===\n");
                this.source.loadSim.resetSim();
                this.source.loadSim.updateSim();
            } else {
                new MsgBox(this.source.console.frame, "===Parsing Failed===", false).dispose();
                ProcSim.outErr("\n======!!-Parsing Failed-!!=======\n");
            }
        }
        if (actionEvent.getSource() == this.butDone) {
            this.butCancel.setEnabled(false);
            saveFile(this.source.sim.path);
            if (doParse()) {
                ProcSim.out("\n===Parsing Succeded===\n");
                if (this.oldDoc.equals(this.txtDoc.getText())) {
                    this.source.loadSim.newArch(true);
                } else {
                    this.source.loadSim.newArch(false);
                }
                dispose();
            } else {
                new MsgBox(this.source.console.frame, "===Parsing Failed===", "Please fix the problems before continuing (You could just reload)", false).dispose();
                ProcSim.outErr("\n======!!-Parsing Failed-!!=======\n");
            }
        }
        if (actionEvent.getSource() == this.fmLoad) {
            openXML();
        }
        if (actionEvent.getSource() == this.fmSaveAs) {
            saveAs(false);
        }
        if (actionEvent.getSource() == this.fmSave) {
            this.butCancel.setEnabled(false);
            saveFile(this.source.sim.path);
        }
        if (actionEvent.getSource() == this.fmNew) {
            MsgBox msgBox = new MsgBox(this, "Save changes before creating new?", true);
            msgBox.dispose();
            if (msgBox.id) {
                saveFile(this.source.sim.path);
            }
            this.txtDoc.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Simulator>\n\t<simname>New Architecture</simname>\n\t<Component>\n\t\t<name>A Component</name>\n\t\t<description>A sample component</description>\n\t\t<outputBuses>\n\t\t\t<output name=\"AnOutBus\" bits=\"32\">\n\t\t\t\t<to name=\"Another ProcBus\" comp=\"Another Comp\"/>\n\t\t\t</output>\n\t\t</outputBuses>\n\t\t<inputBuses>\n\t\t\t<input name=\"AnInpBus\" bits=\"1\"/>\n\t\t</inputBuses>\n\t</Component>\n\t\n</Simulator>");
            saveAs(true);
        }
    }

    /* renamed from: EditXML$2, reason: invalid class name */
    /* loaded from: ProcSim.jar:EditXML$2.class */
    class AnonymousClass2 implements UndoableEditListener {
        final EditXML this$0;

        AnonymousClass2(EditXML arg0) {
            this.this$0 = arg0;
        }

        public void undoableEditHappened(UndoableEditEvent arg0) {
            this.this$0.undo.addEdit(arg0.getEdit());
        }
    }

    /* renamed from: EditXML$3, reason: invalid class name */
    /* loaded from: ProcSim.jar:EditXML$3.class */
    class AnonymousClass3 extends AbstractAction {
        final EditXML this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(EditXML arg0, String arg1) {
            super(arg1);
            this.this$0 = arg0;
        }

        public void actionPerformed(ActionEvent arg0) {
            try {
                if (this.this$0.undo.canUndo()) {
                    this.this$0.undo.undo();
                }
            } catch (CannotUndoException e) {
            }
        }
    }

    /* renamed from: EditXML$4, reason: invalid class name */
    /* loaded from: ProcSim.jar:EditXML$4.class */
    class AnonymousClass4 extends AbstractAction {
        final EditXML this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(EditXML arg0, String arg1) {
            super(arg1);
            this.this$0 = arg0;
        }

        public void actionPerformed(ActionEvent arg0) {
            try {
                if (this.this$0.undo.canRedo()) {
                    this.this$0.undo.redo();
                }
            } catch (CannotRedoException e) {
            }
        }
    }

    public boolean doParse() {
        return this.source.loadSim.parser.startParse(this.source.sim);
    }

    public boolean saveAs(boolean z) {
        String fileDialog;
        if (z) {
            fileDialog = fileDialog(true, new Frame(), "Create New Architecture File...", ".\\", "newSim.xml");
            this.source.sim.path = "newSim.xml";
        } else {
            fileDialog = fileDialog(true, new Frame(), "Save Architecture File As...", ".\\", this.source.sim.getFileName(this.source.sim.path));
        }
        if (fileDialog.equals(this.source.sim.path) && !z) {
            saveFile(this.source.sim.path);
            return true;
        }
        if (fileDialog.length() > 5 && (fileDialog.substring(0, 6).equals(".\\null") || fileDialog.substring(0, 7).equals(".\\\\null"))) {
            return false;
        }
        this.source.sim.path = fileDialog;
        saveFile(fileDialog);
        return true;
    }

    public void openXML() {
        String fileDialog = fileDialog(false, new Frame(), "Open Assembly File...", ".\\", "*.xml");
        if (fileDialog.equals(".\\\\null") || fileDialog.equals(".\\null")) {
            return;
        }
        this.source.sim.path = fileDialog;
        openFile(fileDialog);
    }

    public void starting() {
        openFile(this.source.sim.path);
        this.butCancel.setEnabled(true);
        this.oldDoc = this.txtDoc.getText();
    }
}
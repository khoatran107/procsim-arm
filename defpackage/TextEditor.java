package defpackage;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/* loaded from: ProcSim.jar:TextEditor.class */
abstract class TextEditor extends JFrame implements ActionListener {
    JScrollPane jpan;
    ProcSim source;
    MenuItem fmNew;
    MenuItem fmLoad;
    MenuItem fmSave;
    MenuItem fmSaveAs;
    MenuItem fmUndo;
    MenuItem fmCopy;
    MenuItem fmPaste;
    MenuItem fmFind;
    MenuItem fmFindNext;
    MenuItem fmClose;
    MenuItem fmHelp;
    String title;
    String path;
    Button butDone = new Button("Done");
    JTextArea txtDoc = new JTextArea();
    Panel mainPanel = new Panel(new BorderLayout());
    Panel buttonPanel = new Panel(new FlowLayout());
    final UndoManager undo = new UndoManager();
    Document doc = this.txtDoc.getDocument();
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    Menu editMenu = new Menu("Edit");
    Menu helpMenu = new Menu("Help");
    String findString = "";
    int positionFind = 0;

    public TextEditor(ProcSim procSim, String str, boolean z) {
        setupEditor(procSim, str, z);
    }

    public TextEditor(ProcSim procSim, String str) {
        setupEditor(procSim, str, false);
    }

    private void setupEditor(ProcSim procSim, String str, boolean z) {
        this.title = str;
        this.source = procSim;
        setDefaultCloseOperation(2);
        this.fmNew = new MenuItem("New");
        this.fmNew.setShortcut(new MenuShortcut(78));
        this.fmLoad = new MenuItem("Open...");
        this.fmLoad.setShortcut(new MenuShortcut(79));
        this.fmSave = new MenuItem("Save");
        this.fmSave.setShortcut(new MenuShortcut(83));
        this.fmSaveAs = new MenuItem("Save As...");
        this.fmUndo = new MenuItem("Undo");
        this.fmUndo.setShortcut(new MenuShortcut(90));
        this.fmUndo.addActionListener(this);
        this.fmCopy = new MenuItem("Copy");
        this.fmCopy.setShortcut(new MenuShortcut(67));
        this.fmCopy.addActionListener(this);
        this.fmPaste = new MenuItem("Paste");
        this.fmPaste.setShortcut(new MenuShortcut(86));
        this.fmPaste.addActionListener(this);
        this.fmFind = new MenuItem("Find...");
        this.fmFind.setShortcut(new MenuShortcut(70));
        this.fmFind.addActionListener(this);
        this.fmFindNext = new MenuItem("Find Next");
        this.fmFindNext.setShortcut(new MenuShortcut(114));
        this.fmFindNext.addActionListener(this);
        this.fmHelp = new MenuItem("Help...");
        this.fmHelp.setShortcut(new MenuShortcut(112));
        this.fmHelp.addActionListener(this);
        this.fmClose = new MenuItem("Close");
        this.fmClose.setShortcut(new MenuShortcut(67));
        if (!z) {
            this.fileMenu.add(this.fmNew);
            this.fileMenu.add(this.fmLoad);
            this.fileMenu.add(this.fmSave);
            this.fileMenu.add(this.fmSaveAs);
            this.editMenu.add(this.fmUndo);
            this.editMenu.addSeparator();
            this.editMenu.add(this.fmCopy);
            this.editMenu.add(this.fmPaste);
            this.editMenu.addSeparator();
            this.editMenu.add(this.fmFind);
            this.editMenu.add(this.fmFindNext);
            this.helpMenu.add(this.fmHelp);
            this.menuBar.add(this.fileMenu);
            this.menuBar.add(this.editMenu);
            this.menuBar.add(this.helpMenu);
            setMenuBar(this.menuBar);
        } else {
            this.butDone.addActionListener(this);
        }
        setTitle(this.title);
        this.txtDoc.getActionMap().put("NextFind", new AbstractAction("NextFind") { // from class: TextEditor.1
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    TextEditor.this.findTextNext();
                } catch (Exception e) {
                }
            }
        });
        this.txtDoc.getInputMap().put(KeyStroke.getKeyStroke("F3"), "NextFind");
        this.doc.addUndoableEditListener(new UndoableEditListener() { // from class: TextEditor.2
            public void undoableEditHappened(UndoableEditEvent undoableEditEvent) {
                TextEditor.this.undo.addEdit(undoableEditEvent.getEdit());
            }
        });
        this.txtDoc.getActionMap().put("Undo", new AbstractAction("Undo") { // from class: TextEditor.3
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (TextEditor.this.undo.canUndo()) {
                        TextEditor.this.undo.undo();
                    }
                } catch (CannotUndoException e) {
                }
            }
        });
        this.txtDoc.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        this.txtDoc.getActionMap().put("Redo", new AbstractAction("Redo") { // from class: TextEditor.4
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (TextEditor.this.undo.canRedo()) {
                        TextEditor.this.undo.redo();
                    }
                } catch (CannotRedoException e) {
                }
            }
        });
        this.txtDoc.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.fmHelp) {
            this.source.showHelp();
        }
        if (actionEvent.getSource() == this.butDone) {
            dispose();
        }
        if (actionEvent.getSource() == this.fmUndo) {
            this.undo.undo();
        }
        if (actionEvent.getSource() == this.fmCopy) {
            this.txtDoc.copy();
        }
        if (actionEvent.getSource() == this.fmPaste) {
            this.txtDoc.paste();
        }
        if (actionEvent.getSource() == this.fmFind) {
            findText();
        }
        if (actionEvent.getSource() == this.fmFindNext) {
            findTextNext();
        }
    }

    public void findTextNext() {
        if (this.findString.equals("")) {
            return;
        }
        this.positionFind = this.txtDoc.getText().toLowerCase().indexOf(this.findString, this.positionFind + 1);
        if (this.positionFind > -1) {
            this.txtDoc.setSelectionStart(this.positionFind);
            this.txtDoc.setSelectionEnd(this.positionFind + this.findString.length());
        }
    }

    public void findText() {
        String str = (String) JOptionPane.showInputDialog(this, "Find what:", "Find", -1, (Icon) null, (Object[]) null, this.findString);
        if (str != null && str.length() > 0) {
            this.findString = str.toLowerCase();
            this.positionFind = this.txtDoc.getText().toLowerCase().indexOf(this.findString);
            if (this.positionFind > -1) {
                this.txtDoc.setSelectionStart(this.positionFind);
                this.txtDoc.setSelectionEnd(this.positionFind + this.findString.length());
            }
        }
    }

    public void setupFrame(double d, double d2) {
        getContentPane().add(this.buttonPanel, "South");
        this.buttonPanel.add(this.butDone);
        this.butDone.setFont(new Font("Arial", 1, 12));
        this.txtDoc.setTabSize(4);
        this.txtDoc.setFont(new Font("Courier", 0, 14));
        this.txtDoc.setRows(20);
        this.txtDoc.setColumns(40);
        this.jpan = new JScrollPane(this.txtDoc);
        getContentPane().add(this.jpan, "Center");
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.width * d), (int) (screenSize.height * d2));
        setLocation((int) ((screenSize.width / 2) - ((screenSize.width * d) / 2.0d)), (int) ((screenSize.height / 2) - ((screenSize.height * d2) / 2.0d)));
    }

    public void openFile(String str) {
        File file = new File(str);
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
                ProcSim.outErr("File not found: " + str);
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
            this.txtDoc.setText(stringBuffer.toString());
            this.txtDoc.setSelectionStart(0);
            this.txtDoc.setSelectionEnd(0);
            setTitle(this.title + " - " + str);
            ProcSim.out("Opened file: " + str);
        } catch (Throwable th) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                    throw th;
                }
            }
            throw th;
        }
    }

    public void saveFile(String str) {
        BufferedWriter bufferedWriter = null;
        try {
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(new File(str)));
                bufferedWriter.write(this.txtDoc.getText());
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                        ProcSim.out("Saved to File: " + str);
                    } catch (Exception e) {
                    }
                }
            } catch (Throwable th) {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                        ProcSim.out("Saved to File: " + str);
                    } catch (Exception e2) {
                        throw th;
                    }
                }
                throw th;
            }
        } catch (Exception e3) {
            ProcSim.outErr("Error saving assembly file " + e3.toString());
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    ProcSim.out("Saved to File: " + str);
                } catch (Exception e4) {
                }
            }
        }
        setTitle(this.title + " - " + str);
    }

    public String fileDialog(boolean z, Frame frame, String str, String str2, String str3) {
        FileDialog fileDialog;
        String str4;
        if (z) {
            fileDialog = new FileDialog(frame, str, 1);
        } else {
            fileDialog = new FileDialog(frame, str, 0);
        }
        fileDialog.setFile(str3);
        fileDialog.setDirectory(str2);
        fileDialog.setLocation(50, 50);
        fileDialog.setVisible(true);
        if (fileDialog.getDirectory().substring(fileDialog.getDirectory().length() - 1, fileDialog.getDirectory().length()).equals(ProcFunc.fileSep())) {
            str4 = fileDialog.getDirectory() + fileDialog.getFile();
        } else {
            str4 = fileDialog.getDirectory() + ProcFunc.fileSep() + fileDialog.getFile();
        }
        return str4;
    }
}
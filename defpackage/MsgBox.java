package defpackage;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* loaded from: ProcSim.jar:MsgBox.class */
class MsgBox extends Dialog implements ActionListener {
    boolean id;
    Button ok;
    Button can;
    boolean yesno;

    MsgBox(Frame frame, String str, boolean z) {
        super(frame, "Message", true);
        this.id = false;
        this.yesno = z;
        setLayout(new BorderLayout());
        add("Center", new Label(str));
        addOKCancelPanel(z);
        pack();
        createFrame();
        setVisible(true);
    }

    MsgBox(Frame frame, String str, String str2, boolean z) {
        super(frame, "Message", true);
        this.id = false;
        this.yesno = z;
        setLayout(new BorderLayout());
        Panel panel = new Panel(new GridLayout(2, 1, -10, -10));
        Panel panel2 = new Panel(new FlowLayout(1));
        Panel panel3 = new Panel(new FlowLayout(1));
        panel2.add(new Label(str));
        if (!str2.equals("")) {
            panel3.add(new Label(str2));
        }
        panel.add(panel2);
        panel.add(panel3);
        add("Center", panel);
        addOKCancelPanel(z);
        pack();
        createFrame();
        setVisible(true);
    }

    void addOKCancelPanel(boolean z) {
        Panel panel = new Panel();
        panel.setLayout(new FlowLayout());
        createOKButton(panel);
        if (this.yesno) {
            createCancelButton(panel);
        }
        add("South", panel);
    }

    void createOKButton(Panel panel) {
        if (this.yesno) {
            Button button = new Button("Yes");
            this.ok = button;
            panel.add(button);
        } else {
            Button button2 = new Button("OK");
            this.ok = button2;
            panel.add(button2);
        }
        this.ok.addActionListener(this);
    }

    void createCancelButton(Panel panel) {
        Button button = new Button("No");
        this.can = button;
        panel.add(button);
        this.can.addActionListener(this);
    }

    void createFrame() {
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation((screenSize.width / 2) - (getWidth() / 2), (screenSize.height / 2) - (getHeight() / 2));
        addWindowListener(new WindowAdapter() { // from class: MsgBox.1
            public void windowClosing(WindowEvent windowEvent) {
                MsgBox.this.dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.ok) {
            this.id = true;
            setVisible(false);
        } else if (actionEvent.getSource() == this.can) {
            setVisible(false);
        }
    }
}
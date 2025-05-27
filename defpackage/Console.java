package defpackage;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/* loaded from: ProcSim.jar:Console.class */
public class Console extends WindowAdapter implements WindowListener, Runnable, ActionListener {
  public JFrame frame;
  JScrollPane scroll1;
  JScrollPane scroll2;
  private JTextArea textArea;
  private JTextArea textAreaErr;
  private Thread reader;
  private Thread reader2;
  private boolean quit;
  ProcSim source;
  private JPanel panel = new JPanel(new GridLayout(1, 2));
  private JPanel btnPanel = new JPanel(new FlowLayout());
  boolean hidden = false;
  private JButton butOut = new JButton("Show/Hide Console Messages");
  private JButton butErr = new JButton("Show/Hide Error Messages");
  private final PipedInputStream inStream2 = new PipedInputStream();
  private final PipedInputStream inStream = new PipedInputStream();

  public Console(ProcSim procSim) {
    this.source = procSim;
    ProcSim procSim2 = this.source;
    ProcSim.SHOWOUT = true;
    ProcSim procSim3 = this.source;
    ProcSim.SHOWERR = true;
    this.frame = new JFrame("ProcSim Console");
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dimension = new Dimension((screenSize.width / 2) + 200, (screenSize.height / 2) + 100);
    this.frame.setBounds((dimension.width / 2) - 200, (dimension.height / 2) - 100, dimension.width, dimension.height);
    this.textArea = new JTextArea();
    this.textArea.setEditable(false);
    this.textArea.setTabSize(4);
    this.textAreaErr = new JTextArea();
    this.textAreaErr.setEditable(false);
    this.textAreaErr.setTabSize(4);
    this.btnPanel.add(this.butOut);
    this.btnPanel.add(this.butErr);
    this.butOut.addActionListener(this);
    this.butErr.addActionListener(this);
    this.scroll1 = new JScrollPane(this.textArea);
    this.scroll2 = new JScrollPane(this.textAreaErr);
    this.panel.add(this.scroll1);
    this.panel.add(this.scroll2);
    this.frame.getContentPane().add(this.panel, "Center");
    this.frame.getContentPane().add(this.btnPanel, "South");
    this.textAreaErr.setLineWrap(true);
    this.frame.addWindowListener(this);
    try {
      System.setOut(new PrintStream((OutputStream) new PipedOutputStream(this.inStream), true));
    } catch (Exception e) {
      this.textArea.append("Error: \n" + e.getMessage());
    }
    try {
      System.setErr(new PrintStream((OutputStream) new PipedOutputStream(this.inStream2), true));
    } catch (Exception e2) {
      this.textAreaErr.append("Error: \n" + e2.getMessage());
    }
    this.quit = false;
    this.reader = new Thread(this);
    this.reader.setDaemon(true);
    this.reader.start();
    this.reader2 = new Thread(this);
    this.reader2.setDaemon(true);
    this.reader2.start();
  }

  public void setupPanels() {
    ProcSim procSim = this.source;
    if (ProcSim.SHOWOUT) {
      this.panel.add(this.scroll1);
    }
    ProcSim procSim2 = this.source;
    if (ProcSim.SHOWERR) {
      this.panel.add(this.scroll2);
    }
    this.frame.repaint();
    this.frame.setVisible(true);
  }

  public void updatePanels() {
    this.panel.removeAll();
    setupPanels();
  }

  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == this.butOut) {
      ProcSim procSim = this.source;
      ProcSim procSim2 = this.source;
      ProcSim.SHOWOUT = !ProcSim.SHOWOUT;
      updatePanels();
      ProcSim.out("Showing Console Messages");
      return;
    }
    if (actionEvent.getSource() == this.butErr) {
      ProcSim procSim3 = this.source;
      ProcSim procSim4 = this.source;
      ProcSim.SHOWERR = !ProcSim.SHOWERR;
      updatePanels();
      ProcSim.outErr("Showing Error Messages");
    }
  }

  public synchronized void windowClosed(WindowEvent windowEvent) {
    this.quit = true;
    notifyAll();
    try {
      this.reader.join(1000L);
      this.inStream.close();
    } catch (Exception e) {
    }
    try {
      this.reader2.join(1000L);
      this.inStream2.close();
    } catch (Exception e2) {
    }
  }

  public synchronized void windowClosing(WindowEvent windowEvent) {
    this.frame.setVisible(false);
    this.frame.dispose();
  }

  @Override // java.lang.Runnable
  public synchronized void run() {
    while (Thread.currentThread() == this.reader) {
      try {
        try {
          wait(400L);
        } catch (InterruptedException e) {
        }
        if (this.inStream.available() != 0) {
          String readLine = readLine(this.inStream);
          if (!this.hidden) {
            this.textArea.append(readLine);
            this.textArea.setCaretPosition(this.textArea.getText().length());
          }
        }
        if (this.quit) {
          return;
        }
      } catch (Exception e2) {
        return;
      }
    }
    while (Thread.currentThread() == this.reader2) {
      try {
        wait(400L);
      } catch (InterruptedException e3) {
      }
      try {
        String readLine2 = "";
        if (this.inStream2.available() != 0) {
          readLine2 = readLine(this.inStream2);
        }
        if (!this.hidden) {
          this.textAreaErr.append(readLine2);
          this.textAreaErr.setCaretPosition(this.textAreaErr.getText().length());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (this.quit) {
        return;
      }
    }
  }

  public synchronized String readLine(PipedInputStream pipedInputStream) throws IOException {
    String str = "";
    do {
      int available = pipedInputStream.available();
      if (available != 0) {
        byte[] bArr = new byte[available];
        pipedInputStream.read(bArr);
        str = str + new String(bArr, 0, bArr.length);
        if (str.endsWith("\n") || str.endsWith("\r\n")) {
          break;
        }
      } else {
        break;
      }
    } while (!this.quit);
    return str;
  }
}

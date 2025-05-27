package defpackage;

import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: ProcSim.jar:NumericTextField.class */
public class NumericTextField extends TextField {
    public NumericTextField(String str, int i) {
        super(str, i);
        addKeyListener(new KeyAdapter() { // from class: NumericTextField.1
            public void keyTyped(KeyEvent keyEvent) {
                char keyChar = keyEvent.getKeyChar();
                if (keyChar != '\b' && keyChar != 127 && keyChar != '\n' && keyChar != '\t' && !Character.isDigit(keyChar)) {
                    keyEvent.consume();
                }
            }
        });
    }

    public NumericTextField(int i) {
        this("", i);
    }
}
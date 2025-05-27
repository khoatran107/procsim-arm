package defpackage;

/* loaded from: ProcSim.jar:InputCheck.class */
public class InputCheck {
    ProcBus input;
    String checkagainst;
    boolean always = false;

    InputCheck(ProcBus procBus, String str) {
        this.checkagainst = "error";
        this.input = procBus;
        this.checkagainst = str;
    }

    InputCheck() {
        this.checkagainst = "error";
        this.checkagainst = "always";
    }

    public boolean check() {
        if (this.checkagainst.equals("always")) {
            this.always = true;
        }
        if (this.always) {
            return true;
        }
        for (int i = 0; i < 10; i++) {
            if (this.input.binaryValue.equals(this.checkagainst) && !this.checkagainst.equals("error")) {
                return true;
            }
        }
        return false;
    }
}
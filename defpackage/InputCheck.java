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
            if (checkSpecialWithX(this.input.binaryValue, this.checkagainst) && !this.checkagainst.equals("error")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSpecialWithX(String binaryValue, String checkAgainst) {
        if (binaryValue.length() != checkAgainst.length()) return false;
        int n = binaryValue.length();
        for (int i = 0; i < n; i++) {
            if (checkAgainst.charAt(i) != 'x' && binaryValue.charAt(i) != checkAgainst.charAt(i)) return false;
        }
        return true;
    }
}
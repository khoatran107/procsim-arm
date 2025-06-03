package defpackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/* loaded from: ProcSim.jar:ProcFunc.class */
class ProcFunc {
    public static String zeroExtend(String str, int i) {
        String str2 = str.toString();
        if (str2.length() == i) {
            return str2;
        }
        for (int i2 = 0; i2 < i - str.length(); i2++) {
            str2 = "0" + str2;
        }
        if (i < str.length()) {
            str2 = str2.substring(str.length() - i, str.length());
        }
        return str2;
    }

    static String slimBinary(String str) {
        return slimBinary(str, false, false);
    }

    public static String slimBinary(String str, boolean z, boolean z2) {
        if (str.length() != 32 && !z) {
            return str;
        }
        String substring = str.substring(0, 1);
        if (substring.equals("1")) {
            return str;
        }
        String substring2 = str.substring(1, str.length());
        boolean z3 = false;
        for (int i = 0; i < substring2.length() && !z3; i++) {
            if (substring2.substring(i, i + 1).equals("1")) {
                substring2 = substring2.substring(i, substring2.length());
                z3 = true;
            }
        }
        if (!z3) {
            if (!z2) {
                return "0..0";
            }
            return "0";
        }
        if (!z2) {
            return substring + ".." + substring2;
        }
        return substring2;
    }

    public static String signExtend(String str, int i) {
        String str2 = str.toString();
        if (str2.length() == i) {
            return str2;
        }
        if (str2.length() == 64) {
            return str2.substring(64 - i, 64);
        }
        if (str2.length() == 0 || str2.substring(0, 1).equals("0")) {
            for (int i2 = 0; i2 < i - str.length(); i2++) {
                str2 = "0" + str2;
            }
        } else {
            for (int i3 = 0; i3 < i - str.length(); i3++) {
                str2 = "1" + str2;
            }
        }
        return str2;
    }

    public static int round(int i, int i2) {
        if (i <= 0) {
            return 0;
        }
        return (i / 10) * 10;
    }

    public static void copyFile(String str, String str2) throws Exception {
        File file = new File(str);
        File file2 = new File(str2);
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read != -1) {
                fileOutputStream.write(bArr, 0, read);
            } else {
                fileInputStream.close();
                fileOutputStream.close();
                return;
            }
        }
    }

    public static String fileSep() {
        return String.valueOf(System.getProperty("file.separator").charAt(0));
    }
}
package defpackage;

import defpackage.DiagBus;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JScrollPane;

/* loaded from: ProcSim.jar:DiagCanvas.class */
public class DiagCanvas extends JScrollPane {
    ProcSim source;
    ViewSim viewSim;
    Image buffer;
    AnimThrd animThread;
    Vector<PComponent> comps = new Vector<>();
    Vector<DiagBus> buses = new Vector<>();
    boolean showAttach = false;
    int busSide = 0;
    int curEditBus = -1;
    int curEditComp = -1;
    Color compGradient1 = new Color(132, 234, 0);
    Color compGradient2 = new Color(255, 255, 255);
    Color compNameCol = new Color(177, 51, 51);
    boolean b_ViewSim = false;
    boolean showBusNames = true;
    boolean b_Transparency = true;

    DiagCanvas(ProcSim procSim) {
        setSize(700, 500);
        setPreferredSize(new Dimension(700, 500));
        this.source = procSim;
        setBackground(Color.white);
        this.animThread = new AnimThrd(this);
    }

    public void calcCanvasSize(Dimension dimension) {
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.comps.size(); i3++) {
            if (this.comps.get(i3).x + this.comps.get(i3).width > i - 50) {
                i = this.comps.get(i3).x + this.comps.get(i3).width + 50;
            }
            if (this.comps.get(i3).y + this.comps.get(i3).height > i2 - 40) {
                i2 = this.comps.get(i3).y + this.comps.get(i3).height + 40;
            }
        }
        for (int i4 = 0; i4 < this.buses.size(); i4++) {
            DiagBus diagBus = this.buses.get(i4);
            for (int i5 = 0; i5 < diagBus.numPoints; i5++) {
                if (diagBus.x[i5] > i - 50) {
                    i = diagBus.x[i5] + 50;
                }
                if (diagBus.y[i5] > i2 - 40) {
                    i2 = diagBus.y[i5] + 40;
                }
            }
        }
        setPreferredSize(new Dimension(i, i2));
        setSize(i, i2);
    }

    public void addComponent(PComponent pComponent) {
        if (!this.comps.contains(pComponent)) {
            this.comps.add(pComponent);
        }
        repaint();
    }

    public void addBus(DiagBus diagBus) {
        if (!this.buses.contains(diagBus)) {
            this.buses.add(diagBus);
        }
        repaint();
    }

    public int findBus(String str, PComponent pComponent, PComponent pComponent2) {
        for (int i = 0; i < this.buses.size(); i++) {
            DiagBus diagBus = this.buses.get(i);
            if (diagBus.out.equals(pComponent) && diagBus.in.equals(pComponent2) && diagBus.outName.equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public void showAnim() {
        showAnim(false);
    }

    public void showAnim(boolean z) {
        int i;
        if (this.animThread.instantSpeed || !this.animThread.animating) {
            return;
        }
        try {
            Graphics graphics = getGraphics();
            getWidth();
            getHeight();
            Font font = new Font("Arial", 1, 14);
            for (int i2 = 0; i2 < this.animThread.anims.size(); i2++) {
                Anim anim = this.animThread.anims.get(i2);
                if (anim.animating || z) {
                    String str = anim.animStr;
                    if (anim.width == 0 || anim.height == 0) {
                        anim.width = graphics.getFontMetrics(font).stringWidth(str);
                        anim.height = graphics.getFontMetrics(font).getHeight();
                    }
                    int i3 = anim.width + 7;
                    int i4 = anim.height;
                    int i5 = anim.x - 3;
                    int i6 = anim.y - i4;
                    int i7 = (anim.x - 3) + i3;
                    int i8 = (anim.y - i4) + i4 + 3;
                    int i9 = i7 - i5;
                    int i10 = i8 - i6;
                    int max = Math.max(anim.frameSkip, anim.oldSkip);
                    if (!anim.bus.bus.showStrVal) {
                        i = 5;
                    } else {
                        i = 3;
                    }
                    int i11 = i5 - (i + max);
                    int i12 = i6 - (i + max);
                    int i13 = i9 + ((i + max) * 2);
                    int i14 = i10 + ((i + max) * 2);
                    if (anim.isHoriz() && !anim.changingDirection) {
                        i12 = i12 + max + i;
                        i14 = (i14 - (max * 2)) - i;
                    } else if (anim.isVert() && !anim.changingDirection) {
                        i11 = i11 + max + (i - 1);
                        i13 = ((i13 - (max * 2)) - i) + 1;
                    }
                    anim.animDimSet = true;
                    repaint(i11, i12, i13, i14);
                }
            }
        } catch (Exception e) {
        }
    }

    public void resetImage() {
        repaint();
    }

    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.white);
        graphics2D.clearRect(0, 0, getWidth(), getHeight());
        Font font = new Font("Arial", 1, 12);
        graphics2D.setFont(font);
        graphics2D.setColor(Color.black);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < this.comps.size(); i++) {
            drawComponent(i, graphics2D, font);
        }
        graphics2D.setColor(new Color(12, 172, 218));
        for (int i2 = 0; i2 < this.buses.size(); i2++) {
            if (!this.b_ViewSim && this.curEditBus != i2) {
                drawBus(i2, graphics2D);
            }
            if ((this.animThread.instantSpeed || !this.b_ViewSim || !this.buses.get(i2).newVal || this.buses.get(i2).bus.checkOptional()) && this.b_ViewSim) {
                drawBus(i2, graphics2D);
            }
        }
        if (!this.animThread.instantSpeed) {
            for (int i3 = 0; i3 < this.buses.size(); i3++) {
                if (this.b_ViewSim && this.buses.get(i3).newVal) {
                    graphics2D.setColor(Color.red);
                    drawBus(i3, graphics2D);
                    graphics2D.setColor(new Color(12, 172, 218));
                }
            }
        }
        if (this.curEditBus > -1 && !this.b_ViewSim) {
            graphics2D.setColor(Color.red);
            drawBus(this.curEditBus, graphics2D);
        }
        for (int i4 = 0; i4 < this.buses.size(); i4++) {
            DiagBus diagBus = this.buses.get(i4);
            graphics2D.setColor(Color.ORANGE);
            graphics2D.fillOval(diagBus.x[0] - 5, diagBus.y[0] - 5, 10, 10);
            if (this.showAttach && !diagBus.doneBus) {
                graphics2D.fillOval(diagBus.x[diagBus.numPoints] - 5, diagBus.y[diagBus.numPoints] - 5, 10, 10);
            } else if (diagBus.doneBus) {
                graphics2D.fillOval(diagBus.x[diagBus.numPoints - 1] - 5, diagBus.y[diagBus.numPoints - 1] - 5, 10, 10);
            }
        }
        Font font2 = new Font("Arial", 1, 14);
        Font font3 = new Font("Arial", 0, 12);
        graphics2D.setStroke(new BasicStroke(1.0f));
        graphics2D.setFont(font3);
        for (int i5 = 0; i5 < this.buses.size(); i5++) {
            Composite composite = graphics2D.getComposite();
            if (this.b_Transparency) {
                graphics2D.setComposite(AlphaComposite.getInstance(3, 0.8f));
            }
            DiagBus diagBus2 = this.buses.get(i5);
            if (this.showBusNames && diagBus2.busLabelOut.str != null && !diagBus2.busLabelOut.str.equals("") && !diagBus2.busLabelOut.hidden) {
                drawBusNames(i5, graphics2D, font3, diagBus2.outName, diagBus2.busLabelOut);
            }
            if (this.showBusNames && diagBus2.busLabelIn.str != null && !diagBus2.busLabelIn.str.equals("") && !diagBus2.busLabelIn.hidden) {
                drawBusNames(i5, graphics2D, font3, diagBus2.inText, diagBus2.busLabelIn);
            }
            if (this.b_Transparency) {
                graphics2D.setComposite(composite);
            }
        }
        graphics2D.setFont(font2);
        if (this.animThread.instantSpeed) {
            return;
        }
        for (int i6 = 0; i6 < this.animThread.anims.size(); i6++) {
            DiagBus diagBus3 = this.animThread.anims.get(i6).bus;
            drawAnimation(diagBus3, this.buses.indexOf(diagBus3), this.animThread.anims.get(i6), font2, graphics2D);
        }
        if (this.b_ViewSim) {
            drawStatus(graphics2D);
        }
    }

    private void drawStatus(Graphics2D graphics2D) {
    }

    private void drawAnimation(DiagBus diagBus, int i, Anim anim, Font font, Graphics2D graphics2D) {
        int i2;
        if (anim.animDimSet && this.animThread.animating) {
            String str = anim.animStr;
            Font font2 = new Font("Arial", 1, 8);
            int i3 = anim.width;
            int i4 = anim.height;
            Composite composite = null;
            if (this.b_Transparency) {
                composite = graphics2D.getComposite();
                graphics2D.setComposite(AlphaComposite.getInstance(3, 0.7f));
            }
            if (!diagBus.bus.showStrVal) {
                i2 = i3 + 11;
            } else {
                i2 = i3 + 7;
            }
            if (anim.animating) {
                graphics2D.setColor(Color.green);
            } else {
                graphics2D.setColor(new Color(154, 240, 156));
            }
            graphics2D.fillRoundRect(anim.x - 3, anim.y - i4, i2, i4 + 3, 10, 10);
            graphics2D.setColor(Color.black);
            graphics2D.drawRoundRect(anim.x - 3, anim.y - i4, i2, i4 + 3, 10, 10);
            if (this.b_Transparency) {
                graphics2D.setComposite(composite);
            }
            if (!diagBus.bus.showStrVal) {
                graphics2D.setFont(font2);
                graphics2D.drawString("2", anim.x + i3 + 2, (anim.y - 2) + 2);
            }
            graphics2D.setFont(font);
            graphics2D.drawString(str, anim.x + 2, anim.y - 2);
            graphics2D.setColor(new Color(237, 113, 3));
            graphics2D.fillOval(anim.x - 4, anim.y - 4, 8, 8);
        }
    }

    private void drawComponent(int i, Graphics2D graphics2D, Font font) {
        PComponent pComponent = this.comps.get(i);
        if (pComponent.x != -999) {
            boolean drawBackup = false;
            if (!pComponent.imgPath.isEmpty()) {
                try {
                    BufferedImage image = ImageIO.read(new File(pComponent.imgPath));
                    graphics2D.drawImage(image, pComponent.x, pComponent.y, pComponent.width, pComponent.height, null);
                } catch (IOException e) {
                    System.err.println("Error loading image: " + pComponent.imgPath);
                    drawBackup = true;
                }
            }
            if (drawBackup) {
                graphics2D.setPaint(this.compGradient1);
                graphics2D.fillRect(pComponent.x, pComponent.y, pComponent.width, pComponent.height);
            }
            boolean z = this.source.loadSim.addingComponent;
            boolean z2 = this.source.loadSim.addingBus;
            PComponent currentAnimComp = this.animThread.getCurrentAnimComp();
            PComponent pComponent2 = this.animThread.lastComp;
            if (!this.b_ViewSim && ((i == this.curEditComp && z) || (z2 && this.curEditBus > -1 && this.buses.get(this.curEditBus).out != null && this.buses.get(this.curEditBus).out.equals(pComponent)))) {
                graphics2D.setColor(Color.red);
                graphics2D.setStroke(new BasicStroke(3.0f, 0, 1));
                graphics2D.drawRect(pComponent.x, pComponent.y, pComponent.width, pComponent.height);
                graphics2D.setStroke(new BasicStroke(1.0f, 0, 1));
            } else if (z2 && this.curEditBus > -1 && this.buses.get(this.curEditBus).in != null && this.buses.get(this.curEditBus).in.equals(pComponent)) {
                graphics2D.setColor(Color.blue);
                graphics2D.setStroke(new BasicStroke(3.0f, 0, 1));
                graphics2D.drawRect(pComponent.x, pComponent.y, pComponent.width, pComponent.height);
                graphics2D.setStroke(new BasicStroke(1.0f, 0, 1));
            } else if (((currentAnimComp != null && pComponent.equals(currentAnimComp) && pComponent2 == null) || (pComponent2 != null && pComponent2.equals(pComponent))) && this.b_ViewSim && !this.animThread.instantSpeed) {
                graphics2D.setColor(Color.red);
                graphics2D.setStroke(new BasicStroke(3.0f, 0, 1));
                graphics2D.drawRect(pComponent.x, pComponent.y, pComponent.width, pComponent.height);
                graphics2D.setStroke(new BasicStroke(1.0f, 0, 1));
            } else {
                graphics2D.setColor(Color.black);
                graphics2D.drawRect(pComponent.x, pComponent.y, pComponent.width, pComponent.height);
            }
            int stringWidth = graphics2D.getFontMetrics(font).stringWidth(pComponent.name);
            int height = graphics2D.getFontMetrics(font).getHeight();
            graphics2D.setColor(this.compNameCol);
            graphics2D.drawString(pComponent.name, ((pComponent.x + (pComponent.width / 2)) + 1) - (stringWidth / 2), pComponent.y + (pComponent.height / 2) + ((int) (height * 0.35d)));
        }
    }

    private void drawBusNames(int i, Graphics2D graphics2D, Font font, String str, DiagBus.PermText permText) {
        int stringWidth = graphics2D.getFontMetrics(font).stringWidth(str);
        permText.height = graphics2D.getFontMetrics(font).getHeight();
        permText.width = stringWidth + 2;
        graphics2D.setColor(Color.yellow);
        graphics2D.fillRoundRect(permText.x - 2, (permText.y - permText.height) + 3, permText.width, permText.height, 10, 10);
        if (permText.moving) {
            graphics2D.setColor(Color.red);
            graphics2D.drawRoundRect(permText.x - 2, (permText.y - permText.height) + 3, permText.width, permText.height, 10, 10);
        }
        graphics2D.setColor(Color.black);
        graphics2D.drawString(permText.str, permText.x, permText.y);
    }

    private void drawBus(int i, Graphics2D graphics2D) {
        drawBus(this.buses.get(i), graphics2D);
    }

    private void drawBus(DiagBus diagBus, Graphics2D graphics2D) {
        Polygon arrow;
        if (diagBus.bits >= 32) {
            graphics2D.setStroke(new BasicStroke(3.0f, 0, 1));
        } else if (diagBus.bits > 2) {
            graphics2D.setStroke(new BasicStroke(2.0f, 0, 1));
        } else {
            graphics2D.setStroke(new BasicStroke(1.0f, 0, 1));
        }
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < diagBus.numPoints; i5++) {
            if (diagBus.x[i5] != -20 && diagBus.y[i5] != -20 && diagBus.x[i5 + 1] != -20 && diagBus.y[i5 + 1] != -20) {
                i = diagBus.x[i5];
                i2 = diagBus.x[i5 + 1];
                i3 = diagBus.y[i5];
                i4 = diagBus.y[i5 + 1];
                graphics2D.drawLine(i, i3, i2, i4);
                if (diagBus.doneBus) {
                    diagBus.numPoints = i5 + 2;
                }
            }
        }
        graphics2D.setStroke(new BasicStroke(1.0f));
        if (i > 0 && i2 > 0 && i4 > 0 && i3 > 0) {
            if (diagBus.doneBus || this.showAttach) {
                arrow = getArrow(i, i3, i2, i4, -8, false);
            } else {
                arrow = getArrow(i, i3, i2, i4, -4, false);
            }
            graphics2D.fillPolygon(arrow);
            if (diagBus.x[1] != -20) {
                graphics2D.fillPolygon(getArrow(diagBus.x[0], diagBus.y[0], diagBus.x[1], diagBus.y[1], 15, true));
            }
        }
    }

    public Polygon getArrow(int i, int i2, int i3, int i4, int i5, boolean z) {
        double d = -Math.atan2(i4 - i2, i3 - i);
        double[] dArr = {((-5.0d) * 1.0d) + i5, ((-5.0d) * 1.0d) + i5, (5.0d * 1.0d) + i5};
        double[] dArr2 = {(5.0d * 1.0d) + 0, ((-5.0d) * 1.0d) + 0, 0 + 0};
        double[] dArr3 = {dArr[0], dArr[1], dArr[2]};
        double[] dArr4 = {dArr2[0], dArr2[1], dArr2[2]};
        dArr[0] = (dArr3[0] * Math.cos(d)) + (dArr4[0] * Math.sin(d));
        dArr2[0] = ((-dArr3[0]) * Math.sin(d)) + (dArr4[0] * Math.cos(d));
        dArr[1] = (dArr3[1] * Math.cos(d)) + (dArr4[1] * Math.sin(d));
        dArr2[1] = ((-dArr3[1]) * Math.sin(d)) + (dArr4[1] * Math.cos(d));
        dArr[2] = (dArr3[2] * Math.cos(d)) + (dArr4[2] * Math.sin(d));
        dArr2[2] = ((-dArr3[2]) * Math.sin(d)) + (dArr4[2] * Math.cos(d));
        if (!z) {
            dArr[0] = dArr[0] + i3;
            dArr2[0] = dArr2[0] + i4;
            dArr[1] = dArr[1] + i3;
            dArr2[1] = dArr2[1] + i4;
            dArr[2] = dArr[2] + i3;
            dArr2[2] = dArr2[2] + i4;
        } else {
            dArr[0] = dArr[0] + i;
            dArr2[0] = dArr2[0] + i2;
            dArr[1] = dArr[1] + i;
            dArr2[1] = dArr2[1] + i2;
            dArr[2] = dArr[2] + i;
            dArr2[2] = dArr2[2] + i2;
        }
        int[] iArr = new int[3];
        int[] iArr2 = new int[3];
        for (int i6 = 0; i6 < 3; i6++) {
            iArr[i6] = (int) dArr[i6];
            iArr2[i6] = (int) dArr2[i6];
        }
        return new Polygon(iArr, iArr2, 3);
    }

    public void animate() {
        this.animThread.animate();
    }

    public boolean checkConnected(PComponent pComponent) {
        if (this.buses.size() == 0) {
            return false;
        }
        for (int i = 0; i < this.buses.size(); i++) {
            if (this.buses.get(i).doneBus && (this.buses.get(i).in == pComponent || this.buses.get(i).out == pComponent)) {
                return true;
            }
            if (this.buses.get(i).out == pComponent && this.buses.get(i).x[0] > 0) {
                return true;
            }
            if (this.buses.get(i).in == pComponent && this.buses.get(i).x[this.buses.get(i).numPoints] > 0) {
                return true;
            }
        }
        return false;
    }
}
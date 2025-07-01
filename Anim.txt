package defpackage;

/* loaded from: ProcSim.jar:Anim.class */
public class Anim {
    String animStr;
    int x;
    int y;
    DiagCanvas source;
    DiagBus bus;
    int x0;
    int x1;
    int y0;
    int y1;
    boolean steep;
    int deltax;
    int deltay;
    int error;
    int deltaerr;
    int xx;
    int yy;
    int xstep;
    int ystep;
    int busToAnim = 0;
    int pointNum = -1;
    boolean animating = true;
    int length = 0;
    int frameSkip = 1;
    int oldSpeed = 50;
    int oldSkip = 1;
    int width = 0;
    int height = 0;
    boolean changingDirection = false;
    boolean animDimSet = false;

    Anim(DiagCanvas diagCanvas, DiagBus diagBus) {
        this.bus = diagBus;
        this.source = diagCanvas;
        this.x = this.bus.x[0];
        this.y = this.bus.y[0];
        if (this.bus.bus.showStrVal) {
            this.animStr = this.bus.bus.strValue;
        } else {
            this.animStr = ProcFunc.slimBinary(this.bus.bus.binaryValue);
        }
        calcFrameSkip();
    }

    public void calcFrameSkip() {
        this.oldSpeed = this.source.animThread.speed;
        this.length = calcLength();
        if (this.length < 100) {
            this.frameSkip = 1;
        } else if (this.length < 200) {
            this.frameSkip = 2;
        } else if (this.length < 300) {
            this.frameSkip = 3;
        } else if (this.length < 500) {
            this.frameSkip = 4;
        } else if (this.length < 800) {
            this.frameSkip = 5;
        } else if (this.length >= 800) {
            this.frameSkip = 6;
        }
        if (this.oldSpeed == 0) {
            this.frameSkip *= 4;
        } else if (this.oldSpeed < 4) {
            this.frameSkip *= 3;
        } else if (this.oldSpeed < 10) {
            this.frameSkip = (int) (this.frameSkip * 1.4d);
        } else if (this.oldSpeed < 50) {
            this.frameSkip = (int) (this.frameSkip * 0.9d);
        } else if (this.oldSpeed < 100) {
            this.frameSkip = (int) (this.frameSkip * 0.8d);
        } else if (this.oldSpeed < 200) {
            this.frameSkip = (int) (this.frameSkip * 0.8d);
        } else if (this.oldSpeed < 300) {
            this.frameSkip = (int) (this.frameSkip * 0.7d);
        } else if (this.oldSpeed < 400) {
            this.frameSkip = (int) (this.frameSkip * 0.6d);
        } else if (this.oldSpeed >= 400) {
            this.frameSkip = 1;
        }
        if (this.frameSkip <= 0) {
            this.frameSkip = 1;
        }
        if (this.x0 < this.x1) {
            this.xstep = this.frameSkip;
        } else {
            this.xstep = -this.frameSkip;
        }
        if (this.y0 < this.y1) {
            this.ystep = this.frameSkip;
        } else {
            this.ystep = -this.frameSkip;
        }
    }

    public boolean isHoriz() {
        if (Math.abs(this.bus.y[this.pointNum] - this.bus.y[this.pointNum + 1]) > 0) {
            return false;
        }
        return true;
    }

    public boolean isVert() {
        if (Math.abs(this.bus.x[this.pointNum] - this.bus.x[this.pointNum + 1]) > 0) {
            return false;
        }
        return true;
    }

    public void setup() {
        this.pointNum++;
        this.x0 = this.bus.x[this.pointNum];
        this.x1 = this.bus.x[this.pointNum + 1];
        this.y0 = this.bus.y[this.pointNum];
        this.y1 = this.bus.y[this.pointNum + 1];
        this.steep = Math.abs(this.y1 - this.y0) > Math.abs(this.x1 - this.x0);
        if (this.steep) {
            int i = this.x0;
            this.x0 = this.y0;
            this.y0 = i;
            int i2 = this.x1;
            this.x1 = this.y1;
            this.y1 = i2;
        }
        this.deltax = Math.abs(this.x1 - this.x0);
        this.deltay = Math.abs(this.y1 - this.y0);
        this.error = 0;
        this.deltaerr = this.deltay;
        this.xx = this.x0;
        this.yy = this.y0;
        if (this.x0 < this.x1) {
            this.xstep = this.frameSkip;
        } else {
            this.xstep = -this.frameSkip;
        }
        if (this.y0 < this.y1) {
            this.ystep = this.frameSkip;
        } else {
            this.ystep = -this.frameSkip;
        }
        if (this.steep) {
            this.x = this.yy;
            this.y = this.xx;
        } else {
            this.x = this.xx;
            this.y = this.yy;
        }
        this.changingDirection = true;
    }

    public int calcLength() {
        int i = 0;
        for (int i2 = 0; i2 < this.bus.numPoints - 1; i2++) {
            int i3 = this.bus.x[i2];
            int i4 = this.bus.x[i2 + 1];
            int i5 = i4 - i3;
            int i6 = this.bus.y[i2 + 1] - this.bus.y[i2];
            i += (int) Math.sqrt((i5 * i5) + (i6 * i6));
        }
        return i;
    }

    public void doPlot() {
        if (this.oldSpeed != this.source.animThread.speed) {
            this.oldSkip = this.frameSkip;
            calcFrameSkip();
        }
        if (!this.source.animThread.superSpeed && ((this.xx < this.x1 || this.xx - this.frameSkip > this.x1) && (this.oldSkip <= -1 || this.xx < this.x1 || this.xx - this.oldSkip > this.x1))) {
            this.animating = true;
            this.xx += this.xstep;
            this.error += this.deltaerr;
            if (2 * this.error >= this.deltax) {
                this.yy += this.ystep;
                this.error -= this.deltax;
            }
            if (this.steep) {
                this.x = this.yy;
                this.y = this.xx;
            } else {
                this.x = this.xx;
                this.y = this.yy;
            }
            this.changingDirection = false;
            return;
        }
        if (this.pointNum < this.bus.numPoints - 2) {
            setup();
            return;
        }
        this.animating = false;
        this.xx = this.x1;
        this.x = this.bus.x[this.bus.numPoints - 1];
        this.y = this.bus.y[this.bus.numPoints - 1];
        this.source.animThread.doneAnim(this);
    }

    public void reset() {
        this.animating = true;
        this.x = this.bus.x[0];
        this.y = this.bus.y[0];
        this.pointNum = -1;
        if (this.bus.bus.showStrVal) {
            this.animStr = this.bus.bus.strValue;
        } else {
            this.animStr = ProcFunc.slimBinary(this.bus.bus.binaryValue);
        }
        setup();
    }
}
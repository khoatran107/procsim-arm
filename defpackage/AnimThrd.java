package defpackage;

import defpackage.ViewSim;
import java.util.Vector;

/* loaded from: ProcSim.jar:AnimThrd.class */
public class AnimThrd implements Runnable {
    DiagCanvas dCanv;
    int numAnimItem;
    Thread t;
    Vector<ViewSim.AnimationListItem> animationList;
    PComponent currentComp;
    PComponent lastComp;
    Vector<Anim> anims = new Vector<>();
    boolean animating = false;
    int speed = 10;
    int waitTime = this.speed;
    boolean stopAnim = false;
    boolean pause = false;
    boolean superSpeed = false;
    boolean instantSpeed = false;
    boolean step = false;
    boolean doOneStep = false;

    AnimThrd(DiagCanvas diagCanvas) {
        this.dCanv = diagCanvas;
    }

    public void addAnim(int i) {
        addAnim(i, false);
    }

    public void addAnim(int i, boolean z) {
        if (!this.animating || z) {
            Anim anim = new Anim(this.dCanv, this.dCanv.buses.get(i));
            if (!alreadyExists(anim)) {
                this.anims.add(anim);
            } else {
                for (int i2 = 0; i2 < this.anims.size(); i2++) {
                    Anim anim2 = this.anims.get(i2);
                    if (anim2.bus.equals(anim.bus)) {
                        anim2.bus = this.dCanv.buses.get(i);
                        anim2.reset();
                    }
                }
            }
            this.currentComp = anim.bus.out;
            if (!this.dCanv.animThread.instantSpeed) {
                this.dCanv.viewSim.statusBar.repaint();
            }
        }
    }

    public PComponent getCurrentAnimComp() {
        if (this.currentComp != null) {
            return this.currentComp;
        }
        return null;
    }

    public boolean alreadyExists(Anim anim) {
        for (int i = 0; i < this.anims.size(); i++) {
            if (this.anims.get(i).bus.equals(anim.bus)) {
                return true;
            }
        }
        return false;
    }

    public void startAnim() {
        this.animationList = this.dCanv.viewSim.animationList;
        for (int i = 0; i < this.dCanv.viewSim.animationList.get(this.numAnimItem).numBuses; i++) {
            addAnim(this.dCanv.viewSim.animationList.get(this.numAnimItem).bus[i], true);
        }
        this.numAnimItem++;
        ProcSim.out("Animating... ");
        this.t = new Thread(this);
        this.t.start();
    }

    public void animate() {
        animate(this.speed);
    }

    public void animate(int i) {
        this.stopAnim = false;
        this.speed = i;
        calcWaitTime();
        this.numAnimItem = 0;
        this.anims.clear();
        if (this.t == null) {
            startAnim();
        } else if (!this.t.isAlive()) {
            startAnim();
        } else {
            ProcSim.out("Stopping old animation. ");
            this.numAnimItem = 0;
            this.stopAnim = true;
        }
        this.dCanv.resetImage();
    }

    public void nextAnim() {
        this.animationList = this.dCanv.viewSim.animationList;
        if (this.numAnimItem > this.animationList.size() - 1 || this.animationList.size() == 0) {
            if (this.step) {
                this.step = false;
                this.dCanv.repaint();
                return;
            } else {
                this.numAnimItem = 0;
                if (!this.dCanv.viewSim.nextCompAnim()) {
                    return;
                }
            }
        }
        while (this.animationList.size() == 0) {
            if (!this.dCanv.viewSim.nextCompAnim()) {
                return;
            }
        }
        for (int i = 0; i < this.animationList.get(this.numAnimItem).numBuses; i++) {
            addAnim(this.animationList.get(this.numAnimItem).bus[i], true);
        }
        this.numAnimItem++;
        if (!this.t.isAlive()) {
            this.t = new Thread(this);
            this.t.start();
        }
        this.dCanv.repaint();
    }

    private int calcAnimsLeft() {
        int i = 0;
        for (int i2 = 0; i2 < this.anims.size(); i2++) {
            if (this.anims.get(i2).animating) {
                i++;
            }
        }
        return i;
    }

    public void doneAnim(Anim anim) {
        if (calcAnimsLeft() == 0) {
            nextAnim();
        } else {
            this.dCanv.repaint();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        for (int i = 0; i < this.anims.size(); i++) {
            Anim anim = this.anims.get(i);
            if (anim.animating) {
                anim.setup();
            }
        }
        this.animating = true;
        while (calcAnimsLeft() > 0) {
            for (int i2 = 0; i2 < this.anims.size(); i2++) {
                if (this.anims.get(i2).animating) {
                    this.anims.get(i2).doPlot();
                }
            }
            if (!this.instantSpeed) {
                Thread thread = this.t;
                Thread.yield();
            }
            long j = this.speed;
            if (j > 0 && !this.instantSpeed) {
                try {
                    Thread thread2 = this.t;
                    Thread.sleep(j);
                } catch (Exception e) {
                }
            }
            while (this.pause && !this.step) {
                try {
                    Thread thread3 = this.t;
                    Thread.yield();
                    Thread thread4 = this.t;
                    Thread.sleep(100L);
                } catch (Exception e2) {
                }
            }
            if (this.stopAnim) {
                this.stopAnim = false;
                this.animating = false;
                this.anims.clear();
                this.dCanv.resetImage();
                return;
            }
            this.dCanv.showAnim();
        }
        restart();
        this.animating = false;
        this.dCanv.resetImage();
    }

    public void calcWaitTime() {
        if (this.speed < 100) {
            this.waitTime = this.speed;
            return;
        }
        if (this.speed < 200) {
            this.waitTime = (int) (this.speed * 0.8d);
            return;
        }
        if (this.speed < 300) {
            this.waitTime = (int) (this.speed * 0.6d);
        } else if (this.speed < 400) {
            this.waitTime = (int) (this.speed * 0.6d);
        } else if (this.speed >= 400) {
            this.waitTime = (int) (this.speed * 0.55d);
        }
    }

    public void restart() {
        this.numAnimItem = 0;
        for (int i = 0; i < this.anims.size(); i++) {
            this.anims.get(i).bus.newVal = false;
            this.anims.get(i).bus.animated = false;
            this.anims.get(i).bus.bus.doneAnimOnce = false;
        }
        this.anims.clear();
        this.currentComp = null;
        this.animating = false;
    }
}
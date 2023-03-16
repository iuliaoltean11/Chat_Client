package org.example;

import java.awt.*;

public class MyReclame extends Thread {
    int pA;
    Reclame parent = null;
    Image imag = null;
    public MyReclame(Reclame parent, int pA, Image imgA) {
        this.parent = parent;
        this.pA = pA;
         this.imag = imgA;
    }

    void paint(){
        Graphics g = parent.getGraphics();
        g.drawImage(imag, 0, 0, null);
    }

    public void run(){
        while(true){
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            paint();
        }
    }
}

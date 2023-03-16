package org.example;

import java.awt.*;
import java.awt.event.WindowEvent;

public class Reclame extends Frame {
    public Reclame() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
        Image imgA = null;
        Image imgB = null;
        Image imgC = null;
        MediaTracker mt = new MediaTracker(this); // this - fereastra
        imgA = Toolkit.getDefaultToolkit().getImage("imgA.jpg");
        mt.addImage(imgA, 0);
        imgB = Toolkit.getDefaultToolkit().getImage("imgB.jpg");
        mt.addImage(imgB, 0);
        imgC = Toolkit.getDefaultToolkit().getImage("imgC.jpg");
        mt.addImage(imgC, 0);
        try{
            mt.waitForAll();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        MyReclame reclame = new MyReclame(this,5, imgA);
        MyReclame reclame1 = new MyReclame(this, 3, imgB);
        MyReclame reclame2 = new MyReclame(this, 2, imgC);
        reclame.start();
        reclame1.start();
        reclame2.start();
    }
    private void jbInit() throws Exception {
        this.setSize(new Dimension(1500, 1500));
        this.setTitle("Reclame");
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });
    }
    void this_windowClosing(WindowEvent e) {
        System.exit(0);
    }
}

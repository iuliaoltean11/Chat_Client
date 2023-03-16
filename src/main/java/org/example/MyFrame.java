package org.example;

import java.awt.*;
import java.awt.event.WindowEvent;

public class MyFrame extends Frame {
    Ball ball = null;
    Ball ball_new = null;
    public MyFrame() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
        ball = new Ball(this, 20, 50, 20, Color.blue);
        ball_new = new Ball(this, 40, 60, 30, Color.pink);
        ball.start();
        ball_new.start();
    }
    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 300));
        this.setTitle("Balls");
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

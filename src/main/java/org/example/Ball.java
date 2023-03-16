package org.example;

import java.awt.*;

public class Ball extends Thread {
    int px = 0;
    int py = 0;
    int size = 0;
    int gravy = 1;
    int speed = -25;
    int speedy = -25;
    int speedx = 0;
    Color color = null;
    MyFrame parent = null;
    Image buffer = null;
    public Ball(MyFrame parent, int px, int py, int size, Color color) {
        this.parent = parent;
        this.px = px;
        this.py = py;
        this.size = size;
        this.color = color;
        buffer = parent.createImage(parent.getSize().width, parent.getSize().height);
    }
    void paint(){
        Graphics gbuffer = buffer.getGraphics();
        //se deseneaza mai intai in buffer (tehnica Double Buffering)
        gbuffer.setColor(Color.white);
        gbuffer.fillRect(0, 0, parent.getSize().width, parent.getSize().height);
        gbuffer.setColor(color);
        gbuffer.fillOval(px, py, size, size);
        parent.paint(gbuffer);
        //se copiaza imaginea din buffer pe fereastra (tehnica Double Buffering)
        Graphics g = parent.getGraphics();
        g.drawImage(buffer, 0, 0, parent.getSize().width, parent.getSize().height, 0, 0, parent.getSize().width,
                parent.getSize().height, parent);
    }
    public void run(){
        while(true){
            speedy += gravy;
            py += speedy;
            px += speedx;
            paint();
            if(py >= parent.getSize().height){
                speedy = speed;
                speed += 1;
            }
            if(speed == 0) break;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        }
    }





package org.example;
//evenimentele sunt reutilizabile, deci se reseteaza , facem stocplin.reset, se reseteaza eveniment dupa orice wait
public class Ec2{
    private Thread_X1 tx1;
    private Thread_X2 tx2;
    private Thread_Delta tdelta;
    int a = 1;
    int b = -4;
    int c = 1;
    double x1, x2;
    Double d = null;
    public Ec2() {
        tx1 = new Thread_X1(this);
        tx2 = new Thread_X2(this);
        tdelta = new Thread_Delta(this);
        tx1.start();
        tx2.start();
        tdelta.start();
    }
    public synchronized void delta(){
        d = new Double(b*b - 4*a*c);
        System.out.println("delta = " + d);
        System.out.println("tdelta: sleeping");
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.notifyAll();
        System.out.println("tdelta: notified");
    }
    public synchronized void x1(){
        if(d == null){
            System.out.println("tx1: waiting...");
            try {
                this.wait();
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        x1 = (-b + Math.sqrt(d.doubleValue())) / (2 * a);
        System.out.println("x1 = " + x1);
    }
    public synchronized void x2(){
        if(d == null){
            System.out.println("tx2: waiting...");
            try {
                this.wait();
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        x2 = (-b - Math.sqrt(d.doubleValue())) / (2 * a);
        System.out.println("x2 = " + x2);
    }
    public static void main(String[] args) {
        new Ec2();
    }
}


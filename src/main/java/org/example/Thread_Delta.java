package org.example;

class Thread_Delta extends Thread{
    Ec2 ec2;
    public Thread_Delta(Ec2 ec2) {
        this.ec2 = ec2;
    }
    public void run(){
        ec2.delta();
    }
}
class Thread_X1 extends Thread{
    Ec2 ec2;
    public Thread_X1(Ec2 ec2) {
        this.ec2 = ec2;
    }
    public void run(){
        ec2.x1();
    }


}

class Thread_X2 extends Thread{
    Ec2 ec2;
    public Thread_X2(Ec2 ec2) {
        this.ec2 = ec2;
    }
    public void run(){
        ec2.x2();
    }


}
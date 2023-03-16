package org.example;

 class Numbers extends Thread {
    public void run(){
        for(int i=0; i<1000; i++){
            System.out.println(i);
        }
    }
}
 class Letters implements Runnable {
    char a = 'a';
    public void run(){
        for(int i=0; i<1000; i++){
            int c = a + i%26;
            System.out.println((char)c);
        }
    }
}
 class Main {
    public static void main(String[] args) {
        Numbers numbers = new Numbers();
        Thread letters = new Thread(new Letters());
        letters.start();
        numbers.start();
    }
}


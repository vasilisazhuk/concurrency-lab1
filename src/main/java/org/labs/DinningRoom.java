package org.labs;

import java.util.concurrent.locks.ReentrantLock;

public class DinningRoom {
    public static void main(String[] args) throws Exception {
        Programmer[] programmers = new Programmer[7];
        ReentrantLock[] spoons = new ReentrantLock[programmers.length];

        for (int i = 0; i < spoons.length; i++){
            spoons[i] = new ReentrantLock();
        }

        for (int i = 0; i < programmers.length; i++){
            ReentrantLock leftSpoon = spoons[i];
            ReentrantLock rightSpoon = spoons[(i + 1) % spoons.length];

            programmers[i] = new Programmer(leftSpoon, rightSpoon);

            Thread t = new Thread(programmers[i], "Programmer " + (i + 1));
            t.start();
        }
    }
}


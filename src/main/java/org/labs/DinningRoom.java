package org.labs;

public class DinningRoom {
    public static void main(String[] args) throws Exception {
        Programmer[] programmers = new Programmer[7];
        Object[] spoons = new Object[programmers.length];

        for (int i = 0; i < spoons.length; i++){
            spoons[i] = new Object();
        }

        for (int i = 0; i < programmers.length; i++){
            Object leftSpoon = spoons[i];
            Object rightSpoon = spoons[(i + 1) % spoons.length];

            programmers[i] = new Programmer(leftSpoon, rightSpoon);

            Thread t = new Thread(programmers[i], "Programmer " + (i + 1));
            t.start();
        }
    }
}


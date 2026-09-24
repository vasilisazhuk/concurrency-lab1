package org.labs;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Programmer implements  Runnable {

    private static final AtomicInteger totalPortionsLeft = new AtomicInteger(100);
    private Object leftSpoon;
    private Object rightSpoon;

    //private final ReentrantLock lockForks = new ReentrantLock(true);

    private static Semaphore waiters = new Semaphore(2, true);
    public Programmer(Object leftSpoon, Object rightSpoon) {
        this.leftSpoon = leftSpoon;
        this.rightSpoon = rightSpoon;
    }

    private void doAction(String action) throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + " " + action
        );
        Thread.sleep((int) (Math.random() * 100));
    }

    private void orderMeal() throws InterruptedException {
        doAction(Thread.currentThread().getName() + ": Waiting for a waiter");
        waiters.acquire();
    }

    private void takeMeal() throws InterruptedException {

        doAction(Thread.currentThread().getName() + ": Take meal from waiter");
        waiters.release();
    }

    @Override
    public void run(){
        try {
            while (true) {
                doAction(System.nanoTime() + ": Thinking");
                if (totalPortionsLeft.get() <= 0) break;
                orderMeal();

                synchronized (leftSpoon) {
                    doAction(System.nanoTime() + ": Picked left spoon");
                    synchronized (rightSpoon) {
                        int remaining = totalPortionsLeft.get();
                        //if (remaining <= 0) break;
                        if (remaining > 0 &&
                                totalPortionsLeft.compareAndSet(remaining, remaining - 1)){
                            doAction(System.nanoTime() + ": Picked right spoon - eating. Portions left: " + remaining);
                            doAction(System.nanoTime() + ": Put down right spoon");
                        }
                    }
                    doAction(System.nanoTime() + ": Put down left spoon. Back to thinking");
                }
                takeMeal();
            }
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            Thread.currentThread().interrupt();
            return;
        }
    }

}

package org.labs;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Programmer implements  Runnable {

    private static final AtomicInteger totalPortionsLeft = new AtomicInteger(100);
    private ReentrantLock leftSpoon;
    private ReentrantLock rightSpoon;


    private static Semaphore waiters = new Semaphore(2, true);
    public Programmer(ReentrantLock leftSpoon, ReentrantLock rightSpoon) {
        this.leftSpoon = leftSpoon;
        this.rightSpoon = rightSpoon;
    }

    private void doAction(String action) throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + " " + action
        );
        Thread.sleep((int) ( ThreadLocalRandom.current().nextInt(100)));
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
                try {
                if (leftSpoon.tryLock()){
                    try {
                        doAction(System.nanoTime() + ": Picked left spoon");
                        if (rightSpoon.tryLock()){
                            try {
                                int remaining = totalPortionsLeft.decrementAndGet();
                                if (remaining >= 0) {
                                    doAction(System.nanoTime() + ": Picked right spoon. Portions left: " + totalPortionsLeft.get());
                                } else {
                                    //waiters.release();
                                    break;
                                }
                                //totalPortionsLeft.compareAndSet(remaining, remaining - 1);
                            } finally {
                                doAction(System.nanoTime() + ": Put down right spoon");
                                rightSpoon.unlock();
                            }
                        }
                    } finally {
                        doAction(System.nanoTime() + ": Put down left spoon, back to thinking");
                        leftSpoon.unlock();
                    }

                }

            } finally {
                    //чтобы отпустить семафор
                    takeMeal();
                }
            }
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            Thread.currentThread().interrupt();
            return;
        }
    }

}

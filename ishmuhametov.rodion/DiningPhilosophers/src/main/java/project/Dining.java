package project;

import org.joda.time.LocalTime;

public class Dining {

    public static void main(String[] args) {
		
		LocalTime time = new LocalTime();
		System.out.println("Local time: " + time);

        Fork[] forks = new Fork[5];
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Fork("Fork " + i);
        }

        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < philosophers.length - 1; i++) {
            philosophers[i] = new Philosopher("Philosopher " + i, forks[i], forks[i + 1]);
        }
        philosophers[4] = new Philosopher("Philosopher " + 4, forks[4], forks[0]);

        for (int i = 0; i < philosophers.length; i++) {
            System.out.println("Starting Thread " + i);
            Thread t = new Thread(philosophers[i]);
            t.start();
        }

    }

}

class Philosopher implements Runnable {

    private String name;
    private final Fork leftFork, rightFork;

    public Philosopher(String _name, Fork _leftFork, Fork _rightFork) {
        name = _name;
        leftFork = _leftFork;
        rightFork = _rightFork;
    }

    public void eat() {

        synchronized (leftFork) {
            synchronized (rightFork) {
                if (!leftFork.isTaken() && !rightFork.isTaken()) {
                    leftFork.take();
                    rightFork.take();

                    System.out.println(name + " is eating");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    leftFork.release();
                    rightFork.release();
                }
            }
        }

        think();

    }

    public void think() {

        System.out.println(name + " is thinking");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        for (int i = 0; i < 5; i++) {
            if (i != 1) {
                System.out.println(name + " has eaten " + i + " times");
            } else {
                System.out.println(name + " has eaten " + i + " time");
            }

            eat();
        }

    }

}

class Fork {

    private String name;
    private boolean isTaken;

    public Fork(String _name) {

        name = _name;
        isTaken = false;

    }

    public void take() {

        if (!isTaken) {
            isTaken = true;
            System.out.println("Take " + name);
        } else {
            System.out.println(name + " is already taken");
        }

    }

    public void release() {

        if (isTaken) {
            isTaken = false;
            System.out.println("Release " + name);
        } else {
            System.out.println(name + " is already released");
        }

    }

    public boolean isTaken() {

        return isTaken;

    }

}

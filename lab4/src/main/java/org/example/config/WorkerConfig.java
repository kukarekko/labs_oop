package org.example.config;

public class WorkerConfig {
    private final int speed;
    private final int count;

    public WorkerConfig(int speed, int count) {
        this.speed = speed;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int getSpeed() {
        return speed;
    }
}

package org.example.storage;

import org.example.factory.FactoryUI;

import java.util.LinkedList;
import java.util.Queue;

public class Storage<T> {
    private final int maxSize;
    private final String type;
    private Queue<T> items = new LinkedList<>();
    private FactoryUI ui;
    private static int totalCars = 0;

    public Storage(int maxSize, String type, FactoryUI ui) {
        this.maxSize = maxSize;
        this.type = type;
        this.ui = ui;
    }

    private void updateUI(int currentSize) {
        if (ui == null) return;

        switch (type) {
            case "body" -> ui.updateBody(currentSize);
            case "engine" -> ui.updateEngine(currentSize);
            case "accessory" -> ui.updateAccessory(currentSize);
            case "car" -> ui.updateCar(currentSize);
        }
    }

    public synchronized void put(T item) throws InterruptedException {
        while (items.size() >= maxSize) {
            wait();
        }
        items.add(item);

        updateUI(items.size());
        if ("car".equals(type)) {
            ui.updateCar(items.size());
            totalCars++;
            ui.updateTotal(totalCars);
            ui.updateQueueSize(0);
        }

        notifyAll(); //будит все ждущие потоки
    }

    public synchronized T get() throws InterruptedException {
        while (items.isEmpty()) {
            wait();
        }
        T item = items.poll();

        updateUI(items.size());

        notifyAll();
        return item;
    }

    public synchronized void clear() {
        items.clear();

        if (ui != null) {
            switch (type) {
                case "body" -> ui.updateBody(0);
                case "engine" -> ui.updateEngine(0);
                case "accessory" -> ui.updateAccessory(0);
                case "car" -> {
                    ui.updateCar(0);
                    totalCars = 0;
                    ui.updateTotal(0);
                }
            }
        }

        notifyAll();
    }

    public synchronized int size() {
        return items.size();
    }

    public int getMaxSize() {
        return maxSize;
    }
}
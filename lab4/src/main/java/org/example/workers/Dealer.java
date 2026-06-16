package org.example.workers;

import org.example.logger.Logger;
import org.example.storage.Storage;
import org.example.model.Car;

public class Dealer implements Runnable {
    private final String name;
    private final Storage<Car> carStorage;
    private final int purchaseTime;

    public Dealer(String name, Storage carStorage, int purchaseTime) {
        this.name = name;
        this.carStorage = carStorage;
        this.purchaseTime = purchaseTime;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(purchaseTime);
            Car car = carStorage.get();
            Logger.logDealerPurchase(name, car);
            System.out.println(name + " купил" + car);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
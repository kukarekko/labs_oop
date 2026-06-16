package org.example.workers;

import org.example.logger.Logger;
import org.example.storage.Storage;
import org.example.model.Car;
import org.example.model.Part;

//сборщик машин
public class Worker implements Runnable{
    private final Storage<Part> bodyStorage;
    private final Storage<Part> engineStorage;
    private final Storage<Part> accessoryStorage;
    private final Storage<Car> carStorage;
    private final int assemblyTime;

    public Worker(Storage<Part> body, Storage<Part> engine,
                  Storage<Part> accessory, Storage<Car> car, int assemblyTime) {
        this.bodyStorage = body;
        this.engineStorage = engine;
        this.accessoryStorage = accessory;
        this.carStorage = car;
        this.assemblyTime = assemblyTime;
    }


    @Override
    public void run() {
        try {
            Part body = bodyStorage.get();
            Part engine = engineStorage.get();
            Part accessory = accessoryStorage.get();

            Thread.sleep(assemblyTime);
            Car car = new Car(body, engine, accessory);
            carStorage.put(car);
            Logger.logCarProduced(car);
            System.out.println("Машина собрана!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

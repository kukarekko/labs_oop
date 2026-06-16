package org.example.workers;

import org.example.storage.Storage;
import org.example.model.Part;

//поставщик
public class Supplier implements Runnable{
    private String name;
    private Storage<Part> storage;
    private final int productionTime;

    public Supplier(String name, Storage storage, int productionTime) {
        this.name = name;
        this.storage = storage;
        this.productionTime = productionTime;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(productionTime);
            Part part = new Part();
            storage.put(part);
            System.out.println(name + " поставил" + part);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

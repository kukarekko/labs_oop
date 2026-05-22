package org.example.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Car {
    private static AtomicInteger idGenerator = new AtomicInteger(1);
    private final int id;
    private final Part body;
    private final Part engine;
    private final Part accessory;

    public Car(Part body, Part engine, Part accessory) {
        this.id = idGenerator.getAndIncrement();
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
    }

    public String getLogString() {
        return String.format("Auto %d (Body: %d, Engine: %d, Accessory: %d)",
                id, body.getId(), engine.getId(), accessory.getId());
    }
}

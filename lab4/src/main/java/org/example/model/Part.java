package org.example.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Part {
    private static final AtomicInteger idGenerator = new AtomicInteger(1); //счетчик безопасный для многопоточности
    private final int id; //уникальный id каждой детали

    public Part() {
        this.id = idGenerator.getAndIncrement();
    }

    public int getId() {
        return id;
    }
}

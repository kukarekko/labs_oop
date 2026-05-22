package org.example.config;

import org.example.model.Car;
import org.example.model.PartType;
import org.example.storage.Storage;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class FactoryData {
    private final Map<PartType, PartData> partsData;
    private final Storage<Car> carStorage;
    private final WorkerConfig workerConfig;
    private final WorkerConfig dealerConfig;
    private final ExecutorService supplierPool;
    private final ExecutorService workerPool;
    private final ExecutorService dealerPool;
    private volatile boolean running = true;

    public FactoryData(Map<PartType, PartData> partsData,
                       Storage<Car> carStorage,
                       WorkerConfig workerConfig,
                       WorkerConfig dealerConfig,
                       ExecutorService supplierPool,
                       ExecutorService workerPool,
                       ExecutorService dealerPool) {
        this.partsData = partsData;
        this.carStorage = carStorage;
        this.workerConfig = workerConfig;
        this.dealerConfig = dealerConfig;
        this.supplierPool = supplierPool;
        this.workerPool = workerPool;
        this.dealerPool = dealerPool;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }
    public Map<PartType, PartData> getPartsData() { return partsData; }
    public Storage<Car> getCarStorage() { return carStorage; }
    public WorkerConfig getWorkerConfig() { return workerConfig; }
    public WorkerConfig getDealerConfig() { return dealerConfig; }
    public ExecutorService getSupplierPool() { return supplierPool; }
    public ExecutorService getWorkerPool() { return workerPool; }
    public ExecutorService getDealerPool() { return dealerPool; }
}

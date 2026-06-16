package org.example.factory;

import org.example.config.Config;
import org.example.config.FactoryData;
import org.example.config.PartData;
import org.example.config.WorkerConfig;
import org.example.logger.Logger;
import org.example.model.Car;
import org.example.model.Part;
import org.example.model.PartType;
import org.example.storage.Storage;
import org.example.storage.StorageController;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FactoryController {
    private static ExecutorService supplierPool;
    private static ExecutorService workerPool;
    private static ExecutorService dealerPool;
    private static ExecutorService[] allPools;

    private static FactoryData factoryData;

    private static Thread controllerThread;
    private static StorageController controller;
    private static Map<PartType, Storage<Part>> partStorages = new EnumMap<>(PartType.class);
    private static Storage<Car> cars;
    private static FactoryUI ui;
    private static boolean isInitialized = false;

    private static int getSupplierSpeed(PartType type) {
        switch (type) {
            case BODY: return ui.getBodySupplierSpeed();
            case ENGINE: return ui.getEngineSupplierSpeed();
            case ACCESSORY: return ui.getAccessorySupplierSpeed();
            default: return 1000;
        }
    }

    private static int getSupplierCount(PartType type) {
        switch (type) {
            case BODY: return ui.getBodySupplierCount();
            case ENGINE: return ui.getEngineSupplierCount();
            case ACCESSORY: return ui.getAccessorySupplierCount();
            default: return 1;
        }
    }

    private static void updatePartCount(PartType type, int count) {
        switch (type) {
            case BODY: ui.updateBody(count); break;
            case ENGINE: ui.updateEngine(count); break;
            case ACCESSORY: ui.updateAccessory(count); break;
        }
    }

    private static void updateSuppliersCount(PartType type, int count) {
        switch (type) {
            case BODY: ui.updateBodySuppliers(count); break;
            case ENGINE: ui.updateEngineSuppliers(count); break;
            case ACCESSORY: ui.updateAccessorySuppliers(count); break;
        }
    }

    public static void startFactory(FactoryUI factoryUI) throws InterruptedException {
        ui = factoryUI;

        stopAllPools();

        //читаем конфиг
        Config config = new Config("config.properties");
        boolean logEnabled = config.getInt("log.enabled") == 1;
        String logFile = config.getProperty("log.file", "factory.log");
        Logger.init(logEnabled, logFile);

        Map<PartType, Integer> partSizes = new EnumMap<>(PartType.class);
        for (PartType type : PartType.values()) {
            partSizes.put(type, config.getInt("storage." + type.getStorageType() + ".size"));
        }

        int carSize = config.getInt("storage.car.size");

        if (!isInitialized) {
            for (PartType type : PartType.values()) {
               partStorages.put(type, new Storage<>(partSizes.get(type), type.getStorageType(), ui));
            }
            cars = new Storage<>(carSize, "car", ui);
            isInitialized = true;
        } else {
            for (Map.Entry<PartType, Storage<Part>> entry : partStorages.entrySet()) {
                updatePartCount(entry.getKey(), entry.getValue().size());
           }
            if (cars != null) {
                ui.updateCar(cars.size());
           }
        }

        Map<PartType, PartData> partData = new EnumMap<>(PartType.class);
        for (PartType type : PartType.values()) {
            partData.put(type, new PartData(partStorages.get(type), getSupplierSpeed(type), getSupplierCount(type)));
        }

        int workerCount = ui.getWorkerCount();
        int dealerCount = ui.getDealerCount();
        int workerSpeed = ui.getWorkerSpeed();
        int dealerSpeed = ui.getDealerSpeed();

        WorkerConfig workerConfig = new WorkerConfig(workerSpeed, workerCount);
        WorkerConfig dealerConfig = new WorkerConfig(dealerSpeed, dealerCount);

        supplierPool = Executors.newCachedThreadPool();
        workerPool = Executors.newFixedThreadPool(workerCount);
        dealerPool = Executors.newCachedThreadPool();
        allPools = new ExecutorService[]{supplierPool, workerPool, dealerPool};


        //запускаем контроллер склада
        FactoryData factoryData = new FactoryData(partData, cars, workerConfig, dealerConfig,
                supplierPool, workerPool, dealerPool);

        controller = new StorageController(factoryData);

        controllerThread = new Thread(controller);
        controllerThread.start();

        ui.updateQueueSize(0);

        for (PartType type : PartType.values()) {
            updateSuppliersCount(type, getSupplierCount(type));
        }
        ui.updateWorkers(workerCount);
        ui.updateDealers(dealerCount);

        while (ui.isRunning()) {
            int totalQueueSize = 0;
            for (ExecutorService pool : allPools) {
                if (pool instanceof ThreadPoolExecutor) {
                    totalQueueSize += ((ThreadPoolExecutor) pool).getQueue().size();
                }
            }
            ui.updateQueueSize(totalQueueSize);
            Thread.sleep(1000);
        }

        stopAllPools();
    }

    private static void stopAllPools() {
        if (supplierPool != null) supplierPool.shutdownNow();
        if (workerPool != null) workerPool.shutdownNow();
        if (dealerPool != null) dealerPool.shutdownNow();

        try {
            if (supplierPool != null) supplierPool.awaitTermination(2, TimeUnit.SECONDS);
            if (workerPool != null) workerPool.awaitTermination(2, TimeUnit.SECONDS);
            if (dealerPool != null) dealerPool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (factoryData != null) {
            factoryData.stop();
        }
        if (controllerThread != null) controllerThread.interrupt();

        Logger.close();
    }

    public static void resetStorages() {
        if (cars != null) {
            synchronized (cars) {
                cars.clear();
            }
        }

        for (Map.Entry<PartType, Storage<Part>> entry : partStorages.entrySet()) {
            if (entry.getValue() != null) {
                synchronized (entry.getValue()) {
                    entry.getValue().clear();
                }
            }
        }

        if (ui != null) {
            ui.updateBody(0);
            ui.updateEngine(0);
            ui.updateAccessory(0);
            ui.updateCar(0);
            ui.updateTotal(0);
            ui.updateQueueSize(0);
        }
    }


    public static void stopFactory() {
        stopAllPools();
    }
}
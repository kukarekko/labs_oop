package org.example.storage;

import org.example.config.FactoryData;
import org.example.config.PartData;
import org.example.model.PartType;
import org.example.workers.Dealer;
import org.example.workers.Supplier;
import org.example.workers.Worker;

import java.util.EnumMap;
import java.util.Map;

public class StorageController implements Runnable{
    private final FactoryData factoryData;
    private static final int CHECK_INTERVAL_MS = 1000;

    public StorageController(FactoryData factoryData) {
        this.factoryData = factoryData;
    }

    @Override
    public void run() {
        while (true) {
            try {
                scheduleAllSuppliers();
                scheduleWorkers();
                scheduleDealers();

                Thread.sleep(CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void scheduleAllSuppliers() {
        for (PartType type : PartType.values()) {
            scheduleSuppliersForType(type);
        }
    }

    private void scheduleSuppliersForType(PartType type) {
        PartData data = factoryData.getPartsData().get(type);
        int tasksToAdd = data.getSuppliersToSchedule();

        for (int i = 0; i < tasksToAdd; i++) {
            factoryData.getSupplierPool().submit(
                    new Supplier(type.getSupplierName(), data.getStorage(), data.getSupplierSpeed())
            );
        }
    }

    private void scheduleWorkers() {
        Map<PartType, Integer> possibleWorkersByType = new EnumMap<>(PartType.class);

        for (PartType type : PartType.values()) {
            PartData data = factoryData.getPartsData().get(type);

            int workersForThisPart = Math.min(data.getCurrentSize(), factoryData.getWorkerConfig().getCount());
            possibleWorkersByType.put(type, workersForThisPart);
        }

        //деталей которых меньше
        int workersToAdd = possibleWorkersByType.values().stream().min(Integer::compareTo).orElse(0);

        int freeCarSpace = factoryData.getCarStorage().getMaxSize() - factoryData.getCarStorage().size();
        workersToAdd = Math.min(workersToAdd, freeCarSpace);

        // запускаем сборщиков
        for (int i = 0; i < workersToAdd; i++) {
            factoryData.getWorkerPool().submit(
                    new Worker(
                            factoryData.getPartsData().get(PartType.BODY).getStorage(),
                            factoryData.getPartsData().get(PartType.ENGINE).getStorage(),
                            factoryData.getPartsData().get(PartType.ACCESSORY).getStorage(),
                            factoryData.getCarStorage(),
                            factoryData.getWorkerConfig().getSpeed()
                    )
            );
        }
    }

    private void scheduleDealers() {
        int availableCars = factoryData.getCarStorage().size();
        int maxDealers = factoryData.getDealerConfig().getCount();
        int dealersToAdd = Math.min(availableCars, maxDealers);

        for (int i = 0; i < dealersToAdd; i++) {
            factoryData.getDealerPool().submit(
                    new Dealer(
                            "Дилер " + (i + 1),
                            factoryData.getCarStorage(),
                            factoryData.getDealerConfig().getSpeed()
                    )
            );
        }
    }
}

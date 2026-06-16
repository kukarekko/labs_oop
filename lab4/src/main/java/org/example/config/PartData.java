package org.example.config;

import org.example.storage.Storage;
import org.example.model.Part;

public class PartData {
    private final Storage<Part> storage;
    private final int supplierSpeed;
    private final int supplierCount;

    public PartData(Storage<Part> storage, int supplierSpeed, int supplierCount) {
        this.storage = storage;
        this.supplierSpeed = supplierSpeed;
        this.supplierCount = supplierCount;
    }

    public Storage<Part> getStorage() {
        return storage;
    }
    public int getSupplierSpeed() {
        return supplierSpeed;
    }
    public int getCurrentSize() {
        return storage.size();
    }
    public int getFreeSpace() {
        return storage.getMaxSize() - storage.size();
    }
    public int getSuppliersToSchedule() {
        return Math.min(getFreeSpace(), supplierCount);
    }
}

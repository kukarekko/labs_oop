package org.example.model;

public enum PartType {
    BODY("кузова", "body", "Поставщик кузовов"),
    ENGINE("двигатели", "engine", "Поставщик двигателей"),
    ACCESSORY("аксессуары", "accessory", "Поставщик аксессуаров");

    private final String uiName;
    private final String storageType;
    private final String supplierName;

    PartType(String uiName, String storageType, String supplierName) {
        this.uiName = uiName;
        this.storageType = storageType;
        this.supplierName = supplierName;
    }


    public String getStorageType() {
        return storageType;
    }

    public String getSupplierName() {
        return supplierName;
    }
}

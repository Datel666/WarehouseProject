package com.example.warehouseproject.Code;

/**
 * historyitem class
 *
 * Данный класс описывает структуру записи в истории импорта и экспорта товаров
 */
public class Historyitem {


    public Historyitem(String _operation, String _vendor, String _type, String _name, String _count,String _date) {
        operation = _operation;
        vendor = _vendor;
        type = _type;
        name = _name;
        count = _count;
        date = _date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String operation;
    public String vendor;
    public String type;
    public String name;
    public String count;
    public String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

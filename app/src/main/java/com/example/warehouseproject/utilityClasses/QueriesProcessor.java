package com.example.warehouseproject.utilityClasses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.warehouseproject.Code.Item;
import com.example.warehouseproject.Code.Historyitem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *  Класс, реализующий сложную логику обращения к базе данных
 *
 */
public class QueriesProcessor {

    /**
     * Получение списка товаров определённого типа без использования фильтров
     * @param itemtype тип товара
     * @param db база данных
     * @return  лист товаров определённого типа
     */
    public List<Item> rawqueries(String itemtype, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("Select * from itemtable where itemtype = ? order by itemname", new String[]{itemtype});
        List<Item> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
            int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
            int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
            int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int itemphotoIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMPHOTO);

            do {

                res.add(new Item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex),cursor.getBlob(itemphotoIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        cursor.close();
        return res;
    }

    /**
     * Получение списка товаров определенного типа с использованием фильтров
     * @param db база данных
     * @param selection массив с телом запроса
     * @return лист товаров определённого типа в соответствии с фильтрами
     */
    public List<Item> filteredqueries(SQLiteDatabase db, String[] selection) {
        Cursor cursor = db.query(DBHelper.TABLE_WAREHOUSE, null, selection[0] + selection[1], null, null, null, null);
        List<Item> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
            int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
            int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
            int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int itemphotoIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMPHOTO);

            do {

                res.add(new Item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex),cursor.getBlob(itemphotoIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        cursor.close();
        return res;
    }

    /**
     * Получение информации о товаре с заданным идентификатором
     * @param itemid идентификатор товара
     * @param database база данных
     * @return информация о товаре
     */
    public Item getitemInformation(int itemid, SQLiteDatabase database) {
        Cursor cursor;
        Item itemInfo;

        String queryString = "SELECT * FROM " + DBHelper.TABLE_WAREHOUSE + " WHERE " + DBHelper.KEY_ID + "=" + String.valueOf(itemid);
        cursor = database.rawQuery(queryString, null);

        cursor.moveToFirst();
        int itemidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int itemtypeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
        int itemnameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
        int itemcountIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT);
        int itemphotoIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMPHOTO);
        int itemdescriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);

        itemInfo = new Item(cursor.getInt(itemidIndex), cursor.getString(itemnameIndex), cursor.getString(itemtypeIndex), cursor.getString(itemcountIndex), cursor.getString(itemdescriptionIndex),cursor.getBlob(itemphotoIndex));
        cursor.close();
        return itemInfo;
    }

    public boolean itemExit(SQLiteDatabase database,String id)
    {
        String queryString = "SELECT * FROM " + DBHelper.TABLE_WAREHOUSE + " WHERE " + DBHelper.KEY_ID + "=" + id;

        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor.getCount()>0)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean itemExist(SQLiteDatabase database,String name,String type)
    {
        String queryString = "SELECT * FROM " + DBHelper.TABLE_WAREHOUSE + " WHERE " + DBHelper.KEY_ITEMNAME + " = '" + name + "' AND " + DBHelper.KEY_ITEMTYPE+ " = '" + type + "'";

        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor.getCount()>0)
        {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Получение истории импорта и экспорта товаров
     * @param database база данных
     * @return История импорта и экспорта товаров
     */
    public List<Historyitem> loadhistory(SQLiteDatabase database){
        String operation = "";

        List<Historyitem> res = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_SUPPLY + " a JOIN ( SELECT itemid, "+ DBHelper.KEY_ITEMNAME + " as itemname ," + DBHelper.KEY_ITEMTYPE+ " " +
                "as itemtype FROM " + DBHelper.TABLE_WAREHOUSE + ") b  on a.itemid = b.itemid",null);

        if (cursor.moveToFirst()) {
            int operationIndex = cursor.getColumnIndex(DBHelper.KEY_SUPPLYTYPE);
            int vendorIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMVENDOR);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
            int countIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT2);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);

            do {
                if(cursor.getString(operationIndex).equals("+")){
                    operation = "Импорт";
                }
                else{
                    operation = "Экспорт";
                }
                res.add(new Historyitem(operation,cursor.getString(vendorIndex),cursor.getString(typeIndex),cursor.getString(nameIndex),cursor.getString(countIndex),cursor.getString(dateIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    /**
     * Получение истории импорта и экспорта в соответствии с фильтрами
     * @param search фильтр
     * @param database база данных
     * @return История импорта и экспорта в соответствии с фильтрами
     */
    public List<Historyitem> loadhistoryfiltered(String search, SQLiteDatabase database, String orderby){
        String operation = "";
        String operationsearch = "";
        operationsearch = search.contains("Импорт") ? "+" : "-";

        List<Historyitem> res = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_SUPPLY + " a JOIN ( SELECT itemid, "+ DBHelper.KEY_ITEMNAME + " as itemname ," + DBHelper.KEY_ITEMTYPE+ " " +
                "as itemtype FROM " + DBHelper.TABLE_WAREHOUSE + ") b  on a.itemid = b.itemid WHERE itemname LIKE" + " '%" + search + "%'" + " " +
                " OR supplytype LIKE "+ "'%"+ search + "%'" + " OR itemvendor LIKE " + "'%" + search + "%'" + " ORDER BY " + orderby,null);

        if (cursor.moveToFirst()) {
            int operationIndex = cursor.getColumnIndex(DBHelper.KEY_SUPPLYTYPE);
            int vendorIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMVENDOR);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMTYPE);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_ITEMNAME);
            int countIndex = cursor.getColumnIndex(DBHelper.KEY_COUNT2);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);

            do {
                if(cursor.getString(operationIndex).equals("+")){
                    operation = "Импорт";
                }
                else{
                    operation = "Экспорт";
                }
                res.add(new Historyitem(operation,cursor.getString(vendorIndex),cursor.getString(typeIndex),cursor.getString(nameIndex),cursor.getString(countIndex),cursor.getString(dateIndex)));
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }
}

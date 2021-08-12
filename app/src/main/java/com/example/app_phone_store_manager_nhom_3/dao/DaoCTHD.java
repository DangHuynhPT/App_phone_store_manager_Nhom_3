package com.example.app_phone_store_manager_nhom_3.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_phone_store_manager_nhom_3.database.DbHelper;
import com.example.app_phone_store_manager_nhom_3.model.ChiTietHoaDon;

import java.util.ArrayList;
import java.util.List;

public class DaoCTHD {
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public DaoCTHD(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long add(ChiTietHoaDon chiTietHoaDon) {
        ContentValues values = new ContentValues();

        values.put(ChiTietHoaDon.TB_COL_ID_HD, chiTietHoaDon.getMaHD());
        values.put(ChiTietHoaDon.TB_COL_ID_SP, chiTietHoaDon.getMaSP());
        values.put(ChiTietHoaDon.TB_COL_AMOUNT, chiTietHoaDon.getSoLuong());
        values.put(ChiTietHoaDon.TB_COL_SALE, chiTietHoaDon.getGiamGia());
        values.put(ChiTietHoaDon.TB_COL_PRICE, chiTietHoaDon.getDonGia());
        values.put(ChiTietHoaDon.TB_COL_BH, chiTietHoaDon.getBaoHanh());

        return database.insert(ChiTietHoaDon.TB_NAME, null, values);
    }

    public int delete(ChiTietHoaDon chiTietHoaDon) {
        return database.delete(ChiTietHoaDon.TB_NAME, "maCTHD = ?", new String[]{chiTietHoaDon.getIdCTHD() + ""});
    }

    public int update(ChiTietHoaDon chiTietHoaDon) {
        ContentValues values = new ContentValues();

        values.put(ChiTietHoaDon.TB_COL_ID_HD, chiTietHoaDon.getMaHD());
        values.put(ChiTietHoaDon.TB_COL_ID_SP, chiTietHoaDon.getMaSP());
        values.put(ChiTietHoaDon.TB_COL_AMOUNT, chiTietHoaDon.getSoLuong());
        values.put(ChiTietHoaDon.TB_COL_SALE, chiTietHoaDon.getGiamGia());
        values.put(ChiTietHoaDon.TB_COL_PRICE, chiTietHoaDon.getDonGia());
        values.put(ChiTietHoaDon.TB_COL_BH, chiTietHoaDon.getBaoHanh());

        return database.update(ChiTietHoaDon.TB_NAME, values, "maCTHD = ?", new String[]{chiTietHoaDon.getIdCTHD() + ""});
    }

    public List<ChiTietHoaDon> getAll() {
        String sql = "SELECT * FROM ChiTietHoaDon";
        List<ChiTietHoaDon> list = getData(sql);
        return list;
    }

    public ChiTietHoaDon getMaHD(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ?";
        list = getData(sql, maHD);
        return list.get(0);
    }
    public List<ChiTietHoaDon> getListMaHD(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ?";
        list = getData(sql, maHD);
        return list;
    }

    public List<ChiTietHoaDon> getData(String sql, String... args) {

        List<ChiTietHoaDon> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, args);

        while (cursor.moveToNext()) {
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setIdCTHD(cursor.getInt(cursor.getColumnIndex(ChiTietHoaDon.TB_COL_ID)));
            chiTietHoaDon.setMaHD(cursor.getString(cursor.getColumnIndex(ChiTietHoaDon.TB_COL_ID_HD)));
            chiTietHoaDon.setMaSP(cursor.getString(cursor.getColumnIndex(ChiTietHoaDon.TB_COL_ID_SP)));
            chiTietHoaDon.setSoLuong(cursor.getInt(cursor.getColumnIndex(ChiTietHoaDon.TB_COL_AMOUNT)));
            chiTietHoaDon.setGiamGia(cursor.getInt(cursor.getColumnIndex(ChiTietHoaDon.TB_COL_SALE)));
            chiTietHoaDon.setDonGia(cursor.getDouble(cursor.getColumnIndex(ChiTietHoaDon.TB_COL_PRICE)));
            chiTietHoaDon.setBaoHanh(cursor.getInt(cursor.getColumnIndex(ChiTietHoaDon.TB_COL_BH)));

            list.add(chiTietHoaDon);
        }
        return list;
    }

    public int getDoanhThu(String startDay, String endDay) {
        String doanhThu = "SELECT SUM(donGia) AS doanhThu FROM ChiTietHoaDon WHERE ngay >= ? AND ngay <= ?";
        Cursor cursor = database.rawQuery(doanhThu, new String[]{startDay, endDay});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public List<ChiTietHoaDon> getDoanhThuCT(String startDay, String endDay) {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE ngay >= ? AND ngay <= ? ORDER BY ngay";
        List<ChiTietHoaDon> list = getData(sql, startDay, endDay);
        return list;
    }
}

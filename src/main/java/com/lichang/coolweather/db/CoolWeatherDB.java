package com.lichang.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lichang.coolweather.model.City;
import com.lichang.coolweather.model.County;
import com.lichang.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichang on 2016/7/4.
 */
public class CoolWeatherDB {

    public static final String DB_NAME = "cool_weather";

    public static final int VERSION = 1;
    private  SQLiteDatabase db;
    private static CoolWeatherDB coolWeatherDB;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper openHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = openHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /*
    将省份实例存到数据库
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    /*
    加载全部省份信息
     */
    public  List<Province> loadProvinces() {
        List<Province> provinces = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
            } while (cursor.moveToNext());
        }
        return provinces;
    }

    /*
    将City实例存放到数据库
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    /*
    读取某个省份的城市信息
     */
    public List<City> loadCities(int provinceId) {
        List<City> cities = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                cities.add(city);
            } while (cursor.moveToNext());
        }
        return cities;
    }

    /*
    将County实例存到数据库
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            db.insert("County", null, values);
        }
    }

    /*
    加载某个City的所有乡镇信息
     */
    public List<County> loadCounties(int cityId) {
        List<County> counties = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                counties.add(county);
            } while (cursor.moveToNext());
        }
        return counties;
    }
}

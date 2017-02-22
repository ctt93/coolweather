package db;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.Country;
import model.Provice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
    //���ݿ���
	public static final String DB_NAME="cool_weather";
	
    //���ݿ�汾
	public static final int version=1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;

	//���췽��˽�л�
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context, DB_NAME, null, version);
		db=dbHelper.getWritableDatabase();
	}
	
	//��ȡCoolWeatherDBʵ��
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
		
	}
	
	//��Proviceʵ���洢�����ݿ�
	public void saveProvice(Provice provice){
		if(provice!=null){
			ContentValues values=new ContentValues();
			values.put("provice_name", provice.getProviceName());
			values.put("provice_code",provice.getProviceCode());
			
			db.insert("Provice", null, values);
		}
	}
	
	
	//�����ݿ��ȡȫ������ʡ��Ϣ
	public List<Provice> loadProvices(){
		List<Provice> list=new ArrayList<Provice>();
		Cursor cursor=db
			.query("Provice", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Provice provice=new Provice();
				provice.setId(cursor.getInt(cursor.getColumnIndex("id")));
				provice.setProviceName(cursor.getString(cursor.getColumnIndex("provice_name")));
				provice.setProviceCode(cursor.getString(cursor.getColumnIndex("provice_code")));
				list.add(provice);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	//��Cityʵ���洢
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("provice_id", city.getProviceId());
			db.insert("City", null, values);
		}
	}
	
	//�����ݿ��ȡʡ����ĳ�����Ϣ
	public List<City> loadCities(int proviceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db
			.query("City", null, "provice_id=?", new String[]{String.valueOf(proviceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	//��Countryʵ���洢
		public void saveCountry(Country country){
			if(country!=null){
				ContentValues values=new ContentValues();
				values.put("country_name", country.getCountryName());
				values.put("country_code", country.getCountryCode());
				values.put("city_id", country.getCityId());
				db.insert("Country", null, values);
			}
		}
	
		//�����ݿ��ȡ�������������Ϣ
		public List<Country> loadCounties(int cityId){
			List<Country> list=new ArrayList<Country>();
			Cursor cursor=db
				.query("Country", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
			if(cursor.moveToFirst()){
				do{
					Country country=new Country();
					country.setId(cursor.getInt(cursor.getColumnIndex("id")));
					country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
					country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
					list.add(country);
				}while(cursor.moveToNext());
			}
			if(cursor!=null){
				cursor.close();
			}
			return list;
		}
	
	
	
	
	
	
	
	
	
	
	
	
}

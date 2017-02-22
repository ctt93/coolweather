package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	//Provice表建表语句
	public static final String CREATE_PROVICE="create table Provice ("
			+"id integer primary key autoincrement, "
			+"provice_name text, "
			+"provice_code text)";
	//City表
	public static final String CREATE_CITY="create table City ("
			+"id integer primary key autoincrement, "
			+"city_name text, "
			+"city_code text, "
			+"provice_id integer)";
	//Country表
	public static final String CREATE_COUNTRY="create table Country ("
			+"id integer primary key autoincrement, "
			+"country_name text, "
			+"country_code text, "
			+"city_id integer)";
	
	

	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(CREATE_PROVICE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}

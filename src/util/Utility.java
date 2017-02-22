package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import model.City;
import model.Country;
import model.Provice;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import db.CoolWeatherDB;

public class Utility {

	//�����ʹ����������ʡ������
	public synchronized static boolean handleProvicesResponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvices=response.split(",");
			if(allProvices!=null&&allProvices.length>0){
				for(String p : allProvices){
					String[] array=p.split("\\|");
					Provice provice=new Provice();
					provice.setProviceCode(array[0]);
					provice.setProviceName(array[1]);
					coolWeatherDB.saveProvice(provice);
							
				}
				return true;
			}
		}
		return false;
	}
	
	//�����ʹ����м�����
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int proviceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c : allCities){
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProviceId(proviceId);
					coolWeatherDB.saveCity(city);
							
				}
				return true;
			}
		}
		return false;
	}
	
	//�����ʹ����ؼ�����
		public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
			if(!TextUtils.isEmpty(response)){
				String[] allCounties=response.split(",");
				if(allCounties!=null&&allCounties.length>0){
					for(String c : allCounties){
						String[] array=c.split("\\|");
						Country country=new Country();
						country.setCountryCode(array[0]);
						country.setCountryName(array[1]);
						country.setCityId(cityId);
						coolWeatherDB.saveCountry(country);
								
					}
					return true;
				}
			}
			return false;
		}
	
	
	//����Json���ݣ����������������ݴ洢������
		public static void handleWeatherResponse(Context context,String response){
			try{
				JSONObject jsonObject=new JSONObject(response);
				JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
				String cityName=weatherInfo.getString("city");
				String weatherCode=weatherInfo.getString("cityid");
				String temp1=weatherInfo.getString("temp1");
				String temp2=weatherInfo.getString("temp2");
				String weatherDesp=weatherInfo.getString("weather");
				String publishTime=weatherInfo.getString("ptime");
				saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
				
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
//�����������ص�������Ϣ�洢��SharedPreferences�ļ���
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
	
	
	
	
}

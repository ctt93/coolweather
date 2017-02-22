package util;

import model.City;
import model.Country;
import model.Provice;
import android.text.TextUtils;
import db.CoolWeatherDB;

public class Utility {

	//解析和处理服务器的省级数据
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
	
	//解析和处理市级数据
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int proviceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String p : allCities){
					String[] array=p.split("\\|");
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
	
	//解析和处理县级数据
		public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
			if(!TextUtils.isEmpty(response)){
				String[] allCounties=response.split(",");
				if(allCounties!=null&&allCounties.length>0){
					for(String p : allCounties){
						String[] array=p.split("\\|");
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
	
	
	
	
	
	
	
}

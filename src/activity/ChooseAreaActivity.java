package activity;

import java.util.ArrayList;
import java.util.List;

import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

import com.example.coolweather.R;

import model.City;
import model.Country;
import model.Provice;

import db.CoolWeatherDB;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVICE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTRY=2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList=new ArrayList<String>();
	//ʡ�б�
	private List<Provice> proviceList;
	//���б�
	private List<City> cityList;
	//���б�
	private List<Country> countryList;
	
	private Provice selectedProvice;
	private City selectedCity;
	private Country selectedCountry;
	
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView=(ListView)findViewById(R.id.list_view);
		titleText=(TextView)findViewById(R.id.title_text);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if(currentLevel==LEVEL_PROVICE){
					selectedProvice=proviceList.get(index);
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					selectedCity=cityList.get(index);
					queryCounties();
				}
			}

			
			
		});
		queryProvices();
	}

	

	private void queryProvices() {
		// TODO Auto-generated method stub
		proviceList=coolWeatherDB.loadProvices();
		if(proviceList.size()>0){
			dataList.clear();
			for(Provice provice : proviceList){
				dataList.add(provice.getProviceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel=LEVEL_PROVICE;
		}else{
			queryFromServer(null,"provice");
		}
	}
	
	private void queryCities() {
		// TODO Auto-generated method stub
		cityList=coolWeatherDB.loadCities(selectedProvice.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city : cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvice.getProviceName());
			currentLevel=LEVEL_CITY;
		}else{
			queryFromServer(selectedProvice.getProviceCode(),"city");
		}
	}
	
	protected void queryCounties() {
		// TODO Auto-generated method stub
		countryList=coolWeatherDB.loadCounties(selectedCity.getId());
		if(countryList.size()>0){
			dataList.clear();
			for(Country country : countryList){
				dataList.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTRY;
		}else{
			queryFromServer(selectedCity.getCityCode(),"country");
		}
	}
	
	private void queryFromServer(final String code,final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
			
		}else{
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address,new HttpCallbackListener(){
			@Override
			public void onFinish(String response){
				boolean result=false;
				if("provice".equals(type)){
					result=Utility.handleProvicesResponse(coolWeatherDB, response);
				}else if("city".equals(type)){
					result=Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvice.getId());
				}else if("country".equals(type)){
					result=Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				if(result){
					//ͨ��runOnUiThread()�����ص����̴߳����߼�
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("provice".equals(type)){
								queryProvices();
							}else if("city".equals(type)){
								queryCities();
							}else if("country".equals(type)){
								queryCounties();
							}
						}
						
					});
				}
			}
			
			@Override
			public void onError(Exception e){
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this,"����ʧ��", Toast.LENGTH_SHORT).show();
					}
					
				});
			}
			
			
		});
		
		
	}
	
	
	//��ʾ���ȶԻ���
	private void showProgressDialog(){
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("���ڼ��ء�����");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	//�رս��ȶԻ���
	private void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
	}
	
	
	@Override
	public void onBackPressed(){
		if(currentLevel==LEVEL_COUNTRY){
			queryCities();
		}else if(currentLevel==LEVEL_CITY){
			queryProvices();
		}else{
			finish();
		}
	}
	
	
	
}

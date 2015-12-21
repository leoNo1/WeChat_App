package org.baosight.util;

import java.util.ArrayList;
import java.util.List;

import org.baosight.message.resp.Article;
import org.baosight.pojo.WeatherData;
import org.baosight.pojo.WeatherResp;

import com.google.gson.Gson;

/**
 * 天气查询
 * @author tangbin
 * @date 2015-12-15
 * */
public class WeatherUtil {
	public static String queryWeather(String cityName){
	//请求地址	
	String requestUrl = 
"http://api.map.baidu.com/telematics/v3/weather?location={LOCATION}&output=json&ak={AK}";	
	requestUrl=	requestUrl.replace("{LOCATION}", CommonUtil.urlEncodeUTF8(cityName));
	requestUrl =requestUrl.replace("{AK}", "t1XcUE2KI7oeiNAqlqDxw7ja");
	
	//调用接口获取天气
	String respJSON = CommonUtil.httpRequest(requestUrl, "GET", null);
	
	Gson gson = new Gson();
	WeatherResp weatherResp = gson.fromJson(respJSON, WeatherResp.class);
	
	
	//解析返回结果，拼装返回给用户的天气数据
	String currentCity = weatherResp.getResults().get(0).getCurrentCity();
	List<WeatherData> weather_Data = weatherResp.getResults().get(0).getWeather_data();
	StringBuffer buffer = new StringBuffer();
	
	buffer.append(currentCity).append("天气预报").append("\n\n");
	for(WeatherData data:weather_Data){
		
		buffer.append(data.getDate()).append("  ")
		.append(data.getTemperature()).append(",");
		buffer.append(data.getWeather()).append(",");
		buffer.append(data.getWind()).append("\n");
	}
	
	
	return buffer.toString();
			
	}
	
	
	
	public static List<Article> queryWeatherList(String cityName){
	//请求地址	
	String requestUrl = 
"http://api.map.baidu.com/telematics/v3/weather?location={LOCATION}&output=json&ak={AK}";	
	requestUrl=	requestUrl.replace("{LOCATION}", CommonUtil.urlEncodeUTF8(cityName));
	requestUrl =requestUrl.replace("{AK}", "t1XcUE2KI7oeiNAqlqDxw7ja");
	
	//调用接口获取天气
	String respJSON = CommonUtil.httpRequest(requestUrl, "GET", null);
	
	Gson gson = new Gson();
	WeatherResp weatherResp = gson.fromJson(respJSON, WeatherResp.class);
	
	
	//解析返回结果，拼装返回给用户的天气数据
	String currentCity = weatherResp.getResults().get(0).getCurrentCity();
	List<WeatherData> weather_Data = weatherResp.getResults().get(0).getWeather_data();

	
	List<Article> articleList = new ArrayList<Article>();
	//取得当日天气情况
	WeatherData  weatherData = weather_Data.get(0);
	Article article = new Article();
	article.setTitle(currentCity+" "+weatherData.getDate()+" "
			+weatherData.getWeather()+" "+weatherData.getWind());
	article.setDescription("");
	article.setPicUrl(ConfigUtil.PROJECT_ROOT+"/images/weather/"+getWeatherPic(weatherData.getDayPictureUrl()));
	article.setUrl("http://www.wls96121.com/");
	articleList.add(article);
	
	weather_Data.remove(0);//移除当天的天气
	//遍历未来3天的天气
	for(WeatherData data:weather_Data){
		article = new Article();
		article.setTitle(data.getDate()+" "+data.getTemperature()+" "
				+data.getWeather()+" "+data.getWind());
		article.setDescription("");
		article.setPicUrl(ConfigUtil.PROJECT_ROOT+"/images/weather/"+getWeatherPic(data.getDayPictureUrl()));
		article.setUrl("http://www.wls96121.com/");
		articleList.add(article);
	}
	
	return articleList;
			
	}
	
	private static String getWeatherPic(String picUrl){
		String result = picUrl.substring(picUrl.lastIndexOf("/")+1);
		result= "weather_"+result;
		return result;
		
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
System.out.println(queryWeather("上海"));

}
}

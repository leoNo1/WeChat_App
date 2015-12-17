package org.baosight.pojo;

public class WeatherData {
	
	private String date; //日期
	private String dayPictureUrl;
	private String nightPictureUrl;
	private String weather;//天气
	private String wind;//风力
	private String temperature;//温度
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDayPictureUrl() {
		return dayPictureUrl;
	}
	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}
	public String getNightPictureUrl() {
		return nightPictureUrl;
	}
	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	
}

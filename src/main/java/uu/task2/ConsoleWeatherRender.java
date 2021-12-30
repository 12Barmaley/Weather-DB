package uu.task2;

import uu.task2.entity.WeatherData;

import java.util.List;

public class ConsoleWeatherRender {

    List<WeatherData> weatherDataList;

    public ConsoleWeatherRender(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

    public void getWeatherFromDb() {
        for (WeatherData weatherData : weatherDataList) {
            System.out.println(weatherData);
        }
    }

}

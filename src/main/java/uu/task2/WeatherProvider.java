package uu.task2;

import uu.task2.enums.Periods;

import java.io.IOException;
import java.sql.SQLException;

public interface WeatherProvider {

    public void getWeather(Periods period) throws IOException, SQLException;
}

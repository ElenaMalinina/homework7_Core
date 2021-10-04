package lesson7;

import lesson7.entity.Weather;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface WeatherModel {
    void getWeather(String city, Period period) throws IOException, SQLException;
    List<Weather> getSavedToDB();
}

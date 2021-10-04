package lesson7;

import com.fasterxml.jackson.databind.ObjectMapper;
import lesson7.entity.Weather;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static java.lang.Math.round;

public class AccuweatherModel implements WeatherModel {
    //http://dataservice.accuweather.com/forecasts/v1/daily/5day/288968
    private static final String PROTOCOL = "https";
    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECASTS = "forecasts";
    private static final String VERSION = "v1";
    private static final String DAILY = "daily";
    private static final String ONE_DAY = "1day";
    private static final String FIVE_DAYS = "5day";
    private static final String API_KEY = "O06ZRrYCpieK1jhrn40ZGtOJYm0auGs9";
    private static final String API_KEY_QUERY_PROPERTY = "apikey";
    private static final String LOCATIONS = "locations";
    private static final String CITIES = "cities";
    private static final String AUTOCOMPLETE = "autocomplete";
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DataBaseRepository dataBaseRepository = new DataBaseRepository();

    @Override
    public void getWeather(String city, Period period) throws IOException, SQLException {
        //http://dataservice.accuweather.com/forecasts/v1/daily/5day/288968
        switch (period) {
            case NOW: {
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(ONE_DAY)
                        .addPathSegment(detectCityKey(city))
                        .addQueryParameter(API_KEY_QUERY_PROPERTY, API_KEY)
                        .build();
                Request request = new Request.Builder()
                        .url(httpUrl)
                        .build();
                Response oneDayForecastResponse = okHttpClient.newCall(request).execute();
                String weatherResponse = oneDayForecastResponse.body().string();
                String localDate = objectMapper.readTree(weatherResponse)
                        .at("/DailyForecasts")
                        .get(0).at("/Date")
                        .asText();
                double temperature = objectMapper.readTree(weatherResponse).at("/DailyForecasts")
                        .get(0)
                        .at("/Temperature/Maximum/Value")
                        .asDouble();
                temperature = round((temperature - 32.0) * 5 / 9.0 * 100.0) / 100.0;
                System.out.println("В " + city + " " + localDate.split("T")[0] + " температатура воздуха " + temperature + " C");
                //System.out.println(weatherResponse);

        }
                break;

            case FIVE_DAYS: {
                HttpUrl httpUrlFiveDays = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(FIVE_DAYS)
                        .addPathSegment(detectCityKey(city))
                        .addQueryParameter(API_KEY_QUERY_PROPERTY, API_KEY)
                        .build();
                Request requestFiveDays = new Request.Builder()
                        .url(httpUrlFiveDays)
                        .build();
                Response fiveDayForecastResponse = okHttpClient.newCall(requestFiveDays).execute();
                String weatherResponseFiveDays = fiveDayForecastResponse.body().string();
                int numberOfElements = objectMapper.readTree(weatherResponseFiveDays).at("/DailyForecasts").size();
                for (int i = 0; i < numberOfElements; i++) {

                    String localDate = objectMapper.readTree(weatherResponseFiveDays)
                            .at("/DailyForecasts")
                            .get(i)
                            .at("/Date")
                            .asText();
                    double temperature = objectMapper.readTree(weatherResponseFiveDays)
                            .at("/DailyForecasts")
                            .get(i)
                            .at("/Temperature/Maximum/Value")
                            .asDouble();
                    temperature = round((temperature - 32.0) * 5 / 9.0 * 100.0) / 100.0;
                    System.out.println("В " + city + " " + localDate.split("T")[0] + " температатура воздуха " + temperature + " C");
                    //System.out.println(weatherResponseFiveDays);
                }
        }
        break;
    }

        }

    @Override
    public List<Weather> getSavedToDB() {
        return dataBaseRepository.getSavedToDBList();
    }


//    @Override
//    public boolean saveWeather(Weather weather) throws SQLException {
//
//        return dataBaseRepository.saveWeather(weather);
//    }



    public void saveWeather(List<Weather> weatherList) {
        dataBaseRepository.saveWeather(weatherList);
    }

    public List<Weather> getSavedToDBList() {
        return dataBaseRepository.getSavedToDBList();
    }

    private String detectCityKey(String city) throws IOException {
        //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PROTOCOL)
                .host(BASE_HOST)
                .addPathSegment(LOCATIONS)
                .addPathSegment(VERSION)
                .addPathSegment(CITIES)
                .addPathSegment(AUTOCOMPLETE)
                .addQueryParameter(API_KEY_QUERY_PROPERTY, API_KEY)
                .addQueryParameter("q", city)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String locationResponseString = response.body().string();
        //System.out.println(locationResponseString);
        String cityKey = objectMapper.readTree(locationResponseString).get(0).at("/Key").asText();
        //System.out.println(cityKey);
        return cityKey;
    }
        }

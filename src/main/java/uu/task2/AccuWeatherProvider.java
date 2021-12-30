package uu.task2;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import uu.task2.dto.WeatherResponse;
import uu.task2.dto.WeatherResponseFiveDay;
import uu.task2.dto.WeatherResponseFiveDayHeader;
import uu.task2.entity.WeatherData;
import uu.task2.enums.Periods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccuWeatherProvider implements WeatherProvider {

    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECAST_ENDPOINT = "forecasts";
    private static final String CURRENT_CONDITIONS_ENDPOINT = "currentconditions";
    private static final String API_VERSION = "v1";

    private static final String FORECAST_TYPE = "daily";
    private static final String FORECAST_PERIOD = "5day";

    private static final String API_KEY = ApplicationGlobalState.getInstance().getApiKey();

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String cityName;

    private WeatherData weatherData = new WeatherData();
    private DatabaseRepositorySQLiteImpl databaseRepositorySQLite = new DatabaseRepositorySQLiteImpl();

    @Override
    public void getWeather(Periods periods) throws IOException, SQLException {
        String cityKey = detectCityKey();
        if (periods.equals(Periods.NOW)) {
            HttpUrl url1 = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(CURRENT_CONDITIONS_ENDPOINT)
                    .addPathSegment(API_VERSION)
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", API_KEY)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url1)
                    .build();

            ResponseBody responseBody = client.newCall(request).execute().body();
            String jsonResponse = responseBody.string();

            List<WeatherResponse> weatherResponseList = objectMapper.readValue(jsonResponse, new TypeReference<List<WeatherResponse>>() {
            });

            System.out.print("Response 1 day: ");
            System.out.print("В городе " + cityName + " ");
            System.out.println(weatherResponseList.get(0));

            weatherData.setLocalDate(weatherResponseList.get(0).getDate());
            weatherData.setTemperature(weatherResponseList.get(0).getTemperature().getMetric().getValue());
            weatherData.setText(weatherResponseList.get(0).getWeatherText());

            databaseRepositorySQLite = new DatabaseRepositorySQLiteImpl();
            databaseRepositorySQLite.saveWeatherData(weatherData);

            //  TODO: Сделать в рамках д/з вывод более приятным для пользователя.
            //  Создать класс WeatherResponse, десериализовать ответ сервера в экземпляр класса
            //  Вывести пользователю только текущую температуру в C и сообщение (weather text)
        }
        if (periods.equals(Periods.FIVE_DAYS)) {
            HttpUrl url2 = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(FORECAST_ENDPOINT)
                    .addPathSegment(API_VERSION)
                    .addPathSegment(FORECAST_TYPE)
                    .addPathSegment(FORECAST_PERIOD)
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "ru-ru")
                    .addQueryParameter("metric", "true")

                    .build();

            Request request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url2)
                    .build();

            ResponseBody responseBody = client.newCall(request).execute().body();
            String jsonResponse = responseBody.string();

            JsonNode jsonNodeHeadline = objectMapper.readTree(jsonResponse).at("/Headline");

            WeatherResponseFiveDayHeader weatherResponseFiveDayHeader = objectMapper.readValue(jsonNodeHeadline.toString(), WeatherResponseFiveDayHeader.class);
            System.out.println(weatherResponseFiveDayHeader);

            JsonNode jsonNodeDailyForecasts = objectMapper.readTree(jsonResponse).at("/DailyForecasts");

            List<WeatherResponseFiveDay> weatherResponseList = objectMapper.readValue(jsonNodeDailyForecasts.toString(), new TypeReference<List<WeatherResponseFiveDay>>() {
            });

            databaseRepositorySQLite.getConnection().setAutoCommit(false);

            for (WeatherResponseFiveDay weatherResponseFiveDay : weatherResponseList) {
                System.out.println(weatherResponseFiveDay);

                weatherData.setLocalDate(weatherResponseFiveDay.getDate());
                weatherData.setTemperature(weatherResponseFiveDay.getTemperature().getMax().getValue());
                weatherData.setText(weatherResponseFiveDay.getDay().getDay());
                databaseRepositorySQLite.saveWeatherData(weatherData);
            }

            databaseRepositorySQLite.getConnection().commit();
        }
    }

    public String detectCityKey() throws IOException {
        String selectedCity = ApplicationGlobalState.getInstance().getSelectedCity();

        HttpUrl detectLocationURL = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment("locations")
                .addPathSegment(API_VERSION)
                .addPathSegment("cities")
                .addPathSegment("autocomplete")
                .addQueryParameter("apikey", API_KEY)
                .addQueryParameter("q", selectedCity)
                .build();

        Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(detectLocationURL)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Невозможно прочесть информацию о городе. " +
                    "Код ответа сервера = " + response.code() + " тело ответа = " + response.body().string());
        }
        String jsonResponse = response.body().string();
        System.out.println("Произвожу поиск города " + selectedCity);

        if (objectMapper.readTree(jsonResponse).size() > 0) {
            cityName = objectMapper.readTree(jsonResponse).get(0).at("/LocalizedName").asText();
            String countryName = objectMapper.readTree(jsonResponse).get(0).at("/Country/LocalizedName").asText();
            System.out.println("Найден город " + cityName + " в стране " + countryName);
            weatherData.setCity(cityName);
        } else throw new IOException("Server returns 0 cities");

        return objectMapper.readTree(jsonResponse).get(0).at("/Key").asText();
    }
}

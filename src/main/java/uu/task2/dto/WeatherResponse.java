package uu.task2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonRootName(value = "Прогноз погоды на 5 дней")
public class WeatherResponse {

    private String city;

    @JsonProperty(value = "LocalObservationDateTime")
    private String date;

    @JsonProperty(value = "WeatherText")
    private String weatherText;

    @JsonProperty(value = "Temperature")
    private Temperature temperature;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "на дату " + date.split("T")[0] +
                " ожидается " + weatherText +
                ", температура: " + temperature.getMetric().getValue() + temperature.getMetric().getUnit();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Temperature {

        @JsonProperty(value = "Metric")
        private Metric metric;

        public Metric getMetric() {
            return metric;
        }

        public void setMetric(Metric metric) {
            this.metric = metric;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Metric {

            @JsonProperty(value = "Value")
            private String value;

            @JsonProperty(value = "Unit")
            private String unit;

            public Metric() {
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }
        }

    }

}


////В городе CITY на дату DATE ожидается WEATHER_TEXT, температура - TEMPERATURE
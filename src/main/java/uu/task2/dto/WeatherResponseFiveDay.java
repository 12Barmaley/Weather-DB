package uu.task2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseFiveDay {

    @JsonProperty(value = "Date")
    private String date;

    @JsonProperty(value = "Temperature")
    private Temperature temperature;

    @JsonProperty(value = "Day")
    private Day day;

    public WeatherResponseFiveDay() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Temperature {

        @JsonProperty(value = "Minimum")
        private Minimum min;

        @JsonProperty(value = "Maximum")
        private Maximum max;

        public Temperature() {
        }

        public Minimum getMin() {
            return min;
        }

        public void setMin(Minimum min) {
            this.min = min;
        }

        public Maximum getMax() {
            return max;
        }

        public void setMax(Maximum max) {
            this.max = max;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Minimum {

            @JsonProperty(value = "Value")
            private String value;

            public Minimum() {
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Maximum {

            @JsonProperty(value = "Value")
            private String value;

            public Maximum() {
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Day {

        @JsonProperty(value = "IconPhrase")
        private String day;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }



    @Override
    public String toString() {
        return  date.split("T")[0]  +
                " [" +
                temperature.getMax().getValue() +
                "..." + temperature.getMin().getValue() +
                "] " +
                day.getDay()
                ;
    }
}

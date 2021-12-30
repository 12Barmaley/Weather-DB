package uu.task2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseFiveDayHeader {

    @JsonProperty(value = "Text")
    private String text;

    @JsonProperty(value = "EffectiveDate")
    private String startDate;

    @JsonProperty(value = "EndDate")
    private String endDate;

    @Override
    public String toString() {
        return "Прогноз на пять дней. " + text;
    }
}

package uu.task2.entity;


public class WeatherData {

    private String city;
    private String localDate;
    private String text;
    private String temperature;

    public WeatherData() {
    }

    public WeatherData(String city, String localDate, String text, String temperature) {
        this.city = city;
        this.localDate = localDate;
        this.text = text;
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return
                "В городе: '" + city + '\'' +
                ", на дату: '" + localDate + '\'' +
                " " + text + '\'' +
                ", температура: '" + temperature;
    }
}

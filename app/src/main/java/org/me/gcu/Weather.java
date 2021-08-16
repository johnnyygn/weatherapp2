package org.me.gcu;

public class Weather {

    private String city;
    private String date;
    private String temperature;
    private String wind;
    private String pressure;
    private String humidity;
    private String polution;
    private String icon;
    private String sunrise;
    private String sunset;
    private String uv;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSunrise() {
        return this.sunrise;
    }

    public void setSunrise(String dateString) {
        this.sunrise = dateString;
    }


    public String getSunset() {
        return this.sunset;
    }

    public void setSunset(String dateString) {
        this.sunset = dateString;
    }

    public String getUv() {
        return this.uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String dateString) {
        this.date = dateString;
    }

    public String getPolution() {
        return polution;
    }

    public void setPolution(String polution) {
        this.polution = polution;
    }
}

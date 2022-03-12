package com.example.weather.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OneCallObject {

    public static OneCallObject getObject(JSONObject jsonObject) throws JSONException {
        OneCallObject o = new OneCallObject();
        o.setLat(jsonObject.getDouble("lat"));
        o.setLon(jsonObject.getDouble("lon"));
        o.setTimezone(jsonObject.getString("timezone"));
        o.setTimezone_offset(jsonObject.getInt("timezone_offset"));
        ArrayList<OneCallObject.Weather> cWeather = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONObject("current").getJSONArray("weather").length();i++){
            OneCallObject.Weather weather = new OneCallObject.Weather(
                    jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(i).getInt("id"),
                    jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(i).getString("main"),
                    jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(i).getString("description"),
                    jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(i).getString("icon")
            );
            cWeather.add(weather);
        }
        o.setCurrent(new OneCallObject.Current(
                jsonObject.getJSONObject("current").getLong("dt"),
                jsonObject.getJSONObject("current").getLong("sunrise"),
                jsonObject.getJSONObject("current").getLong("sunset"),
                jsonObject.getJSONObject("current").getDouble("temp"),
                jsonObject.getJSONObject("current").getDouble("feels_like"),
                jsonObject.getJSONObject("current").getInt("pressure"),
                jsonObject.getJSONObject("current").getInt("humidity"),
                jsonObject.getJSONObject("current").getDouble("dew_point"),
                jsonObject.getJSONObject("current").getDouble("uvi"),
                jsonObject.getJSONObject("current").getInt("clouds"),
                jsonObject.getJSONObject("current").getInt("visibility"),
                jsonObject.getJSONObject("current").getInt("wind_speed"),
                jsonObject.getJSONObject("current").getInt("wind_deg"),
                cWeather
        ));
        ArrayList<OneCallObject.Minutely> minutelies = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("minutely").length();i++){
            OneCallObject.Minutely minutely = new OneCallObject.Minutely(
                    jsonObject.getJSONArray("minutely").getJSONObject(i).getLong("dt"),
                    jsonObject.getJSONArray("minutely").getJSONObject(i).getDouble("precipitation")
            );
            minutelies.add(minutely);
        }
        o.setMinutely(minutelies);
        ArrayList<ArrayList<OneCallObject.Weather>> hWeather = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("hourly").length();i++){
            ArrayList<OneCallObject.Weather> weather1 = new ArrayList<>();
            for (int j = 0; j < jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").length(); j++) {
                OneCallObject.Weather weather = new OneCallObject.Weather(
                        jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getInt("id"),
                        jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("main"),
                        jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("description"),
                        jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("icon")
                );
                weather1.add(weather);
            }
            hWeather.add(weather1);
        }
        ArrayList<OneCallObject.Hourly> hourlies = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("hourly").length();i++){
            OneCallObject.Hourly hourly = new OneCallObject.Hourly(
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getLong("dt"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("temp"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("feels_like"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("pressure"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("humidity"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("dew_point"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("uvi"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("clouds"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("visibility"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("wind_speed"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("wind_deg"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("wind_gust"),
                    hWeather.get(i),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("pop")
            );
            hourlies.add(hourly);
        }
        o.setHourly(hourlies);
        ArrayList<ArrayList<OneCallObject.Weather>> dWeather = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("daily").length();i++){
            ArrayList<OneCallObject.Weather> weather1 = new ArrayList<>();
            for (int j = 0; j <jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").length(); j++) {
                OneCallObject.Weather weather = new OneCallObject.Weather(
                        jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getInt("id"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("main"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("description"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("icon")
                );
                weather1.add(weather);
            }
            dWeather.add(weather1);
        }
        ArrayList<OneCallObject.Daily> dailies = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("daily").length();i++){
            try {
                OneCallObject.Daily daily = new OneCallObject.Daily(
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("dt"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("sunrise"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("sunset"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("moonrise"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("moonset"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("moon_phase"),
                        new OneCallObject.Temp(
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("day"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("min"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("max"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("night"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("eve"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("morn")
                        ),
                        new OneCallObject.FeelsLike(
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("day"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("night"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("eve"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("morn")
                        ),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("pressure"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("humidity"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("dew_point"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("wind_speed"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("wind_deg"),
                        dWeather.get(i),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("clouds"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("pop"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("rain"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("uvi")
                );
                dailies.add(daily);
            }catch (Exception e){
                OneCallObject.Daily daily = new OneCallObject.Daily(
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("dt"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("sunrise"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("sunset"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("moonrise"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("moonset"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("moon_phase"),
                        new OneCallObject.Temp(
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("day"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("min"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("max"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("night"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("eve"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("morn")
                        ),
                        new OneCallObject.FeelsLike(
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("day"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("night"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("eve"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("morn")
                        ),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("pressure"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("humidity"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("dew_point"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("wind_speed"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("wind_deg"),
                        dWeather.get(i),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("clouds"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("pop"),
                        0,
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("uvi")
                );
                dailies.add(daily);
            }
        }
        o.setDaily(dailies);
        try {
            ArrayList<OneCallObject.Alerts> alerts = new ArrayList<>();
            ArrayList<ArrayList<String>> tags = new ArrayList<>();
            for (int i = 0;i<jsonObject.getJSONArray("alerts").length();i++){
                ArrayList<String> tag = new ArrayList<>();
                for (int j = 0;j<jsonObject.getJSONArray("alerts").getJSONObject(i).getJSONArray("tags").length();j++){
                    tag.add(jsonObject.getJSONArray("alerts").getJSONObject(i).getJSONArray("tags").getString(j));
                }
                tags.add(tag);
            }
            for (int i = 0;i<jsonObject.getJSONArray("alerts").length();i++){
                OneCallObject.Alerts alert = new OneCallObject.Alerts(
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getString("sender_name"),
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getString("event"),
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getLong("start"),
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getLong("end"),
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getString("description"),
                        tags.get(i)
                );
                alerts.add(alert);
            }
            o.setAlerts(alerts);
        }catch (Exception e){
            o.setAlerts(null);
        }
        return o;
    }


    public static class Current{
        private long dt;
        private long sunrise;
        private long sunset;
        private double temp;
        private double feels_like;
        private int pressure;
        private int humidity;
        private double dew_point;
        private double uvi;
        private int clouds;
        private int visibility;
        private int wind_speed;
        private int wind_deg;
        private ArrayList<Weather> weather;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getDew_point() {
            return dew_point;
        }

        public void setDew_point(double dew_point) {
            this.dew_point = dew_point;
        }

        public double getUvi() {
            return uvi;
        }

        public void setUvi(double uvi) {
            this.uvi = uvi;
        }

        public int getClouds() {
            return clouds;
        }

        public void setClouds(int clouds) {
            this.clouds = clouds;
        }

        public int getVisibility() {
            return visibility;
        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public int getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(int wind_speed) {
            this.wind_speed = wind_speed;
        }

        public int getWind_deg() {
            return wind_deg;
        }

        public void setWind_deg(int wind_deg) {
            this.wind_deg = wind_deg;
        }

        public ArrayList<Weather> getWeather() {
            return weather;
        }

        public void setWeather(ArrayList<Weather> weather) {
            this.weather = weather;
        }

        public Current(long dt, long sunrise, long sunset, double temp, double feels_like, int pressure, int humidity, double dew_point, double uvi, int clouds, int visibility, int wind_speed, int wind_deg, ArrayList<Weather> weather) {
            this.dt = dt;
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.temp = temp;
            this.feels_like = feels_like;
            this.pressure = pressure;
            this.humidity = humidity;
            this.dew_point = dew_point;
            this.uvi = uvi;
            this.clouds = clouds;
            this.visibility = visibility;
            this.wind_speed = wind_speed;
            this.wind_deg = wind_deg;
            this.weather = weather;
        }
    }


    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Weather(int id, String main, String description, String icon) {
            this.id = id;
            this.main = main;
            this.description = description;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return "Weather{" +
                    "id=" + id +
                    ", main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }

    public static class Hourly{
        private long dt;
        private double temp;
        private double feels_like;
        private int pressure;
        private int humidity;
        private double dew_point;
        private double uvi;
        private int clouds;
        private int visibility;
        private double wind_speed;
        private int wind_deg;
        private double wind_gust;
        private ArrayList<Weather> weather;
        private int pop;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getDew_point() {
            return dew_point;
        }

        public void setDew_point(double dew_point) {
            this.dew_point = dew_point;
        }

        public double getUvi() {
            return uvi;
        }

        public void setUvi(double uvi) {
            this.uvi = uvi;
        }

        public int getClouds() {
            return clouds;
        }

        public void setClouds(int clouds) {
            this.clouds = clouds;
        }

        public int getVisibility() {
            return visibility;
        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public double getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(double wind_speed) {
            this.wind_speed = wind_speed;
        }

        public int getWind_deg() {
            return wind_deg;
        }

        public void setWind_deg(int wind_deg) {
            this.wind_deg = wind_deg;
        }

        public double getWind_gust() {
            return wind_gust;
        }

        public void setWind_gust(double wind_gust) {
            this.wind_gust = wind_gust;
        }

        public ArrayList<Weather> getWeather() {
            return weather;
        }

        public void setWeather(ArrayList<Weather> weather) {
            this.weather = weather;
        }

        public int getPop() {
            return pop;
        }

        public void setPop(int pop) {
            this.pop = pop;
        }

        public Hourly(long dt, double temp, double feels_like, int pressure, int humidity, double dew_point, double uvi, int clouds, int visibility, double wind_speed, int wind_deg, double wind_gust, ArrayList<Weather> weather, int pop) {
            this.dt = dt;
            this.temp = temp;
            this.feels_like = feels_like;
            this.pressure = pressure;
            this.humidity = humidity;
            this.dew_point = dew_point;
            this.uvi = uvi;
            this.clouds = clouds;
            this.visibility = visibility;
            this.wind_speed = wind_speed;
            this.wind_deg = wind_deg;
            this.wind_gust = wind_gust;
            this.weather = weather;
            this.pop = pop;
        }

        @Override
        public String toString() {
            return "Hourly{" +
                    "dt=" + dt +
                    ", temp=" + temp +
                    ", feels_like=" + feels_like +
                    ", pressure=" + pressure +
                    ", humidity=" + humidity +
                    ", dew_point=" + dew_point +
                    ", uvi=" + uvi +
                    ", clouds=" + clouds +
                    ", visibility=" + visibility +
                    ", wind_speed=" + wind_speed +
                    ", wind_deg=" + wind_deg +
                    ", wind_gust=" + wind_gust +
                    ", weather=" + weather +
                    ", pop=" + pop +
                    '}';
        }
    }

    public static class Minutely{
        private long dt;
        private double precipitation;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public double getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(double precipitation) {
            this.precipitation = precipitation;
        }

        public Minutely(long dt, double precipitation) {
            this.dt = dt;
            this.precipitation = precipitation;
        }

        @Override
        public String toString() {
            return "Minutely{" +
                    "dt=" + dt +
                    ", precipitation=" + precipitation +
                    '}';
        }
    }

    public static class Daily{
        private long dt;
        private long sunrise;
        private long sunset;
        private long moonrise;
        private long moonset;
        private double moon_phase;
        private Temp temp;
        private FeelsLike feels_like;
        private int pressure;
        private int humidity;
        private double dew_point;
        private double wind_speed;
        private int wind_deg;
        private ArrayList<Weather> weather;
        private int clouds;
        private double pop;
        private double rain;
        private double uvi;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }

        public long getMoonrise() {
            return moonrise;
        }

        public void setMoonrise(long moonrise) {
            this.moonrise = moonrise;
        }

        public long getMoonset() {
            return moonset;
        }

        public void setMoonset(long moonset) {
            this.moonset = moonset;
        }

        public double getMoon_phase() {
            return moon_phase;
        }

        public void setMoon_phase(double moon_phase) {
            this.moon_phase = moon_phase;
        }

        public Temp getTemp() {
            return temp;
        }

        public void setTemp(Temp temp) {
            this.temp = temp;
        }

        public FeelsLike getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(FeelsLike feels_like) {
            this.feels_like = feels_like;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getDew_point() {
            return dew_point;
        }

        public void setDew_point(double dew_point) {
            this.dew_point = dew_point;
        }

        public double getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(double wind_speed) {
            this.wind_speed = wind_speed;
        }

        public int getWind_deg() {
            return wind_deg;
        }

        public void setWind_deg(int wind_deg) {
            this.wind_deg = wind_deg;
        }

        public ArrayList<Weather> getWeather() {
            return weather;
        }

        public void setWeather(ArrayList<Weather> weather) {
            this.weather = weather;
        }

        public int getClouds() {
            return clouds;
        }

        public void setClouds(int clouds) {
            this.clouds = clouds;
        }

        public double getPop() {
            return pop;
        }

        public void setPop(double pop) {
            this.pop = pop;
        }

        public double getRain() {
            return rain;
        }

        public void setRain(double rain) {
            this.rain = rain;
        }

        public double getUvi() {
            return uvi;
        }

        public void setUvi(double uvi) {
            this.uvi = uvi;
        }

        public Daily(long dt, long sunrise, long sunset, long moonrise, long moonset, double moon_phase, Temp temp, FeelsLike feels_like, int pressure, int humidity, double dew_point, double wind_speed, int wind_deg, ArrayList<Weather> weather, int clouds, double pop, double rain, double uvi) {
            this.dt = dt;
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.moonrise = moonrise;
            this.moonset = moonset;
            this.moon_phase = moon_phase;
            this.temp = temp;
            this.feels_like = feels_like;
            this.pressure = pressure;
            this.humidity = humidity;
            this.dew_point = dew_point;
            this.wind_speed = wind_speed;
            this.wind_deg = wind_deg;
            this.weather = weather;
            this.clouds = clouds;
            this.pop = pop;
            this.rain = rain;
            this.uvi = uvi;
        }

        @Override
        public String toString() {
            return "Daily{" +
                    "dt=" + dt +
                    ", sunrise=" + sunrise +
                    ", sunset=" + sunset +
                    ", moonrise=" + moonrise +
                    ", moonset=" + moonset +
                    ", moon_phase=" + moon_phase +
                    ", temp=" + temp +
                    ", feels_like=" + feels_like +
                    ", pressure=" + pressure +
                    ", humidity=" + humidity +
                    ", dew_point=" + dew_point +
                    ", wind_speed=" + wind_speed +
                    ", wind_deg=" + wind_deg +
                    ", weather=" + weather +
                    ", clouds=" + clouds +
                    ", pop=" + pop +
                    ", rain=" + rain +
                    ", uvi=" + uvi +
                    '}';
        }
    }

    public static class Temp{
        private double day;
        private double min;
        private double max;
        private double night;
        private double eve;
        private double morn;

        public double getDay() {
            return day;
        }

        public void setDay(double day) {
            this.day = day;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }

        public double getNight() {
            return night;
        }

        public void setNight(double night) {
            this.night = night;
        }

        public double getEve() {
            return eve;
        }

        public void setEve(double eve) {
            this.eve = eve;
        }

        public double getMorn() {
            return morn;
        }

        public void setMorn(double morn) {
            this.morn = morn;
        }

        public Temp(double day, double min, double max, double night, double eve, double morn) {
            this.day = day;
            this.min = min;
            this.max = max;
            this.night = night;
            this.eve = eve;
            this.morn = morn;
        }

        @Override
        public String toString() {
            return "Temp{" +
                    "day=" + day +
                    ", min=" + min +
                    ", max=" + max +
                    ", night=" + night +
                    ", eve=" + eve +
                    ", morn=" + morn +
                    '}';
        }
    }

    public static class FeelsLike{
        private double day;
        private double night;
        private double eve;
        private double morn;

        public double getDay() {
            return day;
        }

        public void setDay(double day) {
            this.day = day;
        }

        public double getNight() {
            return night;
        }

        public void setNight(double night) {
            this.night = night;
        }

        public double getEve() {
            return eve;
        }

        public void setEve(double eve) {
            this.eve = eve;
        }

        public double getMorn() {
            return morn;
        }

        public void setMorn(double morn) {
            this.morn = morn;
        }

        public FeelsLike(double day, double night, double eve, double morn) {
            this.day = day;
            this.night = night;
            this.eve = eve;
            this.morn = morn;
        }

        @Override
        public String toString() {
            return "FeelsLike{" +
                    "day=" + day +
                    ", night=" + night +
                    ", eve=" + eve +
                    ", morn=" + morn +
                    '}';
        }
    }

    public static class Alerts{
        private String sender_name;
        private String event;
        private long start;
        private long end;
        private String description;
        private ArrayList<String> tags;


        public String getSender_name() {
            return sender_name;
        }

        public void setSender_name(String sender_name) {
            this.sender_name = sender_name;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ArrayList<String> getTags() {
            return tags;
        }

        public void setTags(ArrayList<String> tags) {
            this.tags = tags;
        }

        public Alerts(String sender_name, String event, long start, long end, String description, ArrayList<String> tags) {
            this.sender_name = sender_name;
            this.event = event;
            this.start = start;
            this.end = end;
            this.description = description;
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "Alerts{" +
                    "sender_name='" + sender_name + '\'' +
                    ", event='" + event + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    ", description='" + description + '\'' +
                    ", tags=" + tags +
                    '}';
        }
    }



    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;
    private Current current;
    private ArrayList<Minutely> minutely;
    private ArrayList<Hourly> hourly;
    private ArrayList<Daily> daily;
    private ArrayList<Alerts> alerts;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(int timezone_offset) {
        this.timezone_offset = timezone_offset;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public ArrayList<Minutely> getMinutely() {
        return minutely;
    }

    public void setMinutely(ArrayList<Minutely> minutely) {
        this.minutely = minutely;
    }

    public ArrayList<Hourly> getHourly() {
        return hourly;
    }

    public void setHourly(ArrayList<Hourly> hourly) {
        this.hourly = hourly;
    }

    public ArrayList<Daily> getDaily() {
        return daily;
    }

    public void setDaily(ArrayList<Daily> daily) {
        this.daily = daily;
    }

    public ArrayList<Alerts> getAlerts() {
        return alerts;
    }

    public void setAlerts(ArrayList<Alerts> alerts) {
        this.alerts = alerts;
    }

    @Override
    public String toString() {
        return "OneCallObject{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", timezone='" + timezone + '\'' +
                ", timezone_offset=" + timezone_offset +
                ", current=" + current +
                ", minutely=" + minutely +
                ", hourly=" + hourly +
                ", daily=" + daily +
                ", alerts=" + alerts +
                '}';
    }
}

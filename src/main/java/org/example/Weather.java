package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather {
    private String APIkey = "34e072219725530bc05a5ec67ee612b9"; // Здесь нужно вставить реальный API ключ
    private String weatherText;

    // Метод для подключения к Web-странице и получения с неё данных.
    public String getUrlContent(String urlAddress) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    content.append(inputLine);
                }
                reader.close();
            } else {
                System.err.println("HTTP Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    // Метод, который получает текущую погоду в указанном городе.
    public String getWeather(String city) {
        String output = getUrlContent(
                "https://api.openweathermap.org/data/2.5/weather?q="
                        + city
                        + "&appid=" + APIkey + "&units=metric"
        );

        if (!output.isEmpty()) {
            try {
                JSONObject object = new JSONObject(output);
                JSONArray weatherArray = object.getJSONArray("weather");
                JSONObject mainObject = object.getJSONObject("main");

                weatherText = "Погода в " + city + ":"
                        + "\n\nТемпература: " + mainObject.getInt("temp") + "℃"
                        + "\nОщущается: " + mainObject.getInt("feels_like") + "℃"
                        + "\nВлажность: " + mainObject.getInt("humidity") + "%"
                        + "\nДавление: " + mainObject.getInt("pressure") + " hPa";
            } catch (Exception e) {
                e.printStackTrace();
                weatherText = "Ошибка при обработке данных.";
            }
        } else {
            weatherText = "Город не найден!";
        }
        return weatherText;
    }
}

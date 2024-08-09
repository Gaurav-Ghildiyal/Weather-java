package com.weatherapp;
import java.util.Properties;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherApp {

    private static  String API_KEY ;  // Replace with your API key
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

    static {
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            API_KEY = prop.getProperty("api_key");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter the city");
        String city = sc.next();  // You can take this as input from the user
        String url = BASE_URL + city + "&appid=" + API_KEY + "&units=metric";
        fetchWeatherData(url);
    }

    public static void fetchWeatherData(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            double temp = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
            String weather = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

            System.out.println("Current temperature in " + jsonResponse.get("name").getAsString() + " is " + temp + "°C");
            System.out.println("Weather description: " + weather);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.nnedimkpa.greenhouse.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParseJSON {

    public ParseJSON() {
    }

    public ArrayList<Reading> readJson(String jsonString) throws JSONException {
        JSONObject readingsJsonObject = new JSONObject(jsonString);

        JSONArray feedsArray = readingsJsonObject.getJSONArray("feeds");

        ArrayList<Reading> allReadings = new ArrayList<>();
        for (int i = 0; i < feedsArray.length(); i++) {
            JSONObject feedArrayObject = feedsArray.getJSONObject(i);
            String date = feedArrayObject.getString("created_at");
            int timeIndex = date.indexOf("T");
            String time = date.substring(timeIndex+1, date.length() - 4);


            long innerTemp, humidity, light, waterLevel;

            if (feedArrayObject.getString("field1").equals("null")) innerTemp = 0;
            else innerTemp = feedArrayObject.getLong("field1");

            if (feedArrayObject.get("field2").equals("null")) humidity = 0;
            else humidity = feedArrayObject.getLong("field2");

            if (feedArrayObject.getString("field3").equals("null")) light = 0;
            else light = feedArrayObject.getLong("field3");

            if (feedArrayObject.getString("field4").equals("null")) waterLevel = 0;
            else waterLevel = feedArrayObject.getLong("field4");

            Reading reading = new Reading();
            reading.setId(i);
            reading.setDate(time);
            reading.setInnerTemperature(innerTemp);
            reading.setHumidity(humidity);
            reading.setLight(light);
            reading.setWaterLevel(waterLevel);
            allReadings.add(reading);

        }
        return allReadings;
    }

    public Reading readSingleObject(String jsonString) throws JSONException {
        Reading reading = new Reading();

        JSONObject jsonObject = new JSONObject(jsonString);
        String date = jsonObject.getString("created_at");
        long innerTemp = jsonObject.getLong("field1");
        long humidity = jsonObject.getLong("field2");
        long light = jsonObject.getLong("field3");
        long waterLevel = jsonObject.getLong("field4");

        reading.setDate(date);
        reading.setInnerTemperature(innerTemp);
        reading.setHumidity(humidity);
        reading.setLight(light);
        reading.setWaterLevel(waterLevel);

        return reading;
    }

}

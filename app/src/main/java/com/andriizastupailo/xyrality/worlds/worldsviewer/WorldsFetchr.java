package com.andriizastupailo.xyrality.worlds.worldsviewer;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Class for network connection
 */

public class WorldsFetchr {

    private  static final String TAG = "WorldsFetchr";

    private static final String LOGIN_KEY = "login";
    private static final String PASSWORD_KEY = "password";
    private static final String DEVICE_TYPE_KEY = "deviceType";
    private static final String DEVIDE_ID_KEY = "deviceId";
    private static final String ACCEPT_KEY_KEY = "accept";
    private static final String CONTENT_TYPE_KEY = "content-type";

    private static final String ACCEPT_KEY_VALUE = "application/json";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String URL_VALUE = "http://backend1.lordsandknights.com/XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds";


    public boolean fetchItems(String login, String password){
        String url = Uri.parse(URL_VALUE)
                .buildUpon()
                .appendQueryParameter(LOGIN_KEY, login)
                .appendQueryParameter(PASSWORD_KEY, password)
                .appendQueryParameter(DEVICE_TYPE_KEY, getDeviceType())
                .appendQueryParameter(DEVIDE_ID_KEY, getDeviceId())
                .build().toString();
        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            parseItems(jsonObject);
            return true;
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return false;
    }

    public String getUrlString(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty(ACCEPT_KEY_KEY, ACCEPT_KEY_VALUE);
        connection.setRequestProperty(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return new String(out.toByteArray());
        } finally {
            connection.disconnect();
        }
    }

    private void parseItems(JSONObject jsonBody) throws IOException, JSONException{
        JSONArray worldsArray = jsonBody.getJSONArray("allAvailableWorlds");
        final  WorldBank worldBank = WorldBank.get();
        worldBank.clearWorlds();
        List<World> worlds = worldBank.getWorlds();
        for(int i = 0; i < worldsArray.length(); i++){
            JSONObject item = worldsArray.getJSONObject(i);
            World worldItem = new World();
            worldItem.setCountry(item.getString("country"));
            worldItem.setId(item.getInt("id"));
            worldItem.setLanguage(item.getString("language"));
            worldItem.setMapUrl(item.getString("mapURL"));
            worldItem.setName(item.getString("name"));
            worldItem.setUrl(item.getString("url"));
            JSONObject worldStatus = item.getJSONObject("worldStatus");
            worldItem.setDescription(worldStatus.getString("description"));
            worldItem.setWorldId(worldStatus.getInt("id"));
            worlds.add(worldItem);
        }

        Log.i(TAG, "Receive " + worldsArray.length() + " world(s)");
    }

    private static String getDeviceType(){
        return String.format("%s %s", Build.MODEL, Build.VERSION.RELEASE);
    }

    private static String getDeviceId() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
}

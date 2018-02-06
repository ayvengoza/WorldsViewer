package com.andriizastupailo.xyrality.worlds.worldsviewer;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    private static final String LOGIN_VALUE = "android.test@xyrality.com";
    private static final String PASSWORD_VALUE = "password";
    private static final String DEVICE_TYPE_VALUE = "SM-A510F 7.0";
    private static final String DEVIDE_ID_VALUE = "B8:57:D8:FE:D9:AB";
    private static final String ACCEPT_KEY_VALUE = "application/json";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String URL_VALUE = "http://backend1.lordsandknights.com/XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds";


    public void fetchItems(){
        String url = Uri.parse(URL_VALUE)
                .buildUpon()
                .appendQueryParameter(LOGIN_KEY, LOGIN_VALUE)
                .appendQueryParameter(PASSWORD_KEY, PASSWORD_VALUE)
                .appendQueryParameter(DEVICE_TYPE_KEY, DEVICE_TYPE_VALUE)
                .appendQueryParameter(DEVIDE_ID_KEY, DEVIDE_ID_VALUE)
                .build().toString();
        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
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

}
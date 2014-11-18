package org.gkhighelf.vktokenrefresher.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gkhighelf on 18.11.14.
 */
public class NetworkUtils {
    private static final String LOG_KEY = "NetworkUtils";

    public static class Response {
        public int responseCode = 0;
        public String body;

        Response(int responseCode, String body) {
            this.responseCode = responseCode;
            this.body = body;
        }
    }

    public static Response LoadUrl(String link) {
        Response result = null;
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = getResponseCode(connection);
            String body = readFromInput(connection);
            result = new Response(responseCode, body);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return result;
    }

    private static String readFromInput(HttpURLConnection connection) {
        boolean readError = false;

        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = null;

        try {
            InputStream inputStream;
            try{
                inputStream = new BufferedInputStream(connection.getInputStream());
            } catch( Exception bis ) {
                inputStream = new BufferedInputStream(connection.getErrorStream());
            }

            InputStreamReader streamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(streamReader);

            stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            readError = true;
        } finally {
            closeStream(bufferedReader);
            connection.disconnect();
        }

        if (readError) {
            return null;
        }

        return stringBuilder.toString();
    }

    private static int getResponseCode(HttpURLConnection connection) {
        int responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {}
        return responseCode;
    }

    private static void closeStream(Closeable stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (IOException e) {
        }
    }
}

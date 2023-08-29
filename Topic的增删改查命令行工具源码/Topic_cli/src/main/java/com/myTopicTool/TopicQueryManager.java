package com.myTopicTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The TopicQueryManager class handles querying topic information using API calls.
 */
public class TopicQueryManager {

    private static String API_URL = "http://127.0.0.1:8080/webapi.htm";
    // Replace with your auth token
    private static String CONF_MOD_AUTH_TOKEN = "abc";

    public static void main(String[] args) {
        loadConfig();
        try {
            String queryResult = queryTopicInfo("testTopic");
            System.out.println("Query result:");
            System.out.println(queryResult);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error querying topic information.");
        }
    }

    /**
     * Queries topic information using the specified topic name.
     * @param topicName The name of the topic to query.
     * @return The response data from the query.
     * @throws IOException If an I/O error occurs.
     */
    public static String queryTopicInfo(String topicName) throws IOException {
        String apiUrl = API_URL + "?type=op_query&method=admin_query_topic_info";
        Map<String, String> apiParams = buildApiParams(topicName);

        return sendApiRequest(apiUrl, apiParams);
    }

    /**
     * Builds a map of API parameters with the provided topic name and authentication token.
     * @param topicName The name of the topic to query.
     * @return A map containing the API parameters.
     */
    private static Map<String, String> buildApiParams(String topicName) {
        Map<String, String> apiParams = new HashMap<>();
        apiParams.put("topicName", topicName);
        apiParams.put("confModAuthToken", CONF_MOD_AUTH_TOKEN);
        return apiParams;
    }

    /**
     * Sends an API request and returns the response data.
     * @param apiUrl The API URL for the request.
     * @param apiParams The API parameters.
     * @return The response data from the API request.
     * @throws IOException If an I/O error occurs.
     */
    private static String sendApiRequest(String apiUrl, Map<String, String> apiParams) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : apiParams.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(param.getKey());
                postData.append('=');
                postData.append(param.getValue());
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            connection.getOutputStream().write(postDataBytes);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("API request successful.");
                // Process and return the response data
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } else {
                String responseMessage = connection.getResponseMessage();
                System.err.println("Error in API request. Response code: " + responseCode + ", Response message: " + responseMessage);
                // Additional error handling logic here
                return null;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Loads configuration from a properties file.
     */
    public static void loadConfig() {
        try (InputStream input = TopicQueryManager.class.getResourceAsStream("/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            API_URL = prop.getProperty("api.url");
            CONF_MOD_AUTH_TOKEN = prop.getProperty("conf.mod.auth.token");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading configuration.");
        }
    }
}

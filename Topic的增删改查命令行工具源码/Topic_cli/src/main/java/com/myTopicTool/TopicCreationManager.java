package com.myTopicTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The TopicCreationManager class handles the creation and polling of topics using APIs.
 */
public class TopicCreationManager {

    private static String API_URL;
    private static String CONF_MOD_AUTH_TOKEN;

    /**
     * The main method initializes configuration, sets topic parameters, and initiates the topic creation process.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        loadConfig();
        String topicName = "demo4";
        int brokerId = 1;
        // Modify this
        String createUser = "laoxishuai";

        try {
            createAndPollTopic(topicName, brokerId, createUser);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating topic.");
        }
    }

    /**
     * Combines the topic creation and polling processes.
     * @param topicName The name of the topic to be created.
     * @param brokerId The ID of the broker for the topic.
     * @param createUser The user responsible for creating the topic.
     * @throws IOException If an I/O error occurs.
     */
    public static void createAndPollTopic(String topicName, int brokerId, String createUser) throws IOException {
        createTopic(topicName, brokerId, createUser);
        pollForTopicCreation(topicName);
    }

    /**
     * Creates a topic using API calls with specified parameters.
     * @param topicName The name of the topic to be created.
     * @param brokerId The ID of the broker for the topic.
     * @param createUser The user responsible for creating the topic.
     * @throws IOException If an I/O error occurs.
     */
    private static void createTopic(String topicName, int brokerId, String createUser) throws IOException {
        String apiUrl = API_URL + "?type=op_modify&method=admin_add_new_topic_record";

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put("topicName", topicName);
        apiParams.put("acceptPublish", "true");
        apiParams.put("deletePolicy", "delete,168");
        apiParams.put("unflushInterval", "10000");
        apiParams.put("acceptSubscribe", "true");
        apiParams.put("confModAuthToken", CONF_MOD_AUTH_TOKEN);
        apiParams.put("createUser", createUser);
        // Add the brokerId
        apiParams.put("brokerId", String.valueOf(brokerId));

        sendApiRequest(apiUrl, apiParams);
    }

    /**
     * Polls for topic creation status until the topic is successfully created.
     * @param topicName The name of the topic to be checked.
     */
    private static void pollForTopicCreation(String topicName) {
        while (true) {
            try {
                if (queryTopicInfo(topicName)) {
                    System.out.println("Topic creation successful.");
                    // Exit loop if topic created
                    break;
                } else {
                    System.out.println("Polling for changes...");
                    Thread.sleep(5000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends an API request with specified parameters.
     * @param apiUrl The API URL for the request.
     * @param apiParams The parameters for the API request.
     * @throws IOException If an I/O error occurs.
     */
    private static void sendApiRequest(String apiUrl, Map<String, String> apiParams) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
            System.out.println("Topic creation request successful.");
        } else {
            System.err.println("Error creating topic. Response code: " + responseCode);
            // Additional error handling logic here
        }

        connection.disconnect();
    }

    /**
     * Queries topic information to check if a topic exists.
     * @param topicName The name of the topic to be queried.
     * @return True if the topic exists, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    private static boolean queryTopicInfo(String topicName) throws IOException {
        String apiUrl = API_URL + "?type=op_query&method=admin_query_topic_info";

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put("topicName", topicName);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
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
                // Successfully queried topic info, you can check the response content here
                // Parse the response JSON or XML to determine if the topic exists
                // If topic exists
                return true;
            } else {
                // Error handling logic here
                // If topic doesn't exist
                return false;
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
        try (InputStream input = TopicCreationManager.class.getResourceAsStream("/config.properties")) {
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

package com.myTopicTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The TopicDeletionManager class provides functions for deleting or removing topics using API calls.
 */
public class TopicDeletionManager {

    private static String API_URL = "http://127.0.0.1:8080/webapi.htm";
    private static String CONF_MOD_AUTH_TOKEN = "abc";
    private static final int DEFAULT_MAX_RETRIES = 3;

    public static void main(String[] args) {
        loadConfig();
        String topicName = "demo8";
        int brokerId = 1;
        // Modify this
        String modifyUser = "xishuai";

        try {
            deleteTopic(topicName, brokerId, "hardDelete", modifyUser);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error deleting or removing topic.");
        }
    }

    /**
     * Deletes or removes a topic based on the delete type.
     * @param topicName The name of the topic to be deleted.
     * @param brokerId The ID of the broker for the topic.
     * @param deleteType The type of deletion ("softDelete", "redo", or "hardDelete").
     * @param modifyUser The user initiating the deletion.
     * @throws IOException If an I/O error occurs.
     */
    public static void deleteTopic(String topicName, int brokerId, String deleteType, String modifyUser) throws IOException {
        switch (deleteType) {
            case "softDelete":
                softDeleteTopicWithRetry(topicName, brokerId, modifyUser);
                break;
            case "redo":
                redoDeletedTopicWithRetry(topicName, brokerId, modifyUser);
                break;
            case "hardDelete":
                hardRemoveTopicWithRetry(topicName, brokerId, modifyUser);
                break;
            default:
                throw new IllegalArgumentException("Invalid delete type: " + deleteType);
        }
    }

    private static void softDeleteTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException {
        disablePublishAndSubscribe(topicName, brokerId, modifyUser);

        String apiUrl = API_URL + "?type=op_modify&method=admin_delete_topic_info";
        Map<String, String> apiParams = buildApiParams(topicName, brokerId, modifyUser);

        performWithRetry(apiUrl, apiParams, "Soft delete topic");
    }

    private static void disablePublishAndSubscribe(String topicName, int brokerId, String modifyUser) throws IOException {
        boolean acceptPublish = false;
        boolean acceptSubscribe = false;

        try {
            UpdateTopicParameters updateParams = new UpdateTopicParameters(topicName, brokerId, modifyUser)
                    .setAcceptPublish(acceptPublish)
                    .setAcceptSubscribe(acceptSubscribe);
            TopicUpdateManager.updateTopic(updateParams);
            System.out.println("Publish and subscribe disabled for topic: " + topicName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error disabling publish and subscribe for topic: " + topicName);
        }
    }

    private static void redoDeletedTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException {
        String apiUrl = API_URL + "?type=op_modify&method=admin_redo_deleted_topic_info";
        Map<String, String> apiParams = buildApiParams(topicName, brokerId, modifyUser);

        performWithRetry(apiUrl, apiParams, "Redo deleted topic");
    }

    private static void hardRemoveTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException {
        String apiUrl = API_URL + "?type=op_modify&method=admin_remove_topic_info";
        Map<String, String> apiParams = buildApiParams(topicName, brokerId, modifyUser);

        performWithRetry(apiUrl, apiParams, "Hard remove topic");
    }

    private static void performWithRetry(String apiUrl, Map<String, String> apiParams, String operationName) throws IOException {
        int retries = 0;
        boolean success = false;
        while (retries < DEFAULT_MAX_RETRIES && !success) {
            try {
                sendApiRequest(apiUrl, apiParams);
                success = true;
                System.out.println(operationName + " successful.");
            } catch (IOException e) {
                System.err.println("Error in " + operationName + " operation. Retrying...");
                retries++;
            }
        }
    }

    private static Map<String, String> buildApiParams(String topicName, int brokerId, String modifyUser) {
        Map<String, String> apiParams = new HashMap<>();
        apiParams.put("topicName", topicName);
        apiParams.put("brokerId", String.valueOf(brokerId));
        apiParams.put("confModAuthToken", CONF_MOD_AUTH_TOKEN);
        apiParams.put("modifyUser", modifyUser);
        return apiParams;
    }

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
            System.out.println("API request successful.");
        } else {
            String responseMessage = connection.getResponseMessage();
            System.err.println("Error in API request. Response code: " + responseCode + ", Response message: " + responseMessage);
            // Additional error handling logic here
        }

        connection.disconnect();
    }

    public static void loadConfig() {
        try (InputStream input = TopicDeletionManager.class.getResourceAsStream("/config.properties")) {
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

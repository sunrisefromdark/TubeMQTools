package com.myTopicTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The TopicUpdateManager class handles updating topic information using API requests.
 */
public class TopicUpdateManager {

    private static String API_URL = "http://127.0.0.1:8080/webapi.htm";
    private static String CONF_MOD_AUTH_TOKEN = "abc";

    public static void main(String[] args) {
        loadConfig();

        String topicName = "demo7";
        int brokerId = 1;
        String modifyUser = "xishuai";
        int unflushThreshold = 1400;
        int numPartition = 6;

        try {
            UpdateTopicParameters params = new UpdateTopicParameters(topicName, brokerId, modifyUser)
                    .setAcceptPublish(true)
                    .setNumPartitions(numPartition)
                    .setUnflushThreshold(unflushThreshold);

            updateTopic(params);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error modifying topic.");
        }
    }

    /**
     * Updates a topic with the provided parameters.
     * @param params The parameters for updating the topic.
     * @throws IOException If an IO error occurs during the API request.
     */
    public static void updateTopic(UpdateTopicParameters params) throws IOException {
        modifyTopic(params);
    }

    /**
     * Modifies a topic using API request based on the given parameters.
     * @param params The parameters for modifying the topic.
     * @throws IOException If an IO error occurs during the API request.
     */
    public static void modifyTopic(UpdateTopicParameters params) throws IOException {
        String apiUrl = API_URL + "?type=op_modify&method=admin_modify_topic_info";

        Map<String, String> apiParams = new HashMap<>();
        apiParams.put("topicName", params.getTopicName());
        apiParams.put("brokerId", String.valueOf(params.getBrokerId()));
        apiParams.put("modifyUser", params.getModifyUser());

        // Add optional parameters
        if (params.isAcceptPublish() != null) {
            apiParams.put("acceptPublish", String.valueOf(params.isAcceptPublish()));
        }
        if (params.getNumPartitions() != null) {
            apiParams.put("numPartitions", String.valueOf(params.getNumPartitions()));
        }
        if (params.getDeletePolicy() != null) {
            apiParams.put("deletePolicy", params.getDeletePolicy());
        }
        if (params.getUnflushThreshold() != null) {
            apiParams.put("unflushThreshold", String.valueOf(params.getUnflushThreshold()));
        }
        if (params.getUnflushInterval() != null) {
            apiParams.put("unflushInterval", String.valueOf(params.getUnflushInterval()));
        }
        if (params.isAcceptSubscribe() != null) {
            apiParams.put("acceptSubscribe", String.valueOf(params.isAcceptSubscribe()));
        }

        apiParams.put("confModAuthToken", CONF_MOD_AUTH_TOKEN);

        sendApiRequest(apiUrl, apiParams);
    }

    /**
     * Sends an API request with the given parameters.
     * @param apiUrl The URL of the API.
     * @param apiParams The parameters for the API request.
     * @throws IOException If an IO error occurs during the API request.
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
            System.out.println("Topic modification request successful.");
        } else {
            System.err.println("Error modifying topic. Response code: " + responseCode);
            // Additional error handling logic here
        }

        connection.disconnect();
    }

    /**
     * Loads configuration properties for the TopicUpdateManager.
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

/**
 * The UpdateTopicParameters class encapsulates parameters for updating a topic.
 */
class UpdateTopicParameters {
    private final String topicName;
    private final int brokerId;
    private final String modifyUser;
    private Boolean acceptPublish;
    private Boolean acceptSubscribe;
    /**
     * Optional parameter: The unflush threshold for the topic.
     */
    private Integer unflushThreshold;
    private Integer numPartitions;
    private String deletePolicy;
    private Integer unflushInterval;


    public UpdateTopicParameters(String topicName, int brokerId, String modifyUser) {
        this.topicName = topicName;
        this.brokerId = brokerId;
        this.modifyUser = modifyUser;
    }

    public UpdateTopicParameters setAcceptPublish(Boolean acceptPublish) {
        this.acceptPublish = acceptPublish;
        return this;
    }

    public UpdateTopicParameters setAcceptSubscribe(Boolean acceptSubscribe) {
        this.acceptSubscribe = acceptSubscribe;
        return this;
    }

    public UpdateTopicParameters setUnflushThreshold(Integer unflushThreshold) {
        this.unflushThreshold = unflushThreshold;
        return this;
    }

    public UpdateTopicParameters setNumPartitions(Integer numPartitions) {
        this.numPartitions = numPartitions;
        return this;
    }

    public UpdateTopicParameters setDeletePolicy(String deletePolicy) {
        this.deletePolicy = deletePolicy;
        return this;
    }

    public UpdateTopicParameters setUnflushInterval(Integer unflushInterval) {
        this.unflushInterval = unflushInterval;
        return this;
    }

    public String getTopicName() {
        return topicName;
    }

    public int getBrokerId() {
        return brokerId;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public Boolean isAcceptPublish() {
        return acceptPublish;
    }

    public Integer getNumPartitions() {
        return numPartitions;
    }

    public String getDeletePolicy() {
        return deletePolicy;
    }

    public Integer getUnflushThreshold() {
        return unflushThreshold;
    }

    public Integer getUnflushInterval() {
        return unflushInterval;
    }

    public Boolean isAcceptSubscribe() {
        return acceptSubscribe;
    }
}


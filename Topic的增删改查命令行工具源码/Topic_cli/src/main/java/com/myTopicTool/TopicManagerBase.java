package com.myTopicTool;

import java.util.HashMap;
import java.util.Map;

/**
 * The TopicManagerBase class serves as a base class for managing topics and provides common functions.
 */
public class TopicManagerBase {
    protected String apiUrl;
    protected String confModAuthToken;

    /**
     * Constructs an instance of TopicManagerBase with the specified API URL and authentication token.
     * @param apiUrl The API URL for the topic management operations.
     * @param confModAuthToken The authentication token for accessing the configuration modification APIs.
     */
    public TopicManagerBase(String apiUrl, String confModAuthToken) {
        this.apiUrl = apiUrl;
        this.confModAuthToken = confModAuthToken;
    }

    /**
     * Builds the complete API URL using the provided type and method.
     * @param type The type of API operation.
     * @param method The specific method within the API type.
     * @return The constructed API URL.
     */
    protected String buildApiUrl(String type, String method) {
        return apiUrl + "?type=" + type + "&method=" + method + "&confModAuthToken=" + confModAuthToken;
    }

    /**
     * Builds a map of API parameters from pairs of parameter names and values.
     * @param params Pairs of parameter names and values (name1, value1, name2, value2, ...).
     * @return A map containing the parameter names and corresponding values.
     * @throws IllegalArgumentException If the number of parameters is not even.
     */
    protected Map<String, String> buildApiParams(String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Params should be provided in pairs (name, value).");
        }

        Map<String, String> apiParams = new HashMap<>();
        for (int i = 0; i < params.length; i += 2) {
            apiParams.put(params[i], params[i + 1]);
        }
        return apiParams;
    }
}

package com.myTopicTool;

import org.apache.commons.cli.*;

import java.io.IOException;

/**
 * The TopicTool class is a command-line tool for managing topics using various operations.
 */
public class TopicTool {
    public static void main(String[] args) {

        loadAllConfig();

        CommandLineParser parser = new DefaultParser();
        Options options = createOptions();

        try {
            CommandLine cmd = parser.parse(options, args);
            processCommandLineOptions(cmd);
        } catch (ParseException e) {
            System.err.println("Error parsing command-line arguments: " + e.getMessage());
        }
    }

    /**
     * Loads configurations for all topic management classes.
     */
    private static void loadAllConfig() {
        TopicCreationManager.loadConfig();
        TopicDeletionManager.loadConfig();
        TopicUpdateManager.loadConfig();
        TopicQueryManager.loadConfig();
    }

    /**
     * Creates command-line options for various topic management operations.
     * @return An Options object containing the defined command-line options.
     */
    private static Options createOptions() {
        Options options = new Options();

        // Functional flags
        options.addOption(Option.builder("create")
                .desc("Create a topic")
                .build());
        options.addOption(Option.builder("delete")
                .desc("Delete a topic")
                .build());
        options.addOption(Option.builder("update")
                .desc("Update a topic")
                .build());
        options.addOption(Option.builder("query")
                .desc("Query a topic")
                .build());

        // Actual options
        Option topicNameOption = Option.builder("topicName")
                .hasArg()
                .desc("Topic name")
                .build();

        Option brokerIdOption = Option.builder("brokerId")
                .hasArg()
                .desc("Broker ID")
                .build();
        Option createUser = Option.builder("createUser")
                .hasArg()
                .desc("createUser")
                .build();

        // Add options for deleteTopic
        Option deleteTypeOption = Option.builder("deleteType")
                .hasArg()
                .desc("Delete type")
                .build();

        Option modifyUserOption = Option.builder("modifyUser")
                .hasArg()
                .desc("User performing modification")
                .build();

        // Add options for updateTopic
        Option acceptPublishOption = Option.builder("acceptPublish")
                .hasArg()
                .desc("Accept publish for topic")
                .build();
        Option acceptSubscribeOption = Option.builder("acceptSubscribe")
                .hasArg()
                .desc("Accept subscribe for topic")
                .build();
        Option numPartitions = Option.builder("numPartitions")
                .hasArg()
                .desc("update numPartitions for topic")
                .build();

        Option unflushThreshold = Option.builder("unflushThreshold")
                .hasArg()
                .desc("update unflushThreshold for topic")
                .build();
        Option unflushInterval = Option.builder("unflushInterval")
                .hasArg()
                .desc("update unflushInterval for topic")
                .build();

        // Add all options to the Options object
        options.addOption(brokerIdOption);
        options.addOption(topicNameOption);
        options.addOption(deleteTypeOption);
        options.addOption(createUser);
        options.addOption(modifyUserOption);

        options.addOption(acceptPublishOption);
        options.addOption(acceptSubscribeOption);
        options.addOption(numPartitions);
        options.addOption(unflushThreshold);
        options.addOption(unflushInterval);

        return options;
    }

    /**
     * Processes the command-line options and performs the corresponding topic management operation.
     * @param cmd The parsed command-line options.
     */
    private static void processCommandLineOptions(CommandLine cmd) {
        if (cmd.hasOption("create")) {
            String topicName = cmd.getOptionValue("topicName");
            int brokerId = Integer.parseInt(cmd.getOptionValue("brokerId"));
            String createUser = cmd.getOptionValue("createUser");

            try {
                TopicCreationManager.createAndPollTopic(topicName, brokerId, createUser);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error creating topic.");
            }
        } else if (cmd.hasOption("delete")) {
            String topicName = cmd.getOptionValue("topicName");
            int brokerId = Integer.parseInt(cmd.getOptionValue("brokerId"));
            String deleteType = cmd.getOptionValue("deleteType");
            String modifyUser = cmd.getOptionValue("modifyUser");

            try {
                TopicDeletionManager.deleteTopic(topicName, brokerId, deleteType, modifyUser);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error deleting or removing topic.");
            }
        } else if (cmd.hasOption("update")) {
            String topicName = cmd.getOptionValue("topicName");
            int brokerId = Integer.parseInt(cmd.getOptionValue("brokerId"));
            String modifyUser = cmd.getOptionValue("modifyUser");

            // Other parameters are optional
            try {
                UpdateTopicParameters params = new UpdateTopicParameters(topicName, brokerId, modifyUser);
                // Add optional parameters if provided
                if (cmd.hasOption("acceptPublish")) {
                    params.setAcceptPublish(Boolean.parseBoolean(cmd.getOptionValue("acceptPublish")));
                }
                if (cmd.hasOption("acceptSubscribe")) {
                    params.setAcceptSubscribe(Boolean.parseBoolean(cmd.getOptionValue("acceptSubscribe")));
                }
                if (cmd.hasOption("unflushThreshold")) {
                    params.setUnflushThreshold(Integer.parseInt(cmd.getOptionValue("unflushThreshold")));
                }
                if (cmd.hasOption("numPartitions")) {
                    params.setNumPartitions(Integer.parseInt(cmd.getOptionValue("numPartitions")));
                }
                if (cmd.hasOption("deletePolicy")) {
                    params.setDeletePolicy(cmd.getOptionValue("deletePolicy"));
                }
                if (cmd.hasOption("unflushInterval")) {
                    params.setUnflushInterval(Integer.parseInt(cmd.getOptionValue("unflushInterval")));
                }

                TopicUpdateManager.updateTopic(params);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error modifying topic.");
            }
        } else if (cmd.hasOption("query")) {
            String topicName = cmd.getOptionValue("topicName");

            try {
                String queryResult = TopicQueryManager.queryTopicInfo(topicName);
                System.out.println("Query result:");
                System.out.println(queryResult);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error querying topic information.");
            }
        } else {
            System.out.println("No valid operation specified.");
        }
    }
}

package org.example.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class to log output for documentation purposes.
 * This can be used to capture console output for screenshots in the README.
 */
public class OutputLogger {
    private static final List<String> logEntries = new ArrayList<>();
    private static final String LOG_FILE = "mongodb_operations.log";
    private static boolean loggingEnabled = true;

    /**
     * Logs a message to both console and the log buffer.
     * 
     * @param message the message to log
     */
    public static void log(String message) {
        System.out.println(message);
        if (loggingEnabled) {
            logEntries.add(message);
        }
    }

    /**
     * Logs a section header to both console and the log buffer.
     * 
     * @param section the section title
     */
    public static void logSection(String section) {
        String header = "\n--- " + section.toUpperCase() + " ---";
        System.out.println(header);
        if (loggingEnabled) {
            logEntries.add(header);
        }
    }

    /**
     * Saves all logged entries to a file for documentation.
     */
    public static void saveToFile() {
        if (!loggingEnabled || logEntries.isEmpty()) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE))) {
            writer.println("MongoDB Student Enrollment System - Operation Log");
            writer.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("=".repeat(50));
            
            for (String entry : logEntries) {
                writer.println(entry);
            }
            
            System.out.println("\nLog saved to file: " + LOG_FILE);
        } catch (IOException e) {
            System.err.println("Failed to save log to file: " + e.getMessage());
        }
    }

    /**
     * Enables or disables logging.
     * 
     * @param enabled true to enable logging, false to disable
     */
    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
    }

    /**
     * Clears all log entries.
     */
    public static void clear() {
        logEntries.clear();
    }
} 
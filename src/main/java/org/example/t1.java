package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class t1 {
    public static void main(String[] args) {
        System.out.println("Hello World");

        String connectionString = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            // Get or create database (created automatically when you first store data)
            MongoDatabase database = mongoClient.getDatabase("myNewDatabase");

            System.out.println("Successfully connected to database: " + database.getName());

            // Create a collection (similar to a table in relational DB)
            database.createCollection("users");
            System.out.println("Collection 'users' created successfully");

        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }
}

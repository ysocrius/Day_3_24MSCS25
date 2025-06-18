package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bson.json.JsonWriterSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> studentsCollection;
    private static MongoCollection<Document> coursesCollection;
    private static MongoCollection<Document> enrollmentsCollection;
    private static Properties properties;
    // Create a pretty JSON writer setting
    private static final JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();

    public static void main(String[] args) {
        try {

            properties = loadProperties();


            String connectionString = properties.getProperty("mongodb.connection.string");
            String databaseName = properties.getProperty("mongodb.database.name");

            System.out.println("Connecting to MongoDB at: " + connectionString);
            mongoClient = MongoClients.create(connectionString);


            System.out.println("Creating/accessing database: " + databaseName);
            database = mongoClient.getDatabase(databaseName);


            String studentsCollectionName = properties.getProperty("mongodb.collection.students");
            String coursesCollectionName = properties.getProperty("mongodb.collection.courses");
            String enrollmentsCollectionName = properties.getProperty("mongodb.collection.enrollments");


            boolean studentsExists = collectionExists(database, studentsCollectionName);
            boolean coursesExists = collectionExists(database, coursesCollectionName);
            boolean enrollmentsExists = collectionExists(database, enrollmentsCollectionName);

            if (!studentsExists) {
                System.out.println("Creating students collection...");
                database.createCollection(studentsCollectionName);
            }

            if (!coursesExists) {
                System.out.println("Creating courses collection...");
                database.createCollection(coursesCollectionName);
            }

            if (!enrollmentsExists) {
                System.out.println("Creating enrollments collection...");
                database.createCollection(enrollmentsCollectionName);
            }


            studentsCollection = database.getCollection(studentsCollectionName);
            coursesCollection = database.getCollection(coursesCollectionName);
            enrollmentsCollection = database.getCollection(enrollmentsCollectionName);

            System.out.println("Collections initialized successfully");


            displayMenu();

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.print("\nEnter your choice (0-7): ");
                String input = scanner.nextLine().trim();
                int choice;

                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 0 and 7.");
                    continue;
                }

                switch (choice) {
                    case 0:
                        running = false;
                        System.out.println("Exiting the application...");
                        break;
                    case 1:
                        // Clear previous data (for testing purposes)
                        System.out.println("Clearing previous data from collections...");
                        studentsCollection.deleteMany(new Document());
                        coursesCollection.deleteMany(new Document());
                        enrollmentsCollection.deleteMany(new Document());
                        System.out.println("All collections cleared.");
                        break;
                    case 2:
                        // Insert sample students and courses
                        insertSampleData();
                        break;
                    case 3:
                        // Add two types of enrollments (embedded and referenced)
                        createEnrollments();
                        break;
                    case 4:
                        // Query and print both types with full details
                        queryEnrollments();
                        break;
                    case 5:
                        // Update a student's name and show the difference
                        updateStudentName();
                        break;
                    case 6:
                        // Create indexes for querying students
                        //createIndexes();
                        break;
                    case 7:
                        // Run all operations in sequence
                        System.out.println("Running all operations in sequence...");

                        // Clear previous data
                        studentsCollection.deleteMany(new Document());
                        coursesCollection.deleteMany(new Document());
                        enrollmentsCollection.deleteMany(new Document());

                        // Run all operations
                        insertSampleData();
                        createEnrollments();
                        queryEnrollments();
                        updateStudentName();
                        //createIndexes();

                        System.out.println("\nAll operations completed successfully!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 0 and 7.");
                }

                if (running && choice != 0) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    displayMenu();
                }
            }

            scanner.close();

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- MONGODB STUDENT ENROLLMENT SYSTEM ---");
        System.out.println("Please select an operation to perform:");
        System.out.println("1. Clear all collections");
        System.out.println("2. Insert sample students and courses");
        System.out.println("3. Create enrollments (embedded and referenced)");
        System.out.println("4. Query enrollments and show document structures");
        System.out.println("5. Update student name (demonstrate reference vs. embedded)");
        //System.out.println("6. Create indexes for querying students");
        System.out.println("7. Run all operations in sequence");
        System.out.println("0. Exit");
    }

    private static boolean collectionExists(MongoDatabase database, String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                System.out.println("Collection already exists: " + collectionName);
                return true;
            }
        }
        return false;
    }

    private static Properties loadProperties() throws IOException {
        Properties prop = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("mongodb.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find mongodb.properties");
                throw new IOException("Unable to find mongodb.properties");
            }
            prop.load(input);
        }
        return prop;
    }

    private static void insertSampleData() {
        System.out.println("\n--- INSERTING SAMPLE DATA ---");

        // Insert students
        Document student1 = new Document()
                .append("name", "John Smith")
                .append("studentId", "S1001")
                .append("email", "john.smith@example.com")
                .append("age", 20);
        Document student2 = new Document()
                .append("name", "Emily Johnson")
                .append("studentId", "S1002")
                .append("email", "emily.johnson@example.com")
                .append("age", 21);
        Document student3 = new Document()
                .append("name", "Michael Brown")
                .append("studentId", "S1003")
                .append("email", "michael.brown@example.com")
                .append("age", 19);

        studentsCollection.insertMany(java.util.Arrays.asList(student1, student2, student3));
        System.out.println("Inserted 3 students into the database");

        // Insert courses
        Document course1 = new Document()
                .append("name", "Introduction to Java Programming")
                .append("courseId", "CS101")
                .append("credits", 3)
                .append("instructor", "Prof. Anderson");
        Document course2 = new Document()
                .append("name", "Database Management Systems")
                .append("courseId", "CS202")
                .append("credits", 4)
                .append("instructor", "Prof. Martinez");
        Document course3 = new Document()
                .append("name", "Web Development Fundamentals")
                .append("courseId", "CS303")
                .append("credits", 3)
                .append("instructor", "Prof. Wilson");

        coursesCollection.insertMany(java.util.Arrays.asList(course1, course2, course3));
        System.out.println("Inserted 3 courses into the database");


        System.out.println("\nStudents in database:");
        try (MongoCursor<Document> cursor = studentsCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document student = cursor.next();
                System.out.println("  - " + student.getString("name") +
                        " (ID: " + student.getString("studentId") +
                        ", Email: " + student.getString("email") +
                        ", Age: " + student.getInteger("age") + ")");
            }
        }

        System.out.println("\nCourses in database:");
        try (MongoCursor<Document> cursor = coursesCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document course = cursor.next();
                System.out.println("  - " + course.getString("name") +
                        " (ID: " + course.getString("courseId") +
                        ", Credits: " + course.getInteger("credits") +
                        ", Instructor: " + course.getString("instructor") + ")");
            }
        }


    }

    private static void createEnrollments() {
        System.out.println("\n--- CREATING ENROLLMENTS ---");


        Document student = studentsCollection.find().first();
        Document course = coursesCollection.find().first();



        String studentName = student.getString("name");
        String studentId = student.getString("studentId");
        String courseName = course.getString("name");
        String courseId = course.getString("courseId");

        ObjectId studentObjId = student.getObjectId("_id");
        ObjectId courseObjId = course.getObjectId("_id");


        Document referencedEnrollment = new Document()
                .append("enrollmentType", "referenced")
                .append("date", new java.util.Date())
                .append("studentId", studentObjId)
                .append("courseId", courseObjId)
                .append("grade", "A");

        enrollmentsCollection.insertOne(referencedEnrollment);
        System.out.println("Created referenced enrollment with the following details:");
        System.out.println("  - Student: " + studentName + " (ID: " + studentId + ")");
        System.out.println("  - Course: " + courseName + " (ID: " + courseId + ")");
        System.out.println("  - Grade: A");
        System.out.println("  - Using MongoDB ObjectIds as references");


        Document student2 = studentsCollection.find().skip(1).first();
        Document course2 = coursesCollection.find().skip(1).first();


        String student2Name = student2.getString("name");
        String student2Id = student2.getString("studentId");
        String course2Name = course2.getString("name");
        String course2Id = course2.getString("courseId");


        Document embeddedEnrollment = new Document()
                .append("enrollmentType", "embedded")
                .append("date", new java.util.Date())
                .append("student", student2)
                .append("course", course2)
                .append("grade", "B+");

        enrollmentsCollection.insertOne(embeddedEnrollment);
        System.out.println("\nCreated embedded enrollment with the following details:");
        System.out.println("  - Student: " + student2Name + " (ID: " + student2Id + ")");
        System.out.println("  - Course: " + course2Name + " (ID: " + course2Id + ")");
        System.out.println("  - Grade: B+");
        System.out.println("  - Using embedded documents (entire student and course documents included)");


        System.out.println("\nDocument structure comparison:");
        System.out.println("1. Referenced Enrollment (JSON):");
        System.out.println(referencedEnrollment.toJson(prettyPrint));

        System.out.println("\n2. Embedded Enrollment (JSON):");
        System.out.println(embeddedEnrollment.toJson(prettyPrint));
    }

    private static void queryEnrollments() {
        System.out.println("\n--- QUERYING ENROLLMENTS ---");

        // Query enrollment with references
        System.out.println("Referenced enrollment:");
        Document referencedEnrollment = enrollmentsCollection.find(
                Filters.eq("enrollmentType", "referenced")).first();

        if (referencedEnrollment != null) {
            ObjectId studentId = referencedEnrollment.getObjectId("studentId");
            ObjectId courseId = referencedEnrollment.getObjectId("courseId");

            Document student = studentsCollection.find(Filters.eq("_id", studentId)).first();
            Document course = coursesCollection.find(Filters.eq("_id", courseId)).first();

            if (student != null && course != null) {
                System.out.println("Enrollment Date: " + referencedEnrollment.get("date"));
                System.out.println("Grade: " + referencedEnrollment.get("grade"));
                System.out.println("Student: " + student.get("name") + " (ID: " + student.get("studentId") + ")");
                System.out.println("Course: " + course.get("name") + " (ID: " + course.get("courseId") + ")");

                // Display the document structure in pretty JSON format
                System.out.println("\nReferenced Enrollment Document Structure:");
                System.out.println(referencedEnrollment.toJson(prettyPrint));

                System.out.println("\nReferenced Student Document:");
                System.out.println(student.toJson(prettyPrint));

                System.out.println("\nReferenced Course Document:");
                System.out.println(course.toJson(prettyPrint));


            } else {
                System.out.println("Referenced student or course not found");
            }
        } else {
            System.out.println("No referenced enrollment found");
        }


        System.out.println("\nEmbedded enrollment:");
        Document embeddedEnrollment = enrollmentsCollection.find(
                Filters.eq("enrollmentType", "embedded")).first();

        if (embeddedEnrollment != null) {
            Document embeddedStudent = (Document) embeddedEnrollment.get("student");
            Document embeddedCourse = (Document) embeddedEnrollment.get("course");

            if (embeddedStudent != null && embeddedCourse != null) {
                System.out.println("Enrollment Date: " + embeddedEnrollment.get("date"));
                System.out.println("Grade: " + embeddedEnrollment.get("grade"));
                System.out.println("Student: " + embeddedStudent.get("name") + " (ID: " + embeddedStudent.get("studentId") + ")");
                System.out.println("Course: " + embeddedCourse.get("name") + " (ID: " + embeddedCourse.get("courseId") + ")");


                System.out.println("\nEmbedded Enrollment Document Structure:");
                System.out.println(embeddedEnrollment.toJson(prettyPrint));


            } else {
                System.out.println("Embedded student or course data is missing");
            }
        } else {
            System.out.println("No embedded enrollment found");
        }


        long referencedSize = 0;
        Document refEnrollment = enrollmentsCollection.find(Filters.eq("enrollmentType", "referenced")).first();
        if (refEnrollment != null) {
            referencedSize = refEnrollment.toJson().length();
        }

        long embeddedSize = 0;
        Document embEnrollment = enrollmentsCollection.find(Filters.eq("enrollmentType", "embedded")).first();
        if (embEnrollment != null) {
            embeddedSize = embEnrollment.toJson().length();
        }

        if (referencedSize > 0 && embeddedSize > 0) {
            System.out.println("\nDocument Size Comparison:");
            System.out.println("Referenced Enrollment: ~" + referencedSize + " bytes");
            System.out.println("Embedded Enrollment: ~" + embeddedSize + " bytes");

            if (embeddedSize > referencedSize) {
                double ratio = (double) embeddedSize / referencedSize;
                System.out.println("Embedded document is approximately " + String.format("%.1f", ratio) + "x larger");
            }
        }
    }

    private static void updateStudentName() {
        System.out.println("\n--- UPDATING STUDENT NAME ---");


        Document student = studentsCollection.find(Filters.eq("studentId", "S1001")).first();
        if (student != null) {
            String oldName = student.getString("name");
            String newName = "John Smith-Updated";
            ObjectId studentId = student.getObjectId("_id");

            System.out.println("Found student: " + oldName + " (ID: " + student.getString("studentId") + ")");
            System.out.println("Will update name to: " + newName);

            Bson filter = Filters.eq("_id", studentId);
            Bson update = Updates.set("name", newName);

            studentsCollection.updateOne(filter, update);



            Document updatedStudent = studentsCollection.find(Filters.eq("_id", studentId)).first();
            if (updatedStudent != null) {
                System.out.println("\nVerifying update in students collection:");
                System.out.println("  - Old name: " + oldName);
                System.out.println("  - New name: " + updatedStudent.getString("name"));
                System.out.println("  - Student document after update:");
                System.out.println(updatedStudent.toJson(prettyPrint));
            }



            Document referencedEnrollment = enrollmentsCollection.find(
                    Filters.eq("enrollmentType", "referenced")).first();

            if (referencedEnrollment != null) {
                ObjectId refStudentId = referencedEnrollment.getObjectId("studentId");
                Document retrievedStudent = studentsCollection.find(Filters.eq("_id", refStudentId)).first();


            }


            Document embeddedEnrollment = enrollmentsCollection.find(
                    Filters.eq("enrollmentType", "embedded")).first();

            if (embeddedEnrollment != null) {
                Document embeddedStudent = (Document) embeddedEnrollment.get("student");






                Document updatedEmbeddedEnrollment = enrollmentsCollection.find(
                        Filters.eq("enrollmentType", "embedded")).first();
                if (updatedEmbeddedEnrollment != null) {
                    Document updatedEmbeddedStudent = (Document) updatedEmbeddedEnrollment.get("student");
                    System.out.println("  - After update, embedded student name: " + updatedEmbeddedStudent.getString("name"));
                    System.out.println("  - This demonstrates that embedded documents require separate updates");
                }
            }
        }
    }


}
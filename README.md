# Day3-Task: MongoDB Student Enrollment System

This Java application manages student enrollments in courses using MongoDB. It demonstrates the use of both embedded documents and referenced documents to understand their structural differences.

## Features

1. Creates and manages three MongoDB collections:
   - `students` - stores student details
   - `courses` - stores course details
   - `enrollments` - stores enrollment records using both embedded and referenced approaches

2. Implements the following operations:
   - Inserting sample students and courses
   - Creating two types of enrollments (embedded and referenced)
   - Querying and printing both types of enrollments with full details
   - Updating a student's name and demonstrating the difference between updating referenced vs. embedded documents
   - Creating indexes for optimized queries on the students collection

## Requirements

- Java 17 or higher
- MongoDB server running on localhost:27017
- Maven for dependency management



## MongoDB Document Structure

### Referenced Document Example

In the referenced approach, the enrollment document stores only the ObjectIds of the student and course:

```json
{
  "_id": ObjectId("..."),
  "enrollmentType": "referenced",
  "date": ISODate("..."),
  "studentId": ObjectId("..."),
  "courseId": ObjectId("..."),
  "grade": "A"
}
```

### Embedded Document Example

In the embedded approach, the entire student and course documents are embedded in the enrollment document:

```json
{
  "_id": ObjectId("..."),
  "enrollmentType": "embedded",
  "date": ISODate("..."),
  "student": {
    "_id": ObjectId("..."),
    "name": "Emily Johnson",
    "studentId": "S1002",
    "email": "emily.johnson@example.com",
    "age": 21
  },
  "course": {
    "_id": ObjectId("..."),
    "name": "Database Management Systems",
    "courseId": "CS202",
    "credits": 4,
    "instructor": "Prof. Martinez"
  },
  "grade": "B+"
}
```

## Results

When you run the application, you'll be able to:
1. Connect to MongoDB and create collections
2. Insert sample students and courses
3. Create both referenced and embedded enrollments
4. Query both types of enrollments and see their structural differences
5. Update a student's name and observe how it affects both enrollment types differently

The application provides detailed explanations of each operation's results, highlighting the key differences between referenced and embedded document approaches.

## Screenshots

### 1. Sample Students and Courses in Database

![Students and Courses](screenshots/Screenshot%202025-06-18%20164349.png)

### 2. Creating Enrollments (Referenced and Embedded)

![Creating Enrollments](screenshots/Screenshot%202025-06-18%20164420.png)

### 3. Querying Embedded Enrollment

![Embedded Enrollment](screenshots/Screenshot%202025-06-18%20164524.png)

### 4. Updating Student Name

![Updating Student Name](screenshots/Screenshot%202025-06-18%20164611.png)

### 5. After Updating:

![After Updating](screenshots/Screenshot%202025-06-18%20170622.png)

### 6. Document Size Comparison

![Document Size Comparison](screenshots/Screenshot%202025-06-18%20165128.png)

## MongoDB Collection Screenshots

### Courses Collection in MongoDB

The courses collection contains document details about available courses:

![Courses Collection](screenshots/Screenshot%202025-06-18%20165926.png)

### Students Collection in MongoDB

The students collection shows that John Smith's name was successfully updated:

![Students Collection](screenshots/Screenshot%202025-06-18%20165953.png)

### Enrollments Collection in MongoDB

The enrollments collection shows both document structures:

#### Referenced Enrollment:
![Referenced Enrollment](screenshots/Screenshot%202025-06-18%20170006.png)


These screenshots clearly demonstrate the structural differences between referenced and embedded documents in MongoDB. Note how the embedded enrollment contains complete copies of the student and course documents, while the referenced enrollment only contains their ObjectIds.

## MongoDB Collections

The application creates and manages three MongoDB collections:

1. **students** - Stores information about students
2. **courses** - Stores information about courses
3. **enrollments** - Stores enrollment records with both referenced and embedded approaches

Each collection demonstrates different MongoDB document patterns and relationships. 
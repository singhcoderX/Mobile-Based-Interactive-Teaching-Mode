# Mobile-Based-Interactive-Teaching-Mode | Android 
## Built 2 Android one for Student and one for Instructor, The quizzes will be uploaded by the instructor, and students can answer those quizzes in response and can see the results 

These apps are built for better **two-way communication**  between instructor and students.

Features that are already implemented:
1. Two-way Communication
2. Tracking Malicious Activities
3. GPS based Circumference Restriction

There are basically two apps in this project:

### Teacher app(EditQuiz):

- *Activity 1*: Set Timer for the Quiz.

- *Activity 2*: Enter Questions with 4 options and also enter the answer. Instructor can add as many no of Question he/she wants.

> After the Instructor completes adding questions and submits it, then all the questions will be uploaded to the **Firebase Database**.
- *Activity 3*: Results of All Students will be shown after the timer ends.

### Student app(MiniQuiz):

- *Activity 1*: All Questions uploaded by Instrutor will be shown one by one.

- *Activity 2*: After completing the Quiz, It will ask to enter the roll number of the student.

- *Activity 3*: Result of the Quiz is shown

### Security
- MiniQuiz App will **automatically detect pressing of Home Button** or **Switching** of App or If MiniQuiz **goes to Background**.
- **GPS based location** of every Student is uploaded to Firebase after every location change 
- If Current Location of Student (taking Quiz) from instructor is **more than the defined circumference** MiniQuiz will **detect** it and **end the quiz** and shows the result of the Quiz.
## [DEMO](https://youtu.be/Qtbs9YP8198)

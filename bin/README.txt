Ryan Diep
101229043

Steps to Set Up the Database:
1. Create a new database called A3
2. To create the students table, run:
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    enrollment_date DATE
);

3. To insert the default data into the table, run:
INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES
('John', 'Doe', 'john.doe@example.com', '2023-09-01'),
('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'),
('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');

Steps to Run the Code:
1. Install the PostgresSQL JDBC driver: https://www.postgresql.org/download/
2. Import the JAR in the project as an external library
3. Replace the variables DB_URL, USER, and PASS with your own

Video:
https://youtu.be/R0r_ZwvTI7Q
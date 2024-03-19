import java.sql.*;
import java.util.Scanner;

public class main {
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5432/A3";
    static final String USER = "postgres";
    static final String PASS = "root";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database");

            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            while (choice != 5) {
                System.out.println("\nMenu:");
                System.out.println("1. Get all students");
                System.out.println("2. Add a student");
                System.out.println("3. Update a student's email");
                System.out.println("4. Delete a student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        getAllStudents(conn);
                        break;
                    case 2:
                        System.out.print("Enter first name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter last name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter enrollment date (YYYY-MM-DD): ");
                        String enrollmentDate = scanner.nextLine();
                        addStudent(conn, firstName, lastName, email, enrollmentDate);
                        break;
                    case 3:
                        System.out.print("Enter student id: ");
                        int studentIdUpdate = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine();
                        updateStudentEmail(conn, studentIdUpdate, newEmail);
                        break;
                    case 4:
                        System.out.print("Enter student id: ");
                        int studentIdDelete = scanner.nextInt();
                        deleteStudent(conn, studentIdDelete);
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                        break;
                }
            }

            conn.close();
            scanner.close();
        } 
        catch (SQLException se) {
            se.printStackTrace();
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
            try {
                if (stmt != null) stmt.close();
            } 
            catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } 
            catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    // Retrieve and display all records from the students table
    static void getAllStudents(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM students";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt("id") + ", " +
                    rs.getString("first_name") + ", " +
                    rs.getString("last_name") + ", " +
                    rs.getString("email") + ", " +
                    rs.getDate("enrollment_date"));
        }
        rs.close();
        stmt.close();
    }

    // Insert a new student record into the students table
    static void addStudent(Connection conn, String firstName, String lastName, String email, String enrollmentDate) throws SQLException {
        if (isEmailUnique(conn, email)) {
            String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setDate(4, Date.valueOf(enrollmentDate));
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Student added successfully.");
        } 
        else {
            System.out.println("Email already exists. Please enter a unique email.");
        }
    }

    // Update the email address for a student with the specified id
    static void updateStudentEmail(Connection conn, int studentId, String newEmail) throws SQLException {
    	if (isEmailUnique(conn, newEmail)) {
	        String sql = "UPDATE students SET email = ? WHERE id = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, newEmail);
	        pstmt.setInt(2, studentId);
	        pstmt.executeUpdate();
	        pstmt.close();
	        System.out.println("Email updated successfully.");
    	}
        else {
            System.out.println("Email already exists. Please enter a unique email.");
        }
    }

    // Delete the record of the student with the specified id
    static void deleteStudent(Connection conn, int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, studentId);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Student deleted successfully.");
    }
    
    // Check if the email is unique in the students table
    static boolean isEmailUnique(Connection conn, String email) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM students WHERE email = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt("count");
        rs.close();
        pstmt.close();
        return count == 0;
    }
}

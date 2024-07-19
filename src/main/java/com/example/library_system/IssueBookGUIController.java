package com.example.library_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;

public class IssueBookGUIController {
    @FXML
    private TextField searchStudent;
    @FXML
    private TableView<Student> viewStudentTable;
    @FXML
    private TableColumn<Student, Integer> studentIdCol;  // Assumes Student has 'studentId'
    @FXML
    private TableColumn<Student, String> studentNameCol; // Assumes Student has 'studentName'

    @FXML
    private TextField searchBook;
    @FXML
    private TableView<Book> viewBookTable;
    @FXML
    private TableColumn<Book, Integer> bookIdCol;  // Assumes Book has 'bookId'
    @FXML
    private TableColumn<Book, String> bookTitleCol;
    @FXML
    private TextField studentIssueID;
    @FXML
    private TextField bookIssueID;
    @FXML
    private TextField desiredDays;
    @FXML
    private TextField issueDate;

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Book> books = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        loadAllStudents();
        loadAllBooks();
    }

    private void loadAllStudents() {
        students.clear();
        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT StudentId, StudentName FROM Students")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(rs.getInt("StudentId"), rs.getString("StudentName")));
            }
            viewStudentTable.setItems(students);
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading student data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadAllBooks() {
        books.clear();
        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT Book_ID, Title FROM Books")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(rs.getInt("Book_ID"), rs.getString("Title")));
            }
            viewBookTable.setItems(books);
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading book data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    public void searchStudentMethod() {
        students.clear();
        String studentQuery = "SELECT StudentId, StudentName FROM Students WHERE StudentName LIKE ?";
        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(studentQuery)) {
            statement.setString(1, "%" + searchStudent.getText() + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                students.add(new Student(
                        resultSet.getInt("StudentId"),
                        resultSet.getString("StudentName")
                ));
            }
            viewStudentTable.setItems(students);
        } catch (SQLException e) {
            showAlert("Database Error", "Error searching for students: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void searchBookMethod() {
        books.clear();
        String bookQuery = "SELECT Book_ID, Title FROM Books WHERE books.Title LIKE ?";
        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(bookQuery)) {
            statement.setString(1, "%" + searchBook.getText() + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("Book_ID"),
                        resultSet.getString("Title")
                ));
            }
            viewBookTable.setItems(books);
        } catch (SQLException e) {
            showAlert("Database Error", "Error searching for books: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    public void issueBook() {
        int studentId = Integer.parseInt(studentIssueID.getText());
        int bookId = Integer.parseInt(bookIssueID.getText());
        int days = Integer.parseInt(desiredDays.getText());
        LocalDate currentDate = LocalDate.now();

        String studentName = "";
        String bookTitle = "";

        // Query to get the student's name
        String studentQuery = "SELECT StudentName FROM Students WHERE StudentId = ?";
        // Query to get the book's title
        String bookQuery = "SELECT Title FROM Books WHERE Book_ID = ?";

        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement studentStmt = connection.prepareStatement(studentQuery);
             PreparedStatement bookStmt = connection.prepareStatement(bookQuery)) {

            studentStmt.setInt(1, studentId);
            ResultSet studentRs = studentStmt.executeQuery();
            if (studentRs.next()) {
                studentName = studentRs.getString("StudentName");
            }

            bookStmt.setInt(1, bookId);
            ResultSet bookRs = bookStmt.executeQuery();
            if (bookRs.next()) {
                bookTitle = bookRs.getString("Title");
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Error fetching student or book details: " + e.getMessage(), Alert.AlertType.ERROR);
            return; // Exit the method if fetching details fails
        }

        String insertQuery = "INSERT INTO IssuedBooks (StudentId, StudentName, BookId, BookTitle, DesiredDays, ReturnedOnTime, ReturnDate, IssueDate) " +
                "VALUES (?, ?, ?, ?, ?, FALSE, NULL, ?)";

        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            statement.setInt(1, studentId);
            statement.setString(2, studentName); // Use the fetched student name
            statement.setInt(3, bookId);
            statement.setString(4, bookTitle); // Use the fetched book title
            statement.setInt(5, days);
            statement.setDate(6, Date.valueOf(currentDate)); // Set current date here

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                showAlert("Success", "Book issued successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Failure", "Failed to issue the book.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Error issuing the book: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

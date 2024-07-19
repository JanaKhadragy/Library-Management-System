package com.example.library_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class ViewStudentsController {
    @FXML
    private TextField searchStudent;
    @FXML
    private TableView<Student> ViewStudentTable;
    @FXML
    private TableColumn<Student, String> StudentidCol;
    @FXML
    private TableColumn<Student, String> Namecol;
    @FXML
    private TableColumn<Student, String> Classcol;
    @FXML
    private TableColumn<Student, String> NumberCol;


    ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        LoadStudents();
    }

    @FXML
    public void LoadStudents() {
        String StudentViewQuery = "SELECT StudentId, StudentName, StudentClass, StudentNumber FROM Students";
        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(StudentViewQuery);
             ResultSet queryOutput = statement.executeQuery()) {
            while (queryOutput.next()) {
                students.add(new Student(
                        queryOutput.getInt("StudentId"),
                        queryOutput.getString("StudentName"),
                        queryOutput.getString("StudentClass"),
                        queryOutput.getString("StudentNumber")
                ));
            }
            StudentidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            Namecol.setCellValueFactory(new PropertyValueFactory<>("name")); // Change "Tittle" to "title" if fixed.
            Classcol.setCellValueFactory(new PropertyValueFactory<>("className"));
            NumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));

            ViewStudentTable.setItems(students);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void searchStudent() {
        // Clear the existing data in the TableView
        ViewStudentTable.getItems().clear();

        // Get the book title entered in the TextField
        String studentName = searchStudent.getText();

        // Query the database to search for the book by title
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {
            String query = "SELECT * FROM Students WHERE Students.StudentName LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Use LIKE with wildcard (%) to search for similar titles
                preparedStatement.setString(1, "%" + studentName + "%");
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if any results were found
                boolean found = false;
                StringBuilder searchResults = new StringBuilder();
                while (resultSet.next()) {
                    // Display the search results
                    int studentId = resultSet.getInt("StudentId");
                    String name = resultSet.getString("StudentName");
                    String studentclass = resultSet.getString("StudentClass");
                    String number = resultSet.getString("StudentNumber");

                    // Add the Book object to the TableView
                    ViewStudentTable.getItems().add(new Student(studentId, name, studentclass, number));

                    searchResults.append("studentId: ").append(studentId)
                            .append(", name: ").append(name)
                            .append(", studentclass: ").append(studentclass)
                            .append(", number: ").append(number)
                            .append("\n");

                    found = true;
                }

                // Display search results or message
                if (found) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search Result");
                    alert.setHeaderText(null);
                    alert.setContentText("students found with name containing \"" + studentName + "\":\n" + searchResults.toString());
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search Result");
                    alert.setHeaderText(null);
                    alert.setContentText("No students found with name containing \"" + studentName + "\"");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error searching for students: " + e.getMessage());
            alert.showAndWait();
        }
    }


}

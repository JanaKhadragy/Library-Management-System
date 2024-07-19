package com.example.library_system;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.*;

public class AddStudentController {
    @FXML
    private TextField StudentName;

    @FXML
    private TextField StudentClass;

    @FXML
    private TextField StudentNumber;

    @FXML
    private Button Add;

    @FXML
    private Button CancelButton;
    @FXML
    private void AddStudentMethod(){
        try (Connection connection = DatabaseConnector.getInstance().getConnection()) {
            String query = "INSERT INTO Students (StudentName, StudentClass, StudentNumber) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, StudentName.getText());
                preparedStatement.setString(2, StudentClass.getText());
                preparedStatement.setString(3, StudentNumber.getText());
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText(null);
                        alert.setContentText("Student added successfully!\n id = " + generatedId);
                        alert.showAndWait();
                    }
                }
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error adding student: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void CancelButton(){
}
}

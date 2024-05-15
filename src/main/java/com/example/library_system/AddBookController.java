package com.example.library_system;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddBookController {

    @FXML
    private TextField Tittle;

    @FXML
    private TextField Author;

    @FXML
    private TextField Publisher;

    @FXML
    private Button AddBook;

    @FXML
    private Button CancelButton;

    @FXML
    public void AddBookMethod() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "INSERT INTO books (Title, Author, Publisher) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, Tittle.getText());
                preparedStatement.setString(2, Author.getText());
                preparedStatement.setString(3, Publisher.getText());
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText(null);
                        alert.setContentText("Book added successfully!\n id = " + generatedId);
                        alert.showAndWait();
                    }
                }
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error adding book: " + e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    public void CancelButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Add book operation canceled.");
        alert.showAndWait();
    }
}

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

public class ViewReturnedBooksController {

    @FXML
    private TableView<IssuedBook> viewReturnedBooksTable;
    @FXML
    private TableColumn<IssuedBook, Integer> studentIdCol;
    @FXML
    private TableColumn<IssuedBook, String> studentNameCol;
    @FXML
    private TableColumn<IssuedBook, Integer> bookIdCol;
    @FXML
    private TableColumn<IssuedBook, String> bookTitleCol;
    @FXML
    private TableColumn<IssuedBook, String> issueDateCol;
    @FXML
    private TableColumn<IssuedBook, Integer> desiredDaysCol;
    @FXML
    private TableColumn<IssuedBook, String> returnDateCol;
    @FXML
    private TextField searchField;

    private ObservableList<IssuedBook> returnedBooks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        desiredDaysCol.setCellValueFactory(new PropertyValueFactory<>("desiredDays"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        loadAllReturnedBooks();
    }

    @FXML
    private void loadAllReturnedBooks() {
        returnedBooks.clear();
        String query = "SELECT * FROM IssuedBooks WHERE ReturnDate IS NOT NULL";
        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                returnedBooks.add(new IssuedBook(
                        resultSet.getInt("StudentId"),
                        resultSet.getString("StudentName"),
                        resultSet.getInt("BookId"),
                        resultSet.getString("BookTitle"),
                        resultSet.getDate("IssueDate").toString(),
                        resultSet.getInt("DesiredDays"),
                        resultSet.getDate("ReturnDate").toString()
                ));
            }
            viewReturnedBooksTable.setItems(returnedBooks);
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading returned books: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void searchReturnedBooks() {
        returnedBooks.clear();
        String searchQuery = "SELECT * FROM IssuedBooks WHERE StudentName LIKE ? AND ReturnDate IS NOT NULL";
        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(searchQuery)) {

            statement.setString(1, "%" + searchField.getText() + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                returnedBooks.add(new IssuedBook(
                        resultSet.getInt("StudentId"),
                        resultSet.getString("StudentName"),
                        resultSet.getInt("BookId"),
                        resultSet.getString("BookTitle"),
                        resultSet.getDate("IssueDate").toString(),
                        resultSet.getInt("DesiredDays"),
                        resultSet.getDate("ReturnDate").toString()
                ));
            }
            viewReturnedBooksTable.setItems(returnedBooks);
        } catch (SQLException e) {
            showAlert("Database Error", "Error searching returned books: " + e.getMessage(), Alert.AlertType.ERROR);
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

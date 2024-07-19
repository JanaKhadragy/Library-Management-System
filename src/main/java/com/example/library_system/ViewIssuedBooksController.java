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

public class ViewIssuedBooksController {

    @FXML
    private TableView<IssuedBook> viewIssuedBooksTable;
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
    private TableColumn<IssuedBook, Boolean> returnedOnTimeCol;
    @FXML
    private TextField searchField;

    private ObservableList<IssuedBook> issuedBooks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        desiredDaysCol.setCellValueFactory(new PropertyValueFactory<>("desiredDays"));
        returnedOnTimeCol.setCellValueFactory(new PropertyValueFactory<>("returnedOnTime"));

        loadAllIssuedBooks();
    }

    @FXML
    private void loadAllIssuedBooks() {
        issuedBooks.clear();
        String query = "SELECT * FROM IssuedBooks WHERE ReturnDate IS NULL";
        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                issuedBooks.add(new IssuedBook(
                        resultSet.getInt("StudentId"),
                        resultSet.getString("StudentName"),
                        resultSet.getInt("BookId"),
                        resultSet.getString("BookTitle"),
                        resultSet.getDate("IssueDate").toString(),
                        resultSet.getInt("DesiredDays"),
                        resultSet.getBoolean("ReturnedOnTime")
                ));
            }
            viewIssuedBooksTable.setItems(issuedBooks);
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading issued books: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void searchIssuedBooks() {
        issuedBooks.clear();
        String searchQuery = "SELECT * FROM IssuedBooks WHERE StudentName LIKE ? AND ReturnDate IS NULL";
        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(searchQuery)) {

            statement.setString(1, "%" + searchField.getText() + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                issuedBooks.add(new IssuedBook(
                        resultSet.getInt("StudentId"),
                        resultSet.getString("StudentName"),
                        resultSet.getInt("BookId"),
                        resultSet.getString("BookTitle"),
                        resultSet.getDate("IssueDate").toString(),
                        resultSet.getInt("DesiredDays"),
                        resultSet.getBoolean("ReturnedOnTime")
                ));
            }
            viewIssuedBooksTable.setItems(issuedBooks);
        } catch (SQLException e) {
            showAlert("Database Error", "Error searching issued books: " + e.getMessage(), Alert.AlertType.ERROR);
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

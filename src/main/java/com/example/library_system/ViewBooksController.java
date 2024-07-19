package com.example.library_system;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;


import java.sql.*;

public class ViewBooksController{
    @FXML
    private TextField Search__Book;
    @FXML
    private TableView<Book> ViewBookTable;
    @FXML
    private TableColumn<Book, String> IDCol;
    @FXML
    private TableColumn<Book, String> TitleCol;
    @FXML
    private TableColumn<Book, String> AuthorCol;
    @FXML
    private TableColumn<Book, String> PublisherCol;

    ObservableList<Book> bookss = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        LoadData();
    }

    @FXML
    public void LoadData() {
        String BookViewQuery = "SELECT Book_ID,Title, Author, Publisher FROM books";
        try (Connection connection = DatabaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(BookViewQuery);
             ResultSet queryOutput = statement.executeQuery()) {
            while (queryOutput.next()) {
                bookss.add(new Book(
                        queryOutput.getInt("Book_ID"),
                        queryOutput.getString("Title"),
                        queryOutput.getString("Author"),
                        queryOutput.getString("Publisher")
                ));
            }
            IDCol.setCellValueFactory(new PropertyValueFactory<>("book_id"));
            TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
            PublisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));

            ViewBookTable.setItems(bookss);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void searchBook() {
        ObservableList<Book> searchResults = FXCollections.observableArrayList();
        String bookTitle = Search__Book.getText().trim();
        String query = "SELECT * FROM books WHERE Title LIKE ?";

        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + bookTitle + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                searchResults.add(new Book(
                        rs.getInt("Book_ID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Publisher")
                ));
            }

            Platform.runLater(() -> {
                ViewBookTable.setItems(searchResults);
                if (searchResults.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "Search Result", "No books found with title containing \"" + bookTitle + "\"");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Search Result", "Books found with title containing \"" + bookTitle + "\": " + searchResults.size());
                }
            });
        } catch (SQLException e) {
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Error", "Error searching for books: " + e.getMessage());
            });
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

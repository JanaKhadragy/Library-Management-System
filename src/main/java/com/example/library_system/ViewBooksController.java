package com.example.library_system;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
        try (Connection connection = DatabaseConnector.getConnection();
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
        ViewBookTable.getItems().clear();
        String bookTitle = Search__Book.getText();
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM books WHERE Title LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + bookTitle + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                boolean found = false;
                StringBuilder searchResults = new StringBuilder();
                while (resultSet.next()) {
                    int bookId = resultSet.getInt("Book_ID");
                    String title = resultSet.getString("Title");
                    String author = resultSet.getString("Author");
                    String publisher = resultSet.getString("Publisher");
                    ViewBookTable.getItems().add(new Book(bookId, title, author, publisher));

                    searchResults.append("Book ID: ").append(bookId)
                            .append(", Title: ").append(title)
                            .append(", Author: ").append(author)
                            .append(", Publisher: ").append(publisher)
                            .append("\n");

                    found = true;
                }

                if (found) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search Result");
                    alert.setHeaderText(null);
                    alert.setContentText("Books found with title containing \"" + bookTitle + "\":\n" + searchResults.toString());
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search Result");
                    alert.setHeaderText(null);
                    alert.setContentText("No books found with title containing \"" + bookTitle + "\"");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error searching for books: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

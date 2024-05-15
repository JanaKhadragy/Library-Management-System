package com.example.library_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuController {
    private void LoadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void Add_Book() {
        LoadWindow("/com/example/library_system/AddBooks.fxml", "Add Book");
        System.out.println("system is working");
    }

    @FXML
    private void View_Book() {
        LoadWindow("/com/example/library_system/ViewBooks.fxml", "View Books");
    }

    @FXML
    private void Add_Student() {
        LoadWindow("/com/example/library_system/AddStudents.fxml", "Add Student");
    }

    @FXML
    private void View_Students() {
        LoadWindow("/com/example/library_system/ViewStudents.fxml", "View Students");
    }

    @FXML
    private void Loan_Book() {
        LoadWindow("/com/example/library_system/AddBooks.fxml", "Add Book");
    }

    @FXML
    private void ViewLoanedBooks() {
        LoadWindow("/com/example/library_system/AddBooks.fxml", "Add Book");
    }

    @FXML
    private void Return_Book() {
        LoadWindow("/com/example/library_system/AddBooks.fxml", "Add Book");
    }

    @FXML
    private void ViewReturnedBooks() {
        showAlert("View Returned Books", "Functionality to view all returned books will be implemented here.");
    }

    private void showAlert(String title, String content) {

    }

    public void addStudent(ActionEvent actionEvent) {
    }
}

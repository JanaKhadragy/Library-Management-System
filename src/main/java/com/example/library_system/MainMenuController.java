package com.example.library_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuController {
    private void LoadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(loc)));
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
    private void Issue_Book() {
        LoadWindow("/com/example/library_system/IssueBookGUI.fxml", "issue book");
    }

    @FXML
    private void View_Issued() {
        LoadWindow("/com/example/library_system/viewissuedbook.fxml", "view issued books");
    }

    @FXML
    private void Return_Book() {
        LoadWindow("/com/example/library_system/returnbook.fxml", "return book");
    }

    @FXML
    private void ViewReturnedBooks() {
        LoadWindow("/com/example/library_system/viewreturnedbooks.fxml", "view returned books");
    }
}

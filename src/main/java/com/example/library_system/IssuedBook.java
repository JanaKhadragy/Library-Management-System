package com.example.library_system;

public class IssuedBook {

    private int studentId;
    private String studentName;
    private int bookId;
    private String bookTitle;
    private String issueDate;
    private int desiredDays;
    private boolean returnedOnTime;
    private String returnDate;

    public IssuedBook(int studentId, String studentName, int bookId, String bookTitle, String issueDate, int desiredDays, boolean returnedOnTime) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.desiredDays = desiredDays;
        this.returnedOnTime = returnedOnTime;
    }

    public IssuedBook(int studentId, String studentName, int bookId, String bookTitle, String issueDate, int desiredDays, String returnDate) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.desiredDays = desiredDays;
        this.returnDate = returnDate;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public int getDesiredDays() {
        return desiredDays;
    }

    public boolean isReturnedOnTime() {
        return returnedOnTime;
    }

    public String getReturnDate() {
        return returnDate;
    }
}

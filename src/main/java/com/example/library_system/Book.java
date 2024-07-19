package com.example.library_system;

public class Book {
    private int book_id;
    private String title; // Corrected the spelling
    private String author;
    private String publisher;

    public Book( int book_id,String title, String author, String publisher) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }
    public Book( int book_id,String title) {
        this.book_id = book_id;
        this.title = title;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

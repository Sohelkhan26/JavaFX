package com.example.new_login_page;

public class Book {
    String BookTitle , Author , Genre , Date;
    Integer Price , Quantity , BookId;
    public Book(String bookTitle, String author, String genre, Integer price, Integer quantity, Integer bookId, String date) {
        BookTitle = bookTitle;
        Author = author;
        Genre = genre;
        Price = price;
        Quantity = quantity;
        BookId = bookId;
        this.Date = date;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Book(String bookTitle, String author, String genre, Integer price, Integer quantity, Integer bookId) {
        BookTitle = bookTitle;
        Author = author;
        Genre = genre;
        Price = price;
        Quantity = quantity;
        BookId = bookId;
    }

    public String getBookTitle() {
        return BookTitle;
    }

    public void setBookTitle(String bookTitle) {
        BookTitle = bookTitle;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public Integer getBookId() {
        return BookId;
    }

    public void setBookId(Integer bookId) {
        BookId = bookId;
    }
}

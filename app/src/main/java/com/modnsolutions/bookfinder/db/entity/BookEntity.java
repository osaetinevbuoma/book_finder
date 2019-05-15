package com.modnsolutions.bookfinder.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book")
public class BookEntity {

    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey
    private String bookId;

    @NonNull
    private String title;

    @NonNull
    private String publisher;

    @NonNull
    @ColumnInfo(name = "published_date")
    private String publishedDate;

    @NonNull
    @ColumnInfo(name = "image_link")
    private String imageLink;

    @NonNull
    private String authors;

    @NonNull
    @ColumnInfo(name = "page_count")
    private int pageCount;

    @NonNull
    private String categories;

    @NonNull
    private String description;

    public BookEntity(@NonNull String bookId, @NonNull String title, @NonNull String publisher,
                      @NonNull String publishedDate, @NonNull String imageLink,
                      @NonNull String authors, int pageCount, @NonNull String categories,
                      @NonNull String description) {
        this.bookId = bookId;
        this.title = title;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.imageLink = imageLink;
        this.authors = authors;
        this.pageCount = pageCount;
        this.categories = categories;
        this.description = description;
    }

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull String bookId) {
        this.bookId = bookId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(@NonNull String publisher) {
        this.publisher = publisher;
    }

    @NonNull
    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(@NonNull String publishedDate) {
        this.publishedDate = publishedDate;
    }

    @NonNull
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(@NonNull String imageLink) {
        this.imageLink = imageLink;
    }

    @NonNull
    public String getAuthors() {
        return authors;
    }

    public void setAuthors(@NonNull String authors) {
        this.authors = authors;
    }

    @NonNull
    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(@NonNull int pageCount) {
        this.pageCount = pageCount;
    }

    @NonNull
    public String getCategories() {
        return categories;
    }

    public void setCategories(@NonNull String categories) {
        this.categories = categories;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }
}

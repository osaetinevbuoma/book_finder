package com.modnsolutions.bookfinder.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.modnsolutions.bookfinder.db.entity.BookEntity;

import java.util.List;

@Dao
public interface BookDao {

    // Save to favorite.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookEntity book);

    // Remove from favorite.
    @Delete
    void remove(BookEntity book);

    // Remove all favorites.
    @Query("DELETE FROM book")
    void removeAll();

    // Update favorite.
    @Update
    void update(BookEntity... books);

    // Find all favorites.
    @Query("SELECT * FROM book")
    LiveData<List<BookEntity>> findAllFavorite();

    // Find one favorite.
    @Query("SELECT * FROM book WHERE id = :bookId")
    BookEntity findFavorite(String bookId);

}

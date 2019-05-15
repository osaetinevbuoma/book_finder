package com.modnsolutions.bookfinder.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.modnsolutions.bookfinder.db.dao.BookDao;
import com.modnsolutions.bookfinder.db.entity.BookEntity;

@Database(entities = { BookEntity.class }, version = 1, exportSchema = false)
public abstract class BookFinderDatabase extends RoomDatabase {

    public abstract BookDao mBookDao();
    private static BookFinderDatabase INSTANCE;

    public static BookFinderDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookFinderDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        BookFinderDatabase.class, "book_finder")
                        // TODO: change migration strategy
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }


        return INSTANCE;
    }

}

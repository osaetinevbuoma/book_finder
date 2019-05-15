package com.modnsolutions.bookfinder.db.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.modnsolutions.bookfinder.db.BookFinderRepository;
import com.modnsolutions.bookfinder.db.entity.BookEntity;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookFinderRepository repository;

    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookFinderRepository(application);
    }

    public void insert(BookEntity book) {
        repository.insert(book);
    }

    public void remove(BookEntity book) {
        repository.remove(book);
    }

    public void removeAll() {
        repository.removeAll();
    }

    public void update(BookEntity... books) {
        repository.update(books);
    }

    public LiveData<List<BookEntity>> findAllFavorite() {
        return repository.findAllFavorite();
    }

    public BookEntity findFavorite(String bookId) {
        return repository.findFavorite(bookId);
    }
}

package com.modnsolutions.bookfinder.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.modnsolutions.bookfinder.db.dao.BookDao;
import com.modnsolutions.bookfinder.db.entity.BookEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookFinderRepository {

    private BookDao mBookDao;

    public BookFinderRepository(Application application) {
        BookFinderDatabase database = BookFinderDatabase.getDatabase(application);
        mBookDao = database.mBookDao();
    }

    public void insert(BookEntity book) {
        new InsertAsyncTask(mBookDao).execute(book);
    }

    public void remove(BookEntity book) {
        new RemoveAsyncTask(mBookDao).execute(book);
    }

    public void removeAll() {
        new RemoveAllAsyncTask(mBookDao).execute();
    }

    public void update(BookEntity... books) {
        new UpdateAsyncTask(mBookDao).execute(books);
    }

    public LiveData<List<BookEntity>> findAllFavorite() {
        return mBookDao.findAllFavorite();
    }

    public BookEntity findFavorite(String bookId) {
        BookEntity book = null;
        try {
            book = new FindFavoriteAsyncTask(mBookDao).execute(bookId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return book;
    }


    /************************* AsyncTask inner classes ********************************************/
    private static class InsertAsyncTask extends AsyncTask<BookEntity, Void, Void> {

        private BookDao mAsyncTaskDao;

        InsertAsyncTask(BookDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(BookEntity... books) {
            mAsyncTaskDao.insert(books[0]);
            return null;
        }
    }

    private static class RemoveAsyncTask extends AsyncTask<BookEntity, Void, Void> {

        private BookDao mDao;

        RemoveAsyncTask(BookDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(BookEntity... books) {
            mDao.remove(books[0]);
            return null;
        }
    }

    private static class RemoveAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private BookDao mDao;

        RemoveAllAsyncTask(BookDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.removeAll();
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<BookEntity, Void, Void> {
        private BookDao mDao;

        UpdateAsyncTask(BookDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(BookEntity... books) {
            mDao.update(books);
            return null;
        }
    }

    private static class FindFavoriteAsyncTask extends AsyncTask<String, Void, BookEntity> {
        private BookDao mDao;

        FindFavoriteAsyncTask(BookDao dao) {
            mDao = dao;
        }

        @Override
        protected BookEntity doInBackground(String... strings) {
            return mDao.findFavorite(strings[0]);
        }
    }

}

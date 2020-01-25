package it.vonneumannapps.mybilly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db";
    public static final int DATABASE_VERSION = 1;

    public static final String TITLE_COL = "titolo";
    public static final String AUTHOR_COL = "autore";
    public static final String PUBLISHER_COL = "editore";
    public static final String GENRE_COL = "genere";

    public DBManager(@Nullable Context context, @Nullable String name,
                     @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE books (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "titolo TEXT, autore TEXT, editore TEXT, genere TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<ContentValues> getBooks() {

        ArrayList<ContentValues> books = new ArrayList<>();

        try(SQLiteDatabase db = getReadableDatabase()) {

            String query = "SELECT * FROM books";

            Cursor cursor = db.rawQuery(query, null);

            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {

                ContentValues book = new ContentValues();

                book.put("id", cursor.getInt(cursor.getColumnIndex("id")));

                book.put(TITLE_COL, cursor.getString(cursor.getColumnIndex(TITLE_COL)));
                book.put(AUTHOR_COL, cursor.getString(cursor.getColumnIndex(AUTHOR_COL)));
                book.put(PUBLISHER_COL, cursor.getString(cursor.getColumnIndex(PUBLISHER_COL)));
                book.put(GENRE_COL, cursor.getString(cursor.getColumnIndex(GENRE_COL)));

                books.add(book);

                cursor.moveToNext();
            }
        }

        return books;
    }


    void deleteBook(ContentValues book) {
        try(SQLiteDatabase db = getWritableDatabase()) {

            db.delete("books", "id = ?", new String[] {book.getAsInteger("id").toString()});
        }
    }

    void deleteAllBooks() {
        try(SQLiteDatabase db = getWritableDatabase()) {

            db.delete("books", "", null);
        }
    }


    void insertBook(ContentValues book) {
        try(SQLiteDatabase db = getWritableDatabase()) {

            db.insert("books", null, book);
        }
    }

    void updateBook(ContentValues book) {
        try(SQLiteDatabase db = getWritableDatabase()) {

            db.update("books", book, "id = ?",
                    new String[] {book.getAsInteger("id").toString()});
        }
    }
}

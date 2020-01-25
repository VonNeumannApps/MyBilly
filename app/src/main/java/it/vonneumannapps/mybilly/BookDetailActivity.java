package it.vonneumannapps.mybilly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BookDetailActivity extends AppCompatActivity {

    DBManager dbManager;

    ContentValues book;

    EditText titleET;
    EditText authorET;
    EditText publisherET;
    EditText genreET;

    public static final String EXTRA_BOOK_PARAM_NAME = "book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        book = getIntent().getParcelableExtra(EXTRA_BOOK_PARAM_NAME);

        dbManager = new DBManager(this, DBManager.DATABASE_NAME, null, DBManager.DATABASE_VERSION);

        titleET = findViewById(R.id.titleEditText);
        authorET = findViewById(R.id.authorEditText);
        publisherET = findViewById(R.id.publisherEditText);
        genreET = findViewById(R.id.genreEditText);

        loadData();

        Button saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveBook();
            }
        });
    }


    void saveBook() {

        boolean isEditMode = this.book != null;

        if (book == null) {

            book = new ContentValues();
        }

        book.put(DBManager.TITLE_COL, titleET.getText().toString());
        book.put(DBManager.AUTHOR_COL, authorET.getText().toString());
        book.put(DBManager.PUBLISHER_COL, publisherET.getText().toString());
        book.put(DBManager.GENRE_COL, genreET.getText().toString());

        if(isEditMode) {

            dbManager.updateBook(book);
        }
        else {

            dbManager.insertBook(book);
        }

        setResult(RESULT_OK);

        Toast.makeText(this, "Salvataggio effettuato", Toast.LENGTH_SHORT).show();

        finish();
    }


    void loadData() {

        if(this.book != null) {
            titleET.setText(book.getAsString(DBManager.TITLE_COL));
            authorET.setText(book.getAsString(DBManager.AUTHOR_COL));
            publisherET.setText(book.getAsString(DBManager.PUBLISHER_COL));
            genreET.setText(book.getAsString(DBManager.GENRE_COL));
        }
    }

}

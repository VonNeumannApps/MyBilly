package it.vonneumannapps.mybilly;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBManager dbManager;
    ArrayList<ContentValues> booksAndUnderwear = new ArrayList<>();
    BaseAdapter baseAdapter;

    static final int ADD_EDIT_CODE = 100;

    void initializeAdapter() {

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {

                return booksAndUnderwear.size();
            }

            @Override
            public ContentValues getItem(int i) {

                return booksAndUnderwear.get(i);
            }

            @Override
            public long getItemId(int i) {

                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                if (view == null) {

                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                    view = layoutInflater.inflate(R.layout.book_item_layout, viewGroup, false);
                }

                ContentValues book = getItem(i);

                TextView bookTitleTV = view.findViewById(R.id.bookTitleTextView);
                TextView authorTV = view.findViewById(R.id.authorTextView);
                TextView publisherTV = view.findViewById(R.id.publisherTextView);
                TextView genreTV = view.findViewById(R.id.genreTitleTextView);

                bookTitleTV.setText(book.getAsString(DBManager.TITLE_COL));

                authorTV.setText(book.getAsString(DBManager.AUTHOR_COL));
                // book.getAsString("author") + " - " + movie.getAsString("publisher")
                publisherTV.setText("(" + book.getAsString(DBManager.PUBLISHER_COL) + ")");

                genreTV.setText(book.getAsString(DBManager.GENRE_COL));

                return view;
            }
        };
    }

    void loadBooks() {

        booksAndUnderwear.clear();

        booksAndUnderwear.addAll(dbManager.getBooks());

        baseAdapter.notifyDataSetChanged();
    }

    void openBookDetailActivity(ContentValues book) {

        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);

        intent.putExtra(BookDetailActivity.EXTRA_BOOK_PARAM_NAME, book);

        startActivityForResult(intent, ADD_EDIT_CODE);
    }

    void openDeletionConfirmationDialog(final ContentValues book) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Attenzione");
        alert.setMessage("Sei sicuro di volere eliminare l'elemento selezionato?");

        alert.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dbManager.deleteBook(book);

                Toast.makeText(MainActivity.this, "Elemento eliminato", Toast.LENGTH_SHORT).show();

                loadBooks();
            }
        });

        alert.setNegativeButton("NO", null);

        alert.show();
    }


    // questo metodo viene chiamato quando, dopo aver chiamato la activity di modifica o aggiunta di un elemento libro,
    // si ritorna sul questa main activity, ed è necessario aggiornare la lista dei libri (quando è stato aggiunto o
    // modificato con successo un libro)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == ADD_EDIT_CODE) {

            if (resultCode == RESULT_OK) {

                loadBooks();
            }
        }
        else {

            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbManager = new DBManager(this, DBManager.DATABASE_NAME, null, DBManager.DATABASE_VERSION);

        initializeAdapter();
        loadBooks();

        ListView booksLV = findViewById(R.id.booksListView);

        booksLV.setAdapter(baseAdapter);

        booksLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ContentValues book = booksAndUnderwear.get(i);
                openBookDetailActivity(book);
            }
        });

        booksLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ContentValues book = booksAndUnderwear.get(i);
                openDeletionConfirmationDialog(book);

                return true;
            }
        });

        ImageView addBookBtn = findViewById(R.id.addBookButton);

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openBookDetailActivity(null);

            }
        });

        Button deleteBtn = findViewById(R.id.deleteAllBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeleteAllConfirmationDialog();
            }
        });
    }


    void openDeleteAllConfirmationDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Attenzione");
        alert.setMessage("Sei sicuro di volere eliminare tutti gli elementi?");

        alert.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dbManager.deleteAllBooks();

                Toast.makeText(MainActivity.this, "Elementi eliminati", Toast.LENGTH_SHORT).show();

                loadBooks();
            }
        });

        alert.setNegativeButton("NO", null);

        alert.show();
    }
}

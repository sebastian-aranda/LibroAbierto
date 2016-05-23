package cl.usm.libroabierto.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cl.usm.libroabierto.R;

public class BookDetailActivity extends AppCompatActivity {

    // TEST DATA - Data Dummy de la base de datos
    private int bookID;
    private String bookTitle;
    private String bookImageURL;
    private String username = "Juan Perez";
    private String fechaPublc = "10/04/16";
    private String estado = "Usado";
    private String descripcion = "Una descripción llamativa para este libro llamativo para que llame la atención a la gente llamativa por los libros llamativos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Test Data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookID = extras.getInt("BOOK_ID");
            bookTitle = extras.getString("BOOK_TITLE");
            bookImageURL = extras.getString("BOOK_IMAGE");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set book title as toolbar title
        toolbar.setTitle(bookTitle);
        setSupportActionBar(toolbar);

        // Action Bar with back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(bookTitle);

        loadBackdrop();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(bookImageURL).centerCrop().into(imageView);

        final TextView userView = (TextView) findViewById(R.id.usuarioPublicadoDetail);
        userView.setText(username);

        final TextView fechaPublicaciónView = (TextView) findViewById(R.id.fechaPublicacionDetail);
        fechaPublicaciónView.setText(fechaPublc);

        final TextView estadoView = (TextView) findViewById(R.id.estadoDetail);
        estadoView.setText(estado);

        final TextView descriptionView = (TextView) findViewById(R.id.descriptionDetail);
        descriptionView.setText(descripcion);
    }

    // Back on pressed Back Arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);

        return true;
    }
}
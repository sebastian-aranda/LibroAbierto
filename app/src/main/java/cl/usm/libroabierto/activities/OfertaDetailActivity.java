package cl.usm.libroabierto.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cl.usm.libroabierto.R;
import cl.usm.libroabierto.models.Book;
import cl.usm.libroabierto.models.Usuario;
import cl.usm.libroabierto.network.LibroAbiertoAPI;
import cl.usm.libroabierto.network.LibroAbiertoClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OfertaDetailActivity extends AppCompatActivity {

    private int bookID;
    private String bookTitle;
    private String bookAutor;
    private String bookEditorial;
    private String bookFechaPublicacion;
    private int bookEstado;
    private String bookDescripcion;
    private int bookPages;
    private String bookImageUrl;
    private int userID;
    private String userPublish;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookID = extras.getInt("BOOK_ID");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        setSupportActionBar(toolbar);
        // Action Bar with back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Retrofit Book ID
        Retrofit retrofit = LibroAbiertoClient.getClient();
        LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);
        Call<Book> call = api.getBook(bookID);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    bookTitle = response.body().getTitulo();
                    bookAutor = response.body().getAutor();
                    bookEditorial = response.body().getEditorial();
                    bookEstado = response.body().getEstado();
                    bookDescripcion = response.body().getDescripcion();
                    bookPages = response.body().getLargo();
                    bookImageUrl = response.body().getRuta_fotografia();
                    bookFechaPublicacion = response.body().getFecha_publicacion();
                    userID = response.body().getId_usuario();

                    // Obtener el Nombre del Usuario
                    Retrofit retrofit = LibroAbiertoClient.getClient();
                    LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);
                    Call<Usuario> callUser = api.getUserNameById(userID);
                    callUser.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                            if (response.isSuccessful()) {
                                userPublish = response.body().getNombre();

                                final TextView userView = (TextView) findViewById(R.id.usuarioPublicadoDetail);
                                userView.setText(userPublish);
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Log.d("RetrofitFailure", t.toString());
                        }
                    });

                    // Set book title as toolbar title
                    toolbar.setTitle(bookTitle);
                    collapsingToolbar.setTitle(bookTitle);

                    final TextView autorView = (TextView) findViewById(R.id.autorDetail);
                    autorView.setText(bookAutor);

                    final TextView editorialView = (TextView) findViewById(R.id.editorialDetail);
                    editorialView.setText(bookEditorial);

                    final TextView pagesView = (TextView) findViewById(R.id.largoDetail);
                    pagesView.setText(String.valueOf(bookPages));

                    final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
                    Glide.with(OfertaDetailActivity.this).load(bookImageUrl).centerCrop().into(imageView);

                    final TextView fechaPublicaciónView = (TextView) findViewById(R.id.fechaPublicacionDetail);
                    fechaPublicaciónView.setText(bookFechaPublicacion);

                    final TextView estadoView = (TextView) findViewById(R.id.estadoDetail);
                    if (bookEstado == 1) {
                        estadoView.setText(R.string.estado_on);
                    } else {
                        estadoView.setText(R.string.estado_off);
                    }

                    final TextView descriptionView = (TextView) findViewById(R.id.descriptionDetail);
                    descriptionView.setText(bookDescripcion);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d("RetrofitFailure", t.toString());
            }
        });
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(bookImageUrl).centerCrop().into(imageView);

        final TextView userView = (TextView) findViewById(R.id.usuarioPublicadoDetail);
        userView.setText(userPublish);

        final TextView fechaPublicaciónView = (TextView) findViewById(R.id.fechaPublicacionDetail);
        fechaPublicaciónView.setText(bookFechaPublicacion);

        final TextView estadoView = (TextView) findViewById(R.id.estadoDetail);
        if(bookEstado == 1){
            estadoView.setText(R.string.estado_on);
        }
        else{
            estadoView.setText(R.string.estado_off);
        }

        final TextView descriptionView = (TextView) findViewById(R.id.descriptionDetail);
        descriptionView.setText(bookDescripcion);
    }

    // Back on pressed Back Arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);

        return true;
    }
}
package cl.usm.libroabierto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cl.usm.libroabierto.R;
import cl.usm.libroabierto.models.Book;
import cl.usm.libroabierto.models.Usuario;
import cl.usm.libroabierto.network.LibroAbiertoAPI;
import cl.usm.libroabierto.network.LibroAbiertoClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OfertasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Callback<List<Book>> {

    private Context mContext;
    private DatabaseHelper db;
    private Usuario usuario;

    ArrayList<Book> books = new ArrayList<>();
    private BooksAdapter booksAdapter;

    private NavigationView navMenu;
    private Menu lateral;
    private MenuItem opcionPublicaciones;
    private MenuItem opcionPublicarLibro;
    private MenuItem opcionOfertas;
    private MenuItem opcionProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);

        mContext = this;
        db = new DatabaseHelper(this);
        usuario = db.getUsuario();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.opcion_ofertas_title);
        setSupportActionBar(toolbar);

        navMenu = (NavigationView) findViewById(R.id.nav_view);

        lateral = navMenu.getMenu();
        opcionPublicaciones = lateral.findItem(R.id.opcion_publicaciones);
        opcionPublicarLibro = lateral.findItem(R.id.opcion_publicar_libro);
        opcionOfertas = lateral.findItem(R.id.opcion_ofertas);
        opcionProfile = lateral.findItem(R.id.opcion_profile);

        opcionPublicarLibro.setChecked(false);
        opcionProfile.setChecked(false);
        opcionPublicaciones.setChecked(false);
        opcionOfertas.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Offer Book List View
        Retrofit retrofit = LibroAbiertoClient.getClient();
        LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);
        //Call<List<Book>> call = api.getOfferBooks(usuario.getId());
        Call<List<Book>> call = api.getBooks();
        call.enqueue(this);

        setListView();
    }

    /* ===============================
    *    Offer Book List View Section
    *  ===============================
    */
    private void setListView() {

        ListView listViewLibros = (ListView) findViewById(R.id.bookListView);
        booksAdapter = new BooksAdapter(this, R.layout.book_row_in_list, books);
        listViewLibros.setAdapter(booksAdapter);

        listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Load book Data for Offer Detail Activity
                final Book selectedBook = (Book) adapterView.getAdapter().getItem(position);

                Intent intent = new Intent(getApplicationContext(),
                        OfertaDetailActivity.class);

                intent.putExtra("BOOK_ID", selectedBook.getId());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
        if (response.isSuccessful()) {
            books.addAll(response.body());
            booksAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(Call<List<Book>> call, Throwable t) {
        Log.d("RetrofitFailure", t.toString());
    }

    // Offer Book Adapter for Offer Book List
    private class BooksAdapter extends ArrayAdapter<Book> {

        public BooksAdapter(Context context, int resource, ArrayList<Book> objects) {
            super(context, resource, R.id.titleBookTextView, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = super.getView(position, convertView, parent);

            Book book = getItem(position);

            final TextView publicadoTextView = (TextView) row.findViewById(R.id.publicadoBookTextView);
            Retrofit retrofit = LibroAbiertoClient.getClient();
            LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);
            Call<Usuario> call = api.getUsuario(book.getId_usuario());
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    publicadoTextView.setText(response.body().getNombre());
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Log.d("RetrofitFailure", t.toString());
                }
            });

            TextView fechaTextView = (TextView) row.findViewById(R.id.fechaPublicacionBookTextView);
            fechaTextView.setText(book.getFecha_publicacion());

            ImageView avatarImageView = (ImageView) row.findViewById(R.id.avatar);
            Glide.with(OfertasActivity.this).load(book.getRuta_fotografia()).centerCrop().into(avatarImageView);

            return row;
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.opcion_publicar_libro) {
            intent = new Intent(this,
                    PublicarLibroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.opcion_publicaciones) {
            intent = new Intent(this,
                    MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.opcion_profile) {
            intent = new Intent(this,
                    ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
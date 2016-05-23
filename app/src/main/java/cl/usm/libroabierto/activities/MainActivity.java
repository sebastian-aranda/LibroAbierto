package cl.usm.libroabierto.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import cl.usm.libroabierto.R;
import cl.usm.libroabierto.models.Book;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navMenu;
    private Menu lateral;
    private MenuItem opcionPublicaciones;
    private MenuItem opcionPublicarLibro;
    private MenuItem opcionOfertas;
    private MenuItem opcionProfile;

    // Books Test Data
    Book[] books = {
            new Book(1,"Harry Potter y el Caliz de Fuego", "Autor Uno", 10, "Una descripcion llamativa", "http://mla-s2-p.mlstatic.com/harry-potter-y-el-caliz-de-fuego-ano-4-tapa-dura-13740-MLA20080157434_042014-F.jpg", "10/04/16"),
            new Book(2,"100 Años de Soledad", "Autor Dos", 12, "Otra descripcion llamativa", "http://static.animalpolitico.com/wp-content/uploads/2014/05/Libro-100-456x304.jpg", "12/02/16"),
            new Book(3,"Matemáticas 8vo Básico", "Autor Tres", 14, "Una descripcion con muchos números", "http://img.yapo.cl/images/47/4729479539.jpg", "9/04/16"),
            new Book(4,"Algun Otro Libro", "Autor Cuatro", 16, "Alguna otra descripcion", "http://www.radiozero.cl/static/2016/04/libros.jpg", "20/03/16")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.opcion_publicaciones_title);
        setSupportActionBar(toolbar);

        navMenu = (NavigationView) findViewById(R.id.nav_view);

        lateral = navMenu.getMenu();
        opcionPublicaciones = lateral.findItem(R.id.opcion_publicaciones);
        opcionPublicarLibro = lateral.findItem(R.id.opcion_publicar_libro);
        opcionOfertas = lateral.findItem(R.id.opcion_ofertas);
        opcionProfile = lateral.findItem(R.id.opcion_profile);

        opcionPublicarLibro.setChecked(false);
        opcionOfertas.setChecked(false);
        opcionProfile.setChecked(false);
        opcionPublicaciones.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Book List View
        setListView();
    }

    /* ===========================
    *    Book List View Section
    *  ===========================
    */
    private void setListView() {

        ListView listViewLibros = (ListView) findViewById(R.id.bookListView);

        BooksAdapter booksAdapter = new BooksAdapter(this,
                R.layout.book_row_in_list,
                books);

        listViewLibros.setAdapter(booksAdapter);

        listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Load book Data for Detail Activity
                Book selectedBook = (Book) adapterView.getAdapter().getItem(position);

                Intent intent = new Intent(getApplicationContext(),
                        BookDetailActivity.class);
                intent.putExtra("BOOK_ID",selectedBook.getBookID());
                //Test Data - Delete after Database setting
                intent.putExtra("BOOK_TITLE",selectedBook.getTitulo());
                intent.putExtra("BOOK_IMAGE", selectedBook.getDrawable());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.opcion_publicar_libro)
        {
            intent = new Intent(this,
                    PublicarLibroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else if (id == R.id.opcion_ofertas)
        {
            intent = new Intent(this,
                    OfertasActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else if (id == R.id.opcion_profile)
        {
            intent = new Intent(this,
                    ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Book Adapter for Book List
    private class BooksAdapter extends ArrayAdapter<Book> {

        public BooksAdapter(Context context, int resource, Book[] objects) {
            super(context, resource,R.id.titleBookTextView, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = super.getView(position, convertView, parent);

            Book book = getItem(position);

            TextView publicadoTextView = (TextView)row.findViewById(R.id.publicadoBookTextView);
            //Dato en la clase entregada en un int, la cual posteriormente se obtendra el nombre a traves del ID
            publicadoTextView.setText("Juan Perez");

            TextView fechaTextView = (TextView)row.findViewById(R.id.fechaPublicacionBookTextView);
            fechaTextView.setText(book.getFechaPublicacion());

            ImageView avatarImageView = (ImageView)row.findViewById(R.id.avatar);
            Glide.with(MainActivity.this).load(book.getDrawable()).centerCrop().into(avatarImageView);

            return row;
        }
    }
}


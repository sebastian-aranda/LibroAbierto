package cl.usm.libroabierto.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import cl.usm.libroabierto.R;
import cl.usm.libroabierto.models.Book;
import cl.usm.libroabierto.network.LibroAbiertoAPI;
import cl.usm.libroabierto.network.LibroAbiertoClient;
import retrofit2.Call;
import retrofit2.Retrofit;

public class OfertarLibroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertar_libro);

    }
}

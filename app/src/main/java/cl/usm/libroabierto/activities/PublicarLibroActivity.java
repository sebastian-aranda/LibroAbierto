package cl.usm.libroabierto.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cl.usm.libroabierto.R;

public class PublicarLibroActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NavigationView navMenu;
    private Menu lateral;
    private MenuItem opcionPublicaciones;
    private MenuItem opcionPublicarLibro;
    private MenuItem opcionOfertas;
    private MenuItem opcionProfile;

    private Uri fileUri;

    private ImageView previewImage;
    private Button btnTakePicture, btnSelectPicture;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private static final String IMAGE_DIRECTORY_NAME = "libroabierto_fotos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_libro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.opcion_publicar_libro_title);
        setSupportActionBar(toolbar);

        navMenu = (NavigationView) findViewById(R.id.nav_view);

        lateral = navMenu.getMenu();
        opcionPublicaciones = lateral.findItem(R.id.opcion_publicaciones);
        opcionPublicarLibro = lateral.findItem(R.id.opcion_publicar_libro);
        opcionOfertas = lateral.findItem(R.id.opcion_ofertas);
        opcionProfile = lateral.findItem(R.id.opcion_profile);

        opcionOfertas.setChecked(false);
        opcionProfile.setChecked(false);
        opcionPublicaciones.setChecked(false);
        opcionPublicarLibro.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        previewImage = (ImageView) findViewById(R.id.previewImage);

        btnTakePicture = (Button) findViewById(R.id.tomarFoto);
        btnSelectPicture =(Button) findViewById(R.id.seleccionarFoto);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });
    }

    public void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void selectPicture(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK){
                showPicture();
            }
            else if (resultCode == RESULT_CANCELED)
                Toast.makeText(getApplicationContext(), "Captura cancelada", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Error al capturar la imagen", Toast.LENGTH_LONG).show();
        }
        else if (requestCode == GALLERY_IMAGE_REQUEST_CODE){
            Uri image = data.getData();
            Bitmap bitmap = null;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                previewImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void showPicture(){
        String path = fileUri.getPath();
        BitmapFactory.Options options = new BitmapFactory.Options();

        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        previewImage.setImageBitmap(bitmap);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Error al crear el directorio"+ IMAGE_DIRECTORY_NAME);
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "libroabierto_" + timeStamp + ".jpg");
        return mediaFile;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.opcion_publicaciones)
        {
            intent = new Intent(this,
                    MainActivity.class);
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
}

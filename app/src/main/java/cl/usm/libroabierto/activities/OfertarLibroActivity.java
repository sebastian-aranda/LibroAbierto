package cl.usm.libroabierto.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cl.usm.libroabierto.R;
import cl.usm.libroabierto.models.ApiResponse;
import cl.usm.libroabierto.models.Book;
import cl.usm.libroabierto.models.Usuario;
import cl.usm.libroabierto.network.LibroAbiertoAPI;
import cl.usm.libroabierto.network.LibroAbiertoClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OfertarLibroActivity extends AppCompatActivity {

    private int bookID;
    private int ofertadoID;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private static DatabaseHelper db;

    private Usuario usuario;
    private Book book;

    private Uri fileUri;
    private Bitmap imagenLibro;

    private ImageView previewImage;
    private Button btnTakePicture, btnSelectPicture, btnPublishBook;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private static final String IMAGE_DIRECTORY_NAME = "libroabierto_fotos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertar_libro);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ofertadoID = extras.getInt("OFERTADO_ID");
            bookID = extras.getInt("BOOK_ID");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ofertar_libro_title);
        setSupportActionBar(toolbar);

        // Action Bar with back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mContext = this;
        db = new DatabaseHelper(this);
        usuario = db.getUsuario();

        previewImage = (ImageView) findViewById(R.id.previewImage);

        btnTakePicture = (Button) findViewById(R.id.tomarFoto);
        btnSelectPicture =(Button) findViewById(R.id.seleccionarFoto);
        btnPublishBook = (Button) findViewById(R.id.btn_publicar_libro);

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

        btnPublishBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishBook();
            }
        });

        final ToggleButton toggleState = (ToggleButton) findViewById(R.id.toggleStateButton);
        toggleState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
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

    public void publishBook(){
        String titulo = ((EditText) findViewById(R.id.libro_nombre)).getText().toString();
        String autor = ((EditText) findViewById(R.id.libro_autor)).getText().toString();
        String editorial = ((EditText) findViewById(R.id.libro_editorial)).getText().toString();
        int largo = Integer.parseInt(((EditText) findViewById(R.id.libro_largo)).getText().toString());
        String descripcion = ((EditText) findViewById(R.id.libro_descripcion)).getText().toString();
        int estado = (((ToggleButton) findViewById(R.id.toggleStateButton)).isChecked()) ? 1 : 0;

        book = new Book(0,titulo,autor,editorial,estado,largo,descripcion,"",usuario.getId(),"", ofertadoID);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        String encodedImage = null;
        if (imagenLibro != null){
            imagenLibro.compress(Bitmap.CompressFormat.PNG, 100, bao);
            byte[] ba = bao.toByteArray();
            encodedImage = Base64.encodeToString(ba, Base64.DEFAULT);
        }
        else
            encodedImage = "";

        Retrofit retrofit = LibroAbiertoClient.getClient();
        LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);
        Call<ApiResponse> callUploadImagen = api.uploadImagenLibro(encodedImage);
        callUploadImagen.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body().getState() == 1) {
                    Log.d("Retrofit response", response.body().toString());
                    Retrofit retrofit = LibroAbiertoClient.getClient();
                    LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);

                    Call<ApiResponse> callAddBook = api.addOfferBook(book.getTitulo(), book.getAutor(), book.getEditorial(), book.getEstado(), book.getLargo(), book.getDescripcion(), response.body().getMsg(), usuario.getId(), book.getId_usuario_ofertado());
                    callAddBook.enqueue(new Callback<ApiResponse>() {

                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("Retrofit response", response.body().toString());
                                Toast.makeText(mContext, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                                // Retorna a la activity anterior tras el exito
                                Intent intent = new Intent(getApplicationContext(), BookDetailActivity.class);
                                intent.putExtra("BOOK_ID", bookID);
                                startActivityForResult(intent, 0);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Log.d("RetrofitFailure", t.toString());
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("RetrofitFailure", t.toString());
            }
        });
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
                String path = fileUri.getPath();
                BitmapFactory.Options options = new BitmapFactory.Options();

                // down sizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;
                imagenLibro = BitmapFactory.decodeFile(path, options);
                previewImage.setImageBitmap(imagenLibro);
            }
            else if (resultCode == RESULT_CANCELED)
                Toast.makeText(getApplicationContext(), "Captura cancelada", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Error al capturar la imagen", Toast.LENGTH_LONG).show();
        }
        else if (requestCode == GALLERY_IMAGE_REQUEST_CODE){
            if (data == null) return;
            Uri image = null;
            try{
                image = data.getData();
                imagenLibro = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                previewImage.setImageBitmap(imagenLibro);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
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
                Log.d(TAG, "Error al crear el directorio" + IMAGE_DIRECTORY_NAME);
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "libroabierto_" + timeStamp + ".jpg");
        return mediaFile;
    }

    // Back on pressed Back Arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), BookDetailActivity.class);

        intent.putExtra("BOOK_ID", bookID);

        startActivityForResult(intent, 0);

        return true;
    }
}

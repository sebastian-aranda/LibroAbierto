package cl.usm.libroabierto.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cl.usm.libroabierto.models.Usuario;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "libroabierto";
    private static final String DATABASE_PATH = "//data/data/cl.usm.libroabierto/databases/"+DATABASE_NAME;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        createUsuario(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onCreate(db);
    }

    public SQLiteDatabase getDatabase(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db;
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null,SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return checkDB != null ? true : false;
    }

    //USUARIO
    public void createUsuario(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("CREATE TABLE usuario (id INTEGER PRIMARY KEY, nombre TEXT, telefono TEXT, email TEXT UNIQUE, foto TEXT)");
        db.execSQL("INSERT INTO usuario (id, nombre, telefono, email, foto) VALUES (0, '', '', '', '')");
    }

    public void updateUsuario(String nombre, String telefono, String email, String foto){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("telefono", telefono);
        values.put("email", email);
        values.put("foto", foto);

        db.update("usuario", values, " id = ?", new String[] {"0"});

        db.close();

    }

    public Usuario getUsuario(){
        String query = "SELECT * FROM usuario";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Usuario usuario = null;
        if (cursor != null){
            try{
                cursor.moveToFirst();
                usuario = new Usuario(0, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            } catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            finally {
                cursor.close();
            }
        }

        return usuario;
    }

    /*public ArrayList<Usuario> getAllUsuarios(){
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        String query = "SELECT * FROM usuario";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        return usuarios;
    }*/
}

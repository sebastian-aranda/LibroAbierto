package cl.usm.libroabierto.models;

/**
 * Created by Slavko Cotoras on 22-05-2016.
 */
public class Book {
    private int bookID;
    private String titulo;
    private String autor;
    private String editorial;
    private int estado;
    private int largo;
    private String descripcion;
    private String ruta_fotografia;
    private int id_usuario;
    private String fecha_publicacion;

    private String drawable;

    public Book(int id, String titulo, String autor, String editorial, int estado, int largo, String descripcion, String ruta_fotografia,int id_usuario, String fecha_publicacion)
    {
        this.bookID = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado;
        this.largo = largo;
        this.descripcion = descripcion;
        this.ruta_fotografia = ruta_fotografia;
        this.id_usuario = id_usuario;
        this.fecha_publicacion = fecha_publicacion;
    }

    public int getBookID() { return bookID; }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getLargo() {
        return largo;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuta_fotografia() {
        return ruta_fotografia;
    }

    public void setRuta_fotografia(String ruta_fotografia) {
        this.ruta_fotografia = ruta_fotografia;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(String fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    @Override
    public String toString() {
        return titulo;
    }
}


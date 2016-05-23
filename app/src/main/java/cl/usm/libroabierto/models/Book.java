package cl.usm.libroabierto.models;

/**
 * Created by Slavko Cotoras on 22-05-2016.
 */
public class Book {

    private int bookID;
    private String titulo;
    private String autor;
    private int usuario;
    private String descripcion;
    private String drawable;
    private String fechaPublicacion;

    public Book(int id, String titulo, String autor, int usuario, String descripcion, String drawable, String fechaPublicacion)
    {
        this.bookID = id;
        this.titulo = titulo;
        this.autor = autor;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.drawable = drawable;
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getBookID() { return bookID; }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String nombre) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable)
    {
        this.drawable = drawable;
    }

    public String getFechaPublicacion() { return fechaPublicacion; }

    public void setFechaPublicacion(String fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    @Override
    public String toString() {
        return titulo;
    }
}


package cl.usm.libroabierto.models;

public class Usuario {
    private int id;
    private String nombre;
    private String telefono;
    private String email;
    private String foto;

    public Usuario(int id, String nombre, String telefono, String email, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}

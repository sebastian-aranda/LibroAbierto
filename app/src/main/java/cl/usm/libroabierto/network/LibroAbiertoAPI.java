package cl.usm.libroabierto.network;

import java.util.List;

import cl.usm.libroabierto.models.ApiResponse;
import cl.usm.libroabierto.models.Book;
import cl.usm.libroabierto.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LibroAbiertoAPI {
    @GET("libroabierto_api.php?accion=obtenerLibros")
    Call<List<Book>> getBooks();

    @GET("libroabierto_api.php?accion=obtenerUsuario")
    Call<Usuario> getUsuario(@Query("email") String email);

    @GET("libroabierto_api.php?accion=obtenerNombreUsuarioById")
    Call<Usuario> getUserNameById(@Query("id_usuario") int id_usuario);

    @GET("libroabierto_api.php?accion=obtenerLibro")
    Call<Book> getBook(@Query("id_libro") int id_libro);

    @FormUrlEncoded
    @POST("libroabierto_api.php?accion=agregarLibro")
    Call<ApiResponse> addBook(@Field("titulo") String titulo, @Field("autor") String autor, @Field("editorial") String editorial, @Field("estado") int estado, @Field("largo") int largo, @Field("descripcion") String descripcion, @Field("ruta_fotografia") String ruta_fotografia, @Field("id_usuario") int id_usuario);

    @FormUrlEncoded
    @POST("libroabierto_api.php?accion=agregarUsuario")
    Call<ApiResponse> addUsuario(@Field("nombre") String nombre, @Field("telefono") String telefono, @Field("email") String email, @Field("foto") String foto);
}

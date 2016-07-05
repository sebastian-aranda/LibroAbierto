package cl.usm.libroabierto.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibroAbiertoClient {
    //REST
    public static final String BASE_URL = "http://192.168.0.50/sites/libro_abierto/";
    //public static final String BASE_URL = "http://libroabierto.cotoras.cl/";

    public static Retrofit getClient() {

        OkHttpClient httpClient = new OkHttpClient();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }
}

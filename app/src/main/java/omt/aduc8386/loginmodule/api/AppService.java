package omt.aduc8386.loginmodule.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppService {

    private static Service service;
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Service init() {
        if (service != null) {
            return service;
        }
        service = retrofit.create(Service.class);
        return service;
    }
}

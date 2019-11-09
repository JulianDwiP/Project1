package com.example.project.Api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
        public  static  String BASE_URL ="http://192.168.43.143/perpus_db/";

    private static Retrofit retrofit = null ;

        public static  Retrofit getClient(String baseUrl){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            if (retrofit == null){
                retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client).build();
            }
            return retrofit;
        }

        public static ApiInterface getAPIService(){
            return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
        }
}

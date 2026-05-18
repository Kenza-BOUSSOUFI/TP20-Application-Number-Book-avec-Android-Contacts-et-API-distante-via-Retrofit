package com.example.tp20_application_number_book_avec_android_contacts_et_api_distante_via_retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_LINK = "http://10.0.2.2/numberbook-api/api/";
    private static Retrofit instance;

    public static Retrofit getRetrofitInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_LINK)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}

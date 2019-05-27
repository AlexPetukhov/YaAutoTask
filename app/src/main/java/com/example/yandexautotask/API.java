package com.example.yandexautotask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;


public final class API {
    public static final String API_URL = "https://api.github.com";


    public interface Get {
        @GET("/search/repositories")
        Call<RepoObject> search(@Query("q") String query);
    }

    public interface Load {
        @GET("/search/repositories")
        Call<RepoObject> search(@Query("q") String query, @Query("page") String page);
    }

    public static RepoObject get(String query) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Get github = retrofit.create(Get.class);
            Call<RepoObject> call = github.search(query);
            RepoObject repoObj = call.execute().body();
            return repoObj;
        }catch (IOException e){
            return new RepoObject();
        }
    }

    public static RepoObject load(String query, String sPage) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Load github = retrofit.create(Load.class);
            Call<RepoObject> call = github.search(query, sPage);
            RepoObject repoObj = call.execute().body();
            return repoObj;
        }catch (IOException e){
            return new RepoObject();
        }
    }
}

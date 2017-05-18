package com.matteoveroni.wordsremember.online;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Matteo Veroni
 */

public interface WebDictionaryReaderService {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    //    public static final String TRANSLTR_BASE_URL = "http://transltr.org/api/translate?text=ciao&to=en";
    public static final String TRANSLTR_BASE_URL = "http://transltr.org/api/";
    public static final String GLOSBE_BASE_URL = "https://glosbe.com/gapi/";

    @GET("translate")
    Call<Void> getTranslation(@Query("text") String wordToTranslate, @Query("to") String translationLanguage);
//
//
//    @GET("translate")
//    Call<User> getUser(@Path("username") String username);
//
//
//    @GET("group/{id}/users")
//    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);
//
//
//    @POST("users/new")
//    Call<User> createUser(@Body User user);
//
//}
}

package com.indra.elections.peruvian.services;


import com.indra.elections.peruvian.json.Eleccion;
import com.indra.elections.peruvian.json.MyResponse;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by lemenzm on 09/05/2016.
 */
public interface API {

    public static final String SERVICE_ENDPOINT = "http://10.1.1.30:8080";

    @Headers({"Content-Type: application/json"})
    @POST("/service/rescue/user")
    Observable<MyResponse> checkUser(@Body Eleccion eleccion);


}

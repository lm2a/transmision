package com.indra.elections.peruvian.services;

//import ie.aaireland.android.theaa.model.CarResponse;
//import ie.aaireland.android.theaa.model.Rescue;
//import ie.aaireland.android.theaa.model.RescueResponse;
//import ie.aaireland.android.theaa.model.TrafficResult;
//import ie.aaireland.android.theaa.model.User;
//import ie.aaireland.android.theaa.model.UserResponse;
//import ie.aaireland.android.theaa.model.routes.DirectionResults;
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

    public static final String SERVICE_ENDPOINT = "http://192.168.2.1:8080";//http://192.168.2.1:8080";//http://aaweb.staging.theaa.local";//http://10.0.2.2:8080";//http://192.168.1.42:8080";//http://192.168.43.239:8080";//http://aaweb.staging.theaa.local";//http://192.168.1.42:8080";//http://192.168.1.42:8080";//https://www.theaa.ie";//http://www.lurpiany.ie";//http://192.168.43.239:8080";//http://webuat3.theaa.local";//"https://www.theaa.ie";


    @GET("/service/roadwatch/traffic")
    Observable<TrafficResult> getIncidents();

    @Headers({"Authorization: 3pigTZuuyIJLALcfr9o68aYA4k6iyNVoQGACltRrtLTU", "Content-Type: application/json"})
    @POST("/service/rescue/user")
    Observable<UserResponse> checkUser(@Body User user);


}

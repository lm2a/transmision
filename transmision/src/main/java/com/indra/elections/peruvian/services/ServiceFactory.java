package com.indra.elections.peruvian.services;


import com.squareup.okhttp.OkHttpClient;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.util.concurrent.TimeUnit;


import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.SimpleXMLConverter;


public class ServiceFactory {

    /**
     * Creates a retrofit service (client) from an arbitrary class (clazz, which contains the REST method call, API.java in this app)
     * @param clazz Java interface of the retrofit service
     * @param endPoint REST endpoint url
     * @return retrofit service with defined endpoint
     */
    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(endPoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        T service = restAdapter.create(clazz);

        return service;
    }

    /**
     * Creates a retrofit service (client) from an arbitrary class (clazz, which contains the REST method call, API.java in this app)
     * @param clazz Java interface of the retrofit service
     * @param endPoint REST endpoint url
     * @return retrofit service with defined endpoint
     */
    public static <T> T createRetrofitXMLService(final Class<T> clazz, final String endPoint) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(endPoint)
                .setConverter(new SimpleXMLConverter(serializer))
                .build();
        T service = restAdapter.create(clazz);

        return service;
    }




}
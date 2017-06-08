package com.indra.elections.peruvian.services;


import com.squareup.okhttp.OkHttpClient;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.util.concurrent.TimeUnit;

import ie.aaireland.android.theaa.customs.AAException;
import ie.aaireland.android.theaa.model.ErrorResponse;
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
                .setLogLevel(RestAdapter.LogLevel.FULL) //TODO remember to remove
                .setErrorHandler(new CustomErrorHandler())
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
                .setErrorHandler(new CustomErrorHandler())
                .build();
        T service = restAdapter.create(clazz);

        return service;
    }


    /**
     * Converts the complex error structure into a single string you can get with error.getLocalizedMessage() in Retrofit error handlers.
     * Also deals with there being no network available
     *
     * Uses a few string IDs for user-visible error messages
     */
    private static class CustomErrorHandler implements ErrorHandler {

        public CustomErrorHandler() {
        }

        @Override
        public Throwable handleError(RetrofitError cause) {

            String errorDescription;
            AAException.Causes causes;
            String url = cause.getUrl();

            if (cause.isNetworkError()) {
                errorDescription = "Network error";//ctx.getString(R.string.error_network);
                causes = AAException.Causes.NETWORK_ERROR;
            } else {
                if (cause.getResponse() == null) {
                    errorDescription = "Server not responding";//ctx.getString(R.string.error_no_response);
                    causes = AAException.Causes.SERVER_DOWN;
                } else {

                    // Error message handling - return a simple error to Retrofit handlers..
                    try {
                        ErrorResponse errorResponse = (ErrorResponse) cause.getBodyAs(ErrorResponse.class);
                        errorDescription = errorResponse.error.data.message;
                        causes = AAException.Causes.X_ERROR;
                    } catch (Exception ex) {
                        try {
                            errorDescription = cause.getResponse().getStatus()+" - "+cause.getMessage();//ctx.getString(R.string.error_network_http_error, cause.getResponse().getStatus());
                            causes=AAException.Causes.HTTP_ERROR;
                        } catch (Exception ex2) {
                            //AALog.e(TAG, "handleError: " + ex2.getLocalizedMessage());
                            errorDescription = "Unknown error - "+ex2.getMessage();//ctx.getString(R.string.error_unknown);
                            causes = AAException.Causes.X_ERROR_II;
                        }
                    }
                }
            }
            //sending email with exception
/*            try {
                GMailSender sender = new GMailSender("juanhesse@gmail.com", "pokokrebay");
                sender.sendMail("This is Subject",
                        "This is Body",
                        "juanhesse@gmail.com",
                        "lamenza@gmail.com");
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            return new AAException(causes, errorDescription, url);
        }
    }



}
package org.eclipse.jkube;


import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {
    private static final String COUNTRY_API = "http://countryapi.gear.host/v1/Country/getCountries";

    @GET
    @Path("/search")
    public String search(@QueryParam("name") String name) {
        return performGetToCountryAPI(name);
    }

    private String performGetToCountryAPI(String countryName) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = HttpUrl.get(COUNTRY_API).newBuilder()
                .addQueryParameter("pName", countryName)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                return "{\"message\": \"Nothing received from Country API. url: "+httpUrl.toString() +"\", }";
            }
        } catch (IOException exception) {
            return "{\"message\": \"error in connecting to Country API. url:" + httpUrl.toString() + " " + exception.getMessage() + "}";
        }
    }
}

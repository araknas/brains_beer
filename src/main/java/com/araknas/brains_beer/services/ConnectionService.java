package com.araknas.brains_beer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Giedrius on 2018-04-24.
 */

@Service
public class ConnectionService {
    public static final String URL_FOR_INTERNET_CONNECTION_CHECKING = "https://www.google.com/";
    public static final String INTERNET_CONNECTION_STATUS_OK = "connected";
    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");

    public String checkInternetConnectionStatus() {
        String status = "";
        try{
            URL url = new URL(URL_FOR_INTERNET_CONNECTION_CHECKING);
            URLConnection connection = url.openConnection();
            connection.connect();
            status = "connected";
        }
        catch (Exception e){
             status = "Failed with message = " + e.getMessage();
            logger.error("Exception connecting to the internet, e = " + status);
        }
        return status;
    }
}

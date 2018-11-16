/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restClient;

import java.util.Base64;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author HaiNT83
 */
public class RestClientOauth2Token {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RestClientOauth2Token.class);
    
    private static final String OAUTH2_TOKEN_PATH = "/oauth2/token";
    
    private static final String OAUTH2_GRANT_TYPE = "client_credentials";
    
    /** The base URL. */
    private String baseURL ;
    
    /** The consumer key. */
    private String consumerKey;
    
    /** The consumer secret. */
    private String consumerSecret;
    
    /**
     *
     * @param baseURL
     * The base URL to set.
     */
    public void setBaseURL(final String baseURL) {
        this.baseURL = baseURL;
    }
    
    /**
     *
     * @param consumerKey
     * The consumer key to set.
     */
    public void setConsumerKey(final String consumerKey) {
        this.consumerKey = consumerKey;
    }
    
    /**
     *
     * @param consumerSecret
     * The consumer secret to set.
     */
    public void setConsumerSecret(final String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }
    
    public String getOauth2Token() {
        
        try {
            byte[] encodedBytes = Base64.getEncoder().encode((this.consumerKey + ":" + this.consumerSecret).getBytes());
            final String credential = "Basic " + new String(encodedBytes);
            
            final URL targetUrl = new URL(this.baseURL + OAUTH2_TOKEN_PATH);
            final HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty( "charset", "utf-8");
            httpConnection.setRequestProperty("Authorization", credential);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            final String input  = "grant_type=" + OAUTH2_GRANT_TYPE;            
            final OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(input.getBytes());
            outputStream.flush();
            if (!"OK".equals(httpConnection.getResponseMessage())) {
                throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
            }
            
            final BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
            final StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = responseBuffer.readLine()) != null) {
                outputBuilder.append(line);
                outputBuilder.append('\n');
            }
            final String output = outputBuilder.toString();
            httpConnection.disconnect();
            
            final JSONParser parser = new JSONParser();
            final Object obj = parser.parse(output);
            final JSONObject jsonObject = (JSONObject) obj;
            
            final String accessToken = (String) jsonObject.get("access_token");
            return accessToken;
            
        } catch(IOException | RuntimeException | ParseException e) {
            LOGGER.debug("{} error: {}.", RestClientOauth2Token.class.getName(), e);
            throw new RuntimeException("Failed: Get access token error.");
        }
    }
    
}

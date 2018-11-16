/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restClient;

import java.util.HashMap;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author HaiNT83
 */
public class RestClientHttpRequest {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RestClientHttpRequest.class);
    
    /** Get Oauth2 token. */
    private RestClientOauth2Token oauth2Token;
    
    /** The base URL. */
    private String baseURL;
    
    /** The actor. */
    private String defaultActor;
    
    public void setOauth2Token(final RestClientOauth2Token oauth2Token) {
        this.oauth2Token = oauth2Token;
    }
    
    public void setBaseURL(final String baseURL) {
        this.baseURL = baseURL;
    }
    
    public void setDefaultActor(final String defaultActor) {
        this.defaultActor = defaultActor;
    }
    
    public JSONObject getHttpRequest(final String username, final String path, 
            final HashMap<String, String> urlParameters) {
        
        try {
            final String accessToken = this.oauth2Token.getOauth2Token();
            String actor = username;
            if (actor == null) {
                actor = this.defaultActor;
            }
            String urlPath = path + "?actor=" + URLEncoder.encode(actor, "UTF-8");
            
            if(!urlParameters.isEmpty()) {
                for(Map.Entry<String, String> entry: urlParameters.entrySet()) {
                    final String key = entry.getKey();
                    final String value = entry.getValue();
                    urlPath += ("&" + key + "=" + URLEncoder.encode(value, "UTF-8"));
                }
            }
            
            final URL targetUrl = new URL(this.baseURL + urlPath);
            final HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty( "charset", "utf-8");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            
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
            return jsonObject;
            
        } catch(IOException | ParseException e) {
            LOGGER.debug("{} error: {}.", RestClientHttpRequest.class.getName(), e);
            throw new RestClientHttpException("Failed: GET http request error.");
        }
    }
    
    public JSONObject postHttpRequest(final String username, final String path, 
            final String inputData, final HashMap<String, String> urlParameters) {
        
        try {
            final String accessToken = this.oauth2Token.getOauth2Token();
            String actor = username;
            if (actor == null) {
                actor = this.defaultActor;
            }
            String urlPath = path + "?actor=" + URLEncoder.encode(actor, "UTF-8");
            
            if(!urlParameters.isEmpty()) {
                for(Map.Entry<String, String> entry: urlParameters.entrySet()) {
                    final String key = entry.getKey();
                    final String value = entry.getValue();
                    urlPath += ("&" + key + "=" + URLEncoder.encode(value, "UTF-8"));
                }
            }
            
            final URL targetUrl = new URL(this.baseURL + urlPath);
            final HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty( "charset", "utf-8");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            
            final OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(inputData.getBytes());
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
            return jsonObject;
            
        } catch(IOException | RuntimeException | ParseException e) {
            LOGGER.debug("{} error: {}.", RestClientHttpRequest.class.getName(), e);
            throw new RestClientHttpException("Failed: POST http request error.");
        } 
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restClient;

/**
 *
 * @author HaiNT83
 */
public class RestClientHttpException extends RuntimeException{
    
    /**
     * Builds an exception with the provided error message.
     *
     * @param message the error message.
     */
    public RestClientHttpException(String message) {
        super(message);
    }
    
    /**
     * Builds an exception with the provided error message and the provided cause.
     *
     * @param message the error message.
     * @param cause the cause.
     */
    public RestClientHttpException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

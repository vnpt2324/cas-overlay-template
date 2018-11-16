package authentication;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * An abstract implementation of the that provides access to
 * the needed scaffolding and services that are necessary to CAS, such as ticket registry, service registry, etc.
 * The intention here is to allow extensions to easily benefit these already-configured components
 * without having to to duplicate them again.
 *
 * @author datpm1
 * @since 4.2.7
 */
public class Users {
    private String username;
    private String password;
    private String ipAddress;
    /**
     *
     */
    public Users() {}
    /**
     * @param username
     * the username
     * @param password
     * the password
     * @param ipAddress
     * the ipAddress
     */
    public Users(final String username, final String password, final String ipAddress) {
        this.username = username;
        this.password = password;
        this.ipAddress = ipAddress;
    }
    /**
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }
    /**
     * @param username
     * the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }
    /**
     * @param password
     * the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }
    /**
     * @return the IP address.
     */
    public String getIpAddress() {
        return this.ipAddress;
    }
    /**
     * @param ipAddress
     * the IP address to set.
     */
    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
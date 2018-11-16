package authentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import restClient.RestClientHttpRequest;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationHandler.class);

    public CustomAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    private RestClientHttpRequest httpRequest;

    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential,
                                                                                        final String originalPassword) {

        try {
            LOGGER.info("Get the IP address");
            // Get the IP address
            final HttpServletRequest request;
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            final String ipAddress = request.getRemoteAddr();
            final String username = credential.getUsername();
            LOGGER.debug("Username = {}", username);
            final String password = credential.getPassword();

            final HashMap<String, String> urlParameters = new HashMap<>();
            final Users user = new Users(username, password, ipAddress);
            final ObjectMapper objectMapper = new ObjectMapper();
            final String inputData = objectMapper.writeValueAsString(user);

//            final JSONObject jsonObject = this.httpRequest.postHttpRequest(username, URI_LOGIN, inputData, urlParameters);
//            final boolean success = (boolean) jsonObject.get("success");
//
//            if (!success) {
//                throw new FailedLoginException();
//            }

            if (!username.equals((String)("datpm1"))){
                throw new FailedLoginException("Sorry, you are a failure!");
            }

            return createHandlerResult(credential,
                    this.principalFactory.createPrincipal(username), new ArrayList<>());
        } catch (final Exception e){
            LOGGER.debug("{} error: {}.", CustomAuthenticationHandler.class.getName(), e);
            throw new RuntimeException("Failed: Login error.");
        }

    }
}
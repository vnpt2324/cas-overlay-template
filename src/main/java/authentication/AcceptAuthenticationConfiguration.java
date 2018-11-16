package authentication;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalFactoryUtils;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import restClient.RestHttpProperties;

@Configuration("AcceptAuthenticationConfiguration")
//@EnableConfigurationProperties({CasConfigurationProperties.class, RestHttpProperties.class})
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class AcceptAuthenticationConfiguration
        implements AuthenticationEventExecutionPlanConfigurer {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AcceptAuthenticationConfiguration.class);

    @Autowired
    private CasConfigurationProperties casProperties;

//    @Autowired
//    private RestHttpProperties customProperties;

//    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @ConditionalOnMissingBean(name = "myAuthenticationPrincipalFactory")
    @Bean
    @RefreshScope
    public PrincipalFactory myAuthenticationPrincipalFactory() {
        return PrincipalFactoryUtils.newPrincipalFactory();
    }

    @RefreshScope
    @Bean
    public AuthenticationHandler myAuthenticationHandler() {
        LOGGER.debug("Location = ", casProperties.getServiceRegistry().getJson().getLocation());

        final CustomAuthenticationHandler handler = new CustomAuthenticationHandler("abc", servicesManager, myAuthenticationPrincipalFactory(), 0);
        return handler;
    }

    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(myAuthenticationHandler());
    }
}

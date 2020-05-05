package br.com.megahack.api;

import br.com.megahack.api.filters.JWTAuthenticationFilter;
import br.com.megahack.api.services.TokenAuthenticationService;
import br.com.megahack.api.business.security.GlobalPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
public class Config {

    @Bean
    protected JWTAuthenticationFilter jwtAuthenticationFilter(@Autowired TokenAuthenticationService tokenAuthenticationService ) {
        JWTAuthenticationFilter authFilter = new JWTAuthenticationFilter();
        authFilter.setTokenAuthenticationService(tokenAuthenticationService);
        return authFilter;
    }

    @Bean
    protected GlobalPermissionEvaluator permissionEvaluator() {
        return new GlobalPermissionEvaluator();
    }

}

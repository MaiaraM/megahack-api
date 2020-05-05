package br.com.megahack.api.config;

import br.com.megahack.api.services.TokenAuthenticationService;
import br.com.megahack.api.errors.ApiError;
import br.com.megahack.api.filters.JWTAuthenticationFilter;
import br.com.megahack.api.filters.JWTLoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;


/*
* Configures authentication strategy, we will use a similar strategy to that of
* Django's JWT Restframework, where each request has a Bearer token with it that
* authenticates the user
* */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private JWTAuthenticationFilter authFilter;

    @Autowired(required = false)
    private List<WebSecurityConfigService> securityConfigurators;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Adds authentication from the database
        auth.jdbcAuthentication().dataSource(dataSource)
                .authoritiesByUsernameQuery("select u.username, a.name from authorities a, user_authorities ua, users u where " +
                        "a.uuid = ua.authority_uuid and ua.user_uuid in (select uuid from users where username = ?) and u.active = true;")
                .usersByUsernameQuery("select username, password, active from users where username = ? and active = true;")
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO add CSRF token filter

        JWTLoginFilter loginFilter = new JWTLoginFilter("/login", authenticationManager());
        loginFilter.setTokenAuthenticationService(tokenAuthenticationService);

        if (securityConfigurators != null) {
            for (WebSecurityConfigService configurator : securityConfigurators) {
                configurator.configure(http);
            }
        }

        http.cors().and().csrf().disable()
                .authorizeRequests()

                // No need to filter requests to the manager's login endpoint
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/login").permitAll()

                // People need to be able to see our events
                .antMatchers("/events/**","/categories/**", "/properties/**", "/forget_password/**", "/uploadFile/**").permitAll()

                // Anyone is able to create a new user account
                .antMatchers(HttpMethod.POST, "/customers").permitAll()

                .antMatchers("/manager/**").hasAnyAuthority("ADMIN","SUPERADMIN")

                .anyRequest().authenticated()

                .and()

                // Login requests must be JWT authenticated
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                // Filter all other requests for JWT header
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)

                // Reroute spring security's access denied exception to our own ExceptionHandler
                .exceptionHandling().authenticationEntryPoint(
                        // No need to implement the interface as we are just changing the exception
                        (httpServletRequest, httpServletResponse, e) -> {
                            ApiError error = new ApiError(HttpStatus.FORBIDDEN, e.getMessage());
                            OutputStream out = httpServletResponse.getOutputStream();
                            ObjectMapper mapper = new ObjectMapper();
                            httpServletResponse.setStatus(403);
                            httpServletResponse.addHeader("Content-Type", "application/json");
                            mapper.writeValue(out, error);
                            out.flush();
                        } );


    }


    @Bean
    public FilterRegistrationBean<CorsFilter> initCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Location"));
        config.addAllowedMethod("*");
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Length","Content-Range", "Location"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

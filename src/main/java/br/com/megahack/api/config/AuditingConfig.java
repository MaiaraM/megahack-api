package br.com.megahack.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Configuration to enable us to add auditing fields to database records
 *
 * */
@Configuration
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        // Should return the user that is modifying the record
        return Optional::empty;
    }
}

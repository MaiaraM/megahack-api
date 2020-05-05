package br.com.megahack.api.config;


import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@EnableSchedulerLock(
        // The amount of time a thread can stay locked (in case the node dies)
        defaultLockAtMostFor = "${shedlock.lock.at_most_for:PT30S}")
@EnableScheduling
public class SchedulingConfig {

    @Bean
    @Primary
    public ScheduledLockConfiguration shedLockConfig(
            LockProvider lockProvider,
            TaskScheduler taskScheduler) {
            return ScheduledLockConfigurationBuilder
                    .withLockProvider(lockProvider)
                    .withTaskScheduler(new ConcurrentTaskScheduler())
                    .withDefaultLockAtMostFor(Duration.ofMinutes(1)).build();
        }

    @Bean
    @Primary
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

}

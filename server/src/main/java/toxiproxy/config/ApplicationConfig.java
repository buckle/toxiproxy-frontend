package toxiproxy.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;

/**
 * The reason we have default.properties explicitly included is to allow for overrides when using --spring.config.location. The other way is
 * environment variables which works..but then only allows for one way to override.
 */
@EnableScheduling
@Configuration
@PropertySource("classpath:default.properties")
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class ApplicationConfig {

  @Bean
  public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);
  }

  @Bean
  public SpringLiquibase liquibase(DataSource dataSource) {
    SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setDataSource(dataSource);
    liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
    return liquibase;
  }

}

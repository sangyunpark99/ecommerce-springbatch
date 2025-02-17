package org.example.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.sql.DataSource;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Sql("/sql/schema.sql")
@SpringBatchTest
@SpringJUnitConfig(classes = {BatchApplication.class})
public abstract class BaseBatchIntegrationTest {

  @Configuration
  class testConfig {

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
      return new JobLauncherTestUtils();
    }
  }

  @Autowired
  protected JobLauncherTestUtils jobLauncherTestUtils;

  protected JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public static void assertJobCompleted(JobExecution jobExecution) {
    assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
  }
}

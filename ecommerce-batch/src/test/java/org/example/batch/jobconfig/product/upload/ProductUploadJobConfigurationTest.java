package org.example.batch.jobconfig.product.upload;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import org.example.batch.BaseBatchIntegrationTest;
import org.example.batch.BatchApplication;
import org.example.batch.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@AutoConfigureObservability
@Sql("/sql/schema.sql")
@SpringBatchTest
@SpringJUnitConfig(classes = {BatchApplication.class})
@TestPropertySource(properties = {"spring.batch.job.name=productUploadJob"})
class ProductUploadJobConfigurationTest extends BaseBatchIntegrationTest {

  @Value("classpath:/data/products_for_upload.csv")
  private Resource input;

  @Autowired
  private ProductService productService;

  @Test
  void testJob(@Autowired Job productUploadJob) throws Exception {
    JobParameters jobParameter = jobParameters();
    jobLauncherTestUtils.setJob(productUploadJob);

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameter);

    assertAll(
        () -> assertThat(productService.countProduct()).isEqualTo(6),
        () -> assertJobCompleted(jobExecution)
    );
  }

  private JobParameters jobParameters() throws IOException {
    return new JobParametersBuilder()
        .addJobParameter("inputFilePath",
            new JobParameter<>(input.getFile().getAbsolutePath(), String.class, false))
        .toJobParameters();
  }
}